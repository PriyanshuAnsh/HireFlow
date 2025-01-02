package edu.ncsu.csc216.app_manager.view.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import edu.ncsu.csc216.app_manager.model.application.Application;
import edu.ncsu.csc216.app_manager.model.application.Application.AppType;
import edu.ncsu.csc216.app_manager.model.command.Command;
import edu.ncsu.csc216.app_manager.model.command.Command.Resolution;
import edu.ncsu.csc216.app_manager.model.manager.AppManager;

/**
 * Container for the ApplicationManager that has the menu options for new application 
 * files, loading existing files, saving files and quitting.
 * Depending on user actions, other JPanels are loaded for the
 * different ways users interact with the UI.
 * 
 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
 */
public class AppManagerGUI extends JFrame implements ActionListener {
	
	/** ID number used for object serialization. */
	private static final long serialVersionUID = 1L;
	/** Title for top of GUI. */
	private static final String APP_TITLE = "Application Manager";
	/** Text for the File Menu. */
	private static final String FILE_MENU_TITLE = "File";
	/** Text for the New Application list menu item. */
	private static final String NEW_TITLE = "New";
	/** Text for the Load Application list menu item. */
	private static final String LOAD_TITLE = "Load";
	/** Text for the Save menu item. */
	private static final String SAVE_TITLE = "Save";
	/** Text for the Quit menu item. */
	private static final String QUIT_TITLE = "Quit";
	/** Menu bar for the GUI that contains Menus. */
	private JMenuBar menuBar;
	/** Menu for the GUI. */
	private JMenu menu;
	/** Menu item for creating a new file containing Applications. */
	private JMenuItem itemNewAppList;
	/** Menu item for loading a file containing Applications. */
	private JMenuItem itemLoadAppList;
	/** Menu item for saving the application list. */
	private JMenuItem itemSaveAppList;
	/** Menu item for quitting the program. */
	private JMenuItem itemQuit;
	/** Panel that will contain different views for the application. */
	private JPanel panel;
	/** Constant to identify ApplicationListPanel for CardLayout. */
	private static final String APP_LIST_PANEL = "AppListPanel";
	/** Constant to identify ReviewPanel for CardLayout. */
	private static final String REVIEW_PANEL = "ReviewPanel";
	/** Constant to identify InterviewPanel for CardLayout. */
	private static final String INTERVIEW_PANEL = "InterviewPanel";
	/** Constant to identify WaitlistPanel for CardLayout. */
	private static final String WAITLIST_PANEL = "WaitlistPanel";
	/** Constant to identify RefCheckPanel for CardLayout. */
	private static final String REFCHECK_PANEL = "RefCheckPanel";
	/** Constant to identify OfferPanel for CardLayout. */
	private static final String OFFER_PANEL = "OfferPanel";
	/** Constant to identify ClosedPanel for CardLayout. */
	private static final String CLOSED_PANEL = "ClosedPanel";
	/** Constant to identify CreateApplicationPanel for CardLayout. */
	private static final String CREATE_APP_PANEL = "CreateAppPanel";
	/** Application List panel - we only need one instance, so it's final. */
	private final AppListPanel pnlAppList = new AppListPanel();
	/** Review panel - we only need one instance, so it's final. */
	private final ReviewPanel pnlReview = new ReviewPanel();
	/** Interview panel - we only need one instance, so it's final. */
	private final InterviewPanel pnlInterview = new InterviewPanel();
	/** Waitlist panel - we only need one instance, so it's final. */
	private final WaitlistPanel pnlWaitlist = new WaitlistPanel();
	/** RefCheck panel - we only need one instance, so it's final. */
	private final RefCheckPanel pnlRefCheck = new RefCheckPanel();
	/** Offer panel - we only need one instance, so it's final. */
	private final OfferPanel pnlOffer = new OfferPanel();
	/** Closed panel - we only need one instance, so it's final. */
	private final ClosedPanel pnlClosed = new ClosedPanel();
	/** Add Application panel - we only need one instance, so it's final. */
	private final AddAppPanel pnlAddApp = new AddAppPanel();
	/** Reference to CardLayout for panel.  Stacks all of the panels. */
	private CardLayout cardLayout;
	
	
	/**
	 * Constructs a ApplicationManagerGUI object that will contain a JMenuBar and a
	 * JPanel that will hold different possible views of the data in
	 * the ApplicationManager.
	 */
	public AppManagerGUI() {
		super();
		
		//Set up general GUI info
		setSize(500, 700);
		setLocation(50, 50);
		setTitle(APP_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUpMenuBar();
		
		//Create JPanel that will hold rest of GUI information.
		//The JPanel utilizes a CardLayout, which stack several different
		//JPanels.  User actions lead to switching which "Card" is visible.
		panel = new JPanel();
		cardLayout = new CardLayout();
		panel.setLayout(cardLayout);
		panel.add(pnlAppList, APP_LIST_PANEL);
		panel.add(pnlReview, REVIEW_PANEL);
		panel.add(pnlInterview, INTERVIEW_PANEL);
		panel.add(pnlWaitlist, WAITLIST_PANEL);
		panel.add(pnlRefCheck, REFCHECK_PANEL);
		panel.add(pnlOffer, OFFER_PANEL);
		panel.add(pnlClosed, CLOSED_PANEL);
		panel.add(pnlAddApp, CREATE_APP_PANEL);
		cardLayout.show(panel, APP_LIST_PANEL);
		
		//Add panel to the container
		Container c = getContentPane();
		c.add(panel, BorderLayout.CENTER);
		
		//Set the GUI visible
		setVisible(true);
	}
	
	/**
	 * Makes the GUI Menu bar that contains options for loading a file
	 * containing applications or for quitting the application.
	 */
	private void setUpMenuBar() {
		//Construct Menu items
		menuBar = new JMenuBar();
		menu = new JMenu(FILE_MENU_TITLE);
		itemNewAppList = new JMenuItem(NEW_TITLE);
		itemLoadAppList = new JMenuItem(LOAD_TITLE);
		itemSaveAppList = new JMenuItem(SAVE_TITLE);
		itemQuit = new JMenuItem(QUIT_TITLE);
		itemNewAppList.addActionListener(this);
		itemLoadAppList.addActionListener(this);
		itemSaveAppList.addActionListener(this);
		itemQuit.addActionListener(this);
		
		//Start with save button disabled
		itemSaveAppList.setEnabled(false);
		
		//Build Menu and add to GUI
		menu.add(itemNewAppList);
		menu.add(itemLoadAppList);
		menu.add(itemSaveAppList);
		menu.add(itemQuit);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}
	
	/**
	 * Performs an action based on the given ActionEvent.
	 * @param e user event that triggers an action.
	 */
	public void actionPerformed(ActionEvent e) {
		//Use ApplicationManager's singleton to create/get the sole instance.
		AppManager model = AppManager.getInstance();
		if (e.getSource() == itemNewAppList) {
			//Create a new application list
			model.createNewAppList();
			itemSaveAppList.setEnabled(true);
			pnlAppList.updateTable(null);
			cardLayout.show(panel, APP_LIST_PANEL);
			validate();
			repaint();			
		} else if (e.getSource() == itemLoadAppList) {
			//Load an existing application list
			try {
				model.loadAppsFromFile(getFileName(true));
				itemSaveAppList.setEnabled(true);
				pnlAppList.updateTable(null);
				cardLayout.show(panel, APP_LIST_PANEL);
				validate();
				repaint();
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, exp.getMessage());
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemSaveAppList) {
			//Save current application list
			try {
				model.saveAppsToFile(getFileName(false));
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, exp.getMessage());
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemQuit) {
			//Quit the program
			try {
				model.saveAppsToFile(getFileName(false));
				System.exit(0);  //Ignore SpotBugs warning here - this is the only place to quit the program!
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, exp.getMessage());
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		}
	}
	
	/**
	 * Returns a file name generated through interactions with a JFileChooser
	 * object.
	 * @param load true if using an open/load dialog, false for save dialog
	 * @return the file name selected through JFileChooser
	 * @throws IllegalStateException if no file name provided
	 */
	private String getFileName(boolean load) {
		JFileChooser fc = new JFileChooser("./");  //Open JFileChoose to current working directory
		int returnVal = Integer.MIN_VALUE;
		if (load) {
			returnVal = fc.showOpenDialog(this);
		} else {
			returnVal = fc.showSaveDialog(this);
		}
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			//Error or user canceled, either way no file name.
			throw new IllegalStateException();
		}
		File gameFile = fc.getSelectedFile();
		return gameFile.getAbsolutePath();
	}

	/**
	 * Starts the GUI for the ApplicationManager application.
	 * @param args command line arguments
	 */
	public static void main(String [] args) {
		new AppManagerGUI();
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * shows the list of applications.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class AppListPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Button for creating a new Application */
		private JButton btnAddNewApp;
		/** Button for deleting the selected application in the list */
		private JButton btnDeleteApp;
		/** Button for editing the selected application in the list */
		private JButton btnEditApp;
		/** Button for listing old applications */
		private JButton btnFilterByOld;
		/** Button for listing new applications */
		private JButton btnFilterByNew;
		/** Button that will show all applications that are currently managed */
		private JButton btnShowAllApps;
		/** JTable for displaying the list of applications */
		private JTable appsTable;
		/** TableModel for Applications */
		private AppTableModel appTableModel;
		
		/**
		 * Creates the application list.
		 */
		public AppListPanel() {
			super(new BorderLayout());
			
			//Set up the JPanel that will hold action buttons
			btnShowAllApps = new JButton("Show All Applications");
			btnShowAllApps.addActionListener(this);
			btnFilterByNew = new JButton("List New Applications");
			btnFilterByNew.addActionListener(this);
			btnFilterByOld = new JButton("List Old Applications");
			btnFilterByOld.addActionListener(this);
			btnAddNewApp = new JButton("Add New Application");
			btnAddNewApp.addActionListener(this);
			btnDeleteApp = new JButton("Delete Selected Application");
			btnDeleteApp.addActionListener(this);
			btnEditApp = new JButton("Edit Selected Application");
			btnEditApp.addActionListener(this);
			
			
			JPanel pnlActions = new JPanel();
			pnlActions.setLayout(new GridLayout(2, 3));
			pnlActions.add(btnShowAllApps);
			pnlActions.add(btnFilterByNew);
			pnlActions.add(btnFilterByOld);
			pnlActions.add(btnAddNewApp);
			pnlActions.add(btnDeleteApp);
			pnlActions.add(btnEditApp);
			
			
						
			//Set up table
			appTableModel = new AppTableModel();
			appsTable = new JTable(appTableModel);
			appsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			appsTable.setPreferredScrollableViewportSize(new Dimension(500, 500));
			appsTable.setFillsViewportHeight(true);
			
			JScrollPane listScrollPane = new JScrollPane(appsTable);
			
			add(pnlActions, BorderLayout.NORTH);
			add(listScrollPane, BorderLayout.CENTER);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnAddNewApp) {
				//If the add button is clicked switch to the createApplicationPanel
				cardLayout.show(panel,  CREATE_APP_PANEL);
			} else if (e.getSource() == btnDeleteApp) {
				//If the delete button is clicked, delete the application
				int row = appsTable.getSelectedRow();
				if (row == -1 || row >= appTableModel.getRowCount()) {
					JOptionPane.showMessageDialog(AppManagerGUI.this, "No application selected.");
				} else {
					try {
						int appId = Integer.parseInt(appTableModel.getValueAt(row, 0).toString());
						AppManager.getInstance().deleteAppById(appId);
					} catch (NumberFormatException nfe ) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, "No application selected.");
					}
				}
				updateTable(null);
			} else if (e.getSource() == btnEditApp) {
				//If the edit button is clicked, switch panel based on state
				int row = appsTable.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(AppManagerGUI.this, "No application selected.");
				} else {
					try {
						int appId = Integer.parseInt(appTableModel.getValueAt(row, 0).toString());
						String stateName = AppManager.getInstance().getAppById(appId).getStateName();
						if (stateName.equals(Application.REFCHK_NAME)) {
							cardLayout.show(panel, REFCHECK_PANEL);
							pnlRefCheck.setAppInfo(appId);
						} 
						if (stateName.equals(Application.REVIEW_NAME)) {
							cardLayout.show(panel, REVIEW_PANEL);
							pnlReview.setAppInfo(appId);
						} 
						if (stateName.equals(Application.INTERVIEW_NAME)) {
							cardLayout.show(panel, INTERVIEW_PANEL);
							pnlInterview.setAppInfo(appId);
						} 
						if (stateName.equals(Application.OFFER_NAME)) {
							cardLayout.show(panel, OFFER_PANEL);
							pnlOffer.setAppInfo(appId);
						}  
						if (stateName.equals(Application.CLOSED_NAME)) {
							cardLayout.show(panel, CLOSED_PANEL);
							pnlClosed.setAppInfo(appId);
						} 
						if (stateName.equals(Application.WAITLIST_NAME)) {
							cardLayout.show(panel, WAITLIST_PANEL);
							pnlWaitlist.setAppInfo(appId);
						} 
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, "No application selected.");
					} catch (NullPointerException npe) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, "No application selected.");
					}
				}
			} else if (e.getSource() == btnFilterByOld) {
				updateTable(AppType.OLD);
			} else if (e.getSource() == btnFilterByNew) {
				updateTable(AppType.NEW);
			} else if (e.getSource() == btnShowAllApps) {
				updateTable(null);
			}
			AppManagerGUI.this.repaint();
			AppManagerGUI.this.validate();
		}
		
		public void updateTable(AppType applicationType) {
			if (applicationType == null) {
				appTableModel.updateAppData();
			} else {
				appTableModel.updateAppDataByType(applicationType);
			}
		}
		
		/**
		 * ApplicationTableModel is the object underlying the JTable object that displays
		 * the list of Applications to the user.
		 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
		 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
		 */
		private class AppTableModel extends AbstractTableModel {
			
			/** ID number used for object serialization. */
			private static final long serialVersionUID = 1L;
			/** Column names for the table */
			private String [] columnNames = {"Application ID", "Application State", "Application Type", "Application Summary"};
			/** Data stored in the table */
			private Object [][] data;
			
			/**
			 * Constructs the ApplicationTableModel by requesting the latest information
			 * from the ApplicationTableModel.
			 */
			public AppTableModel() {
				updateAppData();
			}

			/**
			 * Returns the number of columns in the table.
			 * @return the number of columns in the table.
			 */
			public int getColumnCount() {
				return columnNames.length;
			}

			/**
			 * Returns the number of rows in the table.
			 * @return the number of rows in the table.
			 */
			public int getRowCount() {
				if (data == null) 
					return 0;
				return data.length;
			}
			
			/**
			 * Returns the column name at the given index.
			 * @param col index of column
			 * @return the column name at the given column.
			 */
			public String getColumnName(int col) {
				return columnNames[col];
			}

			/**
			 * Returns the data at the given {row, col} index.
			 * @param row index of row
			 * @param col index of column
			 * @return the data at the given location.
			 */
			public Object getValueAt(int row, int col) {
				if (data == null)
					return null;
				return data[row][col];
			}
			
			/**
			 * Sets the given value to the given {row, col} location.
			 * @param value Object to modify in the data.
			 * @param row location to modify the data.
			 * @param col location to modify the data.
			 */
			public void setValueAt(Object value, int row, int col) {
				data[row][col] = value;
				fireTableCellUpdated(row, col);
			}
			
			/**
			 * Updates the given model with Application information from the ApplicationManager.
			 */
			private void updateAppData() {
				AppManager m = AppManager.getInstance();
				data = m.getAppListAsArray();
			}
			
			/**
			 * Updates the given model with Application information for the 
			 * given application type from the applicationManager.
			 * @param appType application type to search for.
			 */
			private void updateAppDataByType(AppType appType) {
				try {
					AppManager m = AppManager.getInstance();
					if (appType == AppType.NEW) {
						data = m.getAppListAsArrayByAppType("New");
					} else if(appType == AppType.OLD) {
						data = m.getAppListAsArrayByAppType("Old");
					}
				} catch (IllegalArgumentException e) {
					JOptionPane.showMessageDialog(AppManagerGUI.this, e.getMessage());
				}
			}
		}
	}
	
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a review application.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class ReviewPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** AppInfoPanel that presents the Application's information to the user */
		private AppInfoPanel pnlAppInfo;
		/** Label for the state update */
		private JLabel lblNote;
		/** Text field for the state update */
		private JTextArea txtNote;
		/** Label for reviewer id field */
		private JLabel lblReviewerId;
		/** Text field for reviewer id */
		private JTextField txtReviewerId;
		/** Assign action */
		private JButton btnAccept;
		/** Confirm action */
		private JButton btnReject;
		/** Resolve action */
		private JButton btnStandby;
		/** Cancel action */
		private JButton btnCancel;
		/** Current Application's id */
		private int appId;
		
		/**
		 * Constructs the JPanel for editing a Application in the Review State.
		 */
		public ReviewPanel() {
			pnlAppInfo = new AppInfoPanel();
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Application Information");
			pnlAppInfo.setBorder(border);
			pnlAppInfo.setToolTipText("Application Information");
			
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 1);
			lblReviewerId = new JLabel("Reviewer Id");
			txtReviewerId = new JTextField(15);
			btnAccept = new JButton("Accept");
			btnReject = new JButton("Reject");
			btnStandby = new JButton("Standby");
			btnCancel = new JButton("Cancel");
			
			btnAccept.addActionListener(this);
			btnReject.addActionListener(this);
			btnStandby.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlReviewer = new JPanel();
			pnlReviewer.setLayout(new GridLayout(1, 2));
			pnlReviewer.add(lblReviewerId);
			pnlReviewer.add(txtReviewerId);
			
			JScrollPane scrollNote = new JScrollPane(txtNote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 4));
			pnlBtnRow.add(btnAccept);
			pnlBtnRow.add(btnReject);
			pnlBtnRow.add(btnStandby);
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlReviewer, c);
			
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(lblNote, c);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 3;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(scrollNote, c);
			
			c.gridx = 0;
			c.gridy = 5;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
			
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlAppInfo, c);
			
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
			
		}
		
		/**
		 * Set the ApplicationInfoPanel with the given application data.
		 * @param appId id of the application
		 */
		public void setAppInfo(int appId) {
			this.appId = appId;
			pnlAppInfo.setAppInfo(this.appId);
			
			if (AppManager.getInstance().getAppById(appId).getAppType().contentEquals(Application.A_OLD)) {
				btnStandby.setEnabled(false);
			} else {
				btnStandby.setEnabled(true);
			}
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			Resolution r = Resolution.REVCOMPLETED;
			String reviewerId = txtReviewerId.getText();
			//Take care of note.
			String note = txtNote.getText();
			if ("".equals(note)) {
				note = null;
			}
			if (reviewerId == null || "".equals(reviewerId)) {
				reviewerId = null;
			}
			if (e.getSource() == btnAccept) {				
				if(reviewerId == null) {
					//If reviewer id is invalid, show an error message
					JOptionPane.showMessageDialog(AppManagerGUI.this, "Invalid information.");
					reset = false;
				}
				else {
					//Otherwise, try a Command.  If command fails, go back to application list
					try {
						Command c = new Command(Command.CommandValue.ACCEPT, reviewerId, null, note);
						AppManager.getInstance().executeCommand(appId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, iae.getMessage());
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, uoe.getMessage());
						reset = false;
					}		
				}
			} else if (e.getSource() == btnReject) {
				//Try a Command.  If command fails, go back to application list
				
				try {
					Command c = new Command(Command.CommandValue.REJECT, reviewerId, r, note);
					AppManager.getInstance().executeCommand(appId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(AppManagerGUI.this, iae.getMessage());
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(AppManagerGUI.this, uoe.getMessage());
					reset = false;
				}
			} else if (e.getSource() == btnStandby) {
					//Try a command.  If problem, go back to application list.
				try {
					Command c = new Command(Command.CommandValue.STANDBY, reviewerId, r, note);
					AppManager.getInstance().executeCommand(appId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(AppManagerGUI.this, iae.getMessage());
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(AppManagerGUI.this, uoe.getMessage());
					reset = false;
				}	
			}

			if (reset) {
				//All buttons lead to back application list if valid info for reviewer
				cardLayout.show(panel, APP_LIST_PANEL);
				pnlAppList.updateTable(null);
				AppManagerGUI.this.repaint();
				AppManagerGUI.this.validate();
				//Reset fields
				txtReviewerId.setText("");
				txtNote.setText("");
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a interview application.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class InterviewPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** AppInfoPanel that presents the Application's information to the user */
		private AppInfoPanel pnlAppInfo;
		/** Label for the state update */
		private JLabel lblNote;
		/** Text field for the state update */
		private JTextArea txtNote;
		/** Label for reviewer id field */
		private JLabel lblReviewerId;
		/** Text field for reviewer id */
		private JTextField txtReviewerId;
		/** Checkbox for processing paperwork */
		private JCheckBox chkProcessed;
		/** Assign action */
		private JButton btnAccept;
		/** Confirm action */
		private JButton btnReject;
		/** Resolve action */
		private JButton btnStandby;
		/** Cancel action */
		private JButton btnCancel;
		/** Current Application's id */
		private int appId;
		
		/**
		 * Constructs the JPanel for editing a Application in the Interview State.
		 */
		public InterviewPanel() {
			pnlAppInfo = new AppInfoPanel();
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Application Information");
			pnlAppInfo.setBorder(border);
			pnlAppInfo.setToolTipText("Application Information");
			
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 1);
			lblReviewerId = new JLabel("Reviewer Id");
			txtReviewerId = new JTextField(15);
			chkProcessed = new JCheckBox("Process paperwork");
			btnAccept = new JButton("Accept");
			btnReject = new JButton("Reject");
			btnStandby = new JButton("Standby");
			btnCancel = new JButton("Cancel");
			
			btnAccept.addActionListener(this);
			btnReject.addActionListener(this);
			btnStandby.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlReviewer = new JPanel();
			pnlReviewer.setLayout(new GridLayout(1, 2));
			pnlReviewer.add(lblReviewerId);
			pnlReviewer.add(txtReviewerId);
			
			JPanel pnlProcessPaperwork = new JPanel();
			pnlProcessPaperwork.setLayout(new GridLayout(1, 2));
			pnlProcessPaperwork.add(chkProcessed);
			
			JScrollPane scrollNote = new JScrollPane(txtNote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 4));
			pnlBtnRow.add(btnAccept);
			pnlBtnRow.add(btnReject);
			pnlBtnRow.add(btnStandby);
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlReviewer, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlProcessPaperwork, c);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(lblNote, c);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 3;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(scrollNote, c);
			
			c.gridx = 0;
			c.gridy = 5;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
			
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlAppInfo, c);
			
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
			
		}
		
		/**
		 * Set the ApplicationInfoPanel with the given application data.
		 * @param appId id of the application
		 */
		public void setAppInfo(int appId) {
			this.appId = appId;
			pnlAppInfo.setAppInfo(this.appId);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			Resolution r = Resolution.INTCOMPLETED;
			//Take care of note.
			String note = txtNote.getText();
			if ("".equals(note)) {
				note = null;
			}
			String reviewerId = txtReviewerId.getText();
			if (reviewerId == null || "".equals(reviewerId)) {
				reviewerId = null;
			}
			if (e.getSource() == btnAccept) {
				if (reviewerId == null || !chkProcessed.isSelected()) {
					//If reviewer id is invalid and/or process paperwork checkbox is unchecked, show an error message
					JOptionPane.showMessageDialog(AppManagerGUI.this, "Invalid information.");
					reset = false;
				} else {
					//Otherwise, try a Command.  If command fails, go back to application list
					try {
						Command c = new Command(Command.CommandValue.ACCEPT, reviewerId, null, note);
						AppManager.getInstance().executeCommand(appId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, iae.getMessage());
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, uoe.getMessage());
						reset = false;
					}		
				}
			} else if (e.getSource() == btnReject) {
				//Try a Command.  If command fails, go back to application list
				
				if(chkProcessed.isSelected()) {
					JOptionPane.showMessageDialog(AppManagerGUI.this, "Invalid information.");
					reset = false;
				} else {
					try {
						Command c = new Command(Command.CommandValue.REJECT, reviewerId, r, note);
						AppManager.getInstance().executeCommand(appId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, iae.getMessage());
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, uoe.getMessage());
						reset = false;
					}
				}
			} else if (e.getSource() == btnStandby) {
					//Try a command.  If problem, go back to application list.
				if (reviewerId == null || chkProcessed.isSelected()) {
					//If reviewer id is invalid and/or process paperwork checkbox is checked, show an error message
					JOptionPane.showMessageDialog(AppManagerGUI.this, "Invalid information.");
					reset = false;
				} else {
					try {
						Command c = new Command(Command.CommandValue.STANDBY, reviewerId, r, note);
						AppManager.getInstance().executeCommand(appId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, iae.getMessage());
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, uoe.getMessage());
						reset = false;
					}
				}
			}

			if (reset) {
				//All buttons lead to back application list
				cardLayout.show(panel, APP_LIST_PANEL);
				pnlAppList.updateTable(null);
				AppManagerGUI.this.repaint();
				AppManagerGUI.this.validate();
				//Reset fields
				txtReviewerId.setText("");
				txtNote.setText("");
				chkProcessed.setSelected(false);
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a reference check application.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class RefCheckPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** AppInfoPanel that presents the Application's information to the user */
		private AppInfoPanel pnlAppInfo;
		/** Label for the state update */
		private JLabel lblNote;
		/** Text field for the state update */
		private JTextArea txtNote;
		/** Label for reviewer id field */
		private JLabel lblReviewerId;
		/** Text field for reviewer id */
		private JTextField txtReviewerId;
		/** Checkbox for processing paperwork */
		private JCheckBox chkProcessed;
		/** Assign action */
		private JButton btnAccept;
		/** Confirm action */
		private JButton btnReject;
		/** Cancel action */
		private JButton btnCancel;
		/** Current Application's id */
		private int appId;
		
		/**
		 * Constructs the JPanel for editing a Application in the RefCheck State.
		 */
		public RefCheckPanel() {
			pnlAppInfo = new AppInfoPanel();
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Application Information");
			pnlAppInfo.setBorder(border);
			pnlAppInfo.setToolTipText("Application Information");
			
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 1);
			lblReviewerId = new JLabel("Reviewer Id");
			txtReviewerId = new JTextField(15);
			chkProcessed = new JCheckBox("Process paperwork");
			btnAccept = new JButton("Accept");
			btnReject = new JButton("Reject");
			btnCancel = new JButton("Cancel");
			
			btnAccept.addActionListener(this);
			btnReject.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlReviewer = new JPanel();
			pnlReviewer.setLayout(new GridLayout(1, 2));
			pnlReviewer.add(lblReviewerId);
			pnlReviewer.add(txtReviewerId);
			
			JPanel pnlProcessPaperwork = new JPanel();
			pnlProcessPaperwork.setLayout(new GridLayout(1, 2));
			pnlProcessPaperwork.add(chkProcessed);
			
			JScrollPane scrollNote = new JScrollPane(txtNote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 4));
			pnlBtnRow.add(btnAccept);
			pnlBtnRow.add(btnReject);
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlReviewer, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlProcessPaperwork, c);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(lblNote, c);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 3;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(scrollNote, c);
			
			c.gridx = 0;
			c.gridy = 5;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
			
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlAppInfo, c);
			
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
			
		}
		
		/**
		 * Set the ApplicationInfoPanel with the given application data.
		 * @param appId id of the application
		 */
		public void setAppInfo(int appId) {
			this.appId = appId;
			pnlAppInfo.setAppInfo(this.appId);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			Resolution r = Resolution.REFCHKCOMPLETED;
			//Take care of note.
			String note = txtNote.getText();
			if ("".equals(note)) {
				note = null;
			}
			String reviewerId = txtReviewerId.getText();
			if (reviewerId == null || "".equals(reviewerId)) {
				reviewerId = null;
			}
			if (e.getSource() == btnAccept) {
				
				if (reviewerId == null || !chkProcessed.isSelected()) {
					//If reviewer id is invalid and/or process paperwork checkbox is unchecked, show an error message
					JOptionPane.showMessageDialog(AppManagerGUI.this, "Invalid information.");
					reset = false;
				} else {
					//Otherwise, try a Command.  If command fails, go back to application list
					try {
						Command c = new Command(Command.CommandValue.ACCEPT, reviewerId, null, note);
						AppManager.getInstance().executeCommand(appId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, iae.getMessage());
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, uoe.getMessage());
						reset = false;
					}		
				}
			} else if (e.getSource() == btnReject) {
				//If process paperwork checkbox is checked, show an error message
				if(chkProcessed.isSelected()) {
					JOptionPane.showMessageDialog(AppManagerGUI.this, "Invalid information.");
					reset = false;
				} else {
					try {
						Command c = new Command(Command.CommandValue.REJECT, reviewerId, r, note);
						AppManager.getInstance().executeCommand(appId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, iae.getMessage());
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, uoe.getMessage());
						reset = false;
					}
				}
			}

			if (reset) {
				//All buttons lead to back application list
				cardLayout.show(panel, APP_LIST_PANEL);
				pnlAppList.updateTable(null);
				AppManagerGUI.this.repaint();
				AppManagerGUI.this.validate();
				//Reset fields
				txtReviewerId.setText("");
				txtNote.setText("");
				chkProcessed.setSelected(false);
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}	
	
	
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with an offer application.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class OfferPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** AppInfoPanel that presents the Application's information to the user */
		private AppInfoPanel pnlAppInfo;
		/** Label for the state update */
		private JLabel lblNote;
		/** Text field for the state update */
		private JTextArea txtNote;
		/** Label for reviewer id field */
		private JLabel lblReviewerId;
		/** Text field for reviewer id */
		private JTextField txtReviewerId;
		/** Checkbox for processing paperwork */
		private JCheckBox chkProcessed;
		/** Assign action */
		private JButton btnAccept;
		/** Confirm action */
		private JButton btnReject;
		/** Cancel action */
		private JButton btnCancel;
		/** Current Application's id */
		private int appId;
		
		/**
		 * Constructs the JPanel for editing a Application in the OfferState.
		 */
		public OfferPanel() {
			pnlAppInfo = new AppInfoPanel();
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Application Information");
			pnlAppInfo.setBorder(border);
			pnlAppInfo.setToolTipText("Application Information");
			
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 1);
			lblReviewerId = new JLabel("Reviewer Id");
			txtReviewerId = new JTextField(15);
			chkProcessed = new JCheckBox("Process paperwork");
			btnAccept = new JButton("Accept");
			btnReject = new JButton("Reject");
			btnCancel = new JButton("Cancel");
			
			btnAccept.addActionListener(this);
			btnReject.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlReviewer = new JPanel();
			pnlReviewer.setLayout(new GridLayout(1, 2));
			pnlReviewer.add(lblReviewerId);
			pnlReviewer.add(txtReviewerId);
			
			JPanel pnlProcessPaperwork = new JPanel();
			pnlProcessPaperwork.setLayout(new GridLayout(1, 2));
			pnlProcessPaperwork.add(chkProcessed);
			
			JScrollPane scrollNote = new JScrollPane(txtNote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 4));
			pnlBtnRow.add(btnAccept);
			pnlBtnRow.add(btnReject);
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlReviewer, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlProcessPaperwork, c);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(lblNote, c);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 3;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(scrollNote, c);
			
			c.gridx = 0;
			c.gridy = 5;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
			
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlAppInfo, c);
			
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
			
		}
		
		/**
		 * Set the ApplicationInfoPanel with the given application data.
		 * @param appId id of the application
		 */
		public void setAppInfo(int appId) {
			this.appId = appId;
			pnlAppInfo.setAppInfo(this.appId);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			Resolution r = Resolution.OFFERCOMPLETED;
			//Take care of note.
			String note = txtNote.getText();
			if ("".equals(note)) {
				note = null;
			}
			String reviewerId = txtReviewerId.getText();
			if (reviewerId == null || "".equals(reviewerId)) {
				reviewerId = null;
			}
			if (e.getSource() == btnAccept) {
				//If reviewer id is invalid and/or process paperwork checkbox is unchecked, show an error message
				if (reviewerId == null || !chkProcessed.isSelected()) {
					JOptionPane.showMessageDialog(AppManagerGUI.this, "Invalid information.");
					reset = false;
				} else {
					//Otherwise, try a Command.  If command fails, go back to application list
					try {
						Command c = new Command(Command.CommandValue.ACCEPT, reviewerId, r, note);
						AppManager.getInstance().executeCommand(appId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, iae.getMessage());
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, uoe.getMessage());
						reset = false;
					}		
				}
			} else if (e.getSource() == btnReject) {
				//If process paperwork checkbox is checked, show an error message
				if(chkProcessed.isSelected()) {
					JOptionPane.showMessageDialog(AppManagerGUI.this, "Invalid information.");
					reset = false;
				} else {
					//Try a Command.  If command fails, go back to application list
					try {
						Command c = new Command(Command.CommandValue.REJECT, reviewerId, r, note);
						AppManager.getInstance().executeCommand(appId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, iae.getMessage());
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, uoe.getMessage());
						reset = false;
					}
				}
			}

			if (reset) {
				//All buttons lead to back application list
				cardLayout.show(panel, APP_LIST_PANEL);
				pnlAppList.updateTable(null);
				AppManagerGUI.this.repaint();
				AppManagerGUI.this.validate();
				//Reset fields
				txtReviewerId.setText("");
				txtNote.setText("");
				chkProcessed.setSelected(false);
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a waitlist application.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class WaitlistPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** AppInfoPanel that presents the Application's information to the user */
		private AppInfoPanel pnlAppInfo;
		/** Label for the state update */
		private JLabel lblNote;
		/** Text field for the state update */
		private JTextArea txtNote;
		/** Label for reviewer id field */
		private JLabel lblReviewerId;
		/** Text field for reviewer id */
		private JTextField txtReviewerId;
		/** Label for resolution */
		private JCheckBox chkProcessed;
		/** Assign action */
		private JButton btnReopen;
		/** Cancel action */
		private JButton btnCancel;
		/** Current Application's id */
		private int appId;
		
		/**
		 * Constructs the JPanel for editing a Application in the Waitlist State.
		 */
		public WaitlistPanel() {
			pnlAppInfo = new AppInfoPanel();
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Application Information");
			pnlAppInfo.setBorder(border);
			pnlAppInfo.setToolTipText("Application Information");
			
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 1);
			lblReviewerId = new JLabel("Reviewer Id");
			txtReviewerId = new JTextField(15);
			chkProcessed = new JCheckBox("Process paperwork");
			btnReopen = new JButton("Reopen");
			btnCancel = new JButton("Cancel");
			
			btnReopen.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlReviewer = new JPanel();
			pnlReviewer.setLayout(new GridLayout(1, 2));
			pnlReviewer.add(lblReviewerId);
			pnlReviewer.add(txtReviewerId);
			
			JPanel pnlProcessPaperwork = new JPanel();
			pnlProcessPaperwork.setLayout(new GridLayout(1, 2));
			pnlProcessPaperwork.add(chkProcessed);
			
			JScrollPane scrollNote = new JScrollPane(txtNote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 4));
			pnlBtnRow.add(btnReopen);
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlReviewer, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlProcessPaperwork, c);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(lblNote, c);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 3;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(scrollNote, c);
			
			c.gridx = 0;
			c.gridy = 5;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
			
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlAppInfo, c);
			
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
			
		}
		
		/**
		 * Set the ApplicationInfoPanel with the given application data.
		 * @param appId id of the application
		 */
		public void setAppInfo(int appId) {
			this.appId = appId;
			pnlAppInfo.setAppInfo(this.appId);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			Resolution r;
			//Take care of note.
			String note = txtNote.getText();
			String type = AppManager.getInstance().getAppById(appId).getAppType();
			if ("".equals(note)) {
				note = null;
			}
			String reviewerId = txtReviewerId.getText();
			if (reviewerId == null || "".equals(reviewerId)) {
				reviewerId = null;
			}
			if (e.getSource() == btnReopen) {
				//If application type is old and reviewer id is invalid and/or process paperwork checkbox is unchecked, show an error message
				if(type.contentEquals(Application.A_OLD) && (!chkProcessed.isSelected() || reviewerId == null)) {
					JOptionPane.showMessageDialog(AppManagerGUI.this, "Invalid information.");
					reset = false;
				} else if(type.contentEquals(Application.A_NEW) && chkProcessed.isSelected()) {
					//If application type is new and process paperwork checkbox is checked, show an error message
					JOptionPane.showMessageDialog(AppManagerGUI.this, "Invalid information.");
					reset = false;
				} else {
					//Otherwise, try a Command.  If command fails, go back to application list					
					if (type.contentEquals(Application.A_OLD)) {
						r = Resolution.INTCOMPLETED;
					} else {
						r = Resolution.REVCOMPLETED;
					}
					try {
						Command c = new Command(Command.CommandValue.REOPEN, reviewerId, r, note);
						AppManager.getInstance().executeCommand(appId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, iae.getMessage());
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, uoe.getMessage());
						reset = false;
					}	
				}
			}
			

			if (reset) {
				//All buttons lead to back application list
				cardLayout.show(panel, APP_LIST_PANEL);
				pnlAppList.updateTable(null);
				AppManagerGUI.this.repaint();
				AppManagerGUI.this.validate();
				//Reset fields
				txtReviewerId.setText("");
				txtNote.setText("");
				chkProcessed.setSelected(false);
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a closed application.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class ClosedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** AppInfoPanel that presents the Application's information to the user */
		private AppInfoPanel pnlAppInfo;
		/** Label for the state update */
		private JLabel lblNote;
		/** Text field for the state update */
		private JTextArea txtNote;
		/** Label for reviewer id field */
		private JLabel lblReviewerId;
		/** Text field for reviewer id */
		private JTextField txtReviewerId;
		/** Assign action */
		private JButton btnReopen;
		/** Cancel action */
		private JButton btnCancel;
		/** Current Application's id */
		private int appId;
		
		/**
		 * Constructs the JPanel for editing a Application in the Closed State.
		 */
		public ClosedPanel() {
			pnlAppInfo = new AppInfoPanel();
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Application Information");
			pnlAppInfo.setBorder(border);
			pnlAppInfo.setToolTipText("Application Information");
			
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 1);
			lblReviewerId = new JLabel("Reviewer Id");
			txtReviewerId = new JTextField(15);
			btnReopen = new JButton("Reopen");
			btnCancel = new JButton("Cancel");
			
			btnReopen.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlReviewer = new JPanel();
			pnlReviewer.setLayout(new GridLayout(1, 2));
			pnlReviewer.add(lblReviewerId);
			pnlReviewer.add(txtReviewerId);
			
			JScrollPane scrollNote = new JScrollPane(txtNote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 4));
			pnlBtnRow.add(btnReopen);
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlReviewer, c);
			
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(lblNote, c);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 3;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(scrollNote, c);
			
			c.gridx = 0;
			c.gridy = 5;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
			
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlAppInfo, c);
			
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
			
		}
		
		/**
		 * Set the ApplicationInfoPanel with the given application data.
		 * @param appId id of the application
		 */
		public void setAppInfo(int appId) {
			this.appId = appId;
			pnlAppInfo.setAppInfo(this.appId);
			
			if (AppManager.getInstance().getAppById(appId).getAppType().contentEquals(Application.A_OLD)) {
				btnReopen.setEnabled(false);
			} else {
				btnReopen.setEnabled(true);
			}
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			//Take care of note.
			String note = txtNote.getText();
			if ("".equals(note)) {
				note = null;
			}
			if (e.getSource() == btnReopen) {				
					//Otherwise, try a Command.  If command fails, go back to application list
					try {
						Command c = new Command(Command.CommandValue.REOPEN, null, Resolution.REVCOMPLETED, note);
						AppManager.getInstance().executeCommand(appId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, iae.getMessage());
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(AppManagerGUI.this, uoe.getMessage());
						reset = false;
					}		
			}
			

			if (reset) {
				//All buttons lead to back application list
				cardLayout.show(panel, APP_LIST_PANEL);
				pnlAppList.updateTable(null);
				AppManagerGUI.this.repaint();
				AppManagerGUI.this.validate();
				//Reset fields
				txtReviewerId.setText("");
				txtNote.setText("");
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	
	

	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * shows information about the application.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class AppInfoPanel extends JPanel {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Label for id */
		private JLabel lblId;
		/** Field for id */
		private JTextField txtId;
		/** Label for state */
		private JLabel lblState;
		/** Field for state */
		private JTextField txtState;
		/** Label for summary */
		private JLabel lblSummary;
		/** Field for summary */
		private JTextArea txtSummary;
		/** Label for reviewer */
		private JLabel lblReviewer;
		/** Field for reviewer */
		private JTextField txtReviewer;
		/** Label for application type */
		private JLabel lblAppType;
		/** Field for application type */
		private JTextField txtAppType;
		/** Label for processed */
		private JLabel lblProcessed;
		/** Field for processed */
		private JTextField txtProcessed;
		/** Label for resolution */
		private JLabel lblResolution;
		/** Field for resolution */
		private JTextField txtResolution;
		/** Label for notes */
		private JLabel lblNotes;
		/** Field for notes */
		private JTextArea txtNotes;
		
		/** 
		 * Construct the panel for the application information.
		 */
		public AppInfoPanel() {
			super(new GridBagLayout());
			
			lblId = new JLabel("Application Id");
			lblState = new JLabel("Application State");
			lblSummary = new JLabel("Application Summary");
			lblReviewer = new JLabel("Reviewer");
			lblAppType = new JLabel("Application Type");
			lblProcessed = new JLabel("Processed");
			lblResolution = new JLabel("Resolution");
			lblNotes = new JLabel("Notes");
			
			txtId = new JTextField(15);
			txtState = new JTextField(15);
			txtSummary = new JTextArea(15, 3);
			txtReviewer = new JTextField(15);
			txtAppType = new JTextField(15);
			txtProcessed = new JTextField(15);
			txtResolution = new JTextField(15);
			txtNotes = new JTextArea(30, 5);
			
			txtId.setEditable(false);
			txtState.setEditable(false);
			txtSummary.setEditable(false);
			txtReviewer.setEditable(false);
			txtAppType.setEditable(false);
			txtProcessed.setEditable(false);
			txtResolution.setEditable(false);
			txtNotes.setEditable(false);
			
			JScrollPane summaryScrollPane = new JScrollPane(txtSummary, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			JScrollPane notesScrollPane = new JScrollPane(txtNotes, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			GridBagConstraints c = new GridBagConstraints();
						
			//Row 1 - ID and State
			JPanel row1 = new JPanel();
			row1.setLayout(new GridLayout(1, 4));
			row1.add(lblId);
			row1.add(txtId);
			row1.add(lblState);
			row1.add(txtState);
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row1, c);
			
			//Row 2 - Summary title
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(lblSummary, c);
			
			//Row 3 - Summary text area
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(summaryScrollPane, c);
			
			//Row 4 - Application Type & Owner
			JPanel row4 = new JPanel();
			row4.setLayout(new GridLayout(1, 4));
			row4.add(lblAppType);
			row4.add(txtAppType);
			row4.add(lblReviewer);
			row4.add(txtReviewer);
			c.gridx = 0;
			c.gridy = 4;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row4, c);
			
			//Row 5 - Application Type & Confirmed
			JPanel row5 = new JPanel();
			row5.setLayout(new GridLayout(1, 4));
			row5.add(lblProcessed);
			row5.add(txtProcessed);
			row5.add(lblResolution);
			row5.add(txtResolution);
			c.gridx = 0;
			c.gridy = 5;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row5, c);
			
			
			//Row 6 - Notes title
			c.gridx = 0;
			c.gridy = 7;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(lblNotes, c);
			
			//Row 7 - Notes text area
			c.gridx = 0;
			c.gridy = 8;
			c.weightx = 1;
			c.weighty = 4;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(notesScrollPane, c);
		}
		
		/**
		 * Adds information about the application to the display.  
		 * @param appId the id for the application to display information about.
		 */
		public void setAppInfo(int appId) {
			//Get the application from the model
			Application a = AppManager.getInstance().getAppById(appId);
			if (a == null) {
				//If the application doesn't exist for the given id, show an error message
				JOptionPane.showMessageDialog(AppManagerGUI.this, "Invalid information.");
				cardLayout.show(panel, APP_LIST_PANEL);
				AppManagerGUI.this.repaint();
				AppManagerGUI.this.validate();
			} else {
				//Otherwise, set all of the fields with the information
				txtId.setText("" + a.getAppId());
				txtState.setText(a.getStateName());
				txtSummary.setText(a.getSummary());
				txtReviewer.setText(a.getReviewer());
				txtAppType.setText(a.getAppType());
				txtProcessed.setText("" + a.isProcessed());
				String resolutionString = a.getResolution();
				if (resolutionString == null) {
					txtResolution.setText("");
				} else {
					txtResolution.setText("" + resolutionString);
				}
				txtNotes.setText(a.getNotesString());
			}
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * allows for creation of a new application.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class AddAppPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Label for application type */
		private JLabel lblAppType;
		/** Label for application type value*/
		private JTextField lblAppTypeValue;
		/** Label for identifying summary text field */
		private JLabel lblSummary;
		/** Text field for entering summary information */
		private JTextArea txtSummary;
		/** Label for identifying note text field */
		private JLabel lblNote;
		/** Text field for entering note information */
		private JTextArea txtNote;
		/** Button to add a application */
		private JButton btnAdd;
		/** Button for canceling add action */
		private JButton btnCancel;
		
		/**
		 * Creates the JPanel for adding new applications to the 
		 * manager.
		 */
		public AddAppPanel() {
			super(new GridBagLayout());  
			
			//Construct widgets
			lblAppType = new JLabel("Application Type");
			lblAppTypeValue = new JTextField(15);
			lblSummary = new JLabel("Application Summary");
			txtSummary = new JTextArea(1, 30);
			lblNote = new JLabel("Application Note");
			txtNote = new JTextArea(5, 30);
			btnAdd = new JButton("Add Application to List");
			btnCancel = new JButton("Cancel");
			
			lblAppTypeValue.setEditable(false);
			lblAppTypeValue.setText("New");
			
			
			//Adds action listeners
			btnAdd.addActionListener(this);
			btnCancel.addActionListener(this);
			
			GridBagConstraints c = new GridBagConstraints();
			
			//Builds application type panel, which is a 1 row, 2 col grid
			JPanel pnlAppType = new JPanel();
			pnlAppType.setLayout(new GridLayout(1, 2));
			pnlAppType.add(lblAppType);
			pnlAppType.add(lblAppTypeValue);
			//pnlApplicationType.add(comboApplicationType);
			
			//Creates scroll for note text area
			JScrollPane scrollNote = new JScrollPane(txtNote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			//Build button panel, which is a 1 row, 2 col grid
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridLayout(1, 2));
			pnlButtons.add(btnAdd);
			pnlButtons.add(btnCancel);
			
			//Adds all panels to main panel
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlAppType, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(lblSummary, c);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(txtSummary, c);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(lblNote, c);
			
			c.gridx = 0;
			c.gridy = 4;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(scrollNote, c);
			
			c.gridx = 0;
			c.gridy = 7;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlButtons, c);
			
			//Empty panel to cover the bottom portion of the screen
			JPanel pnlFiller = new JPanel();
			c.gridx = 0;
			c.gridy = 8;
			c.weightx = 1;
			c.weighty = 10;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlFiller, c);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true; //Assume done unless error
			if (e.getSource() == btnAdd) {
				AppType type = AppType.NEW;
				String summary = txtSummary.getText();
				String note = txtNote.getText();
				//Get instance of model and add application
				try {
					AppManager.getInstance().addAppToList(type, summary, note);
				} catch (IllegalArgumentException exp) {
					reset = false;
					JOptionPane.showMessageDialog(AppManagerGUI.this, "Application cannot be created.");
				}
			} 
			if (reset) {
				//All buttons lead to back application list
				cardLayout.show(panel, APP_LIST_PANEL);
				pnlAppList.updateTable(null);
				AppManagerGUI.this.repaint();
				AppManagerGUI.this.validate();
				//Reset fields
				txtSummary.setText("");
				txtNote.setText("");
			}
		}
	}
}