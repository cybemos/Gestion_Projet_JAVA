package projet.ihm;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.Component;
import java.awt.Color;
import javax.swing.JPanel;
import java.io.Serializable;

public class BorderPanel extends JPanel implements Serializable {

	/**
	*version pour la sauvegarde Serializable.
	*@see Serializable
	*/

	private static final long serialVersionUID = 0;
	private JPanel panelCentre;
	private JPanel[] panels = {new JPanel(), new JPanel(), new JPanel(), new JPanel()};
	
	public BorderPanel() {
		super(new BorderLayout());
		init(null);
	}

	public BorderPanel(LayoutManager layout) {
		super(new BorderLayout());
		init(layout);
	}

	public BorderPanel(LayoutManager layout, int hgap, int vgap) {
		super(new BorderLayout(hgap, vgap));
		init(layout);
	}

	public BorderPanel(int hgap, int vgap) {
		super(new BorderLayout(hgap, vgap));
		init(null);
	}

	public void init(LayoutManager layout) {
		panelCentre = new JPanel();
		if (layout != null) {
			panelCentre.setLayout(layout);
		}
		String[] constraints = {BorderLayout.EAST, BorderLayout.WEST, BorderLayout.NORTH, BorderLayout.SOUTH};
		for (int i = 0 ; i < panels.length ; i++) {
			super.add(panels[i], constraints[i]);
		}
		super.add(panelCentre, BorderLayout.CENTER);
	}
	
	/**
	* @see super#add(Component, Object)
	*/

	public void add(Component comp, Object constraints) {
		panelCentre.add(comp, constraints);
	}
	
	/**
	* @see super#add(Component)
	*/

	public Component add(Component comp) {
		return panelCentre.add(comp);
	}
	
	/**
	* @see super#setBackground(Color)
	*/

	public void setBackground(Color couleur) {
		super.setBackground(couleur);
		try {
			panelCentre.setBackground(couleur);
			for (int i = 0 ; i < panels.length ; i++) {
				panels[i].setBackground(couleur);
			}
		} catch (NullPointerException e) {}
	}

}
