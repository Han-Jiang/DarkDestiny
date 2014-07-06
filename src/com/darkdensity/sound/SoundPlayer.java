
package com.darkdensity.sound;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


public class SoundPlayer implements Runnable
{

	private AudioFormat format;
    private ExecutorService threadPool;
    
    private InputStream inputStream; 
	
    public SoundPlayer(Sound sound,ExecutorService threadPool){
    	
    	format = sound.getFormat();
    	this.threadPool = threadPool;
    	this.inputStream = new ByteArrayInputStream(sound.getSamples());
    }
    
    public SoundPlayer(Sound sound,ExecutorService threadPool,boolean isRepeat){
    	
    	format = sound.getFormat();
    	this.threadPool = threadPool;
    	if(isRepeat){
    		this.inputStream = new LoopByteArrayInputStream(sound.getSamples());
    	}else{
    		this.inputStream = new ByteArrayInputStream(sound.getSamples());
    	}
    	
    }
    
    public void play(){
    	
    	threadPool.execute(this);
    }
    

	@Override
	public void run(){
		
		// use a short, 100ms (1/10th sec) buffer for real-time
        // change to the sound stream
        int bufferSize = format.getFrameSize() *
            Math.round( format.getSampleRate() / 10);
        byte[] buffer = new byte[bufferSize];

        // create a line to play to
        SourceDataLine line;
        try {
            DataLine.Info info =
                new DataLine.Info(SourceDataLine.class, format);
            line = (SourceDataLine)AudioSystem.getLine(info);
            line.open( format, bufferSize);
        }
        catch (LineUnavailableException ex) {
            ex.printStackTrace();
            return;
        }

        // start the line
        line.start();

        // copy data to the line
        try {
            int numBytesRead = 0;
            while (numBytesRead != -1) {
                numBytesRead =
                    inputStream.read(buffer, 0, buffer.length);
                if (numBytesRead != -1) {
                   line.write(buffer, 0, numBytesRead);
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        // wait until all data is played, then close the line
        line.drain();
        line.close();
	}
	

}
