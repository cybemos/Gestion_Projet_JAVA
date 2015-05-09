package projet.datas;

import java.util.jar.Manifest;
import java.util.jar.Attributes;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.text.Normalizer;
import java.net.URI;
import java.awt.Desktop;

/**
* Utility class for the application
* @author <a href="mailto:nicolas.sonet@hotmail.fr">Nicolas Sonet</a>
* @version 1.0
*/

public final class Util {

	private Util() {}

	/**
	*Write the String contenu in the file fichier.
	*@param fichier output file
	*@param contenu string you want to be written in the file
	*/

	public static void ecrireDansFichier(File fichier, String contenu) {
		try {
			PrintWriter writer = new PrintWriter(fichier);
			writer.print(contenu);
			writer.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	*Return the content of the file.
	*@param fichier input file
	*@return content of the file
	*/

	public static String lireFichier(File fichier) {
		String ret = null;
		try {
			ret = "";
			FileReader fileReader = new FileReader(fichier);
			BufferedReader reader = new BufferedReader(fileReader);
			char charactere = ' ';
			int charCode = 0;
			while (charCode != -1) {
				charCode = reader.read();
				if (charCode != -1) {
					charactere = (char) charCode;
					ret = ret + charactere;
				}
			}
			reader.close();
			fileReader.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return ret;
	}

	/**
	*Copy the content of the file fichierSource in the file fichierSortie.
	*@param fichierSource input file
	*@param fichierSortie output file
	*@see #lireFichier(File)
	*@see #ecrireDansFichier(File, String)
	*/

	public static void copie(File fichierSource, File fichierSortie) {
		Util.ecrireDansFichier(fichierSortie, Util.lireFichier(fichierSource));
	}

	public static void mailTo(String mail) {
		String mailTo = "mailto:" + mail;
		try {
			URI uri = URI.create(mailTo);
			Desktop.getDesktop().mail(uri);
		} catch (Exception e2) {
			//Problème lors du lancement de l'explorateur
			System.out.println(e2.getMessage());
		}
	}

	/**
	*Return a Manifest which indicate the parameter as the class containing the main of the application.
	*@param className name of the class containing the main of the application. If the class is "Main.class" in the package "org.main", then the className is "org.main.Main".
	*@return Manifest which indicate the main
	*/

	public static Manifest getManifest(String className) {
		Manifest mf = new Manifest();
		mf.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
		mf.getMainAttributes().put(new Attributes.Name("Built-By"), "Sonet Nicolas");
		mf.getMainAttributes().put(new Attributes.Name("Created-By"), "Sonet Nicolas");
		mf.getMainAttributes().put(Attributes.Name.MAIN_CLASS, className);
		return mf;
	}

	/**
	*Return the directory containing the class. 
	*@param classe can be obtained by NameObject.class or by an instance of the object and by the method getClass()
	*@return directory containing the class
	*@see Object#getClass()
	*/

	public static File getClassDirectory(Class<?> classe) {
		File ret = new File(classe.getProtectionDomain().getCodeSource().getLocation().getFile());
		if (ret.getName().indexOf(".jar") != -1) ret = ret.getParentFile();
		return ret;
	}

	/**
	*Return all the files (not directories) in the directories "repertoires" recursively.
	*@param repertoires directories
	*@return files in directories "repertoires"
	*/

	public static File[] getFiles(File... repertoires) {
		File[] ret = null;
		ArrayList<File> files = new ArrayList<File>();
		for (int i = 0 ; i < repertoires.length ; i++) addDirectoryContents(files, repertoires[i]);
		ret = new File[files.size()];
		for (int i = 0 ; i < files.size() ; i++) ret[i] = files.get(i);
		return ret;

	}

	/**
	*Add to the ArrayList<File>" files" all files in the directory "dir" recursively.
	*@param files 
	*@param dir files in this directory will be added to "files"
	*/

	private static void addDirectoryContents(ArrayList<File> files, File dir) {
		File[] files1 = dir.listFiles();
		if (files1 != null) {
			for (int i = 0 ; i < files1.length ; i++) {
				if (files1[i].isDirectory()) addDirectoryContents(files, files1[i]);
				else files.add(files1[i]);
			}
		}
	}
	
	/**
	* Return true if the string ends by one of the extensions, else false
	* @param chaine string to test
	* @param extensions possible extensions
	*/

	public static boolean finitPar(String chaine, String... extensions) {
		boolean ret;
		StringBuilder sb = new StringBuilder(".*(");
		for (int i = 0 ; i < extensions.length ; i++) {
			if (i > 0) sb.append("|");
			sb.append("\\Q" + extensions[i] + "\\E");
		}
		sb.append(")");
		Pattern p = Pattern.compile(sb.toString());
		Matcher m = p.matcher(chaine);
		ret = m.matches();
		return ret;
	}
	
	/**
	* Return true if the string chaine contains the regex, else false
	* @param chaine string to test
	* @param regex regex to apply
	* @throws PatternSyntaxException if the regex isn't correct
	*/

	public static boolean contient(String chaine, String regex) throws PatternSyntaxException {
		boolean ret;
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(chaine);
		ret = m.matches();
		return ret;
	}
	
	/**
	* Return the specified line of the text
	* @param texte text to test
	* @param line the number of the line
	*/

	public static String getLine(String texte, int line) {
		String ret;
		String newligne = System.getProperty("line.separator");
		String[] lines = texte.split("\\Q" + newligne + "\\E");
		ret = lines[line - 1];
		return ret;
	}
	
	/**
	* Remove all accents of a string
	* @param input string you want to remove accents
	*/

	public static String stripAccents(String input) {
		return Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

}

