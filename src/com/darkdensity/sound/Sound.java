package com.darkdensity.sound;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.darkdensity.setting.Config;

/**
 * The Sound class use for contain a sound's sample byte array
 */
public class Sound {

	// the sound' sample byte array
	private AudioFormat format;
	private byte[] samples;

	// a pointer points to the sample
	public Sound(String fileName) {

		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(new File(fileName));
			this.format = audioInputStream.getFormat();
			this.samples = getSamples(audioInputStream);

		} catch (UnsupportedAudioFileException e) {
			if (Config.DEBUGMODE) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			if (Config.DEBUGMODE) {
				e.printStackTrace();
			}
		}
	}

	// get the sound sample
	public byte[] getSamples() {
		return samples;
	}

	// get the sound format
	public AudioFormat getFormat() {
		return format;
	}

	/**
	 * Gets the samples from an AudioInputStream as an array of bytes.
	 */
	private byte[] getSamples(AudioInputStream audioStream) {
		// get the number of bytes to read
		int length = (int) (audioStream.getFrameLength() * format
				.getFrameSize());

		// read the entire stream
		byte[] samples = new byte[length];
		DataInputStream is = new DataInputStream(audioStream);

		try {
			is.readFully(samples);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// return the samples
		return samples;
	}

}
