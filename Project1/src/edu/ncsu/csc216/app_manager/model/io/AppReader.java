/**
 * 
 */
package edu.ncsu.csc216.app_manager.model.io;


import java.io.FileReader;
import java.io.BufferedReader;


import java.io.IOException;

import java.util.ArrayList;
import java.util.Scanner;

import edu.ncsu.csc216.app_manager.model.application.Application;

/**
 * The AppReader class is responsible for reading application data from a file
 * and processing it into Application objects. This class provides
 * methods to read the file and convert the data into usable application instances.
 * 
 * It supports file reading and parsing individual lines into applications, 
 * returning them as a list of Application objects.
 * @author Priyanshu Dongre
 */ 
public class AppReader {

	/**
	 * Reads applications from a specified file and returns them as an ArrayList.
	 * Each line in the file represents an application, which is processed and added
	 * to the list.
	 * 
	 * @param fileName the name of the file to read applications from.
	 * @return an ArrayList of Application objects.
	 */
	public static ArrayList<Application> readAppsFromFile(String fileName) {
		
		ArrayList<Application> applications = new ArrayList<>();
		
		String strFromFile = "";
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
			
			
			
			String line = bufferedReader.readLine();
			while(line != null) {
				strFromFile += line + "\n";
				line = bufferedReader.readLine();
				//System.out.print("The Readed Line: " + line);
			}
			strFromFile = strFromFile.substring(0, strFromFile.length());
			//System.out.println("All Lines from the file: " + strFromFile);
			
			Scanner lineProcessor = new Scanner(strFromFile);
			lineProcessor = lineProcessor.useDelimiter("[*]");
			
			while(lineProcessor.hasNext()) {
				String application = lineProcessor.next();
//				System.out.println("Application String: " + application);
				Application app = processApp(application);
				applications.add(app);
				//System.out.println("Application.toString(): " + app.toString());
			}
			lineProcessor.close();
			bufferedReader.close();
			return applications;
			
		} catch (IOException io) {
			throw new IllegalArgumentException("Unable to load file.");
		}
		
		
	}
	
	
	/**
	 * Processes a single line of text and converts it into an Application object.
	 * The line should contain data fields that map to the attributes of the Application.
	 * 
	 * @param line the line of text to process.
	 * @return an Application object created from the line data.
	 */
	
	private static Application processApp(String line) {
		Scanner lineProcessor = new Scanner(line).useDelimiter(",");
		
		int id = Integer.parseInt(lineProcessor.next());
		//System.out.println(id);
		String state = lineProcessor.next();
		//System.out.println(state);
		String type = lineProcessor.next();
		//System.out.println(type);
		String summary = lineProcessor.next();
		//System.out.println(summary);
		String reviewer = lineProcessor.next();
		//System.out.println(reviewer);
		boolean processPaperwork = Boolean.parseBoolean(lineProcessor.next());
		//System.out.println(processPaperwork);
		String resolution = lineProcessor.next();
		
		if(Character.isLetter(resolution.charAt(0))) {
			resolution = resolution.substring(0, resolution.indexOf('\n'));
		} else {
			resolution = "";
		}
		
		if(lineProcessor.hasNext()) {
			throw new IllegalArgumentException("Unable to load file.");
		}
		
		//System.out.println("Index of first `-`: " + line.indexOf("-"));
		
		String notesStr = line.substring(line.indexOf("-"));
		lineProcessor = new Scanner(notesStr).useDelimiter("[-]");
		
		ArrayList<String> notes = new ArrayList<>();
		//System.out.println("LineProcesor with Delimiter`-`: " + lineProcessor.next());
		
		while(lineProcessor.hasNext()) {
			String note = lineProcessor.next();
			notes.add(note);
			//System.out.println("Note:" + note);
		}
		
//		System.out.println("Resolution (0): " + resolution);
		Application app = new Application(id, state, type, summary, reviewer, processPaperwork, resolution, notes);
//		System.out.println("Resolution (1): " + resolution);
//		System.out.println("Application " + app.getAppId() + ": " + app.toString());
//		System.out.println("Resolution (2): " + app.getResolution());
//		System.out.println("Last Time: "+ resolution + ":" + app.getResolution());
//		System.out.println(app.getNotesString() + " -> Last Line");
		lineProcessor.close();
		return app;
		
	}
}
