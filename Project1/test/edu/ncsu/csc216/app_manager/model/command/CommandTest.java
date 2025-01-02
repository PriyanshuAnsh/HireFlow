/**
 * 
 */
package edu.ncsu.csc216.app_manager.model.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.app_manager.model.command.Command.CommandValue;
import edu.ncsu.csc216.app_manager.model.command.Command.Resolution;

/**
 * This class tests the functionality of the Command class.
 * 
 * It verifies the correct behavior of the constructor, including validation 
 * of input parameters and ensuring that exceptions are thrown for invalid inputs. 
 * Additionally, it tests the getter methods for retrieving the command values, 
 * reviewer IDs, resolution status, and notes.
 * 
 * The test cases ensure that the Command class correctly handles various 
 * command scenarios and maintains the integrity of the provided information.
 * 
 * @author Priyanshu Dongre
 */
class CommandTest {

	/**
	 * Command to accept an application, indicating that the candidate 
	 * has been deemed suitable.
	 */
	Command c = new Command(CommandValue.ACCEPT, "DonPs", Resolution.REFCHKCOMPLETED, "Good Candidate");
	
	/**
	 * Command to reject an application, indicating that the application 
	 * has been closed after review.
	 */
	Command c1 = new Command(CommandValue.REJECT, "Cicada3301", Resolution.REVCOMPLETED, "Application Closed");
	
	/**
	 * Command to place an application on standby or waitlist, 
	 * indicating that the application is temporarily put on hold.
	 */
	Command c2 = new Command(CommandValue.STANDBY, "IphoneGuy", Resolution.INTCOMPLETED, "Put to Waitlist");
	/**
	 * Command to reopen a previously closed application, indicating 
	 * that the application is being reconsidered.
	 */
	Command c3 = new Command(CommandValue.REOPEN, "Vegapunk", Resolution.OFFERCOMPLETED, "Reconsider");
	
	
	/**
     * Tests the exceptions thrown by the Command constructor when invalid inputs are provided.
     * 
     * Verifies that an IllegalArgumentException is thrown for null or empty values 
     * in the command, reviewer ID, resolution, or note fields.
     */
	@Test
	public void testExceptions() {
		 Exception e = assertThrows(IllegalArgumentException.class, () -> {
	            new Command(null, "reviewer1", Command.Resolution.REVCOMPLETED, "Note [-1,sbsd]");
	        });
	        assertEquals("Invalid information.", e.getMessage());
	        
	        Exception e1 = assertThrows(IllegalArgumentException.class, () -> {
	            new Command(Command.CommandValue.ACCEPT, null, Command.Resolution.REVCOMPLETED, "VIT PUNE");
	        });
	        assertEquals("Invalid information.", e1.getMessage());

	       
	        Exception e2 = assertThrows(IllegalArgumentException.class, () -> {
	            new Command(Command.CommandValue.ACCEPT, "", Command.Resolution.REVCOMPLETED, "VITEE");
	        });
	        assertEquals("Invalid information.", e2.getMessage());
	        
	        Exception e3 = assertThrows(IllegalArgumentException.class, () -> {
	            new Command(Command.CommandValue.STANDBY, "reviewer1", null, "Note -=");
	        });
	        assertEquals("Invalid information.", e3.getMessage());
	        
	        Exception e4 = assertThrows(IllegalArgumentException.class, () -> {
	            new Command(Command.CommandValue.REJECT, "reviewer1", null, "Note -(12)");
	        });
	        assertEquals("Invalid information.", e4.getMessage());
	        
	        Exception e5 = assertThrows(IllegalArgumentException.class, () -> {
	            new Command(Command.CommandValue.ACCEPT, "reviewer1", Command.Resolution.REVCOMPLETED, null);
	        });
	        assertEquals("Invalid information.", e5.getMessage());

	      
	        Exception e6 = assertThrows(IllegalArgumentException.class, () -> {
	            new Command(Command.CommandValue.ACCEPT, "reviewer1", Command.Resolution.REVCOMPLETED, "");
	        });
	        assertEquals("Invalid information.", e6.getMessage());
	}
	
	/**
     * Tests the getCommand() method.
     * 
     * Verifies that the correct command value is returned for each Command instance.
     */
	@Test
	public void testGetCommand() {
		
		assertEquals(CommandValue.ACCEPT, c.getCommand(), "Testing getCommand()");
		
		
		assertEquals(CommandValue.REJECT, c1.getCommand(), "Testing getCommand()");
		
		
		assertEquals(CommandValue.STANDBY, c2.getCommand(), "Testing getCommand()");
		
		
		assertEquals(CommandValue.REOPEN, c3.getCommand(), "Testing getCommand()");
	}
	
	
	/**
     * Tests the getReviewerId() method.
     * 
     * Verifies that the correct reviewer ID is returned for each Command instance.
     */
	@Test
	public void testGetReviewerId() {
		
		assertEquals("DonPs", c.getReviewerId(), "Testing getReviewerId()");
		
		
		assertEquals("Cicada3301", c1.getReviewerId(), "Testing getReviewerId()");
		
		
		assertEquals("IphoneGuy", c2.getReviewerId(), "Testing getReviewerId()");
		
		
		assertEquals("Vegapunk", c3.getReviewerId(), "Testing getReviewerId()");
	}
	
	
	/**
     * Tests the getResolution() method.
     * 
     * Verifies that the correct resolution status is returned for each Command instance.
     */
	@Test
	public void testgetResolution() {
	
		assertEquals(Resolution.REFCHKCOMPLETED, c.getResolution(), "Testing getResolution()");
		assertEquals(Resolution.REVCOMPLETED, c1.getResolution(), "Testing getResolution()");
		assertEquals(Resolution.INTCOMPLETED, c2.getResolution(), "Testing getResolution()");
		assertEquals(Resolution.OFFERCOMPLETED, c3.getResolution(), "Testing getResolution()");
	}
	
	 /**
     * Tests the getNote() method.
     * 
     * Verifies that the correct note is returned for each Command instance.
     */
	@Test
	public void testGetNote() {
		assertEquals("Good Candidate", c.getNote(), "Testing getNote()");
		assertEquals("Application Closed", c1.getNote(), "Testing getNote()");
		assertEquals("Put to Waitlist", c2.getNote(), "Testing getNote()");
		assertEquals("Reconsider", c3.getNote(), "Testing getNote()");
	}

}
