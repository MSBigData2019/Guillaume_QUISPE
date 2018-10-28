package mapreduce.slave;

import java.util.Arrays;
import java.util.List;

import mapreduce.slave.steps.Map;
import mapreduce.slave.steps.Reduce;
import mapreduce.slave.steps.Shuffle;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		if (args[0].equals("0")) {
			Map map = new Map(args[1]);
			map.createMapsFolder();
			map.readSplitsAndPrintKeys();
		} else if (args[0].equals("1")) {
			List<String> listUms = Arrays.asList(args[3].split(","));
			Shuffle shuffle = new Shuffle(args[1],args[2],listUms);
			shuffle.shuffle();
		} else if (args[0].equals("2")) {
			Reduce reduce = new Reduce(args[1], args[2]);
			reduce.reduce();
		}
	}

}
