package de.jardas.drakensang;

import java.io.InputStream;

import de.jardas.drakensang.dao.SavegameDao;
import de.jardas.drakensang.gui.MainFrame;
import de.jardas.drakensang.shared.ApplicationInfo;
import de.jardas.drakensang.shared.Launcher;
import de.jardas.drakensang.shared.Program;
import de.jardas.drakensang.shared.Settings;

public class Main implements Runnable {
	public void run() {
		Launcher.run(new MainProgram());
	}

	public static void main(String[] args) {
		Settings.init(".drakensang-character-editor-2", EditorSettings.class);

		final Main main = new Main();
		main.run();
	}

	private static class MainProgram implements Program<MainFrame> {
		public String getResourceBundleName() {
			return getClass().getPackage().getName() + ".messages";
		}

		public MainFrame createMainFrame() {
			return new MainFrame();
		}

		public void onMainFrameVisible(MainFrame mainFrame) {
			mainFrame.showLoadDialog();
		}

		public InputStream getFeatureHistory() {
			return getClass().getResourceAsStream("feature-history.xml");
		}

		public void shutDown() {
			SavegameDao.close();
		}

		public ApplicationInfo getApplicationInfo() {
			return ApplicationInfo.load(getClass().getResourceAsStream(
					"version.properties"));
		}
	}
}
