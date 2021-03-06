/**
 * Trans_CrossDissolve.java
 * Fades between two images
 * Original Author: R Coleman
 * Modified by Timothy Couch
 * 
 * Slideshow Creator
 * Timothy Couch, Joseph Hoang, Fernando Palacios, Austin Vickers
 * CS 499 Senior Design with Dr. Rick Coleman
 * 4/11/19
 */

package pkgImageTransitions.Transitions;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import core.SliderColor;
import core.Thumbnail;
import pkgImageTransitions.ColemanTransition;

public class Trans_CrossDissolve extends ColemanTransition
{

	/** How many time to fade between the images per second. Updates on the fly based on machine performance */
	protected static int fps = 10;
	
	//---------------------------------------------------
	/** Perform the transition from one image to another */
	// Args:  
	//  imgPanel - Panel the images are drawn into
	//	ImageA - Current Image on the screen
	//  ImageB - Image to transition to
	//  time - Number of seconds to take to do this transition
	// Note both off screen BufferedImages have already been
	// scaled to exactly fit in the area of the imgPanel
	// Cross dissolves need to be more gradual, i.e. more
	//   redraws with a smaller increment on the alpha
	//---------------------------------------------------------
	public void DrawImageTransition(JPanel imgPanel, BufferedImage ImageA, BufferedImage ImageB, double time)
	{
		Graphics gPan = imgPanel.getGraphics();

		int numIterations = (int) (fps * time);
		int timeMillis = (int) (time * 1000);
		int timeInc = timeMillis / numIterations; // Milliseconds to pause each step
		
		//create an image A the size of the container
		BufferedImage contImageA = new BufferedImage(imgPanel.getWidth(), imgPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Thumbnail.drawImageFillImage(ImageA, contImageA, SliderColor.dark_gray);
		
		//create an image B the size of the container with solid background
		BufferedImage contImageB = new BufferedImage(imgPanel.getWidth(), imgPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Thumbnail.drawImageFillImage(ImageB, contImageB, SliderColor.dark_gray);

		//draw transparent B onto A over and over again and repaint A
		float alphaInc = 1.0f / numIterations;//amount to increase B's alpha each step
		float bAlpha = alphaInc;//compounding amount of alpha for B
		int avgElapsedTime = 0;//how much time each fade step takes on average
		for(int i=0; i < numIterations; i++)
		{
			if (isAborting())
				break;
			
			long startTime = System.currentTimeMillis();
			
			//make image b more visible
			
			float[] scales = { 1.0f, 1.0f, 1.0f, bAlpha};
			float[] offsets = new float[4];
			RescaleOp rop = new RescaleOp(scales, offsets, null);
			BufferedImage contImageBFiltered = rop.filter(contImageB, null);
			
			bAlpha += alphaInc;//increment image B's alpha over time
			
			//draw A onto the screen
			BufferedImage contImageAStacked = (BufferedImage) Thumbnail.cloneImage(contImageA);
			
			//draw the transparent image B onto the screen more visible every loop
			contImageAStacked.createGraphics().drawImage(contImageBFiltered, 0, 0, null);
			
			imgPanel.removeAll();
			
			imgPanel.add(new JLabel(new ImageIcon(contImageAStacked)));
			imgPanel.revalidate();
			
			//pause for a bit
			try 
			{
				int elapsedTime = (int) (System.currentTimeMillis() - startTime);
				avgElapsedTime += elapsedTime;
			    Thread.sleep(Math.max(timeInc - elapsedTime, 0));
			} 
			catch(InterruptedException ex) 
			{
			    Thread.currentThread().interrupt();
			}
		}
		
		if (!isAborting())
		{
			//adjust number of iterations to get a proper transition for next time
			avgElapsedTime /= numIterations;
			
			int prevFps = fps;
			
			//set fps to how many frames of the average elapsed time will fit into one second
			if (avgElapsedTime != 0)
				fps = Math.min(Math.max(Math.round(1000 / avgElapsedTime), 5), 60);//limit framerate to between 5 and 60 fps
			else fps = 60;//so fast that it didn't even take a full millisecond on average
			
			//System.out.println("timeInc: " + timeInc + " numIterations: " + numIterations + " avgElapsedTime: " + avgElapsedTime + "\nprevFps: " + prevFps + " fps: " + fps);
		}
	}
	
}
