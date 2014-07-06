package com.darkdensity.sound;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
    The LoopingByteInputStream is a ByteArrayInputStream that
    loops indefinitly. The looping stops when the close() method
    is called.
    <p>Possible ideas to extend this class:<ul>
    <li>Add an option to only loop a certain number of times.
    </ul>
*/
public class LoopByteArrayInputStream extends ByteArrayInputStream {

    private boolean closed;
    
    private boolean limitedTime = false;
    private int repeatTimes = 1;
    private int currentTimes = 0;
    

    /**
       @Description:  Creates a LoopByteInputStream with a byte array. 
    */
    public LoopByteArrayInputStream(byte[] buffer) {
        super(buffer);
        closed = false;
      
    }
    
    /**
    	@Description:  Creates a LoopByteInputStream with a byte array 
    	and limited repeat time. 
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
        @Description: Closes the stream
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
