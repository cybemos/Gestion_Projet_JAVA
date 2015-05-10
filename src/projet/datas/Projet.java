package projet.datas;

import java.util.ArrayList;
import java.util.jar.Manifest;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.JarFile;
import java.util.jar.Attributes;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.lang.ClassNotFoundException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.net.URL;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

import projet.ctrl.Controleur;
import static projet.ctrl.Controleur.terminal;

/**
* class Projet
* @version 1.0
* @author <a href="mailto:nicolas_sonet@hotmail.fr">Nicolas Sonet</a>
*/

public class Projet {

	/**
	*Directory of the project.
	*/

	private File projet;
	private static URLClassLoader classLoader;
	public final static String pathSeparator = System.getProperty("file.separator");
	private RunProject rp;
	static SecurityManager securityManager;
	
// -----------------------------------------------------------------------------------

	//Constructeur

	/**
	*Contruit un objet Projet.
	*/

	public Projet(File projet) throws Exception {
		this.projet = projet;
		resetClassLoader();
		isValide();
		Projet.addPath(new File(projet, "class").toString());
	}
	
	/**
	* Reset the classLoader of the project
	*/
	
	private void resetClassLoader() {
		classLoader = URLClassLoader.newInstance(new URL[0]);
	}
	
	/**
	* Set the classLoader of the projet with all files + classes of the project
	* @param files directory or jarfile which contains classes
	*/
	
	public void setClassLoader(File[] files) {
		URL[] urls = new URL[files.length+1];
		try {
			urls[0] = new File(projet, "class").toURI().toURL();
			for (int i = 0 ; i < files.length ; i++) urls[i+1] = files[i].toURI().toURL();
			classLoader = URLClassLoader.newInstance(urls);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public static void preventCloseWindows(Controleur ctrl, boolean prevent) {
		securityManager = null;
		if (prevent) securityManager = new ProjectSecurityManager(ctrl);
		System.setSecurityManager(securityManager);
	}

// -----------------------------------------------------------------------------------

	/**
	*Return the project directory.
	*@return project directory
	*@see #projet
	*/

	public File getProjectFile() {
		return projet;
	}

// -----------------------------------------------------------------------------------

	/**
	*Create a new Project in the ditrectory "directory" only if "directory" is a directory or doesn't exist.
	*@param directory directory of the new project
	*@throws Exception if the param exists and !directory.isDirectory() return true
	*/

	public static Projet createNewProject(File directory) throws Exception {
		if (!directory.exists()) directory.mkdirs();
		else {
			if (!directory.isDirectory()) throw new Exception("le projet n'est pas un repertoire.");
		}
		
		String[] repertoires = {"class", "src", "ww", "doc"};
		for (int i = 0 ; i < repertoires.length ; i++) new File(directory, repertoires[i]).mkdir();
		
		return new Projet(directory);
	}

// -----------------------------------------------------------------------------------

	/**
	*Returns the pathname string of this abstract pathname of the directory file.
	*/

	public String toString() {
		return projet.getPath();
	}

// -----------------------------------------------------------------------------------

	/**
	*Throws an Exception if the project isn't valid.
	*@throws Exception if the project isn't valid
	*/

	private void isValide() throws Exception {
		if (projet == null) throw new NullPointerException();
		if (!projet.isDirectory()) throw new Exception("le projet n'est pas un repertoire.");
		
		File file;
		String[] repertoires = {"class", "src", "ww", "doc"};

		for (int i = 0 ; i < repertoires.length ; i++) {
			file = new File(projet, repertoires[i]);
			if (!file.exists()) throw new Exception("le repertoire " + repertoires[i] + " n'existe pas.");
			if (!file.isDirectory()) throw new Exception(repertoires[i] + " n'est pas un repertoire.");
		}
	}

// -----------------------------------------------------------------------------------

	/**
	*Compile all classes in the directory nameProject/class/ using the system compiler.
	*@throws Exception if an error occured
	*/

	public void compiler() throws Exception {
		System.out.println("compilation...");

		File[] files = Util.getFiles(new File(projet, "src"));
		String[] args = new String[files.length + 5];
		
		String classpath = "";
		URL[] urls = classLoader.getURLs();
		for (int i = 0 ; i < urls.length ; i++) classpath += urls[i].getFile()+":";
		
		args[0] = "-Xlint";
		args[1] = "-classpath";
		args[2] = classpath;
		args[3] = "-d";
		args[4] = new File(projet, "class").toString();
		for (int i = 0 ; i < files.length ; i++) {
			args[i + 5] = files[i].toString();
		}
		
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

		if (compiler == null) System.out.println("Compilateur non détecté. Impossible de lancer la compilation.");
		else {
			int result = compiler.run(System.in, System.out, System.err, args);
			if (result == 0) System.out.println("Compilation réussie.");
			else {
				System.out.println("Erreur de compilation.");
				throw new Exception();
			}
		}
	}

// -----------------------------------------------------------------------------------

	/**
	*Run the class using main(String[]).
	*@param className name of the class (including package)
	*@param args arguments of the execution
	*/

	public void executer(String className, String... args) throws ClassNotFoundException, NoSuchMethodException, Exception {
		URLClassLoader systemUrlClassLoader = classLoader;
		URLClassLoader urlClassLoader = new URLClassLoader(systemUrlClassLoader.getURLs());
		//for (int i = 0 ; i < urlClassLoader.getURLs().length ; i++) System.out.println(urlClassLoader.getURLs()[i]);
		this.executer(urlClassLoader, className, args);
	}

// -----------------------------------------------------------------------------------

	/**
	*Run the class using main(String[]).
	*@param className name of the class (including package)
	*@param args arguments of the execution
	*@param urlClassLoader the class loader associed to the class
	*@throws ClassNotFoundException if the class is not found
	*@throws NoSuchMethodException if the main doesn't exist
	*@throws Exception others exceptions...
	*/

	private void executer(URLClassLoader urlClassLoader, String className, String... args) throws ClassNotFoundException, NoSuchMethodException, Exception {
		if (rp != null) {
			if (rp.isAlive()) rp.join();
		}
		rp = new RunProject(urlClassLoader, className, args);
	}
	
	private class RunProject extends Thread {
	
		private URLClassLoader urlClassLoader;
		private String className;
		private String[] args;
		private Method method;
		public boolean isInit;
	
		public RunProject(URLClassLoader urlClassLoader, String className, String... args) throws Exception {
			isInit = false;
			this.urlClassLoader = urlClassLoader;
			this.className = className;
			this.args = args;
			Class<?> urlClass = URLClassLoader.class;
			method = urlClass.getDeclaredMethod("findClass", new Class<?>[]{String.class});
			method.setAccessible(true);
			start();
		}
		
		public void run() {
			System.setSecurityManager(null);
			try {
				Class<?> classe = (Class<?>) method.invoke(urlClassLoader, new Object[]{className});
				Method methode = classe.getDeclaredMethod("main", args.getClass());
				methode.invoke(null, new Object[] { args });
			} catch (InvocationTargetException e) {
				e.getTargetException().printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.setSecurityManager(securityManager);
		}
		
	}

// -----------------------------------------------------------------------------------

	/**
	*Run the jar file.
	*@param fileName name of the jar file
	*@param args arguments of the execution
	*@throws Exception exceptions...
	*/

	public void executerJAR(String fileName, String... args) throws /*ClassNotFoundException, NoSuchMethodException,*/ Exception {
		Projet.addPath(fileName);
		JarFile jarfile = new JarFile(fileName);
		Manifest manifest = jarfile.getManifest();
		String mainClass = (String) manifest.getMainAttributes().get(Attributes.Name.MAIN_CLASS);
		URLClassLoader urlClassLoader = new URLClassLoader(new URL[0]);
		Projet.addPath(urlClassLoader, fileName);

		try {
			String classPath = (String) manifest.getMainAttributes().get(Attributes.Name.CLASS_PATH);
			String[] classPaths = classPath.split(" ");
			for (int i = 0 ; i < classPaths.length ; i++) {
				Projet.addPath(urlClassLoader, classPaths[i]);
			}
		} catch (NullPointerException e) {/* pas de classpath */}
		this.executer(urlClassLoader, mainClass, args);
	}

// -----------------------------------------------------------------------------------

	private static BufferedReader getOutput(Process p) {
		return new BufferedReader(new InputStreamReader(p.getInputStream()));
	}

// -----------------------------------------------------------------------------------

	private static BufferedReader getError(Process p) {
		return new BufferedReader(new InputStreamReader(p.getErrorStream()));
	}

// -----------------------------------------------------------------------------------

	/**
	*Create Javadoc of the project only if the OS is a linux.
	*/

	public void genererJavadoc() {
		if (System.getProperty("os.name").toUpperCase().indexOf("LINUX") > -1) {
			try {
				File file = new File(Util.getClassDirectory(this.getClass()), "javadoc_linux");
				if (file.exists()) {
					String commande = file.getPath() + " " + projet.getPath();
					Process p = Runtime.getRuntime().exec(commande);
					BufferedReader output = getOutput(p);
					BufferedReader error = getError(p);
					String ligne = "";
					while ((ligne = output.readLine()) != null) System.out.println(ligne);
					while ((ligne = error.readLine()) != null) System.out.println(ligne);
					p.waitFor();
				} else System.out.println("le fichier " + file + " n'existe pas.");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else System.out.println("javadoc générée seulement pour linux");
	}

// -----------------------------------------------------------------------------------

	/**
	*Add dynamically to the classpath a directory "s" to the system class loader
	*@param s directory containing class files
	*@throws Exception exceptions...
	*@see #addPath(URLClassLoader, String)
	*/

	public static void addPath(String s) throws Exception {
		//URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		Projet.addPath(classLoader, s);
	}

// -----------------------------------------------------------------------------------

	/**
	*Add dynamically to the classpath a directory "s" to the class loader "urlClassLoader"
	*@param urlClassLoader a classLoader
	*@param s directory containing class files
	*@throws Exception exceptions...
	*/

	private static void addPath(URLClassLoader urlClassLoader, String s) throws Exception {
		File f = new File(s);
		URL u = f.toURI().toURL();
		Class<?> urlClass = URLClassLoader.class;
		Method method = urlClass.getDeclaredMethod("addURL", new Class<?>[]{URL.class});
		method.setAccessible(true);
		method.invoke(urlClassLoader, new Object[]{u});
	}

// -----------------------------------------------------------------------------------

	/**
	*Create a jar file named by param jarFile containing all files in the directory nameProject/class/
	*@param jarFile output file
	*@param mf Manifest containing the main class
	*@see Util#getManifest(String)
	*/

	public void createJarArchive(File jarFile, Manifest mf) {
		if (jarFile == null) throw new IllegalArgumentException("fichier jar null");
		int buffer = 10240;
		try {
			File file = new File(projet, "class");
			File[] listFiles = Util.getFiles(file);
			byte b[] = new byte[buffer];
			FileOutputStream fout = new FileOutputStream(jarFile);
			JarOutputStream out = new JarOutputStream(fout, mf);
			for (int i = 0 ; i < listFiles.length ; i++) {
				String st1 = listFiles[i].toString();
				st1 = st1.replaceFirst(file.toString() + System.getProperty("file.separator"), "");
				JarEntry addFiles = new JarEntry(st1);
				addFiles.setTime(listFiles[i].lastModified());
				out.putNextEntry(addFiles);
				FileInputStream fin = new FileInputStream(listFiles[i]);
				while (true) {
					int len = fin.read(b, 0, b.length);
					if (len <= -1) break;
					out.write(b, 0, len);
				}
				System.out.println("ajout de " + st1 + " au JAR file...");
				fin.close();
			}
			out.close();
			fout.close();
			System.out.println("Creation du JAR file reussit.");
		} catch (Exception ex) {
			System.out.println(ex.getMessage() + "Erreur de la creation du JAR file.");
		}
	}

//-------------------------------------------------------------------------------

	/**
	*Returns the name of the class file.
	*@return name of the class
	*@param classFile file of a class
	*/

	private String getClassName(File classFile) {
		String ret = null;
		String classDirectory = pathSeparator+"class"+pathSeparator;
		String classExt = ".class";
		StringBuilder builder = new StringBuilder(classFile.getAbsolutePath());
		builder.delete(0, builder.lastIndexOf(classDirectory));
		builder.replace(0, classDirectory.length(), "");
		builder.replace(builder.length() - classExt.length(), builder.length(), "");
		ret = builder.toString().replace(pathSeparator.charAt(0), '.');
		return ret;
	}

//-------------------------------------------------------------------------------

	/**
	*Returns all classes of the project.
	*@return classes of the project
	*@see #getClassName(File)
	*/

	public Class<?>[] getClasses() {
		File[] files = Util.getFiles(new File(projet, "class"));
		ArrayList<File> classFiles = new ArrayList<File>();
		for (int i = 0 ; i < files.length ; i++) {
			if (files[i].getPath().indexOf(".class") > -1) classFiles.add(files[i]);
		}
		Class<?>[] ret = new Class<?>[classFiles.size()];
		URLClassLoader systemUrlClassLoader = classLoader;
		URLClassLoader urlClassLoader = new URLClassLoader(systemUrlClassLoader.getURLs());
		Class<?> urlClass = URLClassLoader.class;
		try {
			Method method = urlClass.getDeclaredMethod("findClass", new Class<?>[]{String.class});
			method.setAccessible(true);
			for (int i = 0 ; i < ret.length ; i++) {
				try {
					//System.out.println(getClassName(classFiles.get(i)));
					Object obj = method.invoke(urlClassLoader, new Object[]{getClassName(classFiles.get(i))});
					//System.out.println(obj);
					//System.out.println(method.invoke(urlClassLoader, new Object[]{getClassName(classFiles.get(i))}));
					ret[i] = (Class<?>) obj;// method.invoke(urlClassLoader, new Object[]{getClassName(classFiles.get(i))});
				} catch (InvocationTargetException e) {
					//e.getTargetException().printStackTrace();
					//System.out.println();
				}
			}
		} catch (Exception e) {}
		return ret;
	}
}
