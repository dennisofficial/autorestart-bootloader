package me.dennis.autore_bootloader.core;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class BootLoader {

	public static String VERSION = "1.0";

	public static void main(String[] args) {
		write("Initiated AutoRestart BootLoader v" + VERSION);

		write("Checking if BootLoader was force closed on last run");

		while (true) {

			write("Starting Minecraft Server...");

			write("Server has been stopped. Checking for restart request...");

			if (requestedRestart()) {
				write("Restart request found!");
			} else {
				write("No restart request found. Closing BootLoader!stop");
				break;
			}
		}
	}

	public static void startServer() {
		try {
			ProcessBuilder builder = new ProcessBuilder("java", "-jar", "./Spigot.jar");
			builder.redirectErrorStream(true);
			Process process = builder.start();
			InputStream out = process.getInputStream();
			OutputStream in = process.getOutputStream();

			byte[] buffer = new byte[4000];
			while (isAlive(process)) {
				int no = out.available();
				if (no > 0) {
					int n = out.read(buffer, 0, Math.min(no, buffer.length));
					String line = new String(buffer, 0, n);
					System.out.print(line);
				}

				int ni = System.in.available();
				if (ni > 0) {
					int n = System.in.read(buffer, 0, Math.min(ni, buffer.length));
					in.write(buffer, 0, n);
					in.flush();
				}

				Thread.sleep(10);
			}
		} catch (Exception e) {
			System.out.println("Error during server start:");
			e.printStackTrace();
			System.out.println("End of error.");
		}
	}

	public static boolean isAlive(Process p) {
		try {
			p.exitValue();
			return false;
		} catch (IllegalThreadStateException e) {
			return true;
		}
	}

	public static boolean requestedRestart() {
		File file = new File("restart");
		if (file.exists()) {
			file.delete();
			return true;
		}
		return false;
	}

	public static void write(String s) {
		System.out.println("BOOTLOADER: " + s);
	}

}
