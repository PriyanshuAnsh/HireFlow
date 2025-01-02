/**
 * 
 */
package edu.ncsu.csc216.app_manager.model.manager;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc216.app_manager.model.application.Application;
import edu.ncsu.csc216.app_manager.model.application.Application.AppType;
import edu.ncsu.csc216.app_manager.model.command.Command;

/**
 * The AppList class manages a list of applications. It allows for adding, 
 * retrieving, and deleting applications, as well as executing commands 
 * that update the state of the applications.
 * 
 * The class also provides methods for filtering applications by type or ID.
 *
 * @author Priyanshu Dongre
 */
public class AppList {

	/**
	 * A counter used to track the number of applications
	 */
	private int counter;
	
	/**
	 * A reference to the current application being managed or processed.
	 */
	private ArrayList<Application> appList;
	
	

	/**
	 * Constructs an instance of the AppList class.
	 * Initializes the counter to 0 and creates an empty ArrayList to hold application objects.
	 */
	
	public AppList() {
		counter = 0;
		appList = new ArrayList<>();
	}
	
	
	/**
	 * Adds a new application to the list.
	 * 
	 * @param appType the type of the application (e.g., NEW, OLD, HIRED).
	 * @param summary a brief summary of the application.
	 * @param note any additional notes for the application.
	 * @return the ID of the newly added application.
	 */
	public int addApp(AppType appType, String summary, String note) {
		if(appList.size() == 0) {
			counter = counter + 1;
		}
		
		Application app = new Application(counter, appType, summary, note);
		
		//Adds the above app to the list using list's method ({below} add() is not a custom method).
		this.appList.add(app);
		sortList();
		return counter;
	}
	
	
	/**
	 * Adds a list of applications to the existing application list.
	 * 
	 * @param list the list of applications to add.
	 */
	public void addApps(List<Application> list) {
		
		//This will remove the duplicate elements.
		for(int i = 0; i < this.appList.size(); i++) {
			for(int j = 0; j < list.size(); j++) {
				if(this.appList.get(i).getAppId() == list.get(j).getAppId()) {
					list.remove(j);
				}
			}
		}
		
		//This will add the clearedList to AppList
		for(int i = 0; i < list.size(); i++) {
			this.addApp(list.get(i));
		}
		//This will sort it afterwards.
		sortList();
	}
	
	private void sortList() {
		for(int i = 0; i < appList.size(); i++) {
			for(int j = 0; j < appList.size() && j != i; j++) {
				Application temp = appList.get(i);
				if(appList.get(j).getAppId() > appList.get(i).getAppId()) {
					appList.set(i, appList.get(j));
					appList.set(j, temp);
				}
			}
		}
		if(appList.size() > 0)
		counter = appList.getLast().getAppId() + 1;
	}
	
	/**
	 * Adds a single application to the list.
	 * 
	 * @param application the application to add.
	 */
	private void addApp(Application application) {
		if(this.appList.size() == 0) {
			this.appList.add(application);
			//counter = counter + 1;
			return;
		}
		
		for(int i = 0; i < this.appList.size(); i++) {
			
			if(this.appList.get(i).getAppId() > application.getAppId()) {
				this.appList.add(i, application);
				//counter = counter + 1;
				return;
			}
		}
		appList.add(application);
		//counter += 1;
	}
	
	
	/**
	 * Returns a list of applications filtered by the specified type.
	 * 
	 * @return a list of Application objects of the given type.
	 */
	public List<Application> getApps() { 
		sortList();
		return this.appList;
	}
	
	
	/**
	 * Returns a list of applications filtered by the specified type.
	 * 
	 * @param type the type of applications to return (e.g., NEW, OLD).
	 * @return a list of Application objects of the given type.
	 */
	public List<Application> getAppsByType(String type) { 
		
		if(type == null || "".equals(type)) {
			throw new IllegalArgumentException("Invalid type");
		}
		ArrayList<Application> list = new ArrayList<>();
		for(int i = 0; i < appList.size(); i++) {
			Application temp = appList.get(i);
			if(type.equals(temp.getAppType())) {
				list.add(temp);
			}
		}
		return list;
	}
	
	
	/**
	 * Retrieves an application by its ID.
	 * 
	 * @param id the ID of the application to retrieve.
	 * @return the Application object with the given ID, or null if not found.
	 */
	public Application getAppById(int id) { 
		for(int i = 0; i < appList.size(); i++) {
			if(appList.get(i).getAppId() == id) {
				return this.appList.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Executes a command on the application with the given ID. 
	 * The command updates the application's state.
	 * 
	 * @param id the ID of the application to update.
	 * @param command the command to execute on the application.
	 */
	public void executeCommand(int id, Command command) {
		for(Application app: appList) {
			if(id == app.getAppId()) {
				app.update(command);
			}
		}
	}
	
	/**
	 * Deletes an application by its ID.
	 * 
	 * @param id the ID of the application to delete.
	 */
	public void deleteAppById(int id) {
		for(int i = 0; i < appList.size(); i++) {
			if(appList.get(i).getAppId() == id) {
				this.appList.remove(i);
			}
		}
		
	}
}

