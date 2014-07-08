package com.darkdensity.net.chat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import com.darkdensity.setting.Config;
/** 
* @author Team A1 - Hei Yin Wong
*/
public abstract class VoiceChatManager {

	private AudioFormat audioFormat;
	private boolean isCapture;
	private ByteArrayOutputStream out;

	public VoiceChatManager() {
		//define the voice format
		audioFormat = getFormat();
		isCapture = true;
	}

	//define the voice format
	private AudioFormat getFormat() {
		float sampleRate = 8000;
		int sampleSizeInBits = 8;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = true;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);
	}

	public void captureAudio() {
		//Capture audio and convert it into byte []
		try {
			DataLine.Info info = new DataLine.Info(TargetDataLine.class,
					audioFormat);
			final TargetDataLine line = (TargetDataLine) AudioSystem
					.getLine(info);
			line.open(audioFormat);
			line.start();
			Runnable runner = new Runnable() {
				int bufferSize = (int) audioFormat.getSampleRate()
						* audioFormat.getFrameSize();
				byte buffer[] = new byte[bufferSize];

				public void run() {

					out = new ByteArrayOutputStream();
					isCapture = true;
					try {
						while (isCapture) {
							System.out.println("CaptureAudio is running");
							int count = line.read(buffer, 0, buffer.length);
							if (count > 0) {
								out.write(buffer, 0, count);
							}
						}
						out.close();
					} catch (IOException e) {
						System.err.println("I/O problems: " + e);
						System.exit(-1);
					}
				}
			};
			Thread captureThread = new Thread(runner);
			captureThread.start();
		} catch (LineUnavailableException e) {
			if (Config.DEBUGMODE) {
				e.printStackTrace();
			}
		}
	}

	public void stopCapture() {
		//stop the vocie capturing thread
		this.isCapture = false;
	}

	public void playAudio(byte[] audio) {
		//Used to play the received voice packet
		try {
			InputStream input = new ByteArrayInputStream(audio);
			final AudioFormat format = getFormat();
			final AudioInputStream ais = new AudioInputStream(input, format,
					audio.length / format.getFrameSize());
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			final SourceDataLine line = (SourceDataLine) AudioSystem
					.getLine(info);
			line.open(format);
			line.start();

			Runnable runner = new Runnable() {
				int bufferSize = (int) format.getSampleRate()
						* format.getFrameSize();
				byte buffer[] = new byte[bufferSize];

				public void run() {
					try {
						int count;
						while ((count = ais.read(buffer, 0, buffer.length)) != -1) {
							if (count > 0) {
								line.write(buffer, 0, count);
							}
						}
						line.drain();
						line.close();
					} catch (IOException e) {
						System.err.println("I/O problems: " + e);
						System.exit(-3);
					}
				}
			};
			Thread playThread = new Thread(runner);
			playThread.start();
		} catch (LineUnavailableException e) {
			System.err.println("Line unavailable: " + e);
			System.exit(-4);
		}

	}

	public byte[] getCapturedAudio() {
		return out.toByteArray();
	}

	public abstract void sendVoiceMessage();

	public abstract void stopReceiveMessage();

}
