package projet.ihm;

import projet.ctrl.Controleur;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.io.Serializable;

public class DialogExecuter extends JDialog implements Serializable {

	private static final long serialVersionUID = 0;
	private Controleur ctrl;
	private Bouton boutonConfirmer;
	private Bouton boutonFermer;
	private JComboBox<String> classesChooser;
	
	public DialogExecuter(Controleur ctrl) {
		super(ctrl.getIhm(), "Executer", false);
		this.ctrl = ctrl;
		init();
		creerInterface();
		attacherReactions();
		this.setSize ( 300, 130 );
		this.setResizable(false);
		this.setVisible(false);
	}
	
	public Bouton getBoutonConfirmer() {
		return boutonConfirmer;
	}
	
	public Bouton getBoutonFermer() {
		return boutonFermer;
	}
	
	public String getValue() {
		Object obj = classesChooser.getSelectedItem();
		String value = "";
		if (obj != null) value = obj.toString();
		return value;
	}
	
	public void update() {
		classesChooser.removeAllItems();
		Class<?>[] classes = ctrl.getClasses();
		//System.out.println(classes.length);
		for (int i = 0 ; i < classes.length ; i++) {
			//System.out.println("classe "+(i+1)+" : "+classes[i]);
			if (classes[i] != null) classesChooser.addItem(classes[i].getName());
		}
	}
	
	private void init() {
		classesChooser = new JComboBox<String>();
		update();
		boutonConfirmer = new Bouton("Confirmer");
		boutonConfirmer.setPreferredSize(new Dimension(90, 25));
		boutonFermer = new Bouton("Fermer");
		boutonFermer.setPreferredSize(new Dimension(90, 25));
	}
	
	private void creerInterface() {
		setLayout(new BorderLayout());
		JLabel label = new JLabel("Classe à executer");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		add(label, BorderLayout.NORTH);
		BorderPanel bp = new BorderPanel(new BorderLayout());
		bp.add(classesChooser, BorderLayout.CENTER);
		add(bp, BorderLayout.CENTER);
		JPanel panelSud = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelSud.add(boutonConfirmer);
		panelSud.add(boutonFermer);
		add(panelSud, BorderLayout.SOUTH);
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
