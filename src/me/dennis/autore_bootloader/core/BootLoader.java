package me.dennis.autore_bootloader.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class BootLoader {

	public static String VERSION = "1.0";
	public static String JAR_INIT = "java -jar Spigot.jar";

	public static void main(String[] args) {
		write("Initiated AutoRestart BootLoader v" + VERSION);

		write("Grabbing jar code...");
		
		fetchJarInit();
		
		while (true) {

			write("Starting Minecraft Server...");

			System.gc();
			
			startServer();
			
			write("Server has been stopped. Checking for restart request...");

			if (requestedRestart()) {
				write("Restart request found!");
			} else {
				write("No restart request found. Closing BootLoader!");
				break;
			}
		}
	}
	
	public static void fetchJarInit() {
		File file = new File("./JAR_CODE");
		if (!file.exists()) {
			try {
				file.createNewFile();
				write("Jar Code file has been created. Edit it to your liking!");
				
				PrintWriter writer = new PrintWriter(file);
				writer.write(JAR_INIT);
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String line = reader.readLine();
			if (!line.isEmpty()) {
				JAR_INIT = line.trim();
			}
			
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void startServer() {
		try {
			List<String> args = new ArrayList<String>();
			for (String arg : JAR_INIT.split(" ")) {
				args.add(arg);
			}
			ProcessBuilder builder = new ProcessBuilder(args);
			builder.redirectErrorStream(true);
			builder.inheritIO();
			Process process = builder.start();
			OutputStream in = process.getOutputStream();
			
			byte[] buffer = new byte[4000];
			while (isAlive(process)) {
				
				int ni = System.in.available();
				if (ni > 0) {
					int n = System.in.read(buffer, 0, Math.min(ni, buffer.length));
					in.write(buffer, 0, n);
					in.flush();
				}

				Thread.sleep(10);
			}
			
			System.out.println();
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
		File file = new File("RESTART");
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
