package projet.ctrl;

import projet.datas.*;
import projet.ihm.*;
import java.io.File;
import java.io.PrintStream;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.Cursor;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import java.util.ArrayList;

/**
* Controller of the application
* @author <a href="mailto:nicolas.sonet@hotmail.fr">Nicolas Sonet</a>
* @version 1.0
*/

public class Controleur implements ActionListener, KeyListener, /*MouseListener,*/ WindowListener, PreferencesListener, ItemListener/*, TreeSelectionListener*/ {

	private Projet projet;
	private Interface ihm;
	private DialogPreferences dialogPreferences;
	private DialogRechercher dialogRechercher;
	private DialogAPropos dialogAPropos;
	private DialogInfos dialogInfos;
	private DialogAide dialogAide;
	private DialogGenererJar dialogGenererJar;
	private DialogExecuter dialogExecuter;
	private JFileChooser fileChooser;
	private JTextOutputStream out;
	private Preferences preferences;
	private Recherche recherche;
	
	/**
	* PrintSteam that represent the default output. 
	* Use Controleur.terminal.println(...) to print.
	*/
	
	public static final PrintStream terminal = System.out;
	
	/**
	* Allows to add or remove a PreferencesListener.
	* @see projet.datas.PreferencesListener
	* @see projet.datas.PreferencesEvent
	*/
	
	public static final CheckPreferences CHECK_PREFERENCES = new CheckPreferences();
	
	/**
	* Constructor of the Controller, just create a new instance of the class and the application will be launched
	*/

	public Controleur() throws Exception {
		init();
		ihm = new Interface(this);
		out = new JTextOutputStream(ihm.getArea());
		CHECK_PREFERENCES.addPreferencesListener(this);
		setPreferences(Preferences.charger(CHECK_PREFERENCES));
		initRecentsProjects();
		chargerProjet(null);
		dialogPreferences = new DialogPreferences(this);
		dialogRechercher = new DialogRechercher(this);
		dialogInfos = new DialogInfos(this);
		dialogGenererJar = new DialogGenererJar(this);
		dialogExecuter = new DialogExecuter(this);
		dialogAPropos = new DialogAPropos(ihm);
		dialogAide = new DialogAide(ihm);
		recherche = new Recherche(this, ihm.getArea());
		attacherReactions();
		System.setOut(out);
		System.setErr(out);
		ihm.setVisible(true);
		Projet.preventCloseWindows(this, true);
	}
	
	/**
	* Called when the preferences changed
	* @see PreferencesListener
	*/

	public void preferencesChanged(PreferencesEvent e) {
		updatePreferences();
		try {
			recherche.updateColor();
		} catch (NullPointerException ex) {}
	}
	
	/**
	* Method called when the state of an item has changed.
	*/

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == dialogRechercher.getBoutonCasseRespectee()) recherche.respecterCasse(dialogRechercher.getBoutonCasseRespectee().isSelected());
		else dialogPreferences.updateFont();
	}
	
	/**
	* Return the principal interface of the application
	*/

	public Interface getIhm() {
		return ihm;
	}
	
	/**
	* Return the preferences associed to the application
	*/

	public Preferences getPreferences() {
		return preferences;
	}
	
	/**
	* Change the preferences of the application and send a PreferencesEvent to himself
	* @param preferences new preferences
	*/

	public void setPreferences(Preferences preferences) {
		this.preferences = preferences;
		CHECK_PREFERENCES.firePreferencesChanged(new PreferencesEvent(this, preferences));
	}
	
	/**
	* Method which handle the changes in the preferences.
	*/

	public void updatePreferences() {
		JCheckBoxMenuItem check1 = (JCheckBoxMenuItem) ihm.getItems()[2][1];
		JCheckBoxMenuItem check2 = (JCheckBoxMenuItem) ihm.getItems()[2][2];
		
		ihm.getArea().setFont(preferences.getFont());
		ihm.getArea().setBackground(preferences.getBackgroundColor());
		ihm.getArea().setForeground(preferences.getForegroundColor());
		check1.setState(preferences.getAfficherBarreOutilsValue());
		ihm.afficherBarreOutils(check1.getState());
		check2.setState(preferences.getToujoursAfficherValue());
		try {
			ihm.setAlwaysOnTop(check2.getState());
		} catch (Exception ex) {
			terminal.println(ex.getMessage());
		}
		ihm.setMenuColor(preferences.getMenusColor());
		try {
			dialogPreferences.getBoutonsCouleur()[0].setBackground(preferences.getMenusColor());
			dialogPreferences.getBoutonsCouleur()[1].setBackground(preferences.getBackgroundColor());
			dialogPreferences.getBoutonsCouleur()[2].setBackground(preferences.getForegroundColor());
		} catch (NullPointerException e) {}
	}
	
	/**
	* Init some attributs
	*/

	private void init() {
		fileChooser = new JFileChooser(System.getProperty("user.home") + System.getProperty("file.separator") + "Documents");
		fileChooser.setDragEnabled(false);
		fileChooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setApproveButtonToolTipText("Selectionner le répertoire de projet");
	}
	
	/**
	* Create a new Project (create the directory of the project with all sub directories in it)
	* @param directory directory of the project
	*/

	public void createNewProject(File directory) {
		try {
			projet = Projet.createNewProject(directory);
		} catch (Exception e) {
			terminal.println(e.getMessage());
		}
		projectChanged();
	}
	
	/**
	* Load a project if the file (directory) contains all necessary directories
	* @param file directory of the project
	*/

	public void chargerProjet(File file) {
		try {
			projet = new Projet(file);
		} catch (Exception e) {}
		projectChanged();
	}
	
	/**
	* Method which handle the change of a project
	*/

	private void projectChanged() {
		Bouton[] boutons = ihm.getBoutons();
		Bouton[] boutonsProjet = {boutons[2], boutons[3], boutons[4]};
		JMenuItem[][] items = ihm.getItems();
		JMenuItem[] itemsProjet = {items[1][1], items[1][2], items[1][3], items[1][4], items[1][5], items[1][6]};
		boolean isEnabled = true;

		if (projet == null) {
			isEnabled = false;
			ihm.setTitle("aucun projet");
		} else {
			ihm.setTitle("Projet : " + projet);
			if (!preferences.getLastProjects().contains(projet.getProjectFile())) addToRecentsProjects(projet);
		}

		for (int i = 0 ; i < boutonsProjet.length ; i++) {
			boutonsProjet[i].setEnabled(isEnabled);
		}
		for (int i = 0 ; i < itemsProjet.length ; i++) {
			itemsProjet[i].setEnabled(isEnabled);
		}

		try {
			dialogExecuter.update();
			dialogInfos.update();
		} catch (NullPointerException e) {} // pas encore initialisé
	}
	
	/**
	* Initialize and add in a submenu the recents projects
	*/

	public void initRecentsProjects() {
		boolean isEnabled = true;
		int taille = preferences.getLastProjects().size();
		ArrayList<File> projets = preferences.getLastProjects();
		JMenu menu = (JMenu) ihm.getItems()[0][3];
		JMenuItem item;

		if (taille == 0) isEnabled = false;

		ihm.getItems()[0][3].setEnabled(isEnabled);
		ihm.getItems()[0][4].setEnabled(isEnabled);

		for (int i = 0 ; i < menu.getItemCount() ; i++) {
			menu.getItem(i).removeActionListener(this);
		}
		menu.removeAll();

		for (int i = 0 ; i < taille ; i++) {
			item = new JMenuItem(projets.get(i).getName());
			item.addActionListener(this);
			item.setBackground(preferences.getMenusColor());
			item.setBorder(BorderFactory.createLineBorder(preferences.getMenusColor()));
			menu.add(item);
		}
		menu.setBackground(preferences.getMenusColor());
		menu.setBorder(BorderFactory.createLineBorder(preferences.getMenusColor()));
	}
	
	/**
	* Add a project to the rencents projects
	* @param projet project to add
	*/

	public void addToRecentsProjects(Projet projet) {
		preferences.addToRecentsProjects(projet.getProjectFile());
		initRecentsProjects();
	}
	
	/**
	* Remove all recents projects
	*/

	public void resetRecentsProjects() {
		preferences.resetRecentsProjects();
		initRecentsProjects();
	}
	
	/**
	* Return all classes of an application in the directory class
	*/

	public Class<?>[] getClasses() {
		Class<?>[] ret = new Class<?>[0];
		if (projet != null) ret = projet.getClasses();
		return ret;
	}
	
	/**
	* Method called when an action is detected
	*/

	public void actionPerformed(ActionEvent e) {
		Bouton[] boutons = ihm.getBoutons();
		JMenuItem[][] items = ihm.getItems();
		Color couleur;
		
		if (e.getSource() == items[0][1] || e.getSource() == boutons[0]) {
			int res = fileChooser.showDialog(ihm, "create project");
			if (res == JFileChooser.APPROVE_OPTION) createNewProject(fileChooser.getSelectedFile());
		} else if (e.getSource() == items[0][2] || e.getSource() == boutons[1]) {
			int res = fileChooser.showDialog(ihm, "Select project");
			if (res == JFileChooser.APPROVE_OPTION) chargerProjet(fileChooser.getSelectedFile());
		} else if (e.getSource() == items[0][4]) {
			resetRecentsProjects();
		} else if (e.getSource() == items[0][5]) {
			windowClosing(new WindowEvent(ihm, WindowEvent.WINDOW_CLOSING));
		} else if (e.getSource() == items[1][1] || e.getSource() == boutons[2]) {
			boutons[2].setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			try {
				projet.compiler();
				dialogExecuter.update();
				dialogInfos.update();
			} catch (Exception ex) {
				terminal.println("erreur de compilation");
			}
			boutons[2].setCursor(Cursor.getPredefinedCursor(Cursor./*DEFAULT*/HAND_CURSOR));
		} else if (e.getSource() == items[1][2] || e.getSource() == boutons[3]) {
			String className = preferences.getClassToExecute(projet);
			if (("").equals(className)) dialogExecuter.setVisible(true);
			else executeClass(className);
		} else if (e.getSource() == items[1][3]) {
			dialogExecuter.setVisible(true);
		} else if (e.getSource() == items[1][4] || e.getSource() == boutons[4]) {
			boutons[4].setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			projet.genererJavadoc();
			boutons[4].setCursor(Cursor.getPredefinedCursor(Cursor./*DEFAULT*/HAND_CURSOR));
		} else if (e.getSource() == items[1][5]) {
			dialogGenererJar.setVisible(true);
		} else if (e.getSource() == items[1][6]) {
			dialogInfos.setVisible(true);
		} else if (e.getSource() == items[2][1]) {
			preferences.setAfficherBarreOutilsValue(((JCheckBoxMenuItem) items[2][1]).getState());
		} else if (e.getSource() == items[2][2]) {
			try {
				preferences.setToujoursAfficherValue(((JCheckBoxMenuItem) items[2][2]).getState());
			} catch (Exception ex) {
				terminal.println(ex.getMessage());
			}
		} else if (e.getSource() == items[3][1]) {
			dialogPreferences.setVisible(true);
		}else if (e.getSource() == items[3][2]) {
			dialogRechercher.setVisible(true);
		} else if (e.getSource() == items[3][3]) {
			recherche.resetRecherche();
		} else if (e.getSource() == items[4][1]) {
			dialogAide.setVisible(true);
		} else if (e.getSource() == items[4][2]) {
			dialogAPropos.setVisible(true);
		} else if (e.getSource() == items[4][3]) {
			Util.mailTo("nicolas_sonet@hotmail.fr");
		} else if (e.getSource() == dialogInfos.getBoutonFermer()) {
			dialogInfos.setVisible(false);
		} else if (e.getSource() == dialogPreferences.getBoutonFermer()) {
			dialogPreferences.setVisible(false);
		} else if (e.getSource() == dialogAPropos.getBoutonFermer()) {
			dialogAPropos.setVisible(false);
		} else if (e.getSource() == dialogAide.getBoutonFermer()) {
			dialogAide.setVisible(false);
		} else if (e.getSource() == dialogGenererJar.getBoutonFermer()) {
			dialogGenererJar.setVisible(false);
		} else if (e.getSource() == dialogExecuter.getBoutonFermer()) {
			dialogExecuter.setVisible(false);
		} else if (e.getSource() == dialogExecuter.getBoutonConfirmer()) {
			dialogExecuter.setVisible(false);
			preferences.setClassToExecute(projet, dialogExecuter.getValue());
			String className = preferences.getClassToExecute(projet);
			executeClass(className);
		} else if (e.getSource() == dialogGenererJar.getBoutonConfirmer()) {
			projet.createJarArchive(dialogGenererJar.getOutputFile(), dialogGenererJar.getManifest());
			dialogGenererJar.setVisible(false);
		} else if (e.getSource() == dialogPreferences.getBoutonsCouleur()[0]) {
			couleur = JColorChooser.showDialog(dialogPreferences, "Choisir une couleur", preferences.getMenusColor());
			if (couleur != null) preferences.setMenusColor(couleur);
		} else if (e.getSource() == dialogPreferences.getBoutonsCouleur()[1]) {
			couleur = JColorChooser.showDialog(dialogPreferences, "Choisir une couleur", preferences.getBackgroundColor());
			if (couleur != null) preferences.setBackgroundColor(couleur);
		} else if (e.getSource() == dialogPreferences.getBoutonsCouleur()[2]) {
			couleur = JColorChooser.showDialog(dialogPreferences, "Choisir une couleur", preferences.getForegroundColor());
			if (couleur != null) preferences.setForegroundColor(couleur);
		} else if (e.getSource() == dialogRechercher.getFindButton()) {
			recherche.doRecherche(dialogRechercher.getRecherche());
		} else if (e.getSource() == dialogRechercher.getCloseButton()) {
			dialogRechercher.setVisible(false);
		} else { //projets recents
			try {
				JMenuItem item = (JMenuItem) e.getSource();
				int index = indexOfProject(item);
				File file = null;
				if (index > -1) file = preferences.getLastProjects().get(index);
				chargerProjet(file);
			} catch (Exception ex) {}
		}
	}
	
	/**
	* Execute the main of a class
	* @param name of a class (with package)
	*/
	
	private void executeClass(String className) {
		try {
			if (className != null) projet.executer(className);
		} catch (ClassNotFoundException cnfe) {
			System.out.println("classe "+className+" non trouvée.");
		} catch (NoSuchMethodException nsme) {
			System.out.println("méthode main(String[] args) de "+className+" non trouvée.");
		} catch (Exception otherE) {
			otherE.printStackTrace();
		}
	}
	
	/**
	* Return the index of a project in the list of recents projects
	* @param item JMenuItem which represents a project
	*/

	private int indexOfProject(JMenuItem item) {
		int ret = -1;
		ArrayList<File> projets = preferences.getLastProjects();
		JMenu menu = (JMenu) ihm.getItems()[0][3];
		
		for (int i = 0 ; i < menu.getItemCount() ; i++) {
			if (menu.getItem(i).equals(item)) ret = i;
		}
		return ret;
	}
	
	/**
	* Add this for listeners
	*/

	private void attacherReactions() {
		dialogAPropos.getBoutonFermer().addActionListener(this);
		dialogAide.getBoutonFermer().addActionListener(this);
		dialogInfos.getBoutonFermer().addActionListener(this);
	}

	public void windowActivated(WindowEvent e) {}

	public void windowClosed(WindowEvent e) {}
	
	/**
	* Method called just before closing the application
	*/

	public void windowClosing(WindowEvent e) {
		int option = JOptionPane.showConfirmDialog(ihm, "Voulez-vous vraiment quitter ?", "Quitter ?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (option == 0) {
			try {
				out.close();
			} catch (Exception ex) {
				terminal.println("fermeture impossible");
			}
			preferences.sauver();
			System.setSecurityManager(null);
			System.exit(0);
		}
	}

	public void windowDeactivated(WindowEvent e) {}

	public void windowDeiconified(WindowEvent e) {}

	public void windowIconified(WindowEvent e) {}

	public void windowOpened(WindowEvent e) {}

	public void keyPressed(KeyEvent e) {}
	
	/**
	* Method called when a key is released
	*/

	public void keyReleased(KeyEvent e) {
		if (e.getSource() == dialogRechercher.getRechercheField()) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) dialogRechercher.getFindButton().doClick();
		} else dialogPreferences.updateFont();
	}

	public void keyTyped(KeyEvent e) {}

}
