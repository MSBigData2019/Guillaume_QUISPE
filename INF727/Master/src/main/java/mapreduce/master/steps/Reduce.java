package mapreduce.master.steps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mapreduce.master.MRService;

public class Reduce {
	public static void runReduceJar() {
		int i = 0;
		for (String key : MRService.keytoSMMap.keySet()) {
			ArrayList<String> ums = (ArrayList<String>) MRService.mapkeysUmMap.get(key);
			String[] serverAndSM = MRService.keytoSMMap.get(key);
			ProcessBuilder pb = new ProcessBuilder("ssh", "gquispe@" + serverAndSM[1], "java", "-jar",
					"/tmp/gquispe/slave.jar", "2", key, serverAndSM[0]);
			System.out.println(pb.command());
			List<String> output = MRService.startProcessAndPrintOutput(pb);
			for (String out : output) {
				System.out.println(out);

//				keysUmMap.put(key,)
//				System.out
			}
			i++;
		}


	}

	public static void printReduce() {
		for (Map.Entry<String, String[]> entry : MRService.keytoSMMap.entrySet()) {
			ProcessBuilder pb = new ProcessBuilder("ssh", "gquispe@" + entry.getValue()[1], "cat",
					"/tmp/gquispe/reduce/" + "RM" + entry.getValue()[0].substring(2));
			List<String> output = MRService.startProcessAndPrintOutput(pb);
		}
	}
}
