/**
 * 
 */
package edu.ncsu.csc216.app_manager.model.io;

import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import edu.ncsu.csc216.app_manager.model.application.Application;

/**
 * The AppWriter class is responsible for writing a list of applications to a file.
 * It provides a method to output the data of each Application object in the list
 * to the specified file.
 * 
 * The applications are written in a format that can be later read and processed
 * by the AppReader class.
 * 
 * @author Priyanshu Dongre
 */
public class AppWriter {

	/**
	 * Writes a list of Application objects to the specified file.
	 * Each application in the list is converted to a string format and written
	 * to the file.
	 * 
	 * @param fileName the name of the file to write the applications to.
	 * @param list the list of Application objects to write.
	 */
	public static void writeAppsToFile(String fileName, List<Application> list) {
		try {
			PrintWriter writer = new PrintWriter(new File(fileName));
			for(int i = 0; i < list.size(); i++) {
				writer.write(list.get(i).toString());
				writer.write("\n");
			}
			writer.close();
			
		} catch(IOException io) {
			throw new IllegalArgumentException("Unable to save file.");
		} 
		
	}
}
