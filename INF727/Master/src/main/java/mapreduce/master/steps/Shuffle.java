package mapreduce.master.steps;

import java.util.ArrayList;
import java.util.Random;

import mapreduce.master.MRService;

public class Shuffle {


	public static void runShuffleJar() {

		int j = 0;
		for (String key : MRService.mapkeysUmMap.keySet()) {
			Random rand = new Random();

			ArrayList<String> ums = (ArrayList<String>) MRService.mapkeysUmMap.get(key);
			int n = rand.nextInt(ums.size());

			String servertoRunShuffle = MRService.umOnServerMap.get(ums.get(n));

			if (ums.size() > 1) {
				copyNecessaryUmsToServer(key, ums, servertoRunShuffle);
			}

			String listFilesToReduce = String.join(",", ums);
			System.out.println(listFilesToReduce);
			String SMName = "SM" + j + ".txt";
			String[] smInfos = { SMName, servertoRunShuffle };
			MRService.keytoSMMap.put(key, smInfos);
			ProcessBuilder pb = new ProcessBuilder("ssh", "gquispe@" + servertoRunShuffle, "java", "-jar",
					"/tmp/gquispe/slave.jar", "1", key, SMName, listFilesToReduce);
			System.out.println("1");
			System.out.println(pb.command());

			MRService.startProcessAndPrintOutput(pb);

			System.out.println("2");

			j++;
		}
	}
	
	private static void copyNecessaryUmsToServer(String key, ArrayList<String> ums, String serverToCopyFiles) {

		System.out.println("ums.get(0).substring(-1)" + ums.get(0));

		for (int i = 0; i < ums.size(); i++) {

			String strFileToCopy = "gquispe@" + MRService.umOnServerMap.get(ums.get(i)) + ":/tmp/gquispe/maps/" + ums.get(i);

			ProcessBuilder pb = new ProcessBuilder("scp", "-3", "-r", "-p", strFileToCopy,
					"gquispe@" + serverToCopyFiles + ":/tmp/gquispe/maps");
			System.out.print(pb.command());
			MRService.startProcessAndPrintOutput(pb);
		}
	}
}
