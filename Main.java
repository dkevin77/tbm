import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		try {
			Scanner in = new Scanner(System.in);

			System.out.print("Please input the file pathname: ");
			String filepathname = in.nextLine();

			Scanner inFile = new Scanner(new File(filepathname));

			// The first command in the file must be to set the system date
			// (eg. "startNewDay 03-Jan-2024"); and it cannot be undone
			String cmdLine1 = inFile.nextLine();
			String[] cmdLine1Parts = cmdLine1.split("\\|"); // Split by vertical bar character '|'
			System.out.println("\n> " + cmdLine1);
			SystemDate.createTheInstance(cmdLine1Parts[1]);

			while (inFile.hasNext()) {
				String cmdLine = inFile.nextLine().trim();

				// Blank lines exist in data file as separators. Skip them.
				if (cmdLine.equals(""))
					continue;

				System.out.println("\n> " + cmdLine);

				// split the words in actionLine => create an array of word strings
				String[] cmdParts = cmdLine.split("\\|");

				executeCommand(cmdParts);

			}
			inFile.close();

			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error: File not found.");
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void executeCommand(String[] cmdParts) {
		try {
			switch (cmdParts[0]) {
				case "request":
					(new CmdRequest()).execute(cmdParts);
					break;
				case "listReservations":
					(new CmdListReservations()).execute(cmdParts);
					break;
				case "startNewDay":
					(new CmdStartNewDay()).execute(cmdParts);
					break;
				case "undo":
					RecordedCommand.undoOneCommand();
					break;
				case "redo":
					RecordedCommand.undoOneCommand();
					break;
				case "assignTable":
					(new CmdAssignTable()).execute(cmdParts);
					break;
				case "cancel":
					(new CmdCancel()).execute(cmdParts);
					break;
				case "listTableAllocations":
					(new CmdListTableAllocations()).execute(cmdParts);
					break;
				case "suggestTable":
					(new CmdSuggestTable()).execute(cmdParts);
					break;
				default:
					System.out.println("Unknown command.");
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Insufficient command arguments.");
		}
	}
}
