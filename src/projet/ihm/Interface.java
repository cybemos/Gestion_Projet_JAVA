package projet.ihm;

import projet.datas.*;
import projet.ctrl.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.net.*;

public class Interface extends JFrame implements Serializable {

	/**
	*version pour la sauvegarde Serializable.
	*@see Serializable
	*/

	private static final long serialVersionUID = 0;

	/**
	*Controleur de l'application.
	*/

	private Controleur ctrl;
	private JPanel panelBarreOutils;
	private JPanel panelNord;
	private JTextPane area;
	private Color couleurBorder;
	private JMenuBar bar;

	/**
	*items[0] : itemsFichier
	*items[1] : itemsEdition
	*items[2] : itemsExecution
	*items[3] : itemsAffichage
	*items[4] : itemsOutils
	*items[5] : itemsAide
	*/

	private JMenuItem[][] items;
	private Bouton[] boutons;

	public Interface(Controleur ctrl) {
		super("aucun projet");
		this.ctrl = ctrl;
		couleurBorder = Color.BLACK;
		this.init();
		this.creerInterface();
		this.attacherReactions();
		this.setMinimumSize(new Dimension(400, 300));
		this.setSize( 600, 400 );
		this.setLocationRelativeTo(null);
		this.setIconImage(new ImageIcon(getClass().getResource("/data/images/boutons/code.png")).getImage());
		this.setDefaultCloseOperation ( DO_NOTHING_ON_CLOSE );
	}

	public void setMenuColor(Color couleur) {
		this.setBackground(couleur);
		panelBarreOutils.setBackground(couleur);
		bar.setBackground(couleur);
		bar.setBorder(BorderFactory.createLineBorder(couleur));
		MenuElement[] elements = bar.getSubElements();
		JComponent[] elements1 = new JComponent[elements.length];
		for (int i = 0 ; i < elements.length ; i++) elements1[i] = (JMenu) elements[i];
		setMenuColor(elements1, couleur);
	}

	private void setMenuColor(JComponent[] elements, Color couleur) {
		for (int i = 0 ; i < elements.length ; i++) {
			try {
				JMenu menu = (JMenu) elements[i];
				menu.getPopupMenu().setBackground(couleur);
				Component[] tab1 = menu.getMenuComponents();
				JComponent[] tab = new JComponent[tab1.length];
				for (int j = 0 ; j < tab.length ; j++) {
					tab[j] = (JComponent) tab1[j];
				}
				setMenuColor(tab, couleur);
			} catch (Exception e) {}
			elements[i].setBackground(couleur);
			elements[i].setBorder(BorderFactory.createLineBorder(couleur));
		}
	}

	public Bouton[] getBoutons() {
		return boutons;
	}

	public void setTextInArea(String texte) {
		area.setText(texte);
	}

	public String getTextInArea() {
		return area.getText();
	}

	public JTextPane getArea() {
		return area;
	}

	private void init() {
		panelBarreOutils = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelBarreOutils.setBackground(Color.WHITE);
		panelNord = new JPanel(new BorderLayout());
		area = new JTextPane();
		area.setAutoscrolls(true);
		area.setEditable(false);
		initMenu();
		initBoutons();
	}

	private KeyStroke getCtrlKeyStroke(int charactere) {
		return KeyStroke.getKeyStroke(charactere, InputEvent.CTRL_MASK);
	}

	private void initMenu() {
		bar = new JMenuBar();
		bar.setBackground(Color.WHITE);
		items = new JMenuItem[5][];
		int[] tailles = {6, 7, 3, 4, 4};
		for (int i = 0 ; i < items.length ; i++) items[i] = new JMenuItem[tailles[i]];
		
		char[] mnemonics = {'F', 'E', 'A', 'O', '?'};
		
		String[][] noms = {{"Fichier", "Nouveau", "Ouvrir", "Derniers projets", "Vider la liste", "Quitter"}, {"Projet", "Compiler", "Execution rapide", "Executer...", "Générer la javadoc", "Générer un jar file", "Infos sur le projet"}, {"Affichage", "Afficher Barre d'Outils", "Toujours afficher la fenetre"}, {"Outils", "Preferences", "Rechercher", "Réinitialiser recherche"}, {"?", "Aide", "A Propos", "Envoyer un retour"}};
		
		KeyStroke[][] raccourcis = {{null, getCtrlKeyStroke(KeyEvent.VK_N), getCtrlKeyStroke(KeyEvent.VK_O), null, null, getCtrlKeyStroke(KeyEvent.VK_Q)}, {null, null, null, null, null, null, null}, {null, null, null}, {null, null, getCtrlKeyStroke(KeyEvent.VK_F), null}, {null, null, null, null}};
		
		String[][] images = {{null, "/data/images/menus/new_15px.png", "/data/images/menus/open_15px.png", null, null, "/data/images/menus/exit_15px.png"}, {null, "/data/images/menus/code_15px.png", "/data/images/menus/run_15px.png", "/data/images/menus/run_15px.png", "/data/images/menus/java_15px.png", "/data/images/menus/java_15px.png", null}, {null, null, null}, {null, null, "/data/images/menus/research_15px.png", null}, {null, null, null, null}};

		for (int i = 0 ; i < items.length ; i++) {
			for (int j = 0 ; j < items[i].length ; j++) {
				if (j == 0) {
					items[i][j] = new JMenu(noms[i][j]);
					items[i][j].setMnemonic(mnemonics[i]);
				} else if (i == 2) {
					if (j == 1) items[i][j] = new JCheckBoxMenuItem(noms[i][j], true);
					else items[i][j] = new JCheckBoxMenuItem(noms[i][j], false);
				} else if (i == 0 && j == 3) {
					items[i][j] = new JMenu(noms[i][j]);
				} else {
					items[i][j] = new JMenuItem(noms[i][j]);
					if (raccourcis[i][j] != null) items[i][j].setAccelerator(raccourcis[i][j]);
				}
				if (images[i][j] != null) items[i][j].setIcon(new ImageIcon(getClass().getResource(images[i][j])));
			}
		}
	}

	private void initBoutons() {
		String[] pathImages = {"/data/images/boutons/new.png", "/data/images/boutons/open.png", "/data/images/boutons/code.png", "/data/images/boutons/run.png", "/data/images/boutons/java.png"};
		boutons = new Bouton[pathImages.length];
		String[] textes = {"", "", "Compiler", "Executer", "Javadoc"};
		String[] toolTips = {"Nouveau projet", "Ouvrir un projet", "Compiler le projet", "Executer le main", "Générer la javadoc"};

		for (int i = 0 ; i < boutons.length ; i++) {
			boutons[i] = new Bouton(textes[i], new ImageIcon(getClass().getResource(pathImages[i])), couleurBorder);
			boutons[i].setToolTipText(toolTips[i]);
		}
		boutons[3].setForeground(new Color(91, 192, 72));
	}

	private void creerInterface() {
		this.setLayout(new BorderLayout(10, 0));
		area.setBorder(BorderFactory.createLineBorder(couleurBorder));
		//panelNord.setBorder(BorderFactory.createLineBorder(couleurBorder));
		for (int i = 0 ; i < items.length ; i++) {
			bar.add(items[i][0]);
			for (int j = 1 ; j < items[i].length ; j++) {
				items[i][0].add(items[i][j]);
				if ((i == 0 && (j == 2 || j == 4)) || ((i == 1) && (j == 1 || j == 3 || j == 5)) || (i == 3 && j == 1) || (i == 4 && j == 2)) {
					((JMenu) items[i][0]).addSeparator();
				}
			}
		}
		this.setJMenuBar(bar);
		JScrollPane scroller = new JScrollPane(area);
		scroller.setAutoscrolls(true);
		this.add(panelNord, BorderLayout.NORTH);
		this.add(scroller, BorderLayout.CENTER);
		for (int i = 0 ; i < boutons.length ; i++) panelBarreOutils.add(boutons[i]);
		afficherBarreOutils(true);
	}

	public void afficherBarreOutils(boolean b) {
		panelNord.setVisible(false);
		panelNord.removeAll();
		if (b)panelNord.add(panelBarreOutils, BorderLayout.NORTH);
		panelNord.setVisible(true);
	}

	private void attacherReactions() {
		this.addWindowListener(ctrl);
		for (int i = 0 ; i < items.length ; i++) {
			for (int j = 1 ; j < items[i].length ; j++) {
				items[i][j].addActionListener(ctrl);
			}
		}
		for (int i = 0 ; i < boutons.length ; i++) {
			boutons[i].addActionListener(ctrl);
		}
	}

	public JMenuItem[][] getItems() {
		return items;
	}

}
