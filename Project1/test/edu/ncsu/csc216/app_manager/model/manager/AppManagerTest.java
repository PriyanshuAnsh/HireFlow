package edu.ncsu.csc216.app_manager.model.manager;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.app_manager.model.application.Application;
import edu.ncsu.csc216.app_manager.model.application.Application.AppType;
import edu.ncsu.csc216.app_manager.model.command.Command;

/**
 * Test class for AppManager.
 * Provides unit tests for the methods in the AppManager class.
 * 
 * @author Priyanshu Dongre
 */
public class AppManagerTest {

    /** The instance of AppManager for testing */
    private AppManager appManager;

    /** Set up initial conditions before each test */
    @Before
    public void setUp() throws Exception {
        appManager = AppManager.getInstance();
        appManager.createNewAppList(); 
    }

    /**
     * Test the Singleton pattern by verifying only one instance exists.
     */
    @Test
    public void testSingleton() {
        AppManager instance1 = AppManager.getInstance();
        AppManager instance2 = AppManager.getInstance();
        assertSame(instance1, instance2);
    }

    /**
     * Test saving applications to a file.
     * Assumes that AppWriter works correctly.
     */
    @Test
    public void testSaveAppsToFile() {
        appManager.addAppToList(AppType.NEW, "Test Summary", "Test Note");

        
        try {
            appManager.saveAppsToFile("test-file.txt");
        } catch (Exception e) {
            fail("Save to file should not throw an exception.");
        }
    }

    /**
     * Test loading applications from a file.
     * Assumes that AppReader works correctly.
     */
    @Test
    public void testLoadAppsFromFile() {
        
        try {
            appManager.loadAppsFromFile("test-files/app1.txt");
            assertEquals(6, appManager.getAppListAsArray().length); 
        } catch (Exception e) {
            fail("Load from file should not throw an exception.");
        }
    }

    /**
     * Test creating a new application list (resetting the list).
     */
    @Test
    public void testCreateNewAppList() {
        appManager.addAppToList(AppType.NEW, "Test Summary", "Test Note");
        assertEquals(1, appManager.getAppListAsArray().length); 

        appManager.createNewAppList(); // Reset the list
        assertEquals(0, appManager.getAppListAsArray().length); 
    }

    /**
     * Test retrieving the application list as a 2D array.
     */
    @Test
    public void testGetAppListAsArray() {
        appManager.addAppToList(AppType.NEW, "Test Summary 1", "Note1");
        appManager.addAppToList(AppType.OLD, "Test Summary 2", "Note2");

        Object[][] appsArray = appManager.getAppListAsArray();
        assertEquals(2, appsArray.length);
        assertEquals("Test Summary 1", appsArray[0][3]);
    }

    /**
     * Test retrieving the application list by type as a 2D array.
     */
    @Test
    public void testGetAppListAsArrayByAppType() {
        appManager.addAppToList(AppType.NEW, "Test Summary 1", "Note1");
        appManager.addAppToList(AppType.OLD, "Test Summary 2", "Note2");
        appManager.addAppToList(AppType.NEW, "Test Summary 3", "Test Note 3");

        Object[][] newAppsArray = appManager.getAppListAsArrayByAppType(Application.A_NEW);
        assertEquals(2, newAppsArray.length); 
        assertEquals("Test Summary 1", newAppsArray[0][3]); 
    }

    /**
     * Test retrieving an application by its ID.
     */
    @Test
    public void testGetAppById() {
        appManager.addAppToList(AppType.NEW, "Test Summary", "Test Note");

        Application app = appManager.getAppById(1);
        assertNotNull(app); // App should be found
        assertEquals("Test Summary", app.getSummary());

        Application notFoundApp = appManager.getAppById(69); 
        assertNull(notFoundApp); 
    }

    /**
     * Test executing a command on an application.
     */
    @Test
    public void testExecuteCommand() {
        appManager.addAppToList(AppType.NEW, "Test Summary", "Test Note");

        
        Command cmd = new Command(Command.CommandValue.ACCEPT, "user", Command.Resolution.REVCOMPLETED, "Reviewed");

        
        appManager.executeCommand(1, cmd);

        Application app = appManager.getAppById(1);
        assertNotNull(app);
        assertEquals("Interview", app.getStateName());
    }

    /**
     * Test deleting an application by its ID.
     */
    @Test
    public void testDeleteAppById() {
        appManager.addAppToList(AppType.NEW, "Test Summary 1", "Note1");
        appManager.addAppToList(AppType.OLD, "Test Summary 2", "Note2");

        assertEquals(2, appManager.getAppListAsArray().length); 

        appManager.deleteAppById(1);
        assertEquals(1, appManager.getAppListAsArray().length); 
        assertEquals("Test Summary 2", appManager.getAppListAsArray()[0][3]);
    }

    /**
     * Test edge case for deleting an application with an invalid ID.
     */
    @Test
    public void testDeleteAppByInvalidId() {
        appManager.addAppToList(AppType.NEW, "Test Summary", "Test Note");

        appManager.deleteAppById(100);
        assertEquals(1, appManager.getAppListAsArray().length); 
    }

    /**
     * Test adding an application to the list.
     */
    @Test
    public void testAddAppToList() {
        appManager.addAppToList(AppType.NEW, "Test Summary", "Test Note");
        Object[][] appsArray = appManager.getAppListAsArray();
        assertEquals(1, appsArray.length); 
        assertEquals("Test Summary", appsArray[0][3]); 
    }
}

