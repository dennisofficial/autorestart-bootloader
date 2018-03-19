package me.dennis.autore_bootloader.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import me.dennis.autore_bootloader.core.BootLoader;

public class UpdateChecker {

	public static boolean checkUpdate() {
		String latestVersion = "";
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://gitlab.com/dennislysenko/AutoRestart-BootLoader/raw/master/src/version").openStream()));
			latestVersion = reader.readLine().trim();
			reader.close();
			if (latestVersion.equalsIgnoreCase(BootLoader.VERSION)) {
				return false;
			}
			else {
				return true;
			}
		} catch (IOException ex) {
			return false;
		}
	}

}
