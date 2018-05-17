import java.util.*;
import java.util.regex.*;
import java.io.*;

public class Templater {
	private String templateString;

	public Templater(String templateString) {
		this.templateString = templateString;
	}

	public String render(HashMap<String, String> context) {
		String outString = templateString;
		for (String key: context.keySet()) {
			outString = outString.replaceAll("\\{\\{[ \t]*" + key + "[ \t]*\\}\\}", context.get(key));
		}
		return outString;
	}

	public static void main(String[] args) {
		// Initiate the context HashMap
		HashMap<String, String> context = new HashMap<String, String>();
		// Parse args[] and fill context
		Pattern pattern = Pattern.compile("--var=(.*)=(.*)");
		for (String arg: args) {
			Matcher matcher = pattern.matcher(arg);
			if (matcher.matches()) {
				context.put(matcher.group(1), matcher.group(2));
			}
		}

		String inputString = "";
		Scanner sc = new Scanner(System.in);
		while (sc.hasNextLine()) {
			inputString += sc.nextLine() + '\n';
		}
		inputString = inputString.substring(0, inputString.length() - 1);

		Templater Temp = new Templater(inputString);

		System.out.println(Temp.render(context));
	}
}
