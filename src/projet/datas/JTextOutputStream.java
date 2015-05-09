package projet.datas;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;

/**
* Class which represents an output like a terminal
* @author <a href="mailto:nicolas.sonet@hotmail.fr">Nicolas Sonet</a>
* @version 1.0
*/

public class JTextOutputStream extends PrintStream {

	private JTextPane textComponent;
	private static ByteArrayOutputStream os = new ByteArrayOutputStream();
	
	/**
	* Builder of the class
	* @param textComponent the desired output
	*/

	public JTextOutputStream(JTextPane textComponent) {
		super(os, true);
		this.textComponent = textComponent;
	}
	
	/**
	* Write the string texte in the textComponent.
	* @param texte text to write
	*/

	public void update(String texte) {
		StyledDocument doc = textComponent.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), texte, null);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	* @see super#write(byte[], int, int)
	*/

	public void write(byte[] buf, int off, int len) {
		super.write(buf, off, len);
		String str = new String(buf, off, len);
		update(str);
	}
	
	/**
	* @see super#write(byte[])
	*/

	public void write(byte[] b) {
		String str = new String(b);
		update(str);
	}
	
	/**
	* @see super#write(int)
	*/

	public void write(int b) {
		super.write(b);
		update(Character.toString((char) b));
	}

}
