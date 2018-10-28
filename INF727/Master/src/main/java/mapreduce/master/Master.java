package mapreduce.master;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import mapreduce.master.steps.Mapping;
import mapreduce.master.steps.Reduce;
import mapreduce.master.steps.Shuffle;

public class Master {

	ClassLoader classLoader;

	public Master() {
		classLoader = getClass().getClassLoader();
		MRService.ipFile = new File(classLoader.getResource("ips.txt").getFile());
		MRService.ipFileName = MRService.ipFile.toPath().toString();
		MRService.ips = getIpsFile();

	}

	public void process() {
		deleteFoldersBeforeStart();
		copyFiles();
		Mapping.runMapJar();
		Shuffle.runShuffleJar();
		//printMapKeysToUm();
		Reduce.runReduceJar();
		Reduce.printReduce();
	}

	private void deleteFoldersBeforeStart() {
		for (String ip : MRService.ips) {
			ProcessBuilder pb = new ProcessBuilder("ssh", "gquispe@" + ip, "rm", "-r", "/tmp/gquispe/maps");
			MRService.startProcessAndPrintOutput(pb);
			ProcessBuilder pb2 = new ProcessBuilder("ssh", "gquispe@" + ip, "rm", "-r", "/tmp/gquispe/reduce");
			MRService.startProcessAndPrintOutput(pb2);
		}
	}

	private void printUmMap() {
		for (Map.Entry<String, String> entry : MRService.umOnServerMap.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}
	}

	private void copyFiles() {

		int i = 0;
		for (String ip : MRService.ips) {
			String splitFileName = "S" + i + ".txt";
			ProcessBuilder pb = new ProcessBuilder("ssh", "gquispe@" + ip, "mkdir", "/tmp/gquispe/splits");
			System.out.println("ip " + ip);
			ProcessBuilder reducepb = new ProcessBuilder("ssh", "gquispe@" + ip, "mkdir", "/tmp/gquispe/reduce");
			MRService.startProcessAndPrintOutput(pb);
			MRService.startProcessAndPrintOutput(reducepb);
			copyFromRessourceToServer(ip, splitFileName);
			MRService.splitOnServerMap.put(splitFileName, ip);
			i++;

		}
	}

	private void copyFromRessourceToServer(String ip, String splitFileName) {
		System.out.println("finallye " + splitFileName);
		File SFile = new File(classLoader.getResource(splitFileName).getFile());
		String SFileName = SFile.toPath().toString();
		ProcessBuilder pb2 = new ProcessBuilder("scp", "-r", "-p", SFileName, "gquispe@" + ip + ":/tmp/gquispe/splits");
		MRService.startProcessAndPrintOutput(pb2);
	}

	private ArrayList<String> getIpsFile() {

		// This will reference one line at a time
		String line = null;
		ArrayList<String> list = new ArrayList<String>();
		try {
			InputStream in = getClass().getResourceAsStream("/ips.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			while ((line = reader.readLine()) != null) {
				list.add(line);
			}

			// Always close files.
			reader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + MRService.ipFileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + MRService.ipFileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
		return list;

	}

}
