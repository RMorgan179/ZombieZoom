package uk.ac.qub.eeecs.game.sprites;

import uk.ac.qub.eeecs.zombiezoom.world.GameScreen;
import uk.ac.qub.eeecs.zombiezoom.world.Sprite;

/**
 * Define a basic sprite
 * 
 * @version 1.0
 */
public class PlatformSprite extends Sprite {
	private Boolean fallable;
	private float fallingVelocity;

	// /////////////////////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Create a default PlatformSprite which is not fallable
	 * 
	 * @param gameScreen
	 */
	public PlatformSprite(GameScreen gameScreen) {
		super(gameScreen);
		this.fallable = false;
		mBitmap = gameScreen.getGame().getAssetManager().getBitmap("Platform");
		mBound.halfWidth = 56.0f;
		mBound.halfHeight = 8.5f;
	}

	/**
	 * Create a new single impact sprite
	 * 
	 * @param gameScreen
	 * @param fallable
	 */
	public PlatformSprite(GameScreen gameScreen, Boolean fallable) {
		super(gameScreen);
		setFallable(fallable);
		mBound.halfWidth = 56.0f;
		mBound.halfHeight = 8.5f;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Methods: General
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Gets the flag determining whether a platform is fallable
	 * 
	 * @return true if platform is fallable, otherwise false
	 */
	public Boolean isFallable() {
		return fallable;
	}

	/**
	 * Sets the flag determining whether a platform is fallable
	 * 
	 * @param fallable
	 *            Determines whether a platform is fallable
	 */
	public void setFallable(Boolean fallable) {
		setFallable(fallable, -100.0f);
	}

	/**
	 * Sets the flag determining whether a platform is fallable
	 * 
	 * @param fallable
	 *            Determines whether a platform is fallable
	 * @param fallingVelocity
	 *            Velocity which the platform falls if true
	 */
	public void setFallable(Boolean fallable, float fallingVelocity) {
		this.fallable = fallable;
		this.setFallingVelocity(fallingVelocity);
		if (fallable) {
			mBitmap = mGameScreen.getGame().getAssetManager()
					.getBitmap("Fallable Platform");
		} else {
			mBitmap = mGameScreen.getGame().getAssetManager()
					.getBitmap("Platform");
		}
	}

	/**
	 * Gets the velocity which the platform falls
	 * 
	 * @return Velocity which the platform falls
	 */
	public float getFallingVelocity() {
		return fallingVelocity;
	}

	/**
	 * Sets the velocity which the platform falls
	 * 
	 * @param fallingVelocity
	 *            New velocity which the platform falls
	 */
	public void setFallingVelocity(float fallingVelocity) {
		this.fallingVelocity = fallingVelocity;
	}
}