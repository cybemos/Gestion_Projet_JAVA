package projet.ihm;

import projet.datas.Preferences;
import java.util.EventObject;
import java.io.Serializable;

/**
* Class which represents an event for the preferences
* @author <a href="mailto:nicolas.sonet@hotmail.fr">Nicolas Sonet</a>
* @version 1.0
*/

public class PreferencesEvent extends EventObject implements Serializable {

	/**
	* version for the save Serializable.
	* @see Serializable
	*/

	private static final long serialVersionUID = 0;
	private Preferences prefs;
	
	/**
	* Builder of the class
	* @param source source of the event
	* @param newValue new Preferences associed to this event
	*/

	public PreferencesEvent(Object source, Preferences newValue) {
		super(source);
		prefs = newValue;
	}
	
	/**
	* Return the new Preferences
	*/

	public Preferences getPreferences() {
		return prefs;
	}

}
