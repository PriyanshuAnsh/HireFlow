/**
 * 
 */
package edu.ncsu.csc216.app_manager.model.io;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.app_manager.model.application.Application;

/**
 * This class tests the AppWriter functionality. It ensures that applications are correctly 
 * written to a file and that the written output matches the expected content. 
 * 
 * The test reads a file of applications, writes them to another file, and compares the 
 * result with an expected output.
 * 
 * @author Priyanshu Dongre
 */

class AppWriterTest {
	
	 /**
	  *  Path to the expected file for closed applications
	  */
	private final String expFile1 = "test-files/exp_app_closed.txt";
	
//	private final String expFile2 = "test-files/exp_app_interview.txt";

	/**
     * Tests the process of reading a file with closed applications, writing them to a new file, 
     * and verifying that the new file matches the expected output.
     */
	@Test
	public void testReadClosedApplicationFile() {
		try {
			List<Application> apps = AppReader.readAppsFromFile("test-files/app_closed.txt");
			try {
				AppWriter.writeAppsToFile("test-files/act_app_closed.txt", apps);
			} catch(IllegalArgumentException io) {
				fail("Unable to write to the file.");
			}
			checkFiles(expFile1, "test-files/act_app_closed.txt");
		}
		catch(IllegalArgumentException ie) {
			fail("Unable to read file.");
		}
		
		
		
	}
	
	
	 /**
     * Helper method to compare the contents of two files line by line.
     * 
     * @param expFile the expected file path
     * @param actFile the actual file path to be compared
     */
	private void checkFiles(String expFile, String actFile) {
		try (Scanner expScanner = new Scanner(new File(expFile));
				 Scanner actScanner = new Scanner(new File(actFile));) {
				
				while (expScanner.hasNextLine()) {
					assertEquals(expScanner.nextLine(), actScanner.nextLine());
				}
				
				expScanner.close();
				actScanner.close();
			} catch (IOException e) {
				fail("Error reading files.");
			}
	}

}
