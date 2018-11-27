package Audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.sampled.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import sun.audio.AudioStream;

public class AudioPlayer {
	
	private Clip clip;
	
	public AudioPlayer(final String s) {
		
		/*
		Media hit = new Media(new File(s).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(hit);
		mediaPlayer.play();
		//*/
		
		
		/*
		try {
			
			AudioInputStream ais =
				AudioSystem.getAudioInputStream(
					getClass().getResourceAsStream(
						s
					)
				);
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED,
				baseFormat.getSampleRate(),
				16,
				baseFormat.getChannels(),
				baseFormat.getChannels() * 2,
				baseFormat.getSampleRate(),
				false
			);
			AudioInputStream dais =
				AudioSystem.getAudioInputStream(
					decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		//*/
	}
	
	
	
	public void doPlay(final String url) {
	    try {
	        stopPlay();
	        AudioInputStream inputStream = AudioSystem
	                .getAudioInputStream(getClass().getResourceAsStream(url));
	        clip = AudioSystem.getClip();
	        clip.open(inputStream);
	        clip.start();
	    } catch (Exception e) {
	        stopPlay();
	        System.err.println(e.getMessage());
	    }
	}
	
	public void stopPlay() {
	    if (clip != null) {
	        clip.stop();
	        clip.close();
	        clip = null;
	    }
	}
	
	public void play() {
		if(clip == null) return;
		stop();
		clip.setFramePosition(0);
		clip.start();
	}
	
	public void stop() {
		if(clip.isRunning()) clip.stop();
	}
	
	public void close() {
		stop();
		clip.close();
	}
	
}














