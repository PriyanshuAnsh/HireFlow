package edu.ncsu.csc216.app_manager.model.command;


/**
 * The Command class represents actions that can be performed on an application,
 * such as accepting, rejecting, putting on standby, or reopening the application.
 * It also holds information about the reviewer, resolution, and notes.
 * This class is used in the Application's State Pattern to transition between states.
 * 
 * It contains two enumerations: 
 * - CommandValue for actions like ACCEPT, REJECT, etc.
 * - Resolution for various stages of the application process, such as Review or Interview completion.
 * 
 * @author Priyanshu Dongre
 * 
 */
public class Command {
	
	
	/**
	 * Enum representing possible command values that dictate the action
	 * to be taken on the application.
	 */
	
	public enum CommandValue { ACCEPT, REJECT, STANDBY, REOPEN }
	

	/**
	 * Enum representing the various resolution stages of an application.
	 */
	public enum Resolution { REVCOMPLETED, INTCOMPLETED, REFCHKCOMPLETED, OFFERCOMPLETED }
	
	/** 
	 * Constant representing the "Review Completed" resolution as a string. 
	 */
	public static final String R_REVCOMPLETED = "ReviewCompleted";

	/** 
	 * Constant representing the "Interview Completed" resolution as a string. 
	 */
	
	public static final String R_INTCOMPLETED = "InterviewCompleted";
	
	/** 
	 * Constant representing the "Reference Check Completed" resolution as a string. 
	 */
	public static final String R_REFCHKCOMPLETED = "ReferenceCheckCompleted";
	

	/** 
	 * Constant representing the "Offer Completed" resolution as a string. 
	 */
	public static final String R_OFFERCOMPLETED = "OfferCompleted";
	
	/** 
	 * The ID of the reviewer assigned to the application.
	 */
	private String reviewerID;
	
	/** 
	 * notes related to the application or its review process.
	 */
	private String note;
	
	/** 
	 * The command value associated with the current action being performed on the application.
	 */
	private CommandValue c;
	
	/** 
	 * The resolution indicating the current state or outcome of the application process.
	 */
	private Resolution resolution;
	
	
	/**
	 * Constructor for creating a Command object.
	 * 
	 * @param c The action to be taken (CommandValue).
	 * @param reviewerID The ID of the reviewer issuing the command.
	 * @param resolution The stage of completion for the application (Resolution).
	 * @param note An optional note related to the command.
	 */
	public Command(CommandValue c, String reviewerID, Resolution resolution, String note) {
		
		//Invalid Operations: 
		
		//A Command with a null CommandValue parameter. A Command must have a CommandValue 
		if(c == null) {
			throw new IllegalArgumentException("Invalid information.");
		}
		
		//A Command with a CommandValue of ACCEPT and a null or empty string reviewerId.
		if(c == CommandValue.ACCEPT && (reviewerID == null || "".equals(reviewerID))) {
			throw new IllegalArgumentException("Invalid information.");
		}
		
		//A Command with a CommandValue of STANDBY or REJECT and a null resolution.
		if((c == CommandValue.STANDBY || c == CommandValue.REJECT) && resolution == null) {
			throw new IllegalArgumentException("Invalid information.");
		}
		
		//A Command with a null or empty note parameter.
		if(note == null || "".equals(note)) {
			throw new IllegalArgumentException("Invalid information.");
		}
		
		this.c = c;
		this.reviewerID = reviewerID;
		this.note = note;
		this.resolution = resolution;
	}
	
	
	/**
	 * Gets the command value representing the action to be taken.
	 * 
	 * @return the command value.
	 */
	public CommandValue getCommand() {
		return this.c;
	}
	
	
	/**
	 * Gets the reviewer ID associated with the command.
	 * 
	 * @return the reviewer ID.
	 */
	public String getReviewerId() {
		return this.reviewerID;
	}
	
	
	/**
	 * Gets the resolution stage of the application.
	 * 
	 * @return the resolution.
	 */
	public Resolution getResolution() {
		return this.resolution;
	}
	
	
	/**
	 * Gets the note associated with the command.
	 * 
	 * @return the note.
	 */
	public String getNote() {
		return this.note;
	}
}
