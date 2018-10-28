package mapreduce.master;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.map.MultiValueMap;

public class MRService {
	
	public  static File ipFile;
	public  static String ipFileName;
	public  static HashMap<String, String> umOnServerMap = new HashMap<String, String>();
	public  static MultiValueMap<String, String> mapkeysUmMap = new MultiValueMap<String, String>();
	public  static HashMap<String, String> splitOnServerMap = new HashMap<String, String>();
	public  static HashMap<String, String[]> keytoSMMap = new HashMap<String, String[]>();
	public  static ArrayList<String> ips = new ArrayList<String>();

	public static List<String> startProcessAndPrintOutput(ProcessBuilder pb) {
		List<String> output = new ArrayList<String>();

		try {
			Process p = pb.start();
			p.waitFor(10, TimeUnit.SECONDS);
			InputStream is = p.getInputStream();
			InputStream ise = p.getErrorStream();
			InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
			InputStreamReader isre = new InputStreamReader(ise, StandardCharsets.UTF_8);

			BufferedReader br = new BufferedReader(isr);
			BufferedReader br2 = new BufferedReader(isre);
			String line2;

			while ((line2 = br.readLine()) != null) {
				output.add(line2);
				System.out.println(line2);
			}
			while ((line2 = br2.readLine()) != null) {
				System.out.println(line2);
			}
		} catch (IOException e) {
			System.out.println(" io exception");

		} catch (InterruptedException e) {
			System.out.println(" interrupted exception");
		}
		return output;

	}
}
