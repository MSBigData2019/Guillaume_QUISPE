package mapreduce.slave.steps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Reduce {

	private File file;
	private String key;

	public Reduce(String key, String fileName) {
		this.file = new File("/tmp/gquispe/maps/" + fileName);
		this.key = key;
	}

	public void reduce() {
		
		String reduceFileName = "RM" + file.getName().substring(2);
		File rmfile = new File("/tmp/gquispe/reduce/" + reduceFileName);
		file.getParentFile().mkdirs();

		try {
			PrintWriter printWriter = new PrintWriter(rmfile);
			printWriter.print(key + " " + getNumber());
			printWriter.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private int getNumber() {
		int i = 0;
		try {

			BufferedReader reader = new BufferedReader(new FileReader("/tmp/gquispe/maps/" + file.getName()));
			String line;

			while ((line = reader.readLine()) != null) {
				String[] keyValue = line.split(",");
				if (this.key.equals(keyValue[0])) {
					i++;
				}

			}
			reader.close();

		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + ex.getMessage() + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + ex.getMessage() + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
		return i;
	}
}
