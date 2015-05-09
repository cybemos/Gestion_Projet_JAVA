package projet.ihm;

import projet.datas.Preferences;
import projet.ctrl.Controleur;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;
import java.io.Serializable;
import javax.swing.plaf.ColorUIResource;

public class DialogPreferences extends JDialog implements Serializable {

	/**
	*version pour la sauvegarde Serializable.
	*@see Serializable
	*/

	private static final long serialVersionUID = 0;
	private Controleur ctrl;
	private JTabbedPane tabbedPane;
	private Bouton boutonFermer;
	private JButton[] colorButtons;
	private BorderPanel panelCouleurs;
	private BorderPanel panelPolice;
	private JPanel panelCenterPolice;
	private JComboBox<String> fonts;
	private JCheckBox[] styles;
	private JTextField tailleFont;
	private JTextField testFont;

	public DialogPreferences(Controleur ctrl) {
		super(ctrl.getIhm(), "Preferences", false);
		this.ctrl = ctrl;
		init();
		creerInterface();
		attacherReactions();
		this.setSize ( 400, 400 );
		this.setResizable(false);
		this.setVisible (false);
	}

	public JButton[] getBoutonsCouleur() {
		return colorButtons;
	}

	public Bouton getBoutonFermer() {
		return boutonFermer;
	}

	public void updateFont() {
		Font font = Font.decode((String) fonts.getSelectedItem());
		int style = Font.PLAIN;
		if (styles[0].isSelected()) style += Font.ITALIC;
		if (styles[1].isSelected()) style += Font.BOLD;
		font = font.deriveFont(style);
		
		int taille = 15;
		try {
			taille = Integer.parseInt(tailleFont.getText());
			if (taille < 9 || taille > 30) taille = 15;
		} catch (NumberFormatException e) {}
		font = font.deriveFont((float) taille);
		testFont.setFont(font);
		ctrl.getPreferences().setFont(font);
	}

	private void initFont() {
		Font font = ctrl.getPreferences().getFont();
		fonts.setSelectedItem(font.getName());
		styles[0].setSelected(font.isItalic());
		styles[1].setSelected(font.isBold());
		tailleFont.setText(Integer.toString(font.getSize()));
		updateFont();
	}

	private void init() {
		testFont = new JTextField("font");
		tailleFont = new JTextField();
		tailleFont.setHorizontalAlignment(JTextField.CENTER);
		tailleFont.setFont(new Font("Arial", Font.PLAIN, 18));
		testFont.setPreferredSize(new Dimension(100, 45));
		testFont.setHorizontalAlignment(JTextField.CENTER);
		testFont.setEditable(false);
		testFont.setFocusable(false);
		testFont.setBackground(Color.WHITE);
		fonts = new JComboBox<String>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
		styles = new JCheckBox[2];
		styles[0] = new JCheckBox("Italique");
		styles[1] = new JCheckBox("Gras");
		tabbedPane = new JTabbedPane();
		boutonFermer = new Bouton("Fermer");
		boutonFermer.setPreferredSize(new Dimension(110, 25));
		panelCouleurs = new BorderPanel(new GridLayout(0, 2, 10, 10), 10, 0);
		panelPolice = new BorderPanel(new BorderLayout(0, 20), 10, 10);
		panelCenterPolice = new JPanel(new GridLayout(0, 2, 10, 10));
		colorButtons = new JButton[3];
		Preferences prefs = ctrl.getPreferences();
		Color[] couleurs = {prefs.getMenusColor(), prefs.getBackgroundColor(), prefs.getForegroundColor()};
		for (int i = 0 ; i < colorButtons.length ; i++) {
			colorButtons[i] = new JButton("");
			colorButtons[i].setBackground(couleurs[i]);
		}
		initFont();
	}

	private JTextArea initTextArea(String texte) {
		JTextArea area = new JTextArea();
		area.setEditable(false);
		area.setFocusable(false);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.setFont(new Font("Arial", Font.PLAIN, 15));
		area.setBackground(new ColorUIResource(238, 238, 238));
		area.setText(texte);
		return area;
	}

	private void creerInterface() {
		this.setLayout(new BorderLayout());
		String[] noms = {"couleur barre d'outils", "couleur background texte", "couleur texte"};
		for (int i = 0 ; i  < noms.length ; i++) {
			panelCouleurs.add(initTextArea(noms[i]));
			panelCouleurs.add(colorButtons[i]);
		}
		panelPolice.add(panelCenterPolice, BorderLayout.CENTER);
		panelCenterPolice.add(initTextArea("Fonts disponibles"));
		panelCenterPolice.add(fonts);
		panelCenterPolice.add(initTextArea("changer le style"));
		JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
		for (int i = 0 ; i < styles.length ; i++) {
			panel.add(styles[i]);
		}
		panelCenterPolice.add(panel);
		panelCenterPolice.add(initTextArea("changer la taille :" + '\n' + "min : 9" + '\n' + "max : 30"));
		panelCenterPolice.add(tailleFont);
		panelPolice.add(testFont, BorderLayout.SOUTH);
		tabbedPane.addTab("Couleurs", panelCouleurs);
		tabbedPane.addTab("Police", panelPolice);
		this.add(tabbedPane, BorderLayout.CENTER);
		JPanel panelSud = new JPanel(new FlowLayout(FlowLayout.CENTER/*TRAILING*/));
		this.add(panelSud, BorderLayout.SOUTH);
		panelSud.add(boutonFermer);
	}

	private void attacherReactions() {
		boutonFermer.addActionListener(ctrl);
		for (int i = 0 ; i < colorButtons.length ; i++) {
			colorButtons[i].addActionListener(ctrl);
		}
		fonts.addItemListener(ctrl);
		for (int i = 0 ; i < styles.length ; i++) {
			styles[i].addItemListener(ctrl);
		}
		tailleFont.addKeyListener(ctrl);
	}
	
	public void setVisible(boolean b) {
		if (b) this.setLocationRelativeTo(ctrl.getIhm());
		super.setVisible(b);
	}

}
