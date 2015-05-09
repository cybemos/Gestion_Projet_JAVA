package projet.ihm;

import projet.datas.Util;
import projet.ihm.BorderPanel;
import projet.ctrl.Controleur;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.io.Serializable;
import java.io.File;
import java.util.jar.Manifest;

public class DialogGenererJar extends JDialog implements Serializable {

	/**
	*version pour la sauvegarde Serializable.
	*@see Serializable
	*/

	private static final long serialVersionUID = 0;
	private Controleur ctrl;
	private Bouton boutonConfirmer;
	private Bouton boutonFermer;
	private JTextField outputFile;
	private JTextField classToExecute;

	public DialogGenererJar(Controleur ctrl) {
		super(ctrl.getIhm(), "Générer fichier JAR", false);
		this.ctrl = ctrl;
		init();
		creerInterface();
		attacherReactions();
		//this.setMinimumSize (new Dimension(350, 150));
		this.setSize ( 350, 150 );
		this.setResizable(false);
		this.setVisible (false);
	}
	
	public File getOutputFile() {
		File file = null;
		try {
			file = new File(outputFile.getText());
		} catch (Exception e) {}
		return file;
	}
	
	public Manifest getManifest() {
		return Util.getManifest(classToExecute.getText());
	}

	private void init() {
		boutonConfirmer = new Bouton("OK");
		boutonConfirmer.setPreferredSize(new Dimension(90, 25));
		boutonFermer = new Bouton("Annuler");
		boutonFermer.setPreferredSize(new Dimension(90, 25));
		outputFile = new JTextField();
		classToExecute = new JTextField();
	}

	private void creerInterface() {
		this.setLayout(new BorderLayout());
		JPanel panelCenter = new BorderPanel(new GridLayout(0, 2));
		panelCenter.add(new JLabel("Fichier de sortie :"));
		panelCenter.add(outputFile);
		panelCenter.add(new JLabel("Classe à executer :"));
		panelCenter.add(classToExecute);
		this.add(panelCenter, BorderLayout.CENTER);
		JPanel panelSud = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelSud.add(boutonConfirmer);
		panelSud.add(boutonFermer);
		this.add(panelSud, BorderLayout.SOUTH);
	}
	
	public Bouton getBoutonConfirmer() {
		return boutonConfirmer;
	}
	
	public Bouton getBoutonFermer() {
		return boutonFermer;
	}

	private void attacherReactions() {
		boutonConfirmer.addActionListener(ctrl);
		boutonFermer.addActionListener(ctrl);
	}
	
	public void setVisible(boolean b) {
		if (b) this.setLocationRelativeTo(ctrl.getIhm());
		super.setVisible(b);
	}

}
