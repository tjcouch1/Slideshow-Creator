/**
 * SelectScene.java
 * Scene in which users choose which pictures to use
 * 
 * Slideshow Creator
 * Timothy Couch, Joseph Hoang, Fernando Palacios, Austin Vickers
 * CS 499 Senior Design with Dr. Rick Coleman
 * 2/17/19
 */
package creator;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import core.*;

public class SelectScene extends Scene
{
	private static final FileNameExtensionFilter imageFileFilter = new FileNameExtensionFilter("Image File", "jpg", "jpeg", "png", "gif", "bmp");

	/** Create custom color */
	private JPanel optionsPanel;
	
	/** Create custom color */
	private JPanel imagePanel;

	/** main image selection panel constraints */
	private GridBagConstraints imagePanelConstraints;

	/** container for image panel */
	private JPanel imagePanelContainer;
	
	/** Create Settings Pane */
	private JScrollPane imageScroller;
	
	/** Back button */
	private JButton backButton;
	
	/** Arrange button */
	private JButton arrangeButton;
	
	/** Select all button */
	private JButton selectAllButton;
	
	/**export slider file button*/
	private JButton exportButton;
	
	/** List of all thumbnail thumbButtons to be shown on the image panel */
	private JButton[] thumbButtons;
	
	/** Deselect all button */
	private JButton deselectAllButton;
	
    /** Label that shows the directory */
    private JLabel directoryLabel;
	
	/** Back custom button image */
	private ImageIcon back;
	
	/** Arrange custom button image */
	private ImageIcon arrange;
	
	/**Export slider button image*/
	private ImageIcon export;
	
	/** Select all custom button image */
	private ImageIcon selectAll;
	
	/** Deselect all custom button image */
	private ImageIcon deselectAll;
	
	/** Highlighted back custom button image */
	private ImageIcon highlightedBack;
	
	/** Highlighted arrange custom button image */
	private ImageIcon highlightedArrange;
	
	/** Highlighted select all custom button image */
	private ImageIcon highlightedSelectAll;
	
	/** Highlighted deselect all custom button image */
	private ImageIcon highlightedDeselectAll;
	
	/**Highlighted export button image*/
	private ImageIcon highlightedExport;
	
	/** Create ThumbnailsList object to reference */
	private ThumbnailsList allThumbs;

	/**
	 * SelectScene() - sets up selection scene with GUI stuff
	 *
	 * @author Fernando Palacios
     * @author austinvickers
     * @author Timothy Couch
     */
    public SelectScene()
    {
    	allThumbs = new ThumbnailsList();
    	
		// Create GridBagLayout object and constraints
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		
		// Set image locations
		back = new ImageIcon(getClass().getResource("/creator/Images/backButton.png"));
		arrange = new ImageIcon(getClass().getResource("/creator/Images/arrangeButton.png"));
		selectAll = new ImageIcon(getClass().getResource("/creator/Images/selectAllButton.png"));
		deselectAll = new ImageIcon(getClass().getResource("/creator/Images/deselectAllButton.png"));
		export = new ImageIcon(getClass().getResource("/creator/Images/exportButton.png"));
		highlightedBack = new ImageIcon(getClass().getResource("/creator/Images/highlightedBackButton.png"));
		highlightedArrange = new ImageIcon(getClass().getResource("/creator/Images/highlightedArrangeButton.png"));
		highlightedSelectAll = new ImageIcon(getClass().getResource("/creator/Images/highlightedSelectAllButton.png"));
		highlightedDeselectAll = new ImageIcon(getClass().getResource("/creator/Images/highlightedDeselectAllButton.png"));
		highlightedExport = new ImageIcon(getClass().getResource("/creator/Images/highlightedExportButton.png"));
		
		// Set frame configurations
		this.setLayout(gridBag);
		
		// Create back button
		backButton = new JButton(back);
		backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		backButton.setToolTipText("Back");
		backButton.setBorder(BorderFactory.createEmptyBorder());
		backButton.setContentAreaFilled(false);
		backButton.setFocusable(false);
		backButton.setRolloverIcon(highlightedBack);
		backButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	GoToDirectoryScene();
		    }
		});
		
		// Create select all button
		arrangeButton = new JButton(arrange);
		arrangeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		arrangeButton.setToolTipText("Arrange");
		arrangeButton.setBorder(BorderFactory.createEmptyBorder());
		arrangeButton.setContentAreaFilled(false);
		arrangeButton.setFocusable(false);
		arrangeButton.setRolloverIcon(highlightedArrange);
		arrangeButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	
		    	Timeline timeline = SceneHandler.singleton.getTimeline();
		    	
		    	if(timeline.thumbnailsList.getSize() == 0) {
			    	// Open warning pane in the center of our workspace
			    	JFrame parent = SceneHandler.singleton.getMainFrame();
			    	Coord2 point = new Coord2(
			    			parent.getX() + parent.getSize().width/2,
			    			parent.getY() + parent.getSize().height/2
			    			);
			    	WarningPane p = new WarningPane(parent, "Warning - No selection", "You currently have no images selected. Please select at least one image to continue.", point, new Dimension(400, 190));
			    	parent.setEnabled(false);
			    	return;
		    	}
		    	
		    	GoToArrangeScene();
		    }
		});
		
		// Create select all button
		selectAllButton = new JButton(selectAll);
		selectAllButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		selectAllButton.setToolTipText("Select All");
		selectAllButton.setBorder(BorderFactory.createEmptyBorder());
		selectAllButton.setContentAreaFilled(false);
		selectAllButton.setFocusable(false);
		selectAllButton.setRolloverIcon(highlightedSelectAll);
		selectAllButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
				SelectAll(allThumbs);
		    }
		});
		
		// Create deselect all button
		deselectAllButton = new JButton(deselectAll);
		deselectAllButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		deselectAllButton.setToolTipText("Deselect All");
		deselectAllButton.setBorder(BorderFactory.createEmptyBorder());
		deselectAllButton.setContentAreaFilled(false);
		deselectAllButton.setFocusable(false);
		deselectAllButton.setRolloverIcon(highlightedDeselectAll);
		deselectAllButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	DeselectAll(allThumbs);
		    }
		});
		
		// Create export button
		exportButton = new JButton(export);
		exportButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		exportButton.setToolTipText("Export Project");
		exportButton.setBorder(BorderFactory.createEmptyBorder());
		exportButton.setContentAreaFilled(false);
		exportButton.setFocusable(false);
		exportButton.setRolloverIcon(highlightedExport);
		exportButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	
		    	//if user has loaded in a file or has already saved
		    	//directory explorer will not prompt
		    	if(TimelineParser.getHasSavedOnce())
		    	{
		    		try {
						TimelineParser.ExportTimeline(TimelineParser.getLastDirPath());
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    		
		    	}
		    	else
		    	{
			    	JFileChooser chooser = new JFileChooser();
			    	chooser.setDialogTitle("Save File");
					chooser.setCurrentDirectory(new File(SceneHandler.singleton.getDirectory()));//start at this directory
					chooser.setFileFilter(new FileNameExtensionFilter("Slideshow File", "sl"));
					int returnVal = chooser.showSaveDialog(SceneHandler.singleton.getMainFrame());
			    	if(returnVal == JFileChooser.APPROVE_OPTION) 
			    	{
			    		try
			    		{
				    	    File slFile = chooser.getSelectedFile();
				    	    if(slFile.exists() || new File(chooser.getSelectedFile() + ".sl").exists())
				    	    {
				    	    	int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to override existing file?", "Confirm",
		    	    		    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		    	    		    if (confirmation == JOptionPane.YES_OPTION) 
		    	    		    {
		    	    		      TimelineParser.ExportTimeline(slFile.getAbsolutePath().replace(".sl", ""));
		    	    		      TimelineParser.setLastDirPath(slFile.getAbsolutePath().replace(".sl", ""));
		    	    		      TimelineParser.setHasSavedOnce(true);
		    	    		    }
				    	    }
				    	    else
				    	    {
				    	    	TimelineParser.ExportTimeline(slFile.getAbsolutePath().replace(".sl", ""));
				    	    	TimelineParser.setLastDirPath(slFile.getAbsolutePath().replace(".sl", ""));
				    	    	TimelineParser.setHasSavedOnce(true);
				    	    }
			    		} catch (FileNotFoundException fnfe) 
			    		{
			    			
			    		}
			    	}
		    	}
		    }
		});
		
		// Set options panel configurations
		optionsPanel = new JPanel();
		optionsPanel.setLayout(gridBag);
		optionsPanel.setBackground(SliderColor.medium_gray);
		
		// Set constraints and add back button
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		optionsPanel.add(backButton, c);
		
		// Set constraints and add arrange button
		c.gridx = 0;
		c.gridy = 1;
		optionsPanel.add(arrangeButton, c);
		
		// Set constraints and add select all button
		c.gridx = 0;
		c.gridy = 2;
		optionsPanel.add(selectAllButton, c);
		
		// Set constraints and add deselect all button
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 3;
		optionsPanel.add(deselectAllButton, c);
		
		// Set constraints and add deselect all button
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 4;
		optionsPanel.add(exportButton, c);
				
		// Create outerpanel that houses the image panel for layout and whitespace
		imagePanelContainer = new JPanel();
		imagePanelContainer.setLayout(gridBag);
		imagePanelContainer.setBackground(SliderColor.dark_gray);
		
		// Set up image panel constraints
		c.insets = new Insets(44, 44, 44, 44);
		imagePanelConstraints = (GridBagConstraints) c.clone();
		
		// Set up blank image panel
		setupImagePanel(false);
		imagePanelContainer.add(imagePanel, c);
		
		// Set scroll pane configurations
		imageScroller = new JScrollPane(imagePanelContainer, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		imageScroller.getVerticalScrollBar().setBackground(SliderColor.light_gray);
		imageScroller.setBorder(BorderFactory.createEmptyBorder());
		imageScroller.getVerticalScrollBar().setUnitIncrement(25);
		
		// Set constraints and add options panel
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 0, 0);
		c.weightx = 0;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;
		this.add(optionsPanel, c);
		
		// Set constraints and add image panel
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.gridx = 1;
		c.gridy = 0;
		this.add(imageScroller, c);
		this.revalidate();

        directoryLabel = new JLabel("Select Scene! Directory: ");

	}
	
	/**
	 * populates the image panel with images from allThumbs
	 * @param revalidate whether to revalidate the scene once finished
	 * 
	 * @author Fernando Palacios
	 * @author Timothy Couch
	 */
	public void setupImagePanel(boolean revalidate)
	{		
		// Create image panel with new images
		imagePanel = new JPanel();
		imagePanel.setLayout(new GridBagLayout());
		imagePanel.setBackground(SliderColor.dark_gray);
		ShowImages();
		
		// add to outer panel that houses the image panel for layout and whitespace
		imagePanelContainer.removeAll();
		imagePanelContainer.add(imagePanel, imagePanelConstraints);

		if (revalidate)
			this.revalidate();
	}
    
	/**
	 * GoToDirectoryScene() - changes scene to directory
	 *
	 * @author Fernando Palacios
	 * @author Timothy Couch
     */
	public void GoToDirectoryScene()
	{
		//prompt to restart program when returning to directory to clear the scene
		int confirmed = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to return to the project select scene?\n\nAny unsaved changes will be lost.", "Confirm Close Project",
				JOptionPane.YES_NO_OPTION);
		if (confirmed == JOptionPane.YES_OPTION) {
			SceneHandler.singleton.restartProgram();
			SceneHandler.singleton.SwitchToScene(SceneType.DIRECTORY);
		}
	}
	
	/**
	 * GoToArrangeScene()() - changes scene to arrange
	 *
	 * @author Fernando Palacios
     */
	public void GoToArrangeScene()
	{
		System.out.println("Thumbnails in Timeline:");
		for (Thumbnail t : SceneHandler.singleton.getTimeline().thumbnailsList.getThumbnails())
			System.out.println(t.getImagePath());
		SceneHandler.singleton.GetSceneInstanceByType(SceneType.ARRANGE).initialize();
		SceneHandler.singleton.SwitchToScene(SceneType.ARRANGE);
		
		//TimelineParser.ExportTimeline("test");
	}
	
    /**
     * UpdateSelected() - updates the highlighted images to reflect changes in thumbnails list
     * 
     * @author Fernando Palacios
     */
	public void UpdateSelected()
	{
		for(int i = 0; i < thumbButtons.length; i++)
		{
			//get instance of timeline to compare what is in it
			Timeline timeline = SceneHandler.singleton.getTimeline();
			
			//grab individual thumbnail
			Thumbnail buttonThumb = allThumbs.getThumbnail(i);

			int slideIndex = timeline.thumbnailsList.indexOf(buttonThumb);
			//if thumbnail is in the timeline, keep it highlighted
			if (slideIndex >= 0)
			{
				thumbButtons[i].setBorder(new LineBorder(SliderColor.aqua, 3));
				thumbButtons[i].setIcon(new ImageIcon(ImageHover(buttonThumb.getImageThumb())));			
			}
			else//thumbnail not on timeline, remove highlight
			{
				thumbButtons[i].setBorder(BorderFactory.createEmptyBorder());
				thumbButtons[i].setIcon(new ImageIcon(buttonThumb.getImageThumb()));
			}
		}
	}
	
    /**
     * ShowImages() - creates thumbnail icons to display in scrollpane
     * 
     * @author Fernando Palacios
	 * @author Timothy Couch
     */
	private void ShowImages()
	{
		int i = 0; //counter for buttons
		int gridxCounter = 0; //layout counter x
		int gridyCounter = 0; // layout counter y
		thumbButtons = new JButton[allThumbs.getSize()];
		GridBagConstraints c = new GridBagConstraints();

		for(i = 0; i < thumbButtons.length; i++) {
			
			if (gridxCounter > 2) {
				gridxCounter = 0;
				gridyCounter++;
			}
			
			//set constraints
			c.gridx = gridxCounter;
			c.gridy = gridyCounter;
			c.fill = GridBagConstraints.BOTH;
			c.insets = new Insets(40, 40, 40, 40);
			
			gridxCounter++;
			
			Thumbnail buttonThumb = allThumbs.getThumbnail(i);
			
			thumbButtons[i] = new JButton(new ImageIcon(buttonThumb.getImageThumb()));
			JButton keeper = thumbButtons [i];
			thumbButtons[i].setPreferredSize(new Dimension(320, 200));
			thumbButtons[i].setRolloverEnabled(true);
			thumbButtons[i].setRolloverIcon(new ImageIcon(ImageHover(buttonThumb.getImageThumb())));
			thumbButtons[i].setPressedIcon(new ImageIcon(ImageHover(buttonThumb.getImageThumb())));
			thumbButtons[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			thumbButtons[i].setBorder(BorderFactory.createEmptyBorder());
			thumbButtons[i].setFocusable(false);
			thumbButtons[i].setContentAreaFilled(false);
			thumbButtons[i].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					//add button to or remove button from timeline
					Timeline timeline = SceneHandler.singleton.getTimeline();

					int slideIndex = timeline.thumbnailsList.indexOf(buttonThumb);
					//if thumbnail is in the timeline, remove it
					if (slideIndex >= 0)
					{
						timeline.removeSlide(slideIndex);
						keeper.setBorder(BorderFactory.createEmptyBorder());
						keeper.setIcon(new ImageIcon(buttonThumb.getImageThumb()));
					}
					else//thumbnail not on timeline, add it
					{
						timeline.addSlide(buttonThumb);
						keeper.setBorder(new LineBorder(SliderColor.aqua, 3));
						keeper.setIcon(new ImageIcon(ImageHover(buttonThumb.getImageThumb())));
					}
				}
				});
			imagePanel.add(thumbButtons[i], c);
		}
	}

    /**
     * initialize() - opens the images and sets up the scene for use
     * 
     * @precondition must run after project directory has been determined
	 * 
	 * @author Timothy Couch
     */
    @Override
    public void initialize()
    {
		super.initialize();

        directoryLabel.setText(directoryLabel.getText() + SceneHandler.singleton.getDirectory());
        
		//set up thumbnail list
		allThumbs = new ThumbnailsList();
		addImagesInDirectory(new File(SceneHandler.singleton.getDirectory()));
		setupImagePanel(true);
		UpdateSelected();
    }
    
    /**
     * Adds all images that are in the supplied directory to allThumbs recursively (searches subfolders)
     * @param currDir the directory to add images from and below
	 * 
	 * @author Timothy Couch
     */
    private void addImagesInDirectory(File currDir) {
    	//Scraping directory credit to RoflCopterException at https://stackoverflow.com/questions/5694385/getting-the-filenames-of-all-files-in-a-folder
    	File[] files = currDir.listFiles();

    	for (File f : files) {
			//if it's a file
    		if (f.isFile()) {
				//if image, add it to the list of thumbnails
				if (imageFileFilter.accept(f))
				{
					allThumbs.addThumbnail(new Thumbnail(f.getAbsolutePath()));
				}
			}
			//if it's a directory, check in it
			else if (f.isDirectory())
				addImagesInDirectory(f);
    	}
	}
    
    /**
     * SelectAll() - selects all images so they are in timeline
     * @param list - the thumbnail list that holds all the image paths
	 * 
	 * @author Fernando Palacios
     */
    private void SelectAll(ThumbnailsList list) {
    	
    	Timeline timeline = SceneHandler.singleton.getTimeline();
    	
    	for(int i = 0; i < thumbButtons.length; i++) {
    		
    		Thumbnail buttonThumb = list.getThumbnail(i);

			int slideIndex = timeline.thumbnailsList.indexOf(buttonThumb);
			
			if (slideIndex < 0)
			{
				timeline.addSlide(buttonThumb);
				thumbButtons[i].setBorder(new LineBorder(SliderColor.aqua, 3));
				thumbButtons[i].setIcon(new ImageIcon(ImageHover(buttonThumb.getImageThumb())));
			}
		}
    }
    
    /**
     * DeselectAll() - deselects all images so they are not in timeline
     * @param list - the thumbnail list that holds all the image paths
	 * 
	 * @author Fernando Palacios
     */
    private void DeselectAll(ThumbnailsList list) {
    	
    	Timeline timeline = SceneHandler.singleton.getTimeline();
    	
    	for(int i = 0; i < thumbButtons.length; i++) {
    		
    		Thumbnail buttonThumb = list.getThumbnail(i);

			int slideIndex = timeline.thumbnailsList.indexOf(buttonThumb);
			
			if (slideIndex >= 0)
			{
				timeline.removeSlide(slideIndex);
				thumbButtons[i].setBorder(BorderFactory.createEmptyBorder());
				thumbButtons[i].setIcon(new ImageIcon(buttonThumb.getImageThumb()));
			}
		}
    }
    
    /**
     * ImageHover() - darkens the image so that it adds a hovered effect
     * @param thumbnail - the thumbnail image that needs to be processed
	 * 
	 * @author Fernando Palacios
     */
    private Image ImageHover(Image thumbnail) {
        Image img = thumbnail;

        BufferedImage buffered = new BufferedImage(img.getWidth(null),
        img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        buffered.getGraphics().drawImage(img, 0, 0, null);

        for (int i = 0; i < buffered.getWidth(); i++) {
            for (int j = 0; j < buffered.getHeight(); j++) {                    
                int rgb = buffered.getRGB(i, j);
                int alpha = (rgb >> 24) & 0x000000FF;
                Color c = new Color(rgb);
                if (alpha != 0) {
                    int red = (c.getRed() - 40) <= 0 ? 0 : c.getRed() - 40;
                    int green = (c.getGreen() - 40) <= 0 ? 0
                        : c.getGreen() - 40;
                    int blue = (c.getBlue() - 40) <= 0 ? 0 : c.getBlue() - 40;
                    c = new Color(red, green, blue);
                    buffered.setRGB(i, j, c.getRGB());
                }
            }
        }
        return buffered;
    }
    
    private Image tint(Image thumbnail, Color color) {
    	
        Image img = thumbnail;

        BufferedImage image = new BufferedImage(img.getWidth(null),
        img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        image.getGraphics().drawImage(img, 0, 0, null);
        
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color pixelColor = new Color(image.getRGB(x, y), true);
                int r = (pixelColor.getRed() + color.getRed()) / 2;
                int g = (pixelColor.getGreen() + color.getGreen()) / 2;
                int b = (pixelColor.getBlue() + color.getBlue()) / 2;
                int a = pixelColor.getAlpha();
                int rgba = (a << 24) | (r << 16) | (g << 8) | b;
                image.setRGB(x, y, rgba);
            }
        }
        return image;
    }
}
