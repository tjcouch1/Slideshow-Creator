/**
 * AudioPlayer.java
 * Tracks the audio files in the slideshow
 * 
 * Slideshow Creator
 * Timothy Couch, Joseph Hoang, Fernando Palacios, Austin Vickers
 * CS 499 Senior Design with Dr. Rick Coleman
 * 4/11/19
 */

package core;

import java.util.ArrayList;

public class AudioPlayer implements ThreadOnCompleteListener
{	
	/** List of Audio objects */
    private ArrayList<Audio> audioList; 
    
    /** Index of the currently playing audio clip */
    private int currentIndex = 0;
    
    /** Current length of the audio track list */
    private float playingTime = 0;
    
    /** Should be set to true if the audio list should loop, or false otherwise*/
    public boolean shouldLoop = false;
    
    /** Should be set to false if we are in the creator, that way we don't play audio
     * 	one after another
     */
    private boolean shouldNotify = true;
    
    public AudioPlayer() {
    	audioList = new ArrayList<Audio>();
    	
    	if(SceneHandler.singleton.getAppType() == AppType.CREATOR) {
    		shouldNotify = false;
    	}
    }

    /**
     * @return size of arraylist of Audio objects
     * 
     * @author Joe Hoang
     */
    public int getSize()
    {
        return audioList.size();
    }
    
    /**
     * @return the length(playing seconds) of the tracks together in the list
     * 
     * @author Fernando Palacios
     */
    public float getPlayingLength()
    {
        return playingTime;
    }

    /**
     * @return arraylist of Audio objects 
     * 
     * @author Joe Hoang
     */
    public ArrayList<Audio> getAudioList()
    {
        return audioList;
    }
    
    /**
     *  Prints the names of all the audio files in the list
     * 
     *  @author austinvickers
     */
    public void PrintAll() {
    	for(Audio a : audioList) {
    		System.out.println(a.getAudioName());
    	}
    }

    /**
     * getAudio will return the Audio object at the specified index 
     * 
     * @param AudioIndex specified index to get Audio from
     * 
     * @return Audio object from Audio at given index
     * 
     * @author Joe Hoang
     */
    public Audio getAudio(int audioIndex)
    {
    	try {
    		Audio result = audioList.get(audioIndex);
    		return result;
    		
    	}catch(IndexOutOfBoundsException e) {
    		System.out.println("An audio clip does not exist at that index!");
    		return null;
		}
    }

    /**
     * addAudio will append given Audio object to Audio arrayList
     * 
     * @param Audio Audio object to be added to end of Audio list
     * 
     * @author Joe Hoang
     */
    public void addAudio(Audio audio)
    {
        audioList.add(audio);
        audio.addListener(this);
        playingTime += audio.getAudioLength();
    }
    
    /**
     * addAudio will append given Audio object to Audio arrayList
     * overloaded to allow adding at specified index
     * 
     * @param Audio Audio object to be added to end of Audio list
     * @param index specific to be added to
     * @author Joe Hoang
     */
    public void addAudio(Audio audio, int index)
    {
        audioList.add(index, audio);
        audio.addListener(this);
    }

    /**
     * removeAudio will remove given Audio object from Audio arrayList
     * 
     * @param Audio Audio object to be removed from Audio list
     * 
     * @author Joe Hoang
     */
    public void removeAudio(Audio audio)
    {
    	try
    	{
	    	if(audioList.size() != 0)
	    	{
	    		audioList.remove(audio);
	    	}
    	} catch (ArrayIndexOutOfBoundsException e) 
    	{
    		System.out.println("No audio tracks to remove");
    	}
        playingTime -= audio.getAudioLength();
    }
    
    /**
     * removeAudioAtIndex will remove an Audio object from the list at the given index
     * 
     * @param index
     * @author austinvickers
     */
    
    public void removeAudioAtIndex(int index) {
    	Audio a = audioList.get(index);
    	removeAudio(a);
    }
    

    /**
     * swapAudio will swap value of two objects in ArrayList
     * 
     * @param Audio1 first Audio object index to be swapped 
     * 
     * @param Audio2 second Audio object index to be swapped
     * 
     * @author Joe Hoang
     */
    public void swapAudio(int indexAudio1, int indexAudio2)
    {
        
        Audio tempAudio = audioList.get(indexAudio1);
        audioList.set(indexAudio1, audioList.get(indexAudio2));
        audioList.set(indexAudio2, tempAudio);
    }

    /**
     * swapForward - function will swap Audio object in Audio with the object found at the next index
     * 
     * @param Audio object to be moved forward by one index
     * 
     * @return index of new location of Audio
     * 
     * @author Joe Hoang
     */
    public int swapForward(Audio audio)
    {
        int indexAudio1 = audioList.indexOf(audio);
        int indexAudio2 = indexAudio1 + 1;
        if(indexAudio2 != audioList.size())
        {
            swapAudio(indexAudio1, indexAudio2);
            return indexAudio2;
        }
        else
        {
            return -1; 
        }
    }

     /**
     * swapBackground - function will swap Audio object in Audio with the object found at the previous index
     * 
     * @param Audio object to be moved backward by one index
     * 
     * @return index of new location of Audio
     * 
     * @author Joe Hoang
     */
    public int swapBackward(Audio audio)
    {
        int indexAudio1 = audioList.indexOf(audio);
        int indexAudio2 = indexAudio1 - 1;
        if(indexAudio2 != -1)
        {
            swapAudio(indexAudio1, indexAudio2);
            return indexAudio2;
        }
        else
        {
            return -1; 
        }
    }

    /**
     * gets index of supplied Audio or -1 if not found
     * @param Audio Audio to search for
     * 
     * @return index of supplied Audio or -1 if not found
     */
    public int indexOf(Audio audio)
    {
        return audioList.indexOf(audio);
    }
    
    //This method should be called when previewing an individual clip in the Creator
    /** Plays an audio clip at a specific index */
    public void playAudioClipAtIndex(int index) {

    	if(getAudio(index) != null) {
        	getAudio(index).Play();	
    	}
    	
    }

    /* This method should be called by the Viewer - if you spam this method,
    *  you will notice that it can lead to multiple clips playing at once on certain edge 
    *  cases. This would be a problem if this was attached to a button, but since it
    *  will only be called by the viewer, lets just make sure its called automatically
    */
    /** Plays all the audio clips in the list sequentially
     * @author austinvickers
     */
    public void playAudioClipsSequentially() {
    	//First reset the counter
    	currentIndex = 0;
    	
    	//Play the first clip
    	playAudioClipAtIndex(currentIndex);
    	
    	//NotifyOfThreadComplete will get notified when its time to play the next clip
    }
    
    /**
     *  Plays all the audio clips in the list sequentially,
     *  and also sets the should loop parameter on the class
     *  
     *  @param shouldLoop
     *  @author austinvickers
     */
    public void playAudioClipsSequentially(boolean shouldLoop) {
    	//First reset the counter
    	currentIndex = 0;
    	
    	//Set the loop parameter that notifyOfThreadComplete will use
    	this.shouldLoop = shouldLoop;
    	
    	//Play the first clip
    	playAudioClipAtIndex(currentIndex);
    	
    	//NotifyOfThreadComplete will get notified when its time to play the next clip
    }
    
    /**
     * Pauses the audio at the current index - the one that should be playing
     * 
     * @author austinvickers
     */
    public void pauseCurrentAudio() {
    	
    	if(currentIndex >= 0 && currentIndex < audioList.size() && audioList.get(currentIndex) != null) {
    		audioList.get(currentIndex).pausePlaying();
    	}
    }
    
    public void resumeCurrentAudio() {
    	 
    	if(currentIndex >= 0 && currentIndex < audioList.size() && audioList.get(currentIndex) != null) {
    		audioList.get(currentIndex).resumePlaying();
    	}
    }
    
    /**
     *  Stops all audio clips that are playing at once
     *  
     *  @author austinvickers
     */
    public void FullStop() {
    	for(Audio a : audioList) {
    		if(a != null)
    			a.stopPlaying();
    	}
    }

    /** This method is called whenever an audio thread finishes 
     *  @author austinvickers
     */
	@Override
	public void notifyOfThreadComplete(NotifyingThread thread) {
		
		if(!shouldNotify) {
			return;
		}
		
		System.out.println("Audio player was notified");
		
		if(currentIndex + 1 < audioList.size()) {
			currentIndex++;
			playAudioClipAtIndex(currentIndex);
		}
		else {
			if(shouldLoop) {
				currentIndex = 0;
				playAudioClipAtIndex(currentIndex);
			}
		}
		
		//Otherwise, stop playing clips
	}
	
}

