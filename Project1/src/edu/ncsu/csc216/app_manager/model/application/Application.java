package edu.ncsu.csc216.app_manager.model.application;

import java.util.ArrayList;

import edu.ncsu.csc216.app_manager.model.command.Command;
import edu.ncsu.csc216.app_manager.model.command.Command.Resolution;


/**
 * The Application class represents a job application with various states and attributes
 * such as an ID, summary, reviewer, paperwork status, and notes.
 * 
 * @author Priyanshu Dongre
 */
public class Application {

	
	/**
	 * Enumeration to represent the type of application.
	 */
	public enum AppType { NEW, OLD, HIRED }

	
	/** The unique ID of the application */
	private int appId;
	
	
	/** A summary of the application */
	private String summary;
	
	
	/** The name or ID of the reviewer assigned to the application */
	private String reviewer;
	
	
	/** A Flag to indicate whether the paperwork for the application has been processed */
	private boolean processPaperwork;
	
	
	/** A list of notes associated with the application */
	private ArrayList<String> notes;
	
	/** Constant for the "New" application type */
	public static final String A_NEW = "New";
	
	
	/** Constant for the "Old" application type */
	public static final String A_OLD = "Old";
	
	/** Constant for the "Hired" application type */
	public static final String A_HIRED = "Hired";
	
	/** Constant representing the review phase name */
	public static final String REVIEW_NAME = "Review";
	
	/** Constant representing the interview phase name */
	public static final String INTERVIEW_NAME = "Interview";
	
	/** Constant representing the reference check phase name */
	public static final String REFCHK_NAME = "RefCheck";
	
	
	/** Constant representing the offer phase name */
	public static final String OFFER_NAME = "Offer";
	
	/** Constant representing the waitlist phase name */
	public static final String WAITLIST_NAME = "Waitlist";
	
	/** Constant representing the closed phase name */
	public static final String CLOSED_NAME = "Closed";
	
	/** The current state of the application */
	private AppState state;
	
	/** The review state of the application */
	private final AppState reviewState = new ReviewState();
	
	/** The interview state of the application */
	private final AppState interviewState = new InterviewState();
	
	
	/** The reference check state of the application */
	private final AppState refChkState = new RefCheckState();
	
	/** The offer state of the application */
	private final AppState offerState = new OfferState();
	
	/** The waitlist state of the application */
	private final AppState waitlistState = new WaitlistState();
	
	/** The closed state of the application */
	private final AppState closedState = new ClosedState();
	
	/** The type of the application (New, Old, Hired) */
	private AppType appType = AppType.NEW;
	
	/** The resolution status of the application */
	private Resolution resolution;
	
	
	
	/**
	 * Constructor to create a new Application.
	 * 
	 * @param id The application ID.
	 * @param type The type of application (NEW, OLD, HIRED).
	 * @param summary A summary of the application.
	 * @param note A note associated with the application.
	 */
	public Application(int id, AppType type, String summary, String note) {
		
		
		
		if(id < 1 || summary == null || note == null || "".equals(summary) || "".equals(note)) {
			throw new IllegalArgumentException("Application cannot be created.");
		}
		
		
		this.notes = new ArrayList<>();
		this.setState(REVIEW_NAME);
		this.setAppId(id);
		this.setSummary(summary);
		this.addNote(note);
		
		try {
			switch(type) {
			case AppType.NEW:
				this.setAppType(A_NEW);
				break;
			case AppType.OLD:
				this.setAppType(A_OLD);
				break;
			case AppType.HIRED:
				this.setAppType(A_HIRED);
				break;
			default:
				throw new IllegalArgumentException("Application cannot be created.");
			}
		} catch(NullPointerException npe) {
			throw new IllegalArgumentException("Application cannot be created.");
		}
		
	}
	
	
	/**
	 * Overloaded constructor for an existing Application.
	 * 
	 * @param id The application ID.
	 * @param state The current state of the application.
	 * @param appType The type of application (as a String).
	 * @param summary A summary of the application.
	 * @param reviewer The name of the reviewer.
	 * @param confirmed Whether the application is confirmed.
	 * @param resolution The resolution of the application.
	 * @param notes A list of notes related to the application.
	 */
	public Application(int id, String state, String appType, String summary, String reviewer,
						boolean confirmed, String resolution, ArrayList<String> notes) {
		
		if(id < 0) {
			throw new IllegalArgumentException("Application cannot be created.");
		}
		
		if(state == null || state.isEmpty() || appType == null || 
			"".equals(appType) || summary == null || summary.isEmpty() || notes == null || notes.isEmpty()) {
			throw new IllegalArgumentException("Application cannot be created.");
		}
		
		if(appType.equals(A_NEW) && (state.equals(INTERVIEW_NAME) || state.equals(OFFER_NAME) || state.equals(REFCHK_NAME))) {
			throw new IllegalArgumentException("Invalid AppType");
		} else {
			this.setAppId(id);
		}
		
		this.setSummary(summary);
		this.setState(state);
		this.setAppType(appType);
		this.setProcessPaperwork(confirmed);
		this.notes = new ArrayList<>();
		
		if((this.state.getStateName().equals(INTERVIEW_NAME) || this.state.getStateName().equals(REFCHK_NAME)
				|| state.equals(OFFER_NAME)) && (reviewer.isEmpty() || reviewer == null)) {
			throw new IllegalArgumentException("Invalid ReviewerID");
		} else if(state.equals(REVIEW_NAME) && !(reviewer == null || reviewer.isEmpty())) {
			throw new IllegalArgumentException("Invalid ReviewerID");
		} else {
			this.setReviewer(reviewer);
		}
		this.setNotes(notes);
		
		if((this.state.getStateName().equals(WAITLIST_NAME) || this.state.getStateName().equals(CLOSED_NAME)) && (resolution.isEmpty() || resolution == null)) {
			throw new IllegalArgumentException("Invalid Resolution");
		} else {
			this.setResolution(resolution);
		}
		
	}

	/**
	 * Gets the application ID.
	 * 
	 * @return the appId
	 */
	public int getAppId() {
		return appId;
	}

	/**
	 * Sets the application ID.
	 * 
	 * @param appId the appId to set
	 */
	private void setAppId(int appId) {
		
		this.appId = appId;
	}

	/**
	 * Gets the summary of the application.
	 * 
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * Sets the summary of the application.
	 * 
	 * @param summary the summary to set
	 */
	private void setSummary(String summary) {
		
		if(summary == null || "".equals(summary)) {
			throw new IllegalArgumentException("Invalid App summary.");
		}
		this.summary = summary;
	}

	/**
	 * Gets the reviewer of the application.
	 * 
	 * @return the reviewer
	 */
	public String getReviewer() {
		return reviewer;
	}

	/**
	 * Sets the reviewer of the application.
	 * 
	 * @param reviewer the reviewer to set
	 */
	private void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	/**
	 * Checks if the application paperwork is processed.
	 * 
	 * @return true if paperwork is processed, false otherwise.
	 */
	public boolean isProcessed() {
		return processPaperwork;
	}

	/**
	 * Sets the paperwork processing status.
	 * 
	 * @param processPaperwork the processPaperwork to set
	 */
	private void setProcessPaperwork(boolean processPaperwork) {
		this.processPaperwork = processPaperwork;
	}

	/**
	 * Gets the list of notes.
	 * 
	 * @return the notes
	 */
	public ArrayList<String> getNotes() {
		return this.notes;
	}

	/**
	 * Sets the list of notes.
	 * 
	 * @param notes the notes to set
	 */
	private void setNotes(ArrayList<String> notes) {
		for(int i = 0; i < notes.size(); i++) {
			String note = notes.get(i);
			
			note = note.substring(0, note.length() - 1);
		
			this.notes.add("-" + note);
		}
	}
	
	/**
	 * Sets the state of the application.
	 * 
	 * @param state the state of the application as a string.
	 */
	private void setState(String state) {
		switch(state) {
		case CLOSED_NAME:
			this.state = closedState;
			break;
		case INTERVIEW_NAME:
			this.state = interviewState;
			break;
		case OFFER_NAME:
			this.state = offerState;
			break;
		case WAITLIST_NAME:
			this.state = waitlistState;
			break;
		case REVIEW_NAME:
			this.state = reviewState;
			break;
		case REFCHK_NAME:
			this.state = refChkState;
			break;
		default:
			throw new IllegalArgumentException("Invalid state.");
		}
	}
	
	
	/**
	 * Sets the resolution of the application.
	 * 
	 * @param resolution the resolution to set
	 */
	private void setResolution(String resolution) {
		if(resolution.equals(Command.R_REVCOMPLETED)) {
			this.resolution = Resolution.REVCOMPLETED;
			return;
		} else if(resolution.equals(Command.R_INTCOMPLETED)) {
			this.resolution = Resolution.INTCOMPLETED;
			return;
		} else if(resolution.equals(Command.R_REFCHKCOMPLETED)) {
			this.resolution = Resolution.REFCHKCOMPLETED;
			return;
		} else if(resolution.equals(Command.R_OFFERCOMPLETED)) {
			this.resolution = Resolution.OFFERCOMPLETED; 
			return;
		}
	}
	
	/**
	 * Gets the resolution of the application.
	 * 
	 * @return the resolution as a string.
	 */
	public String getResolution() {
		String returnStr = null;
		if(this.resolution == Resolution.INTCOMPLETED) {
			returnStr = Command.R_INTCOMPLETED;
		}
		if(this.resolution == Resolution.OFFERCOMPLETED) {
			returnStr =  Command.R_OFFERCOMPLETED;
		}
		if(this.resolution == Resolution.REFCHKCOMPLETED) {
			returnStr = Command.R_REFCHKCOMPLETED;
		}
		if(this.resolution == Resolution.REVCOMPLETED) {
			returnStr = Command.R_REVCOMPLETED;
		}
		return returnStr;
	}
	
	private void setAppType(String appType) {
		switch(appType) {
			case A_NEW:
				this.appType = AppType.NEW;
				break;
			case A_OLD:
				this.appType = AppType.OLD;
				break;
			case A_HIRED:
				this.appType = AppType.HIRED;
				break;
			default: 
				throw new IllegalArgumentException("Invalid apptype.");
		}
	}
	
	/**
	 * Gets the type of the application.
	 * 
	 * @return the application type as a string.
	 */
	public String getAppType() {
		
		switch(this.appType) {
		case NEW:
			return A_NEW;
		case OLD:
			return A_OLD;
		case HIRED:
			return A_HIRED;
		default: 
			return null;
		}
	}
	
	
	/**
	 * Gets the current state name of the application.
	 * 
	 * @return the current state name as a string.
	 */
	public String getStateName() {
		if(this.state != null) {
			return state.getStateName();
		} else {
			return null;
		}
	}
	
	
	/**
	 * Gets the notes of the application as a single string.
	 * 
	 * @return a concatenated string of notes.
	 */
	public String getNotesString() {
		String returnStr = "";
		
//		for(String note: notes) {
//			returnStr += note + "\n";
		// notes.toString()
//		}
		
		for(int i = 0; i < notes.size(); i++) {
			if(i == notes.size() - 1) {
				returnStr += notes.get(i);
				break;
			}
			returnStr += notes.get(i) + "\n";
		}
		return returnStr;
	}
	
	
	/**
	 * Returns a string representation of the application.
	 * 
	 * @return a string containing details of the application.
	 */
	
	@Override
	public String toString() {
		String strReturn = "*" + appId + "," + state.getStateName() + "," + this.getAppType() + "," + this.summary + ",";
		
		if(this.reviewer != null) {
			strReturn += this.reviewer + ",";
		} else {
			strReturn += "";
		}
		strReturn += this.processPaperwork + ",";
		
		if(this.resolution != null) {
			strReturn += this.getResolution() + "\n";
		} else {
			strReturn += "" + "\n";
		}
		
		strReturn += this.getNotesString();
		return strReturn;
	}


	/**
	 * Adds a note to the application.
	 * 
	 * @param note the note to add.
	 */
	private void addNote(String note) {
		this.notes.add("-[" + this.state.getStateName() + "] " + note);
	}
	
	
	/**
	 * Updates the application based on the given command.
	 * 
	 * @param command the command to update the application.
	 */
	public void update(Command command) throws UnsupportedOperationException {
		this.state.updateState(command);
		this.addNote(command.getNote());
	}
	
	/**
	 * Interface for states in the Application State Pattern.  All 
	 * concrete application states must implement the AppState interface.
	 * The AppState interface should be a private interface of the 
	 * Application class.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu) 
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 * @author Priyanshu Dongre
	 */
	private interface AppState {
		
		/**
		 * Update the Application based on the given Command.
		 * An UnsupportedOperationException is thrown if the Command
		 * is not a valid action for the given state.  
		 * @param command Command describing the action that will update the Application's
		 * state.
		 * @throws UnsupportedOperationException if the Command is not a valid action
		 * for the given state.
		 */
		void updateState(Command command) throws UnsupportedOperationException;
		
		/**
		 * Returns the name of the current state as a String.
		 * @return the name of the current state as a String.
		 */
		String getStateName();
	}

	
	/**
	 * Represents the Interview state of the application.
	 */
	private class InterviewState implements AppState
	{

		@Override
		public void updateState(Command command) {
			
			switch(command.getCommand()) {
			case ACCEPT:
				
				if(command.getReviewerId() != null && !("".equals(reviewer))) {
					setReviewer(command.getReviewerId());
					//setProcessPaperwork(true);
					setState(REFCHK_NAME);
					//addNote(command.getNote());
					break;
				}
			case STANDBY:
				
				if(command.getReviewerId() != null && !"".equals(reviewer)
						&& command.getResolution() == Resolution.INTCOMPLETED) {
					setResolution(Command.R_INTCOMPLETED);
					//setProcessPaperwork(true);
					setState(WAITLIST_NAME);
					//addNote(command.getNote());
					break;
				}
				
				
			case REJECT:
				
				if(command.getResolution() == Resolution.INTCOMPLETED) {
					setResolution(Command.R_INTCOMPLETED);
					//setProcessPaperwork(true);
					setState(CLOSED_NAME);
					//addNote(command.getNote());
					break;
				}

			default: 
				throw new UnsupportedOperationException("Invalid information.");
			}
			
		}

		@Override
		public String getStateName() {
		
			return INTERVIEW_NAME;
		}
		
	}
	
	
	/**
	 * Represents the Review state of the application.
	 */
	private class ReviewState implements AppState {

		@Override
		public void updateState(Command command) {			
			switch(command.getCommand()) {
			case ACCEPT:
				if(command.getReviewerId() != null || !("".equals(getReviewer()))) {
					setReviewer(command.getReviewerId());
					setAppType(A_OLD);
					setState(INTERVIEW_NAME);
					//addNote(command.getNote());
					break;
				}
				
			case STANDBY:
				
				if(command.getResolution() == Resolution.REVCOMPLETED) {
					setResolution(Command.R_REVCOMPLETED);
					setState(WAITLIST_NAME);
					//addNote(command.getNote());
					break;
				}
				
			case REJECT:
				if(command.getResolution() == Resolution.REVCOMPLETED) {
					setResolution(Command.R_REVCOMPLETED);
					setState(CLOSED_NAME);
					//addNote(command.getNote());
					break;
				}
			default: 
				throw new UnsupportedOperationException("Invalid information.");
			}
			
		}

		@Override
		public String getStateName() {
			return REVIEW_NAME;
		}
		
	}
	
	
	/**
	 * Represents the Reference Check state of the application.
	 */
	private class RefCheckState implements AppState {

		@Override
		public void updateState(Command command) {
			switch(command.getCommand()) {
			case ACCEPT:
				if(command.getReviewerId() != null && !("".equals(getReviewer()))) {
					setReviewer(command.getReviewerId());
					setProcessPaperwork(true);
					setState(OFFER_NAME);
					//addNote(command.getNote());
					break;
				}
				
				
			case REJECT:
				if(command.getResolution() == Resolution.REFCHKCOMPLETED) {
					setResolution(Command.R_REFCHKCOMPLETED);
					setState(CLOSED_NAME);
					//addNote(command.getNote());
					break;
				}
				
			default: 
				throw new UnsupportedOperationException("Invalid information.");
			}
			
		}

		@Override
		public String getStateName() {
			return REFCHK_NAME;
		}
		
	}
	
	
	/**
	 * Represents the Offer state of the application.
	 */
	private class OfferState implements AppState {

		@Override
		public void updateState(Command command) {
			switch(command.getCommand()) {
			case ACCEPT:
				
				if(command.getReviewerId() != null && !("".equals(getReviewer())) && command.getResolution() == Resolution.OFFERCOMPLETED) {
					setReviewer(command.getReviewerId());
					setAppType(A_HIRED);
//					setProcessPaperwork(true);
					setResolution(Command.R_OFFERCOMPLETED);
					setState(CLOSED_NAME);
					//addNote(command.getNote());
					break;
				}
				
				
			case REJECT:
				if(command.getResolution() == Resolution.OFFERCOMPLETED) {
					setResolution(Command.R_OFFERCOMPLETED);
					setState(CLOSED_NAME);
					//addNote(command.getNote());
					break;
				}
				
				
			default: 
				throw new UnsupportedOperationException("Invalid information.");
			}
			
		}

		@Override
		public String getStateName() {

			return OFFER_NAME;
		}
		
	}
	
	
	/**
	 * Represents the Closed state of the application.
	 */
	private class ClosedState implements AppState {

		@Override
		public void updateState(Command command) {
			switch(command.getCommand()) {
			case REOPEN:
				if(command.getResolution() == Resolution.REVCOMPLETED && getAppType().equals(A_NEW)) {
					setAppType(A_OLD);
					setState(REVIEW_NAME);
					//addNote(command.getNote());
					break;
				}
				
			default:
				throw new UnsupportedOperationException("Invalid information.");
			}
			
		}

		@Override
		public String getStateName() {

			return CLOSED_NAME;
		}
		
	}
	
	/**
	 * Represents the Waitlist state of the application.
	 */
	private class WaitlistState implements AppState {

		@Override
		public void updateState(Command command) {
			
			switch(command.getCommand()) {
			case REOPEN:
				if(command.getResolution() == Resolution.INTCOMPLETED && command.getReviewerId() != null && !"".equals(getReviewer())) {
					setReviewer(command.getReviewerId());
					setProcessPaperwork(true);
					setState(REFCHK_NAME);
					//addNote(command.getNote());
					break;
					
				} else if(command.getResolution() == Resolution.REVCOMPLETED && getAppType().equals(A_NEW)) {
					setAppType(A_OLD);
					setState(REVIEW_NAME);
					//addNote(command.getNote());
					break;
				}
				
			default: 
				throw new UnsupportedOperationException("Invalid information.");
			}

			
		}

		@Override
		public String getStateName() {

			return WAITLIST_NAME;
		}
		
	}
}
