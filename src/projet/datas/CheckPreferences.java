package projet.datas;

import projet.ihm.PreferencesListener;
import projet.ihm.PreferencesEvent;
import java.util.ArrayList;

/**
* Class which handle PreferencesListeners
* @author <a href="mailto:nicolas.sonet@hotmail.fr">Nicolas Sonet</a>
* @version 1.0
*/

public class CheckPreferences {

	private ArrayList<PreferencesListener> listeners;
	
	/**
	* Constructor of the class
	*/
	
	public CheckPreferences() {
		listeners = new ArrayList<PreferencesListener>();
	}
	
	/**
	* Add a PreferencesListener. His method preferencesChanged will be called when a PreferenceEvent is detected
	* @see #firePreferencesChanged
	* @see #removePreferencesListener
	*/

	public void addPreferencesListener(PreferencesListener listener) {
		listeners.add(listener);
	}
	
	/**
	* Remove a PreferencesListener
	*/

	public void removePreferencesListener(PreferencesListener listener) {
		listeners.remove(listener);
	}
	
	/**
	* Return all listeners of the instance
	*/

	public PreferencesListener[] getListeners() {
		PreferencesListener[] ret = new PreferencesListener[listeners.size()];
		for (int i = 0 ; i < ret.length ; i++) ret[i] = listeners.get(i);
		return ret;
	}
	
	/**
	* Call the method preferencesChanged of all PreferencesListeners
	*/

	public void firePreferencesChanged(PreferencesEvent e) {
		for (int i = 0 ; i < listeners.size() ; i++) listeners.get(i).preferencesChanged(e);
	}

}
