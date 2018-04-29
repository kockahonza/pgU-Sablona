import java.util.*;
import java.util.regex.*;
import java.io.*;

// A "helper" class to read the CSV file. Tries to behave similarly to the Scanner class.
class CSVScanner {
	FileReader reader;
	Scanner scanner;
	String[] keys;

	public CSVScanner(FileReader reader) throws IOException {
		scanner = new Scanner(reader);
		keys = scanner.nextLine().split(",");
	}

	public HashMap<String, String> getNextContext() {
		HashMap<String, String> context = new HashMap<String, String>();
		String[] values = scanner.nextLine().split(",");
		for (int i = 0; i < keys.length; i++) {
			context.put(keys[i], values[i]);
		}
		return context;
	}

	public boolean hasNextContext() {
		return scanner.hasNextLine();
	}
}

public class CSVTemplater {
	public static void main(String[] args) throws IOException {
		// Manage filenames
		String CSVFileName = "data.csv";
		String TemplateFileName = "template.tpl";
		String OutFileName = "templater-out-%05d.txt";
		for (String arg: args) {
			Matcher CSVMatcher = Pattern.compile("--csv=(.*)").matcher(arg);
			Matcher TemplateMatcher = Pattern.compile("--template=(.*)").matcher(arg);
			Matcher OutMatcher = Pattern.compile("--out=(.*)").matcher(arg);
			if (CSVMatcher.matches()) {
				CSVFileName = CSVMatcher.group(1);
			} else if (TemplateMatcher.matches()) {
				TemplateFileName = TemplateMatcher.group(1);
			} else if (OutMatcher.matches()) {
				OutFileName = OutMatcher.group(1);
			}
		}

		// Manage the template file
		String templateString = "";
		Scanner scanner;
		try {
			scanner = new Scanner(new FileReader(TemplateFileName));
		} catch(FileNotFoundException e) {
			System.out.println("Template file \"" + TemplateFileName + "\" not found.");
			return;
		}
		// Put all data from template file into a String
		while (scanner.hasNextLine()) {
			templateString += scanner.nextLine() + "\n";
		}
		// Get rid of the last newline character and create an instance of Templater class with the String
		templateString = templateString.substring(0, templateString.length() - 1);
		Templater Temp = new Templater(templateString);

		// Manage the csv file
		CSVScanner csvscanner;
		try {
			csvscanner = new CSVScanner(new FileReader(CSVFileName));
		} catch (FileNotFoundException e) {
			System.out.println("CSV file \"" + CSVFileName + "\" not found.");
			return;
		}
		// Go through the csv file and run each line through the Templater class instance, then write the output to a file
		int i = 0;
		while (csvscanner.hasNextContext()) {
			PrintWriter writer = new PrintWriter(String.format(OutFileName, i));
			writer.print(Temp.render(csvscanner.getNextContext()));
			writer.close();
			i++;
		}
	}
}
