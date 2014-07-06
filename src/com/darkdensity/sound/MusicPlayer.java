package com.darkdensity.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.darkdensity.setting.Config;

public class MusicPlayer extends Thread {

	public static int STATUS;

	public static final int PAUSED = 1;
	public static final int STOPED = 2;
	public static final int PLAYING = 3;

	/*
	 * Class: MusicPlayer extends Thread
	 * ****************************************** Attributes: private String
	 * filename; private final int EXTERNAL_BUFFER_SIZE = 524288;
	 * ****************************************** Methods: public void run() {}
	 */

	private String filename;

	private static boolean isLoop = false;

	private final int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb

	public MusicPlayer(String wavfile) {
		STATUS = PLAYING;
		// init a MusicPlayer with a wav file
		filename = wavfile;
	}

	public MusicPlayer(String wavfile, boolean isLoop) {
		STATUS = PLAYING;
		// init a MusicPlayer with a wav file
		filename = wavfile;
		this.isLoop = isLoop;
	}

	public void setMusicStop() {
		STATUS = STOPED;
	}

	public void setMusicPause() {
		STATUS = PAUSED;
	}

	public void setMusicPlay() {
		STATUS = PLAYING;
	}

	public void run() {
		// Get the target file
		File soundFile = new File(filename);
		if (!soundFile.exists()) {
			// Show error message if file not found
			System.err.println("Wave file not found: " + filename);
			return;
		}
		// Init a AudioInputStream for reading music file
		AudioInputStream audioInputStream = null;
		try {

			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		} catch (Throwable t) {
			// Do nothing when error
		}

		// Identify the music file format
		AudioFormat format = audioInputStream.getFormat();
		SourceDataLine auline = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

		try {
			// Try to get music file content
			auline = (SourceDataLine) AudioSystem.getLine(info);
			auline.open(format);
		} catch (LineUnavailableException e) {
			if (Config.DEBUGMODE) {
				e.printStackTrace();
			}
			return;
		} catch (Exception e) {
			if (Config.DEBUGMODE) {
				e.printStackTrace();
			}
			return;
		}

		auline.start();

		int nBytesRead = 0;
		byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];

		try {
			while (nBytesRead != -1 && STATUS != STOPED) {
				// Play music
				if (STATUS == PLAYING) {
					nBytesRead = audioInputStream
							.read(abData, 0, abData.length);
					if (nBytesRead >= 0)
						auline.write(abData, 0, nBytesRead);
				}
			}
		} catch (IOException e) {
			if (Config.DEBUGMODE) {
				e.printStackTrace();
			}
			return;
		} finally {
			// Close io
			auline.drain();
			auline.close();
		}

	}
}
