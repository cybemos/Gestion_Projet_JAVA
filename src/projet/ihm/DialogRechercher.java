package projet.ihm;

import projet.ctrl.Controleur;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import java.io.Serializable;
import javax.swing.plaf.ColorUIResource;

public class DialogRechercher extends JDialog implements Serializable {

	/**
	*version pour la sauvegarde Serializable.
	*@see Serializable
	*/

	private static final long serialVersionUID = 0;
	private Controleur ctrl;
	private Bouton closeButton;
	private Bouton findButton;
	private JCheckBox boutonCasseRespectee;
	private JTextField recherche;

	public DialogRechercher(Controleur ctrl) {
		super(ctrl.getIhm(), "Recherche", false);
		this.ctrl = ctrl;
		init();
		creerInterface();
		attacherReactions();
		this.setSize ( 300, 150 );
		this.setResizable(false);
		this.setVisible(false);
	}

	public JCheckBox getBoutonCasseRespectee() {
		return boutonCasseRespectee;
	}

	public Bouton getCloseButton() {
		return closeButton;
	}

	public Bouton getFindButton() {
		return findButton;
	}

	public JTextField getRechercheField() {
		return recherche;
	}

	public String getRecherche() {
		return recherche.getText();
	}

	public void init() {
		closeButton = new Bouton("Close");
		closeButton.setPreferredSize(new Dimension(90, 25));
		findButton = new Bouton("Find");
		findButton.setPreferredSize(new Dimension(90, 25));
		boutonCasseRespectee = new JCheckBox("Respecter la casse");
		recherche = new JTextField();
		recherche.setHorizontalAlignment(JTextField.CENTER);
		recherche.setPreferredSize(new Dimension(100, 45));
	}

	public void creerInterface() {
		setLayout(new BorderLayout());
		BorderPanel center = new BorderPanel(new BorderLayout(0, 20));
		center.add(recherche, BorderLayout.CENTER);
		center.add(boutonCasseRespectee, BorderLayout.SOUTH);
		this.add(center, BorderLayout.CENTER);
		JPanel panelSud = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		panelSud.add(closeButton);
		panelSud.add(findButton);
		this.add(panelSud, BorderLayout.SOUTH);
	}

	public void attacherReactions() {
		boutonCasseRespectee.addItemListener(ctrl);
		closeButton.addActionListener(ctrl);
		findButton.addActionListener(ctrl);
		recherche.addKeyListener(ctrl);
	}
	
	public void setVisible(boolean b) {
		if (b) this.setLocationRelativeTo(ctrl.getIhm());
		super.setVisible(b);
	}

}
