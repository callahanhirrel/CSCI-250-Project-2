package everythingElse;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class UserTests {

	User users = new User();
	
	@Before
	public void setup() {
		users = new User();
	}
	
	@Test
	public void testConstuctor() {
		assertEquals(0, users.getKeys().size());
		assertEquals(0, users.getValues().size());
	}
	
	@Test
	public void testAddUser() {
		users.add("mspain", "SecurePassword124");
		assertEquals(1, users.getKeys().size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testOneUsernamePerTable() {
		users.add("spaintwo", "password1");
		users.add("spaintwo", "password2");
	}

}
