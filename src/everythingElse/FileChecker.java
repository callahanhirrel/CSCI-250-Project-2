package everythingElse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class FileChecker {
	public Boolean check_existence(String filename, String file) throws FileNotFoundException {
		File f = new File(filename);
		Scanner scan = new Scanner(f);
		while (scan.hasNextLine()) {
			String x = scan.nextLine();
			if (x .equals(file)) {
				scan.close();
				System.out.println(x);
				return true;
			}
		}
		scan.close();
		return false;
	}

	public String check_file(File file, File direct) throws IOException {
		FileChecker fileChecker = new FileChecker();
		Integer i = 0;
		String p = direct.getPath();
		File x = new File(p);
		File[] list = x.listFiles();
		while (list.length != i) {
			if (list[i].getName().equals(file.getName())) {
				File new_filename = new File("D" + file.getName());
				return fileChecker.check_file(new_filename, direct);
			}
			i++;
		}
		return file.getName();
	}
	
	public void check_new_file(File file, File direct) throws IOException {
		for (File f : new File(direct.getPath()).listFiles()) {
			if (f.getName().equals(file.getName())) {
				Files.delete(f.toPath());
			}
		}
	}
}
