package uk.ac.qub.eeecs.zombiezoom.engine.audio;

import android.media.SoundPool;

/**
 * Sound effect
 * 
 * @version 1.0
 */
public class Sound {

	// /////////////////////////////////////////////////////////////////////////
	// Properties
	// /////////////////////////////////////////////////////////////////////////
	/**
	 * Flag indicating if music is allowed to be played
	 */
	private boolean isAllowedPlay = true;
	
	/**
	 * Maximum number of sounds that can be played concurrently
	 */
	public static final int MAX_CONCURRENT_SOUNDS = 20;

	/**
	 * Sound Id of this effect
	 */
	private int mSoundId;

	/**
	 * Sound pool containing this sound effect
	 */
	private SoundPool mSoundPool;

	/**
	 * Play back volume of this sound effect
	 */
	private float mVolume;

	// /////////////////////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Create a new sound effect
	 * 
	 * @param soundPool
	 *            Sound pool to which this effect belongs
	 * @param soundId
	 *            Id of this effect within the sound pool
	 */
	public Sound(SoundPool soundPool, int soundId) {
		// Store the parameters and assume a default playback volume
		mSoundId = soundId;
		mSoundPool = soundPool;
		mVolume = 1.0f;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Methods
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Play the sound effect
	 */
	public void play() {
		if (isAllowedPlay == true) {
			mSoundPool.play(mSoundId, mVolume, mVolume, 0, 0, 1);
		}
	}

	/**
	 * Play the sound effect
	 * 
	 * @param volume
	 *            Play back volume (0-1)
	 */
	public void play(float volume) {
		if (isAllowedPlay == true) {
			mSoundPool.play(mSoundId, volume, volume, 0, 0, 1);
		}
	}

	/**
	 * Play the sound effect
	 * 
	 * @param leftVolume
	 *            Left channel play back volume (0-1)
	 * @param rightVolume
	 *            Right channel play back volume (0-1)
	 */
	public void play(float leftVolume, float rightVolume) {
		if (isAllowedPlay == true) {
			mSoundPool.play(mSoundId, leftVolume, rightVolume, 0, 0, 1);
		}
	}

	/**
	 * Set the default play back volume
	 * 
	 * @param volume
	 */
	public void setVolume(float volume) {
		mVolume = volume;
	}

	/**
	 * Dispose of the sound effect
	 */
	public void dispose() {
		mSoundPool.unload(mSoundId);
	}
	

	/**
	 * Sets the flag allowing the music to play
	 * 
	 * @param value
	 *            Boolean value whether the sound is allowed to play or not
	 */
	public void setAllowedToPlay(boolean value) {
		isAllowedPlay = value;
	}
}