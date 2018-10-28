package deploy2.deploy2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Deployer {
	private String slaveFileName;
	File ipFile;
	String ipFileName;

	public Deployer() {
		ClassLoader classLoader = getClass().getClassLoader();
		
		//File slaveFile = new File(classLoader.getResource("slave.jar").getFile());
		File slaveFile = new File("/Users/guillaumequispe/eclipse-workspace/Slave/target/slave.jar");

		slaveFileName = slaveFile.toPath().toString();
		ipFile = new File(classLoader.getResource("ips.txt").getFile());
		ipFileName = ipFile.toPath().toString();
	}

	public void deploy() {

		ArrayList<String> list = getIpsFile();
		for (String ip : list) {

			ProcessBuilder pb = new ProcessBuilder("ssh", "gquispe@" + ip, "mkdir", "/tmp/gquispe");

			System.out.println("ip " + ip);
			try {
				startProcessAndPrintOutput(pb);

			} catch (IOException e) {
				System.out.println("ioexception");

				e.printStackTrace();

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("interupt");
				e.printStackTrace();
			} finally {
				System.out.println("finallye");
				ProcessBuilder pb2 = new ProcessBuilder("scp", "-r", "-p", slaveFileName,
						"gquispe@" + ip + ":/tmp/gquispe/");

				try {

					startProcessAndPrintOutput(pb2);

				} catch (IOException e) {
					System.out.println("ioexception2");

					e.printStackTrace();

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.out.println("interupt2");
					e.printStackTrace();
				}

			}

		}

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
				// System.out.println(line);
			}

			// Always close files.
			reader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + ipFileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + ipFileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
		return list;

	}

	private void startProcessAndPrintOutput(ProcessBuilder pb) throws InterruptedException, IOException {
		Process p = pb.start();
		p.waitFor(3, TimeUnit.SECONDS);
		InputStream is = p.getInputStream();
		InputStream ise = p.getErrorStream();
		InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
		InputStreamReader isre = new InputStreamReader(ise, StandardCharsets.UTF_8);

		BufferedReader br = new BufferedReader(isr);
		BufferedReader br2 = new BufferedReader(isre);
		String line2;

		while ((line2 = br.readLine()) != null) {
			System.out.println(line2);
		}
		while ((line2 = br2.readLine()) != null) {
			System.out.println(line2);
		}
	}

}
