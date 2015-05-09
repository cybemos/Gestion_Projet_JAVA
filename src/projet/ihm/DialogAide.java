package projet.ihm;

import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JDialog;
import javax.swing.JTextPane;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.io.Serializable;
import javax.swing.plaf.ColorUIResource;

public class DialogAide extends JDialog implements Serializable {

	/**
	*version pour la sauvegarde Serializable.
	*@see Serializable
	*/

	private static final long serialVersionUID = 0;
	private Bouton boutonFermer;
	private Frame owner;
	private JTextPane[] textes;
	private JTabbedPane tabbedPane;
	private String[] titresAide = {"Accueil", "Nouveau", "Ouvrir", "Dernier projets", "Compiler", "Executer", "Javadoc", "fichier JAR", "Afficher Barre d'Outils", "Toujours afficher fenetre", "Preferences", "Rechercher", "Envoyer un retour"};
	private String[] textesAide;
	
	public DialogAide(Frame owner) {
		super(owner, "Aide", false);
		this.owner = owner;
		init();
		creerInterface();
		this.setSize ( 650, 300 );
		this.setResizable(false);
		this.setVisible (false);
	}

	public Bouton getBoutonFermer() {
		return boutonFermer;
	}

	private void initTextesAide() {
		textesAide = new String[titresAide.length];
		textesAide[0] = "accueil d'aide";
		textesAide[1] = "Le bouton \"Nouveau\" permet de créer un nouveau projet à l'emplacement indiqué dans le système. Les sous-répertoires de projet \"ww\", \"src\",\"class\" et \"doc\" sont automatiquement créés.";
		textesAide[2] = "Le bouton \"Ouvrir\" permet de charger un projet déjà existant. Le projet est un répertoire qui doit contenir au moins les répertoires \"ww\", \"src\",\"class\" et \"doc\". Si ce n'est pas le cas, le projet ne sera pas chargé.";
		textesAide[3] = "Liste des derniers projets chargés ou créés via l'application. Cliquer sur un élément de la liste a pour effet de charger le projet.";
		textesAide[4] = "Compile tous les fichiers avec l'extension \".java\" dans le répertoire repertoire_projet/src puis mets les fichiers binaires obtenus par la compilation dans le répertoire repertoire_projet/class. Affiche les erreurs et warnings dans la zone de texte au centre de l'application.";
		textesAide[5] = "Execute le main de l'objet sélectionné. Les sorties standard et d'erreur sont redirigées dans la zone de texte au centre de l'application.";
		textesAide[6] = "Génère la javadoc du projet dans le répertoire repertoire_projet/doc. Compatible avec linux seulement.";	
		textesAide[7] = "Génère un fichier JAR à l'adresse indiquée contenant tout ce qui se trouve dans le répertoire repertoire_projet/class + le fichier Manifest.MF qui contient le lanceur (class contnant un main) du projet.";
		textesAide[8] = "Affiche ou cache la barre d'outils contenant des boutons d'accès rapide pour effectuer certaines actions récurentes.";
		textesAide[9] = "Permet d'afficher par dessus les autres fenetres l'application.";
		textesAide[10] = "Preferences de l'application. Permet de personnaliser les couleurs de l'application et la police de la zone de texte au centre de l'application.";
		textesAide[11] = "Permet de rechercher une chaine de caractère dans la zone de texte au centre de l'application. Les chaines de caractère trouvées dans le texte seront mis en évidence en changeant la couleur des caractères et du font.";
		textesAide[12] = "Vous pouvez envoyer un mail (impressions, améliorations souhaitées, encouragements :D) à l'adresse mail du développeur de l'application.";
	}

	private void init() {
		initTextesAide();
		tabbedPane = new JTabbedPane(JTabbedPane.LEFT, JTabbedPane.SCROLL_TAB_LAYOUT);
		textes = new JTextPane[textesAide.length];
		for (int i = 0 ; i < textes.length ; i++) {
			textes[i] = new JTextPane();
			textes[i].setEditable(false);
			textes[i].setFont(new Font("Arial", Font.PLAIN, 15));
			textes[i].setText(textesAide[i]);
			tabbedPane.addTab(titresAide[i], new JScrollPane(textes[i]));
		}
		boutonFermer = new Bouton("Fermer");
		boutonFermer.setPreferredSize(new Dimension(110, 25));
	}

	private void creerInterface() {
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);
		JPanel panelSud = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelSud.add(boutonFermer);
		this.add(panelSud, BorderLayout.SOUTH);
	}
	
	public void setVisible(boolean b) {
		if (b) this.setLocationRelativeTo(owner);
		super.setVisible(b);
	}

}
