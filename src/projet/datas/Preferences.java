package projet.datas;

//import projet.datas.Projet;
import java.util.HashMap;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectInputStream;
import java.io.File;
import java.util.ArrayList;
import java.awt.Font;
import java.awt.Color;
import projet.ihm.PreferencesEvent;

/**
* Class which represents the preferences of the application
* @author <a href="mailto:nicolas.sonet@hotmail.fr">Nicolas Sonet</a>
* @version 1.0
*/

public class Preferences implements Serializable {

	/**
	* Actual version of Preferences. If the version != loaded version of preferences file, initialize attributs when they are null
	*/

	private static final int ACTUAL_VERSION = 3;
	
	/**
	* Version which is saved in the file
	*/
	
	private int version;

	/**
	*version for the save Serializable.
	*@see Serializable
	*/

	private static final long serialVersionUID = 0;
	
	private Font font;
	private Color couleurForegroundTexte;
	private Color couleurBackgoundTexte;
	private Color couleurMenus;
	private boolean afficherBarreOutils;
	private boolean toujoursAfficher;
	private ArrayList<File> lastProjects;
	private HashMap<File, String> classToExecute;
	private ArrayList<File> classpath;
	private transient CheckPreferences checkPreferences;
	
	/**
	* Builder of the class
	* @param checkPreferences class which handle when a preference state change
	*/
	
	public Preferences(CheckPreferences checkPreferences) {
		version = ACTUAL_VERSION;
		this.checkPreferences = checkPreferences;
		lastProjects = new ArrayList<File>();
		classToExecute = new HashMap<File, String>();
		classpath = new ArrayList<File>();
		font = new Font("Arial", Font.PLAIN, 12);
		couleurForegroundTexte = Color.BLACK;
		couleurBackgoundTexte = Color.WHITE;
		couleurMenus = Color.LIGHT_GRAY;
		afficherBarreOutils = true;
		toujoursAfficher = false;
	}
	
	public void addClasspath(File classDirectory) {
		classpath.add(classDirectory);
	}
	
	public void removeClasspath(File classDirectory) {
		classpath.remove(classDirectory);
	}
	
	public void resetClasspath() {
		classpath.clear();
	}
	
	public File[] getClasspath() {
		File[] files = new File[classpath.size()];
		for (int i = 0 ; i < files.length ; i++) {
			files[i] = classpath.get(i);
		}
		return files;
	}
	
	/**
	* Return the last projects
	*/

	public ArrayList<File> getLastProjects() {
		return lastProjects;
	}
	
	/**
	* Add a project to the last projects
	* @param projet project to add
	*/

	public void addToRecentsProjects(File projet) {
		lastProjects.add(projet);
		checkPreferences.firePreferencesChanged(new PreferencesEvent(this, this));
	}
	
	/**
	* Reset all recents projects
	*/

	public void resetRecentsProjects() {
		lastProjects.clear();
		classToExecute.clear();
		checkPreferences.firePreferencesChanged(new PreferencesEvent(this, this));
	}
	
	/**
	* set the class to execute for a project
	* @param projet this project will execute this class when the execution starts
	* @param className class to execute
	*/
	
	public void setClassToExecute(Projet projet, String className) {
		classToExecute.put(projet.getProjectFile(), className);
	}
	
	/**
	* Return the class to execute for a project.
	* If there is no class, return ""
	*/
	
	public String getClassToExecute(Projet projet) {
		return classToExecute.containsKey(projet.getProjectFile()) ? classToExecute.get(projet.getProjectFile()) : "";
	}

	public boolean getAfficherBarreOutilsValue() {
		return afficherBarreOutils;
	}

	public void setAfficherBarreOutilsValue(boolean value) {
		boolean oldValue = afficherBarreOutils;
		afficherBarreOutils = value;
		if (oldValue != afficherBarreOutils) checkPreferences.firePreferencesChanged(new PreferencesEvent(this, this));
	}

	public boolean getToujoursAfficherValue() {
		return toujoursAfficher;
	}

	public void setToujoursAfficherValue(boolean value) {
		boolean oldValue = toujoursAfficher;
		toujoursAfficher = value;
		if (oldValue != toujoursAfficher) checkPreferences.firePreferencesChanged(new PreferencesEvent(this, this));
	}

	public void setFont(Font font) {
		Font oldValue = this.font;
		this.font = font;
		if (oldValue != font) checkPreferences.firePreferencesChanged(new PreferencesEvent(this, this));
	}

	public Font getFont() {
		return font;
	}

	public Color getForegroundColor() {
		return couleurForegroundTexte;
	}

	public Color getBackgroundColor() {
		return couleurBackgoundTexte;
	}

	public Color getMenusColor() {
		return couleurMenus;
	}

	public void setMenusColor(Color couleur) {
		Color oldValue = couleurMenus;
		couleurMenus = couleur;
		if (oldValue != couleurMenus) checkPreferences.firePreferencesChanged(new PreferencesEvent(this, this));
	}

	public void setTextSize(int size) {
		int oldValue = font.getSize();
		font = new Font(font.getName(), font.getStyle(), size);
		if (oldValue != font.getSize()) checkPreferences.firePreferencesChanged(new PreferencesEvent(this, this));
	}

	/**
	*@see Font
	*/

	public void setTextStyle(int style) {
		int oldValue = font.getStyle();
		font = new Font(font.getName(), style, font.getSize());
		if (oldValue != font.getStyle()) checkPreferences.firePreferencesChanged(new PreferencesEvent(this, this));
	}

	public void setForegroundColor(Color couleur) {
		Color oldValue = couleurForegroundTexte;
		couleurForegroundTexte = couleur;
		if (oldValue != couleurForegroundTexte) checkPreferences.firePreferencesChanged(new PreferencesEvent(this, this));
	}

	public void setBackgroundColor(Color couleur) {
		Color oldValue = couleurBackgoundTexte;
		couleurBackgoundTexte = couleur;
		if (oldValue != couleurBackgoundTexte) checkPreferences.firePreferencesChanged(new PreferencesEvent(this, this));
	}
	
	/**
	* Check and handle if the current version is not the last
	*/

	private static void versionDifferente(Preferences p) {
		CheckPreferences check = new CheckPreferences();
		Preferences tmp = new Preferences(check);
		if (p.font == null) p.font = tmp.font;
		if (p.couleurForegroundTexte == null) p.couleurForegroundTexte = tmp.couleurForegroundTexte;
		if (p.couleurBackgoundTexte == null) p.couleurBackgoundTexte = tmp.couleurBackgoundTexte;
		if (p.couleurMenus == null) p.couleurMenus = tmp.couleurMenus;
		if (p.lastProjects == null) p.lastProjects = tmp.lastProjects;
		p.afficherBarreOutils = tmp.afficherBarreOutils;
		p.toujoursAfficher = tmp.toujoursAfficher;
		if (p.classToExecute == null) p.classToExecute = new HashMap<File, String>();
		if (p.classpath == null) p.classpath = new ArrayList<File>();
		p.version = Preferences.ACTUAL_VERSION;
	}
	
	/**
	* Load the file "Preferences" in the current directory
	* If don't exists or fails, create a new instance of Preferences
	* @return Preferences, different of null
	*/

	public static Preferences charger(CheckPreferences checkPreferences) {
		Preferences p = new Preferences(checkPreferences);
		try {
			FileInputStream fs = new FileInputStream(Util.getClassDirectory(Preferences.class) + Projet.pathSeparator + "Preferences");
			ObjectInputStream os = new ObjectInputStream(fs);
			p = (Preferences) os.readObject();
			os.close();
			fs.close();
			p.checkPreferences = checkPreferences;
			if (p.version != Preferences.ACTUAL_VERSION) versionDifferente(p);
		} catch (Exception e) {}
		return p;
	}
	
	/**
	* Save the Preferences in the file "Preferences" in the current directory
	*/

	public void sauver() {
		try {
			FileOutputStream fs = new FileOutputStream(Util.getClassDirectory(Preferences.class) + Projet.pathSeparator + "Preferences");
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(this);
			os.close();
			fs.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
