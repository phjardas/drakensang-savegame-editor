package de.jardas.drakensang;

import java.io.InputStream;
import java.util.Locale;

import javax.swing.JFrame;

import de.jardas.drakensang.dao.SavegameDao;
import de.jardas.drakensang.gui.MainFrame;
import de.jardas.drakensang.shared.Launcher;
import de.jardas.drakensang.shared.Program;
import de.jardas.drakensang.shared.Settings;

public class Main implements Runnable {
	private static Program program = new MainProgram();
	private static Launcher launcher = new Launcher(program);

	public void run() {
		launcher.run();
	}

	public static void main(String[] args) {
		Settings.init(".drakensang-character-editor-2", EditorSettings.class);

		final Main main = new Main();
		main.run();
	}

	public static MainFrame getFrame() {
		return (MainFrame) program.getMainFrame();
	}

	public static void handleException(Exception e) {
		launcher.handleException(e);
	}

	public static void setUserLocale(Locale locale) {
		launcher.setUserLocale(locale);
	}

	private static class MainProgram implements Program {
		private MainFrame mainFrame;

		public String getResourceBundleName() {
			return getClass().getPackage().getName() + ".messages";
		}

		public void showMainFrame() {
			mainFrame = new MainFrame();
			mainFrame.setVisible(true);
			mainFrame.showLoadDialog();
		}

		public JFrame getMainFrame() {
			return mainFrame;
		}

		public InputStream getFeatureHistory() {
			return getClass().getResourceAsStream("feature-history.xml");
		}

		public void shutDown() {
			SavegameDao.close();
			mainFrame.setVisible(false);
		}
	}
}
