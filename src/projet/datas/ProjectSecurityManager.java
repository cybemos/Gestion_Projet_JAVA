package projet.datas;

import java.awt.Frame;
import java.security.Permission;
import projet.ctrl.Controleur;
import static projet.ctrl.Controleur.terminal;

/**
* Class which replace the default SecurityManager to prevent System.exit(...) when you execute an other class
* @author <a href="mailto:nicolas.sonet@hotmail.fr">Nicolas Sonet</a>
* @version 1.0
*/

public class ProjectSecurityManager extends SecurityManager {

	private Controleur ctrl;
	
	/**
	* Builder of the class
	* @param ctrl Controller of the application
	*/

	public ProjectSecurityManager(Controleur ctrl) {
		this.ctrl = ctrl;
	}
	
	/**
	* @throws SecurityException if permission.getName().contains("exitVM")
	* @see super#checkPermission(Permission)
	*/
	
	public void checkPermission( Permission permission )throws SecurityException {
		if(permission.getName().contains("exitVM")) {
			Frame[] activeframes = Frame.getFrames();
			// all without the first
			for (int i = 1; i<activeframes.length; i++) {
				activeframes[i].dispose();
			}
			throw new SecurityException("prevent from closing application...");
        }
     }

}
