package mapreduce.slave.steps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
public class Map {
	File fileR;

	public Map(String fileName) {
		fileR = new File("/tmp/gquispe/splits/" + fileName);
		//System.out.println(fileR.getAbsolutePath());
	}

	public void createMapsFolder() {
		ProcessBuilder pb = new ProcessBuilder("mkdir", "/tmp/gquispe/maps");
		startProcessAndPrintOutput(pb);
	}
	
	public void readSplitsAndPrintKeys() {
		HashMap<String, Integer> mapMap2=readSplits();
		
		for (String key: mapMap2.keySet()) {
			System.out.println(key);
		}
		
		
	}

	private HashMap<String, Integer> readSplits() {

		// This will reference one line at a time
		String line = null;
		HashMap<String, Integer> mapMap = new HashMap<String, Integer>();
		String umName= "UM"+fileR.getName().substring(1);
		try {
			File file = new File("/tmp/gquispe/maps/"+umName);
			file.getParentFile().mkdirs();

			PrintWriter printWriter = new PrintWriter(file);
			BufferedReader reader = new BufferedReader(new FileReader(fileR));

			while ((line = reader.readLine()) != null) {
				String[] words = line.split(" ");
				for (String string : words) {
					printWriter.println(string + ",1");
					mapMap.put(string, 1);
				}
			}

			// Always close files.
			reader.close();
			printWriter.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + ex.getMessage() + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + ex.getMessage() + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
		return mapMap;

	}

	private void startProcessAndPrintOutput(ProcessBuilder pb) {
		try {
			Process p = pb.start();
			p.waitFor(3, TimeUnit.SECONDS);
//			InputStream is = p.getInputStream();
//			InputStream ise = p.getErrorStream();
//			InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
//			InputStreamReader isre = new InputStreamReader(ise, StandardCharsets.UTF_8);
//
//			BufferedReader br = new BufferedReader(isr);
//			BufferedReader br2 = new BufferedReader(isre);
//			String line2;
//
//			while ((line2 = br.readLine()) != null) {
//				System.out.println(line2);
//			}
//			while ((line2 = br2.readLine()) != null) {
//				System.out.println(line2);
//			}
		} catch (IOException e) {
			System.out.println(" io exception");

		} catch (InterruptedException e) {
			System.out.println(" interrupted exception");
		}

	}
}
