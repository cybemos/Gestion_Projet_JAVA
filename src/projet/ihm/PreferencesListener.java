package projet.ihm;

import java.util.EventListener;

/**
* Interface which represents a listener for the preferences
* @author <a href="mailto:nicolas.sonet@hotmail.fr">Nicolas Sonet</a>
* @version 1.0
*/

public interface PreferencesListener extends EventListener {

	/**
	* Method called when a PreferencesEvent is detected
	*/

	public void preferencesChanged(PreferencesEvent e);

}
