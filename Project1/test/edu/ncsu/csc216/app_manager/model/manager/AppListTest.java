package edu.ncsu.csc216.app_manager.model.manager;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.app_manager.model.application.Application;
import edu.ncsu.csc216.app_manager.model.application.Application.AppType;
import edu.ncsu.csc216.app_manager.model.command.Command;

/**
 * Test class for AppList.
 * Provides unit tests for the methods in the AppList class.
 * 
 * @author Priyanshu
 */
public class AppListTest {

    /** AppList object used for testing */
    private AppList appList;

    /** Set up initial conditions before each test */
    @Before
    public void setUp() throws Exception {
        appList = new AppList();
    }

    /** 
     * Test the constructor for AppList.
     * Verifies that the initial state of the app list is empty.
     */
    @Test
    public void testAppList() {
        assertNotNull(appList.getApps());
        assertEquals(0, appList.getApps().size());
    }

    /**
     * Test adding a new application to the list.
     */
    @Test
    public void testAddApp() {
        int appId = appList.addApp(AppType.NEW, "Test Summary", "Test Note");
        assertEquals(2, appId); 
        
     
        List<Application> apps = appList.getApps();
        assertEquals(1, apps.size());
        assertEquals("Test Summary", apps.get(0).getSummary());
    }

    /**
     * Test adding multiple applications at once using a list.
     */
    @Test
    public void testAddApps() {
        List<Application> applications = new ArrayList<>();
        applications.add(new Application(1, AppType.NEW, "Summary 1", "Note 1"));
        applications.add(new Application(2, AppType.OLD, "Summary 2", "Note 2"));
        
        appList.addApps(applications);
        
        List<Application> apps = appList.getApps();
        assertEquals(2, apps.size());
        assertEquals("Summary 1", apps.get(0).getSummary());
        assertEquals("Summary 2", apps.get(1).getSummary());
    }

    /**
     * Test retrieving an application by its ID.
     */
    @Test
    public void testGetAppById() {
        appList.addApp(AppType.NEW, "Summary 1", "Note 1");
        appList.addApp(AppType.OLD, "Summary 2", "Note 2");
        
        Application app = appList.getAppById(1);
        assertNotNull(app);
        assertEquals("Summary 1", app.getSummary());
        
        Application appNotFound = appList.getAppById(169); 
        assertNull(appNotFound);
    }

    /**
     * Test retrieving applications by their type.
     */
    @Test
    public void testGetAppsByType() {
        appList.addApp(AppType.NEW, "Summary 1", "Note 1");
        appList.addApp(AppType.OLD, "Summary 2", "Note 2");
        appList.addApp(AppType.NEW, "Summary 3", "Note 3");

        
        List<Application> newApps = appList.getAppsByType(Application.A_NEW);
        assertEquals(2, newApps.size());
        assertEquals("Summary 1", newApps.get(0).getSummary());
        assertEquals("Summary 3", newApps.get(1).getSummary());
        
        Exception e1 = assertThrows(IllegalArgumentException.class, () -> appList.getAppsByType(""));
        assertEquals("Invalid type", e1.getMessage());
    }

    /**
     * Test deleting an application by its ID.
     */
    @Test
    public void testDeleteAppById() {
        appList.addApp(AppType.NEW, "Summary 1", "Note 1");
        appList.addApp(AppType.OLD, "Summary 2", "Note 2");

        assertEquals(2, appList.getApps().size());

        
        appList.deleteAppById(1);
        assertEquals(1, appList.getApps().size());
        assertEquals("Summary 2", appList.getApps().get(0).getSummary());


        appList.deleteAppById(179);
        assertEquals(1, appList.getApps().size()); 
    }

    /**
     * Test executing a command on an application.
     */
    @Test
    public void testExecuteCommand() {
        appList.addApp(AppType.NEW, "Summary 1", "Note 1");
        

        Command cmd = new Command(Command.CommandValue.ACCEPT, "reviewer123", Command.Resolution.REVCOMPLETED, "Application Approved");
        
        
        appList.executeCommand(1, cmd);
        
        Application app = appList.getAppById(1);
        assertNotNull(app);
        assertEquals("Interview", app.getStateName()); 
    }

    /**
     * Test edge case for executing a command on an invalid application ID.
     */
    @Test
    public void testExecuteCommandInvalidId() {
        appList.addApp(AppType.NEW, "Summary 1", "Note 1");


        Command cmd = new Command(Command.CommandValue.ACCEPT, "reviewer123", Command.Resolution.REVCOMPLETED, "Application Approved");

        
        appList.executeCommand(100, cmd);  

        
        Application app = appList.getAppById(1);
        assertNotNull(app);
        assertEquals("Review", app.getStateName()); 
    }
}

