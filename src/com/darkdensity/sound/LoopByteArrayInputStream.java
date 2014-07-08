package com.darkdensity.sound;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 
* @ClassName: LoopByteArrayInputStream
* @Description: The LoopingByteInputStream is a ByteArrayInputStream that
     can loop
* @author Team A1 - Han
* @date Mar 28, 2014 6:37:04 AM
 */
public class LoopByteArrayInputStream extends ByteArrayInputStream {

    private boolean closed;
    
    private boolean limitedTime = false;
    private int repeatTimes = 1;
    private int currentTimes = 0;
    

    /**
      * @Description:  Creates a LoopByteInputStream with a byte array. 
      */
    public LoopByteArrayInputStream(byte[] buffer) {
        super(buffer);
        closed = false;
      
    }
    
    /**
    *@Description:  Creates a LoopByteInputStream with a byte array 
    *	and limited repeat time. 
	 */
	 public LoopByteArrayInputStream(byte[] buffer,int times) {
	     super(buffer);
	     closed = false;
	     setRepeatTimes(times);
	 }


    /**
     * @Description: read bytes from the array,only stop when closed is
     * set to true
     * Returns -1 if the array hasbeen closed.
    */
    public int read(byte[] buffer, int offset, int length) {
        if (closed) {
            return -1;
        }
        if(limitedTime){
        	if(currentTimes == repeatTimes){
        		 return -1;
        	}
        }
        int totalBytesRead = 0;

        while (totalBytesRead < length) {
            int numBytesRead = super.read(buffer,
                offset + totalBytesRead,
                length - totalBytesRead);

            if (numBytesRead > 0) {
                totalBytesRead += numBytesRead;
            }
            else {
                reset();
                currentTimes++;
            }
        }
        return totalBytesRead;
    }


    /**
     * @Description: Closes the stream
     */
    public void close() throws IOException {
        super.close();
        closed = true;
    }
    
    /**
    * @Title: setRepeatTimes 
    * @Description: make the LoopByteInputStream
    *  only loop a certain number of times.
     */
    public void setRepeatTimes(int times){
    	limitedTime = true;
    	repeatTimes = times;
    }

}
