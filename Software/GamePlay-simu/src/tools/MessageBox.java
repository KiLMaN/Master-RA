package tools;

/*
 Gaulupeau Brice - BTS IG A1 
 MessageBox.java
 Description : Implementation de fenêtre de type messageBox pour java
 Pour afficher une messageBox  : MessageBox.ShowMessage("TEST");
 Le nom de la fenêtre peut être personnalisé par l'appel a : MessageBox.windowName = "Mon Application";
 Le nom est static pour toute l'application
 */

import javax.swing.JOptionPane;

public class MessageBox {

	static public String windowName = "MyApplication";

	public MessageBox() {
	}

	public static void ShowMessage(String Text) {
		ShowMessage(Text, windowName);
	}

	public static void ShowMessage(String Text, String Title) {
		ShowMessage(Text, Title, JOptionPane.NO_OPTION);
	}

	public static void ShowMessage(String Text, String Title, int messageType) {
		JOptionPane.showMessageDialog(null, Text, Title, messageType);
	}

	public static void ShowError(String Text, String Title) {
		ShowMessage(Text, Title, JOptionPane.ERROR_MESSAGE);
	}

	public static void ShowError(Exception e) {
		ShowError("Une erreur est apparue :\r\n\r\n" + e.toString(),
				"Erreur ! ");
		e.printStackTrace();
	}

	public static void ShowWarning(String Text, String Title) {
		ShowMessage(Text, Title, JOptionPane.WARNING_MESSAGE);
	}

	public static void ShowInfo(String Text, String Title) {
		ShowMessage(Text, Title, JOptionPane.INFORMATION_MESSAGE);
	}

	/* input box */
	public static String ShowInput(String Text) {
		return ShowInput(Text, windowName);
	}

	public static String ShowInput(String Text, String Title) {
		return ShowInput(Text, Title, JOptionPane.QUESTION_MESSAGE);
	}

	public static String ShowInput(String Text, String Title, int messageType) {
		return JOptionPane.showInputDialog(null, Text, Title, messageType);
	}
}