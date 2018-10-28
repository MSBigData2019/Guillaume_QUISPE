package mapreduce.slave.steps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Shuffle {

	List<String> listUms;
	List<String> list = new ArrayList<String>();
	String outputFile;

	String key;

	public Shuffle(String key, String outputFile, List<String> listUms) {
		this.listUms = listUms;
		this.key = key;
		this.outputFile = outputFile;
	}

	public void shuffle() {
		readUMsAndPutMap();
	}

	private void readUMsAndPutMap() {
		try {
			File file = new File("/tmp/gquispe/maps/" + outputFile);
			file.getParentFile().mkdirs();
			PrintWriter printWriter = new PrintWriter(file);
			// This will reference one line at a time
			String line = null;
			for (String um : listUms) {

				BufferedReader reader = new BufferedReader(new FileReader("/tmp/gquispe/maps/" + um));

				while ((line = reader.readLine()) != null) {

					String[] keyValue = line.split(",");

					if (key.equals(keyValue[0])) {
						printWriter.println(line);
					}
				}
				reader.close();
			}

			printWriter.close();

		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + ex.getMessage() + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + ex.getMessage() + "'");
		}

	}

}
