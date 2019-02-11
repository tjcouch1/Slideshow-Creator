/**
 * SceneHandler.java
 * Controls and manages the overall program
 * 
 * Slideshow Creator
 * Timothy Couch, Joseph Hoang, Fernando Palacios, Austin Vickers
 * CS 499 Senior Design with Dr. Rick Coleman
 * 2/10/19
 */
package core;

import java.util.*;
import javax.swing.JFrame;

public class SceneHandler {

	/**
	 * AppType - enum of which program is running
	 * 
	 * @author Timothy Couch
	 * 
	 * Special thanks to flash at https://stackoverflow.com/questions/8063852/how-can-i-associate-a-string-with-each-member-of-an-enum
	 *
	 */
	public enum AppType {
		CREATOR("Creator"), 
		VIEWER("Viewer");
		
		/**
		 * title - name of app 
		 */
		private String title;
		
	    AppType(String title)
	    {
	        this.title = title;
	    }

	    public String getTitle()
	    {
	        return title;
	    }
	}
	
	/**
	 * appType - which program is running
	 */
	public AppType appType;
	
	/**
	 * mainFrame - window frame of program
	 */
	private JFrame mainFrame;
	
	private List<Scene> scenes;
	private int sceneIndex = 0;
	
	/**
	 * SceneHandler - creates program with specified app type
	 * @param aT AppType to open
	 * 
	 * @author Timothy Couch
	 * @author austinvickers
	 */
	public SceneHandler(AppType aT)
	{
		appType = aT;
		scenes = new ArrayList<Scene>();
		launch();
	}
	
	/**
	 * launch - opens the program's main frame
	 * @return true if successfully opened, false otherwise
	 * 
	 * @author Timothy Couch
	 */
	public boolean launch()
	{
		//set up main window
		mainFrame = new JFrame();
		mainFrame.setSize(800, 600);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setTitle("Slideshow " + appType.getTitle());
		
		
		//open main window
		//mainFrame.setVisible(true);
		
		return true;
	}
	
	public void SwitchToScene(Scene target) {
		
		if(!scenes.contains(target)) {
			scenes.add(target);
			sceneIndex = scenes.indexOf(target);
		}
		else {
			sceneIndex = scenes.indexOf(target);
		}
		mainFrame.setVisible(false);
		mainFrame = target;
		mainFrame.setVisible(true);
	}
}
