package projet;

import static java.lang.System.out;
import projet.ctrl.Controleur;

/**
* class Launcher. 
* Allow to launch the Application
* @see projet.ctrl.Controleur
* @see projet.datas.Projet
* @author <a href="mailto:nicolas.sonet@hotmail.fr">Nicolas Sonet</a>
* @version 1.0
*/

public final class Launcher {

	private Launcher() {}
	
	/**
	* Launch the application
	*/

	public static void main ( String[] args ) {
		try {
			Controleur ctrl = new Controleur();
		} catch (Exception e) {
			e.printStackTrace();
			out.println(e.getClass());
			out.println(e.getMessage());
		}
	}
	
}
