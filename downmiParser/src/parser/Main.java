package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
	
	private static String[] regions = new String[] { "eea", "in", "id", "ru", "tr", "tw", "jp" };

	public static void main(String[] args) {
		String filename = args[0];
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));
			StringBuilder sb = new StringBuilder("**Añadidas:**\n");
			while (br.ready()) {
				String line = br.readLine();
				String[] words = line.split("/");
				String version = words[3];
				String rom = words[4];
				String codename;
				boolean recovery = rom.endsWith(".zip");
				if (recovery) {
					codename = rom.split("_")[1].toLowerCase().replace("global", "");
					for (String region : regions) {
						int count = codename.length() - codename.replace(region, "").length();
						if (codename.endsWith(region) && count == 1) {
							codename = codename.replace(region, "");
						} else if (codename.endsWith(region) && count > 1) {
							String reverseCodename = new StringBuilder(codename).reverse().toString();
							String reverseRegion = new StringBuilder(region).reverse().toString();
							reverseCodename = reverseCodename.replaceFirst(reverseRegion, "");
							codename = new StringBuilder(reverseCodename).reverse().toString();
						}
					}
				} else {
					codename = rom.split("_")[0].toLowerCase();
				}
					
				String format = recovery ? "Recovery" : "Fastboot";
				sb.append(String.format("- %s %s (%s)\n", version, format, codename));
			}
			System.out.println(sb.toString());
			br.close();
		} catch (FileNotFoundException e) {
			System.err.println("No se ha encontrado el fichero " + filename);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Se ha producido un error de E/S.");
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				System.err.println("Se ha producido un error de E/S.");
				e.printStackTrace();
			}
		}
	}

}
