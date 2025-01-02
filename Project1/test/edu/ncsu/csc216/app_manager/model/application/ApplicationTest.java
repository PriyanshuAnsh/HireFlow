/**
 * 
 */
package edu.ncsu.csc216.app_manager.model.application;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.app_manager.model.application.Application.AppType;
import edu.ncsu.csc216.app_manager.model.command.Command;
import edu.ncsu.csc216.app_manager.model.command.Command.CommandValue;
import edu.ncsu.csc216.app_manager.model.command.Command.Resolution;

/**
 * Tests Application class functionality and behavior.
 * 
 * Validates the creation, state transitions, and command execution on an
 * Application instance. Ensures exceptions are thrown in invalid scenarios
 * and that state transitions occur correctly.
 * 
 * @author Priyanshu Dongre
 */
public class ApplicationTest {

	/**
	 * A Dummy Application for testing Purpose in certain scenarios.
	 */
	Application appDummy;
	
	/**
	 * A Dummy Note Variable for Testing purpose useful for A Dummy Application.
	 */
	String noteDummy;

	
	/**
     * Creates a dummy Application object for testing purposes.
     * Initializes a new Application instance with a sample appId, type, summary, and notes.
     */
	public void createDummyApplicationObject() {
		appDummy = new Application(1, AppType.NEW, "A New Application", "Application for Test Purpose in some cases");
		noteDummy = appDummy.getNotesString();
	}
	
	/**
     * Tests the constructor of Application.
     * 
     * Validates that an Application cannot be created with invalid parameters, such
     * as a null type, or empty summary. Also ensures correct exceptions are thrown
     * when invalid inputs are provided.
     */
	@Test
	public void testConstructor() {
		Exception e1 = assertThrows(IllegalArgumentException.class, () -> new Application(1, null, "hype", "note"));
		assertEquals("Application cannot be created.", e1.getMessage());
	}
	
	/**
     * Tests setting the appId for an Application.
     * 
     * Ensures valid appId values are set correctly, while negative or zero appIds
     * result in exceptions. Validates correct exception messages for invalid inputs.
     */
	@Test
	public void testSetAppId() {
		Application app = new Application(15, AppType.NEW, "Testing", "Testing Setting Id");
		assertEquals(15, app.getAppId());
		
		Application app1 = new Application(1111123415, AppType.NEW, "Testing", "Testing Setting Id");
		assertEquals(1111123415, app1.getAppId());
		
		Exception e1 = assertThrows(IllegalArgumentException.class, () -> new Application(-1, AppType.NEW, "Testing", "Testing Setting Id"));
		
		assertEquals("Application cannot be created.", e1.getMessage());
		
		Exception e2 = assertThrows(IllegalArgumentException.class, () -> new Application(0, AppType.NEW, "Testing", "Testing Setting Id"));
		assertEquals("Application cannot be created.", e2.getMessage());
	}
	
	
	 /**
     * Tests setting the summary for an Application.
     * 
     * Ensures that a valid summary is set correctly, while empty or null summaries
     * result in exceptions with appropriate messages.
     */
	@Test
	public void testSetSummary() {
		Application app = new Application(15, AppType.NEW, "Hello", "testing Summary");
		assertEquals("Hello", app.getSummary());
		
		Application app1 = new Application(1111123415, AppType.NEW, "Testing\nSummary\nfor\ntesting", "Testing Setting Id");
		assertEquals("Testing\nSummary\nfor\ntesting", app1.getSummary());
		
		Exception e1 = assertThrows(IllegalArgumentException.class, () -> new Application(10, AppType.NEW, "", "Testing Setting Id"));
		
		assertEquals("Application cannot be created.", e1.getMessage());
		
		
		Exception e2 = assertThrows(IllegalArgumentException.class, () -> new Application(19, AppType.NEW, null, "Testing Setting Id"));
		assertEquals("Application cannot be created.", e2.getMessage());
	}
	
	/**
     * Tests the behavior of adding notes to an Application.
     * 
     * Verifies that notes are added properly and that the format is maintained
     * when retrieving the notes.
     */
	@Test
	public void testAddNotes() {
		Application app = new Application(1, AppType.NEW, "My First Application", "Note 1");
		assertEquals(app.getNotesString(), "-[Review] Note 1");
	}
	/**
     * Tests the transition of an Application from Review to Closed state.
     * 
     * Verifies that executing the REJECT command after the Review phase correctly
     * moves the application to the Closed state and updates the resolution.
     */
	
	@Test
	public void testReviewToClosedTransition() {
	    Application app = new Application(4, AppType.OLD, "Bad Application", "Review in progress");
	    Command cmd = new Command(CommandValue.REJECT, "reviewer2", Resolution.REVCOMPLETED, "Review Failed");
	    app.update(cmd);
	    assertEquals("Closed", app.getStateName());
	    assertEquals("ReviewCompleted", app.getResolution());
	}
	
	/**
     * Tests that an invalid Application object cannot be created.
     * 
     * Verifies that invalid inputs like a zero appId and empty summary lead to
     * the correct exception being thrown.
     */
	@Test
	public void testInvalidApplicationCreation() {
	    Exception e1 = assertThrows(IllegalArgumentException.class, () -> new Application(0, AppType.NEW, "", ""));
	    assertEquals("Application cannot be created.", e1.getMessage());
	}
	
	
	 /**
     * Tests the transition from Review to Interview state.
     * 
     * Ensures that when an application is accepted after review, it transitions
     * correctly to the Interview state with the proper app type and reviewer.
     */
	@Test
	public void testReviewToInterviewTransition() {
	    Application app = new Application(3, AppType.NEW, "To be Interviewed", "Note1");
	    Command cmd = new Command(CommandValue.ACCEPT, "reviewer1", Resolution.REVCOMPLETED, "Moving to interview");
	    app.update(cmd);
	    assertEquals("Interview", app.getStateName());
	    assertEquals("Old", app.getAppType());
	}
	
	
	/**
     * Tests the behavior of a new application progressing through various stages 
     * (Review, Interview, RefCheck, Offer) and eventually being closed after offer acceptance.
     */
	@Test
	public void testNewApplicationAcceptingOffer() {
		Application app = new Application(1, AppType.NEW, "Good Application", "Review In Progress");
		
		Exception e1 = assertThrows(UnsupportedOperationException.class, () -> app.update(new Command(CommandValue.REOPEN, "01", Resolution.INTCOMPLETED, "Something")));
		assertEquals("Invalid information.", e1.getMessage());
		String note = "-[Review] Review In Progress";
		assertEquals("Review", app.getStateName());
		assertEquals(note, app.getNotesString());
		Command c = new Command(CommandValue.ACCEPT, "007", Resolution.REVCOMPLETED, "Good Potential");
		app.update(c);
		assertEquals("007", app.getReviewer());
		assertEquals("Interview", app.getStateName());
		note += "\n-[Interview] Good Potential";
		c = new Command(CommandValue.ACCEPT, "007", Resolution.INTCOMPLETED, "Good Interview");
		app.update(c);
		assertEquals("007", app.getReviewer());
		assertEquals("RefCheck", app.getStateName());
		note += "\n-[RefCheck] Good Interview";
		assertEquals(note, app.getNotesString());
		c = new Command(CommandValue.ACCEPT, "009", Resolution.REFCHKCOMPLETED, "Good References");
		app.update(c);
		assertEquals("009", app.getReviewer());
		assertEquals("Offer", app.getStateName());
		note += "\n-[Offer] Good References";
		assertEquals(note, app.getNotesString());
		c = new Command(CommandValue.ACCEPT, "007", Resolution.OFFERCOMPLETED, "Accepting Offer");
		app.update(c);
		assertEquals("007", app.getReviewer());
		assertEquals("Closed", app.getStateName());
		note += "\n-[Closed] Accepting Offer";
		assertEquals(note, app.getNotesString());

		//Rejecting a closed application
		Exception exp = assertThrows(UnsupportedOperationException.class, () -> app.update(new Command(CommandValue.REJECT, "007", Resolution.OFFERCOMPLETED, "Closed Application")));
		assertEquals("Invalid information.", exp.getMessage());
	}
	
	/**
     * Tests rejecting a new application.
     * 
     * This method verifies that when a reject command is executed on 
     * a new application, it transitions to the Closed state with 
     * the correct resolution and notes.
     */
	@Test
	public void testRejectingNewApplication() {
		createDummyApplicationObject();
		Command c = new Command(CommandValue.REJECT, "009", Resolution.REVCOMPLETED, "Reject Application");
		appDummy.update(c);
		assertEquals(Application.A_NEW, appDummy.getAppType());
		assertEquals("Closed", appDummy.getStateName());
		assertEquals(Command.R_REVCOMPLETED, appDummy.getResolution());
		String finalNote = "-[Review] Application for Test Purpose in some cases\n-[Closed] Reject Application";
		assertEquals(finalNote, appDummy.getNotesString());
	}
	
	/**
     * Tests the waitlist functionality for a new application.
     * 
     * This method verifies that a new application can be placed on 
     * a waitlist and that the correct transitions and notes are 
     * maintained.
     */
	@Test
	public void testWaitlistNewApplication() {
		createDummyApplicationObject();
		Command c = new Command(CommandValue.STANDBY, "009", Resolution.REVCOMPLETED, "This application can wait");
		appDummy.update(c);
		assertEquals(Application.A_NEW, appDummy.getAppType());
		assertEquals("Waitlist", appDummy.getStateName());
		assertEquals(Command.R_REVCOMPLETED, appDummy.getResolution());
		String finalNote = noteDummy + "\n-[Waitlist] This application can wait";
		assertEquals(finalNote, appDummy.getNotesString());
	}
	
	
	 /**
     * Tests reviewing a waitlisted application.
     * 
     * This method ensures that a waitlisted application can be reviewed 
     * and transitioned back to the Review state, maintaining notes 
     * and state integrity.
     */
	@Test
	public void testReviewWaitlistedApplication() {
		createDummyApplicationObject();
		Command c = new Command(CommandValue.STANDBY, "009", Resolution.REVCOMPLETED, "This application can wait");
		appDummy.update(c);
		assertEquals(Application.A_NEW, appDummy.getAppType());
		assertEquals("Waitlist", appDummy.getStateName());
		assertEquals(Command.R_REVCOMPLETED, appDummy.getResolution());
		String finalNote = noteDummy + "\n-[Waitlist] This application can wait";
		assertEquals(finalNote, appDummy.getNotesString());
		
		c = new Command(CommandValue.REOPEN, "009", Resolution.REVCOMPLETED, "Applicable for Interview");
		appDummy.update(c);
		assertEquals(Application.A_OLD, appDummy.getAppType());
		assertEquals("Review", appDummy.getStateName());
		assertEquals(Command.R_REVCOMPLETED, appDummy.getResolution());
		finalNote += "\n-[Review] Applicable for Interview";
		assertEquals(finalNote, appDummy.getNotesString());
	}
	
	
	/**
     * Tests the transition of a waitlisted application after an interview.
     * 
     * This method verifies that a waitlisted application can be reviewed 
     * and correctly transitions back to the Interview state with updated notes.
     */
	@Test
	public void testWaitlistApplicationAfterInterview() {
		createDummyApplicationObject();
		Command c = new Command(CommandValue.ACCEPT, "007", null, "Good Potential");
		appDummy.update(c);
		assertEquals("007", appDummy.getReviewer());
		assertEquals("Interview", appDummy.getStateName());
		String finalNote = noteDummy + "\n-[Interview] Good Potential";
		assertEquals(finalNote, appDummy.getNotesString());
		
		c = new Command(CommandValue.STANDBY, "007", Resolution.INTCOMPLETED, "Waitlisting");
		appDummy.update(c);
		assertEquals("007", appDummy.getReviewer());
		assertEquals("Waitlist", appDummy.getStateName());
		finalNote += "\n-[Waitlist] Waitlisting";
		assertEquals(finalNote, appDummy.getNotesString());
		
	}
	
	/**
     * Tests the rejection of an application after an interview.
     * 
     * This method verifies that when an application is rejected after 
     * an interview, it correctly transitions to the Closed state and 
     * maintains accurate notes.
     */
	@Test
	public void testRejectApplicationAfterInterview() {
		createDummyApplicationObject();
		Command c = new Command(CommandValue.ACCEPT, "007", null, "Good Potential");
		appDummy.update(c);
		assertEquals("007", appDummy.getReviewer());
		assertEquals("Interview", appDummy.getStateName());
		String finalNote = noteDummy + "\n-[Interview] Good Potential";
		assertEquals(finalNote, appDummy.getNotesString());
		
		c = new Command(CommandValue.REJECT, "007", Resolution.INTCOMPLETED, "Application Rejected");
		appDummy.update(c);
		assertEquals("007", appDummy.getReviewer());
		assertEquals("Closed", appDummy.getStateName());
		finalNote += "\n-[Closed] Application Rejected";
		assertEquals(finalNote, appDummy.getNotesString());
	}
	
	/**
     * Tests reopening a waitlisted application.
     * 
     * This method verifies that a waitlisted application can be reopened 
     * successfully, transitioning back to the Review state with the 
     * appropriate notes.
     */
	@Test
	public void testReopenWaitlistApplication() {
		createDummyApplicationObject();
		Command c = new Command(CommandValue.ACCEPT, "007", null, "Good Potential");
		appDummy.update(c);
		assertEquals("007", appDummy.getReviewer());
		assertEquals("Interview", appDummy.getStateName());
		String finalNote = noteDummy + "\n-[Interview] Good Potential";
		assertEquals(finalNote, appDummy.getNotesString());
		
		c = new Command(CommandValue.STANDBY, "007", Resolution.INTCOMPLETED, "Waitlisting");
		appDummy.update(c);
		assertEquals("007", appDummy.getReviewer());
		assertEquals("Waitlist", appDummy.getStateName());
		finalNote += "\n-[Waitlist] Waitlisting";
		assertEquals(finalNote, appDummy.getNotesString());
		
		c = new Command(CommandValue.REOPEN, "007", Resolution.INTCOMPLETED, "Reconsider Application");
		appDummy.update(c);
		assertEquals("007", appDummy.getReviewer());
		assertEquals("RefCheck", appDummy.getStateName());
		finalNote += "\n-[RefCheck] Reconsider Application";
		assertEquals(finalNote, appDummy.getNotesString());
	}
	
	
	 /**
     * Tests reopening a closed application.
     * 
     * This method verifies that a closed application can be reopened, 
     * transitioning back to the appropriate state with the correct notes.
     */
	@Test
	public void testReopenClosedApplication() {
		createDummyApplicationObject();
		Command c = new Command(CommandValue.REJECT, null, Resolution.REVCOMPLETED, "Incompetent Candidate");
		appDummy.update(c);
		assertEquals("Closed", appDummy.getStateName());
		String finalNote = noteDummy + "\n-[Closed] Incompetent Candidate";
		assertEquals(finalNote, appDummy.getNotesString());
		
		c = new Command(CommandValue.REOPEN, "007", Resolution.REVCOMPLETED, "Reconsider");
		appDummy.update(c);
		assertEquals("Review", appDummy.getStateName());
		finalNote += "\n-[Review] Reconsider";
		assertEquals(finalNote, appDummy.getNotesString());
	}
	
	 /**
     * Tests the rejection of an offer on an application.
     * 
     * This method verifies that when an offer is rejected, the application 
     * correctly transitions to the Closed state and maintains accurate 
     * notes and resolutions.
     */
	@Test
	public void testRejectOffer() {
		
		createDummyApplicationObject();
		Command c = new Command(CommandValue.ACCEPT, "007", Resolution.REVCOMPLETED, "note 1[\n]");
		appDummy.update(c);
		assertEquals("007", appDummy.getReviewer());
		assertEquals("Interview", appDummy.getStateName());
		noteDummy += "\n-[Interview] note 1[\n]";
		c = new Command(CommandValue.ACCEPT, "007", Resolution.INTCOMPLETED, "Good Interview");
		appDummy.update(c);
		assertEquals("007", appDummy.getReviewer());
		assertEquals("RefCheck", appDummy.getStateName());
		noteDummy += "\n-[RefCheck] Good Interview";
		assertEquals(noteDummy, appDummy.getNotesString());
		c = new Command(CommandValue.ACCEPT, "009", Resolution.REFCHKCOMPLETED, "Good References");
		appDummy.update(c);
		assertEquals("009", appDummy.getReviewer());
		assertEquals("Offer", appDummy.getStateName());
		noteDummy += "\n-[Offer] Good References";
		assertEquals(noteDummy, appDummy.getNotesString());
		
		c = new Command(CommandValue.REJECT, "009", Resolution.OFFERCOMPLETED, "note 2[\n]");
		appDummy.update(c);
		assertEquals("009", appDummy.getReviewer());
		assertEquals("Closed", appDummy.getStateName());
		noteDummy += "\n-[Closed] note 2[\n]";
		assertEquals(noteDummy, appDummy.getNotesString());
	}
	
	/**
     * Tests the string representation of an Application.
     * 
     * This method verifies that the toString() method of the Application 
     * class returns the expected formatted string representation of the 
     * application, including its state and details.
     */
	@Test
	public void testToString() {
		createDummyApplicationObject();
		createDummyApplicationObject();
		Command c = new Command(CommandValue.ACCEPT, "007", Resolution.REVCOMPLETED, "Good Potential");
		appDummy.update(c);
		assertEquals("007", appDummy.getReviewer());
		assertEquals("Interview", appDummy.getStateName());
		noteDummy += "\n-[Interview] Good Potential";
		c = new Command(CommandValue.ACCEPT, "007", Resolution.INTCOMPLETED, "Good Interview");
		appDummy.update(c);
		assertEquals("007", appDummy.getReviewer());
		assertEquals("RefCheck", appDummy.getStateName());
		noteDummy += "\n-[RefCheck] Good Interview";
		assertEquals(noteDummy, appDummy.getNotesString());
		c = new Command(CommandValue.ACCEPT, "009", Resolution.REFCHKCOMPLETED, "Good References");
		appDummy.update(c);
		assertEquals("009", appDummy.getReviewer());
		assertEquals("Offer", appDummy.getStateName());
		noteDummy += "\n-[Offer] Good References";
		assertEquals(noteDummy, appDummy.getNotesString());
		
		c = new Command(CommandValue.ACCEPT, "007", Resolution.OFFERCOMPLETED, "Accepting Offer");
		appDummy.update(c);
		assertEquals("007", appDummy.getReviewer());
		assertEquals("Closed", appDummy.getStateName());
		noteDummy += "\n-[Closed] Accepting Offer";
		assertEquals(noteDummy, appDummy.getNotesString());
		
		String testStr = "*1,Closed,Hired,A New Application,007,true,OfferCompleted\n" + noteDummy;
		
		assertEquals(testStr, appDummy.toString());
				
		
	}
	
}
