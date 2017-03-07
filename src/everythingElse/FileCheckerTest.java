package everythingElse;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

public class FileCheckerTest {
	FileChecker fileChecker;
	
	@Before
	public void setup() {
		fileChecker = new FileChecker();
	}

	@Test
	public void teststring() throws FileNotFoundException {
		assertEquals(true, fileChecker.check_existence("Test_Store.txt", "name"));
	}
	
//	@Test
//	public void testfile() throws FileNotFoundException {
//		assertEquals(true, fileChecker.check_file(, ));
//	}
}
