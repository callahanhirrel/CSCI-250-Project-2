package everythingElse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileChecker {
	public Boolean check_existence(String filename, String string) throws FileNotFoundException {
		File f = new File(filename);
		Scanner scan = new Scanner(f);
		while (scan.hasNextLine()) {
			String x = scan.nextLine();
			if (x == string) {
				scan.close();
				return true;
			}
		}
		scan.close();
		return false;
	}

}
