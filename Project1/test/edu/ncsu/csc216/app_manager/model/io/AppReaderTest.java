/**
 * 
 */
package edu.ncsu.csc216.app_manager.model.io;




import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.app_manager.model.application.Application;

/**
 * This class tests the functionality of the AppReader, ensuring that it can
 * correctly read valid application files and handle invalid file inputs properly.
 * 
 * The test cases focus on reading a valid file containing applications, verifying 
 * that each application is correctly processed, and checking that appropriate 
 * exceptions are thrown for invalid files.
 * 
 * @author Priyanshu Dongre
 */
public class AppReaderTest {
	/**
	 * Path of a valid file that'll used for reading data for an application.
	 */
	private final String validFile = "test-files/app1.txt";
	 /**
     * A valid application in the Review state.
     */
    private final String validApplication1 = "*1,Review,New,Application summary,,false,\n-[Review] Note 1";

    /**
     * A valid application in the Interview state with reviewer details.
     * Contains notes related to the review and interview process.
     */
    private final String validApplication3 = "*3,Interview,Old,Application summary,reviewer,false,\n"
            + "-[Review] Note 1\n"
            + "-[Interview] Note 2\n"
            + "that goes on a new line";

    /**
     * A valid application in the RefCheck state with reviewer details.
     * Contains notes related to the review, interview, and reference check processes.
     */
    private final String validApplication7 = "*7,RefCheck,Old,Application summary,reviewer,true,\n"
            + "-[Review] Note 1\n"
            + "-[Interview] Note 2\n"
            + "-[RefCheck] Note 3";

    /**
     * A valid application in the Waitlist state.
     * Contains notes related to the review and waitlist processes.
     */
    private final String validApplication14 = "*14,Waitlist,New,Application summary,,false,ReviewCompleted\n"
            + "-[Review] Note 1\n"
            + "-[Waitlist] Note 2\n"
            + "that goes on a new line";

    /**
     * A valid application in the Offer state with reviewer details.
     * Contains notes related to the review, interview, reference check, and offer processes.
     */
    private final String validApplication16 = "*16,Offer,Old,Application summary,reviewer,true,\n"
            + "-[Review] Note 1\n"
            + "that goes on a new line\n"
            + "-[Interview] Note 2\n"
            + "-[RefCheck] Note 3\n"
            + "-[Offer] Note 4";

    /**
     * A valid application in the Closed state with reviewer details.
     * Contains notes related to the review, interview, reference check, offer, and closure processes.
     */
    private final String validApplication15 = "*15,Closed,Hired,Application summary,reviewer,true,OfferCompleted\n"
            + "-[Review] Note 1\n"
            + "that goes on a new line\n"
            + "-[Interview] Note 2\n"
            + "-[RefCheck] Note 3\n"
            + "-[Offer] Note 4\n"
            + "-[Closed] Note 6";

    /**
     * An array of valid application strings representing different states and their corresponding notes.
     */
    private final String[] applicationToString = {
        validApplication1,
        validApplication3,
        validApplication7,
        validApplication14,
        validApplication16,
        validApplication15
    };

	
	 /**
     * Tests the AppReader's ability to read a valid file containing applications.
     * 
     * It reads the applications from the file, checks that all applications are
     * correctly processed, and ensures that each application's data matches the
     * expected string representations.
     */
	@Test
	public void testReadValidFile() {
		
		ArrayList<Application> applications = AppReader.readAppsFromFile(validFile);
		
		assertEquals(6, applications.size());
		
		for(Application app: applications) {
			assertNotNull(app); // An Application cannot be null.
			assertNotNull(app.getNotes()); // An Application cannot have no notes
			assertNotNull(app.getStateName()); // An Application will always be in a state.
		}
		for(int i = 0; i < applications.size(); i++) {
			assertEquals(applicationToString[i], applications.get(i).toString());
		}
		
	}
	
	
	/**
     * Tests the AppReader's handling of an invalid file.
     * 
     * Ensures that an IllegalArgumentException is thrown when attempting to read from
     * a non-existent or invalid file, and that the exception message is correct.
     */
	@Test
	public void testReadInvalidFile() {
		Exception e1 = assertThrows(IllegalArgumentException.class, () -> AppReader.readAppsFromFile("/test-files/invalid-file"));
		assertEquals("Unable to load file.", e1.getMessage());
	}

}
