package mapreduce.master.steps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mapreduce.master.MRService;

public class Mapping {
	public static void printMapKeysToUm() {
		for (String key : MRService.mapkeysUmMap.keySet()) {
			ArrayList<String> ums = (ArrayList<String>) MRService.mapkeysUmMap.get(key);
			System.out.println(key + "  " + ums);
		}
	}

	public static void runMapJar() {
		for (Map.Entry<String, String> entry : MRService.splitOnServerMap.entrySet()) {
			ProcessBuilder pb = new ProcessBuilder("ssh", "gquispe@" + entry.getValue(), "java", "-jar",
					"/tmp/gquispe/slave.jar", "0", entry.getKey());
			String umName = "UM" + entry.getKey().substring(1);

			MRService.umOnServerMap.put(umName, entry.getValue());
			List<String> keys = MRService.startProcessAndPrintOutput(pb);
			for (String key : keys) {
				MRService.mapkeysUmMap.put(key, umName);
			}
			//printUmMap();
		}
	}
}
