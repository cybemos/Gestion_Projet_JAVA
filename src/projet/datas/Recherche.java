package projet.datas;

import projet.ctrl.Controleur;
import javax.swing.text.StyleConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import javax.swing.JTextPane;
import java.awt.Color;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
* class witch allow to do search
* @version 1.0
* @author <a href="mailto:nicolas_sonet@hotmail.fr">Nicolas Sonet</a>
*/

public class Recherche {

	private JTextPane textPane;
	private Controleur ctrl;
	private boolean casseRespectee;
	private String lastResearch;
	private Color lastColorBackground;
	private Color lastColorForeground;
	private boolean casseChanged;
	
	/**
	* Builder of the class
	* @param ctrl Controleur associed the the class
	* @param textPane JTextPane where the color of the text will change
	*/

	public Recherche(Controleur ctrl, JTextPane textPane) {
		this.textPane = textPane;
		this.ctrl = ctrl;
		casseRespectee = false;
		casseChanged = false;
		lastResearch = "";
		lastColorBackground = ctrl.getPreferences().getBackgroundColor();
		lastColorForeground = ctrl.getPreferences().getForegroundColor();
	}
	
	/**
	* Reset the research
	*/

	public void resetRecherche() {
		resetColorText();
	}
	
	/**
	* Reset the color of the text
	*/

	private void resetColorText() {
		StyledDocument textEditorDoc = textPane.getStyledDocument();
		Color colorBackground = ctrl.getPreferences().getBackgroundColor();
		Color colorForeground = ctrl.getPreferences().getForegroundColor();
		try {
			SimpleAttributeSet attrs = new SimpleAttributeSet();
			StyleConstants.setForeground(attrs, colorForeground);
			StyleConstants.setBackground(attrs, colorBackground);
			textEditorDoc.setCharacterAttributes(0, textPane.getText().length(), attrs, true);
		} catch (Exception e) {}
	}
	
	/**
	* set casseRespectee at false if you want the research found "yo" == "YO"
	*/

	public void respecterCasse(boolean casseRespectee) {
		if (this.casseRespectee != casseRespectee) casseChanged = true;
		this.casseRespectee = casseRespectee;
	}
	
	/**
	* Return the casse
	*/

	public boolean getCasseRespectee() {
		return casseRespectee;
	}
	
	/**
	* Update the color of the text with the reseach
	*/

	public void updateColor() {
		doRecherche(lastResearch);
	}
	
	/**
	* Return true if the newResearch is different from the old, else false
	*/

	private boolean hasChanged(String newRecherche) {
		boolean ret = true;
		ret &= lastColorBackground.equals(ctrl.getPreferences().getBackgroundColor());
		ret &= lastColorForeground.equals(ctrl.getPreferences().getForegroundColor());
		ret &= !casseChanged;
		casseChanged = false;
		if (casseRespectee) ret &= lastResearch.equals(newRecherche);
		else ret &= lastResearch.equalsIgnoreCase(newRecherche);
		ret = !ret;
		return ret;
	}
	
	/**
	* Allow to do a research
	* @param new research
	*/
	
	// TO DO remove "\" in the research
	public void doRecherche(String recherche) {
		if (hasChanged(recherche)) {
			resetColorText();
			lastColorBackground = ctrl.getPreferences().getBackgroundColor();
			lastColorForeground = ctrl.getPreferences().getForegroundColor();
			lastResearch = recherche;
			String pattern = "\\Q" + recherche + "\\E";
			setColor(pattern);
		}
	}
	
	/**
	* Change the color of the text when found the pattern
	* @param patternString pattern of the desired text
	*/

	private void setColor(String patternString) {
		String text = textPane.getText();
		if (!casseRespectee) {
			text = text.toUpperCase();
			patternString = patternString.toUpperCase();
		}
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			setColor(matcher.start(), matcher.end() - matcher.start());
			matcher.group(0);
		}
	}
	
	/**
	* Change the color of the text between a and b
	* @param a first index of the text
	* @param b second index of the text
	*/

	private void setColor(int a, int b) {
		StyledDocument textEditorDoc = textPane.getStyledDocument();
		Color colorBackground = ctrl.getPreferences().getBackgroundColor();
		Color colorForeground = ctrl.getPreferences().getForegroundColor();
		Color textColor = new Color(255 - colorForeground.getRed(), 255 - colorForeground.getGreen(), 255 - colorForeground.getBlue());
		Color backColor = new Color(255 - colorBackground.getRed(), 255 - colorBackground.getGreen(), 255 - colorBackground.getBlue());
		try {
			SimpleAttributeSet attrs = new SimpleAttributeSet();
			StyleConstants.setForeground(attrs, textColor);
			StyleConstants.setBackground(attrs, backColor);
			textEditorDoc.setCharacterAttributes(a, b, attrs, true);
		} catch (Exception e) {System.out.println("1:" + e.getMessage());}
	}

}
