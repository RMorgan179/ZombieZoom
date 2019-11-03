package uk.ac.qub.eeecs.game;

import uk.ac.qub.eeecs.zombiezoom.Game;
import uk.ac.qub.eeecs.zombiezoom.engine.audio.Music;
import android.os.Bundle;

/**
 * ZombieZoom game that is create within the MainActivity class
 * 
 * @version 1.0
 */
public class ZombieZoomGame extends Game {

	/**
	 * Music Control
	 */
	protected Music mMusic;

	/**
	 * Get the game's music control
	 * 
	 * @return Music service
	 */
	public Music getMusic() {
		return mMusic;
	}

	
	public Boolean mGodModeEnabled = false;
	public Boolean mBulletModeEnabled = false;
	public Boolean mCoinHoarderModeEnabled = false;

	private float aspectRatio = (float) getScreenWidth()
			/ (float) getScreenHeight();

	public float getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	/**
	 * Create a new ZombieZoom game
	 */
	public ZombieZoomGame() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.qub.eeecs.gage.Game#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Go with a default 60 UPS/FPS
		setTargetFramesPerSecond(60);
		
		mAssetManager.loadAndAddMusic("MainTheme", "music/MainTheme.mp3");
		
		// Load in the sound assets used by this layer
		mAssetManager.loadAndAddSound("Pistol", "sfx/Pistol.mp3");
		mAssetManager.loadAndAddSound("Shotgun", "sfx/Shotgun.mp3");
		mAssetManager.loadAndAddSound("Acquire Shotgun",
				"sfx/AcquireShotgun.mp3");
		mAssetManager.loadAndAddSound("CoinSFX", "sfx/CoinSFX.mp3");
		mAssetManager.loadAndAddSound("HealthSFX", "sfx/HealthPickUpSFX.mp3");
		mAssetManager.loadAndAddSound("ZombieGroan", "sfx/ZombieGroan.mp3");
		mAssetManager.loadAndAddSound("HealthSFX", "sfx/HealthPickUpSFX.mp3");
		mAssetManager.loadAndAddSound("BombSFX", "sfx/BombSFX.mp3");
		
		mMusic = mAssetManager.getMusic("MainTheme");
		mMusic.play();
		mMusic.setLooping(true);

		// Create and add a Zombie Zoom game screen to the screen manager
		ZombieZoomMenuScreen zombieZoomMenuScreen = new ZombieZoomMenuScreen(
				this);
		mScreenManager.addScreen(zombieZoomMenuScreen);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mScreenManager.getCurrentScreen().resume();
	}
	
	@Override
	public void onPause() {
		mMusic.pause();
		mScreenManager.getCurrentScreen().pause();
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		mMusic.dispose();
		super.onDestroy();
	}
}
