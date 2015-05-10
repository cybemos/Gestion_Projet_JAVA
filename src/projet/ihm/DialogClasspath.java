package projet.ihm;

import projet.ctrl.Controleur;
import java.awt.Dimension;
import java.io.Serializable;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JFileChooser;

public class DialogClasspath extends JDialog implements Serializable {

	private static final long serialVersionUID = 0;
	private Controleur ctrl;
	private JList<File> classList;
	private Bouton boutonImport;
	private Bouton boutonSupprimer;
	private Bouton boutonFermer;
	private JFileChooser fileChooser;
	
	public DialogClasspath(Controleur ctrl) {
		super(ctrl.getIhm(), "Classpath", false);
		this.ctrl = ctrl;
		init();
		creerInterface();
		attacherReactions();
		this.setSize ( 500, 300 );
		this.setVisible(false);
	}
	
	public Bouton getBoutonImport() {
		return boutonImport;
	}
	
	public Bouton getBoutonSupprimer() {
		return boutonSupprimer;
	}
	
	public Bouton getBoutonFermer() {
		return boutonFermer;
	}
	
	public void importEvent() {
		int res = fileChooser.showDialog(ctrl.getIhm(), "Ajouter fichier au classpath");
		if (res == JFileChooser.APPROVE_OPTION) {
			ctrl.getPreferences().addClasspath(fileChooser.getSelectedFile());
			update();
		}
	}
	
	public void supprimerEvent() {
		File file = classList.getSelectedValue();
		if (file != null) {
			ctrl.getPreferences().removeClasspath(file);
			update();
		}
	}
	
	public void selectionChanged() {
		if (classList.getSelectedValue() == null) boutonSupprimer.setEnabled(false);
		else boutonSupprimer.setEnabled(true);
	}
	
	public void update() {
		File[] files = ctrl.getPreferences().getClasspath();
		classList.setListData(files);
		ctrl.setClassLoader(files);
		selectionChanged();
	}
	
	private void init() {
		classList = new JList<File>();
		boutonImport = new Bouton("Importer");
		boutonImport.setPreferredSize(new Dimension(90, 25));
		boutonSupprimer = new Bouton("Supprimer");
		boutonSupprimer.setPreferredSize(new Dimension(90, 25));
		boutonFermer = new Bouton("Fermer");
		boutonFermer.setPreferredSize(new Dimension(90, 25));
		fileChooser = new JFileChooser(System.getProperty("user.home") + System.getProperty("file.separator") + "Documents");
		fileChooser.setDragEnabled(false);
		fileChooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setApproveButtonToolTipText("Ajouter le fichier au classpath");
		
		update();
	}
	
	private void creerInterface() {
		setLayout(new BorderLayout());
		JPanel center = new BorderPanel(new BorderLayout());
		JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel panelSud = new JPanel(new FlowLayout(FlowLayout.CENTER));
		center.add(classList, BorderLayout.CENTER);
		panelBoutons.add(boutonImport);
		panelBoutons.add(boutonSupprimer);
		panelSud.add(boutonFermer);
		
		add(panelBoutons, BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);
		add(panelSud, BorderLayout.SOUTH);
	}
	
	private void attacherReactions() {
		boutonImport.addActionListener(ctrl);
		boutonSupprimer.addActionListener(ctrl);
		boutonFermer.addActionListener(ctrl);
		classList.addListSelectionListener(ctrl);
	}
	
	public void setVisible(boolean b) {
		if (b) this.setLocationRelativeTo(ctrl.getIhm());
		super.setVisible(b);
	}
	
}
