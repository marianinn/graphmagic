package name.dlazerka.gc.ui;

import javax.swing.*;

/**
 * @author Dzmitry Lazerka
 */
public class UI {
	public static void show() {
		initLookAndFeel();

		MainFrame mainFrame = new MainFrame();
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	private static void initLookAndFeel() {
		try {
			try {
				UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName()
				);
			}
			catch (UnsupportedLookAndFeelException e) {
				UIManager.setLookAndFeel(
					UIManager.getCrossPlatformLookAndFeelClassName()
				);

			}
		}
		catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		catch (InstantiationException e1) {
			e1.printStackTrace();
		}
		catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
	}
}
