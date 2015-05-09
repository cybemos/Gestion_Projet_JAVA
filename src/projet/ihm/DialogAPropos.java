package projet.ihm;

import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JDialog;
import javax.swing.JTextPane;
import javax.swing.JPanel;
import java.io.Serializable;
import javax.swing.plaf.ColorUIResource;

public class DialogAPropos extends JDialog implements Serializable {

	/**
	*version pour la sauvegarde Serializable.
	*@see Serializable
	*/

	private static final long serialVersionUID = 0;
	private Bouton boutonFermer;
	private Frame owner;
	private BorderPanel center;
	private JTextPane text;

	public DialogAPropos(Frame owner) {
		super(owner, "A Propos", false);
		this.owner = owner;
		init();
		creerInterface();
		this.setSize ( 400, 400 );
		this.setResizable(false);
		this.setVisible (false);
	}

	public Bouton getBoutonFermer() {
		return boutonFermer;
	}

	private String getText() {
		String ret = "L'application consiste à gerer des projet en java." + System.getProperty("line.separator");
		ret += "" + System.getProperty("line.separator");
		return ret;
	}

	private void init() {
		center = new BorderPanel(new BorderLayout());
		text = new JTextPane();
		text.setEditable(false);
		text.setFont(new Font("Arial", Font.PLAIN, 15));
		text.setBackground(new ColorUIResource(238, 238, 238));
		text.setText(getText());
		boutonFermer = new Bouton("Fermer");
		boutonFermer.setPreferredSize(new Dimension(110, 25));
	}

	private void creerInterface() {
		this.setLayout(new BorderLayout());
		this.add(center, BorderLayout.CENTER);
		center.add(text, BorderLayout.CENTER);
		JPanel panelSud = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelSud.add(boutonFermer);
		this.add(panelSud, BorderLayout.SOUTH);
	}
	
	public void setVisible(boolean b) {
		if (b) this.setLocationRelativeTo(owner);
		super.setVisible(b);
	}

}
