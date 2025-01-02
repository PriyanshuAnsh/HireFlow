/**
 * 
 */
package edu.ncsu.csc216.app_manager.model.manager;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc216.app_manager.model.application.Application;
import edu.ncsu.csc216.app_manager.model.application.Application.AppType;
import edu.ncsu.csc216.app_manager.model.command.Command;
import edu.ncsu.csc216.app_manager.model.io.AppReader;
import edu.ncsu.csc216.app_manager.model.io.AppWriter;

/**
 * The AppManager class is responsible for managing the application's data and
 * interacting with the AppList. It follows a Singleton pattern, ensuring only
 * one instance of the AppManager exists throughout the application lifecycle.
 * 
 * This class provides methods to save and load application data to/from files,
 * create new application lists, retrieve applications by their type or ID, and
 * execute commands on applications.
 * 
 * @author Priyanshu Dongre
 */
public class AppManager {

	
	/** Singleton instance of AppManager */
	private static AppManager singleton = null;
	
	
	/** The AppList instance managed by the AppManager */
	private AppList appList;
	
	/**
	 * Private constructor to prevent external instantiation.
	 * Initializes the AppManager instance.
	 */
	private AppManager() {
		appList = new AppList();
	}
	
	
	/**
	 * Returns the singleton instance of the AppManager.
	 * If the instance does not exist, it is created.
	 * 
	 * @return the singleton instance of AppManager.
	 */
	public static AppManager getInstance() {
		if(singleton == null) {
			singleton = new AppManager();
		}
		return singleton;
	}
	
	
	/**
	 * Saves the current application list to the specified file.
	 * 
	 * @param fileName the name of the file to save the applications to.
	 */
	public void saveAppsToFile(String fileName) { 	
		AppWriter.writeAppsToFile(fileName, appList.getApps());
		
	}
	
	/**
	 * Loads applications from a specified file into the current application list.
	 * 
	 * @param fileName the name of the file to load applications from.
	 */
	public void loadAppsFromFile(String fileName) {
		ArrayList<Application> apps = AppReader.readAppsFromFile(fileName);
		if(apps.size() > 0) {
			System.out.println("Apps Loaded from The file.");
		}
		appList.addApps(apps);
		
		
	}
	
	/**
	 * Creates a new application list, resetting any existing data.
	 */
	public void createNewAppList() {
		this.appList = new AppList();
	}
	
	/**
	 * Retrieves the application list as a 2D array for easy display in tables.
	 * 
	 * @return a 2D array of application data.
	 */
	public Object[][] getAppListAsArray() {
		List<Application> list = appList.getApps();
		Object[][] arr = new Object[list.size()][4];
		
		for(int i = 0; i < list.size(); i++) {
			arr[i][0] = list.get(i).getAppId();
			arr[i][1] = list.get(i).getStateName();
			arr[i][2] = list.get(i).getAppType();
			arr[i][3] = list.get(i).getSummary();
		}
		
		return arr;
	}
	
	/**
	 * Retrieves applications of the specified type as a 2D array.
	 * 
	 * @param type the type of applications to filter by (e.g., NEW, OLD).
	 * @return a 2D array of application data filtered by type.
	 */
	public Object[][] getAppListAsArrayByAppType(String type) {
		
		if(type == null || "".equals(type)) {
			throw new IllegalArgumentException("Invalid type.");
		}
		List<Application> list = appList.getApps();
		
		List<Application> spcList = new ArrayList<>();
		
		for(Application app: list) {
			if(type.equals(app.getAppType())) {
				spcList.add(app);
			}
		}
		
		Object[][] arr = new Object[spcList.size()][4];
		
		for(int i = 0; i < spcList.size(); i++) {
			arr[i][0] = spcList.get(i).getAppId();
			arr[i][1] = spcList.get(i).getStateName();
			arr[i][2] = spcList.get(i).getAppType();
			arr[i][3] = spcList.get(i).getSummary();
		}
		
		
		return arr;
	}
	
	
	/**
	 * Retrieves an application by its ID.
	 * 
	 * @param id the ID of the application to retrieve.
	 * @return the application with the specified ID, or null if not found.
	 */
	public Application getAppById(int id) {
		
		List<Application> apps = appList.getApps();
		for(Application app: apps) {
			if(id == app.getAppId()) {
				return app;
			}
		}
		return null;
	}
	
	/**
	 * Executes a command on an application, updating its state based on the command.
	 * 
	 * @param id the ID of the application to update.
	 * @param command the command to execute on the application.
	 */
	public void executeCommand(int id, Command command) {
		List<Application> apps = appList.getApps();
		
		for(Application app: apps) {
			if(id == app.getAppId()) {
				app.update(command);
				break;
			}
		}
		
	}
	
	/**
	 * Deletes an application by its ID.
	 * 
	 * @param id the ID of the application to delete.
	 */
	public void deleteAppById(int id) {
		List<Application> list = appList.getApps();
		
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).getAppId() == id) {
				list.remove(i);
				break;
			}
		}
	}
	
	
	/**
	 * Adds a new application to the application list.
	 * 
	 * @param appType the type of the application (e.g., NEW, OLD, HIRED).
	 * @param summary a brief summary of the application.
	 * @param note additional notes related to the application.
	 */
	public void addAppToList(AppType appType, String summary, String note) {
		appList.addApp(appType, summary, note);
	}
}
