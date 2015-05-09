package projet.ihm;

import projet.ctrl.Controleur;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import java.io.Serializable;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class DialogInfos extends JDialog implements Serializable {

	/**
	*version pour la sauvegarde Serializable.
	*@see Serializable
	*/

	private static final long serialVersionUID = 0;
	private Controleur ctrl;
	private Bouton boutonFermer;
	private JTabbedPane tabbedPane;

	public DialogInfos(Controleur ctrl) {
		super(ctrl.getIhm(), "Infos", false);
		this.ctrl = ctrl;
		init();
		update();
		creerInterface();
		this.setMinimumSize (new Dimension(500, 300));
		this.setSize ( 650, 350 );
		this.setResizable(true);
		this.setVisible (false);
	}

	public Bouton getBoutonFermer() {
		return boutonFermer;
	}

	public void update() {
		Class<?>[] classes = ctrl.getClasses();
		tabbedPane.removeAll();
		for (int i = 0 ; i < classes.length ; i++) {
			if (classes[i] != null) {
				tabbedPane.addTab(classes[i].getSimpleName(), new ImageIcon(getClass().getResource("/data/images/menus/java_15px.png")), new JScrollPane(getInfos(classes[i])), classes[i].getName());
			}
		}
	}

	private JTree getInfos(Class<?> classe) {
		DefaultMutableTreeNode category = null;
		DefaultMutableTreeNode element = null;
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(classe.getName());
		
		category = new DefaultMutableTreeNode("Champs");
		top.add(category);
		Field[] fields = classe.getDeclaredFields();
		for (int i = 0 ; i < fields.length ; i++) {
			category.add(new DefaultMutableTreeNode(fields[i]));
		}
		
		category = new DefaultMutableTreeNode("Constructeurs");
		top.add(category);
		Constructor<?>[] constructeurs = classe.getConstructors();
		for (int i = 0 ; i < constructeurs.length ; i++) {
			category.add(new DefaultMutableTreeNode(constructeurs[i]));
		}
		
		category = new DefaultMutableTreeNode("Methodes");
		top.add(category);
		Method[] methodes = classe.getDeclaredMethods();
		for (int i = 0 ; i < methodes.length ; i++) {
			category.add(new DefaultMutableTreeNode(methodes[i]));
		}
		
		JTree ret = new JTree(top);
		ret.setScrollsOnExpand(true);
		ret.setToggleClickCount(1);
		return ret;
	}

	private void init() {
		tabbedPane = new JTabbedPane(JTabbedPane.LEFT, JTabbedPane.SCROLL_TAB_LAYOUT);
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
		if (b) this.setLocationRelativeTo(ctrl.getIhm());
		super.setVisible(b);
	}

}
