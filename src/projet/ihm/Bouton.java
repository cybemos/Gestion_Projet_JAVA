package projet.ihm;

import java.awt.Cursor;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import java.io.Serializable;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Bouton extends JButton implements Serializable, MouseListener {

	/**
	*version pour la sauvegarde Serializable.
	*@see Serializable
	*/

	private static final long serialVersionUID = 0;
	private Color couleurPassive;
	private Color couleurActive;
	private Color couleurBorder;
	private Icon icon;

	public Bouton(String text) {
		this(text, Color.BLACK);
	}

	public Bouton(String text, Icon icon) {
		this(text, Color.BLACK);
		this.setIcon(icon);
	}

	public Bouton(String text, Icon icon, Color couleurBorder) throws NullPointerException {
		this(text, Color.LIGHT_GRAY, new Color(210, 210, 210), couleurBorder);
		this.setIcon(icon);
	}

	public Bouton(String text, Color couleurBorder) throws NullPointerException {
		this(text, Color.LIGHT_GRAY, new Color(210, 210, 210), couleurBorder);
	}

	public Bouton(String text, Color couleurPassive, Color couleurActive) throws NullPointerException {
		this(text, couleurPassive, couleurActive, Color.BLACK);
	}

	public Bouton(String text, Color couleurPassive, Color couleurActive, Color couleurBorder) throws NullPointerException {
		super(text);
		if (couleurPassive == null || couleurActive == null || couleurBorder == null) throw new NullPointerException();
		this.couleurPassive = couleurPassive;
		this.couleurActive = couleurActive;
		this.couleurBorder = couleurBorder;
		init();
		attacherReactions();
	}

	public void setEnabled(boolean b) {
		if (b) this.setBackground(couleurPassive);
		else this.setBackground(new Color(240 , 240, 240));
		super.setEnabled(b);
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
		super.setIcon(icon);
	}

	private void setColoredImage(Color couleur) {
		try {
			if (icon != null) {
				BufferedImage buffered = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics g = buffered.getGraphics();
				icon.paintIcon(null, g, 0, 0);
				g.dispose();
				int alpha;
				Color couleur1;
				Color c = couleur;
				for (int i = 0 ; i < buffered.getWidth() ; i++) {
					for (int j = 0 ; j < buffered.getHeight() ; j++) {
						alpha = new Color(buffered.getRGB(i, j), true).getAlpha();
						couleur1 = new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
						buffered.setRGB(i, j, couleur1.getRGB());
					}
				}
				setIcon(new ImageIcon(buffered));
			}
		} catch (Exception e) {
			System.out.println("erreur : " + e.getMessage());
		}
	}

	public void setForeground(Color couleur) {
		super.setForeground(couleur);
		setColoredImage(couleur);
		this.setBorder(BorderFactory.createLineBorder(couleur, 1, true));
	}

	private void attacherReactions() {
		this.addMouseListener(this);
	}

	private void init() {
		this.setBackground(couleurPassive);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.setBorder(BorderFactory.createLineBorder(couleurBorder, 1, true));
	}

	public void mouseEntered(MouseEvent e) {
		if (isEnabled()) this.setBackground(couleurActive);
	}

	public void mouseExited(MouseEvent e) {
		if (isEnabled()) this.setBackground(couleurPassive);
	}

	public void mouseClicked(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {
		if (isEnabled()) {
			if (e.getButton() == MouseEvent.BUTTON1) this.setBackground(couleurPassive);
		}
	}

}

