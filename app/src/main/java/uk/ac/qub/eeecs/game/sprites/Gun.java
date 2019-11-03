package uk.ac.qub.eeecs.game.sprites;

import java.util.ArrayList;

import uk.ac.qub.eeecs.game.ZombieZoomGame;
import uk.ac.qub.eeecs.game.sprites.BulletSprite.BulletType;
import uk.ac.qub.eeecs.zombiezoom.engine.audio.Sound;
import uk.ac.qub.eeecs.zombiezoom.world.Animation;
import uk.ac.qub.eeecs.zombiezoom.world.GameScreen;

public class Gun extends Animation {
	public enum GunType {
		Pistol, Shotgun, Knife
	};

	public final float BULLET_SPREAD = 1.5f;

	private GunType gunType;

	private Sound gunSound;
	private float gunSoundVolume, animationPeroid;

	private ArrayList<BulletSprite> mBullets = new ArrayList<BulletSprite>();

	// /////////////////////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a gun Default gun is a pistol
	 * 
	 * @param x
	 *            Centre x location of the gun
	 * @param y
	 *            Centre y location of the gun
	 * @param playerScale
	 *            Scale of the player
	 * @param gameScreen
	 *            GameScreen to which this gun belongs
	 */
	public Gun(float x, float y, float playerScale, GameScreen gameScreen) {
		super(x, y, 61, 26, playerScale, 3, gameScreen.getGame()
				.getAssetManager().getBitmap("Gun"), 12, 14, gameScreen);
		setGun(GunType.Pistol);
	}

	/**
	 * Creates a gun
	 * 
	 * @param mGunType
	 *            Type of gun
	 * @param x
	 *            Centre x location of the gun
	 * @param y
	 *            Centre y location of the gun
	 * @param playerScale
	 *            Scale of the player
	 * @param gameScreen
	 *            GameScreen to which this gun belongs
	 */
	public Gun(GunType mGunType, float x, float y, float playerScale,
			GameScreen gameScreen) {
		super(x, y, 61, 26, playerScale, 3, gameScreen.getGame()
				.getAssetManager().getBitmap("Gun"), 12, 14, gameScreen);
		setGun(mGunType);
	}

	// /////////////////////////////////////////////////////////////////////////
	// Methods: General
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Fires the gun
	 * 
	 * @param x
	 *            Centre x location of the bullet
	 * @param y
	 *            Centre y location of the bullet
	 * @param playerXvelocity
	 *            Initial velocity of the player along the x axis
	 * @param gameScreen
	 *            GameScreen to which this gun belongs
	 */
	public void fireGun(float x, float y, float playerXvelocity,
			GameScreen gameScreen) {
		switch (gunType) {
		case Shotgun:
			mBullets.add(new BulletSprite(x, y, orientation
					+ (BULLET_SPREAD * 2), playerXvelocity, BulletType.Gun,
					gameScreen));
			mBullets.add(new BulletSprite(x, y, orientation
					- (BULLET_SPREAD * 2), playerXvelocity, BulletType.Gun,
					gameScreen));
		case Pistol:
			mBullets.add(new BulletSprite(x, y, orientation, playerXvelocity,
					BulletType.Gun, gameScreen));
			break;
		case Knife:

			break;
		default:
			break;
		}

		if (((ZombieZoomGame) mGameScreen.getGame()).mBulletModeEnabled) {
			mBullets.add(new BulletSprite(x, y, orientation + BULLET_SPREAD,
					playerXvelocity, BulletType.Gun, gameScreen));
			mBullets.add(new BulletSprite(x, y, orientation - BULLET_SPREAD,
					playerXvelocity, BulletType.Gun, gameScreen));
		}

		play(animationPeroid, false);
		gunSound.play(gunSoundVolume);
	}

	/**
	 * Sets the gun type
	 * 
	 * @param gunType
	 *            New gun type
	 */
	public void setGun(GunType gunType) {
		this.gunType = gunType;
		if (gunType == GunType.Shotgun) {
			setCurrentRow(0);
			setFrameCount(13);
			animationPeroid = .5f;
			gunSoundVolume = 1.25f;
			gunSound = mGameScreen.getGame().getAssetManager()
					.getSound("Shotgun");
			// the user is notified they have acquired a shotgun
			mGameScreen.getGame().getAssetManager().getSound("Acquire Shotgun")
					.play(1.5f);
		} else if (gunType == GunType.Pistol) {
			setCurrentRow(1);
			setFrameCount(9);
			animationPeroid = .35f;
			gunSoundVolume = 2.00f;
			gunSound = mGameScreen.getGame().getAssetManager()
					.getSound("Pistol");
		} else if (gunType == GunType.Knife) {
			setCurrentRow(2);
			setFrameCount(5);
			animationPeroid = 1.35f;
			gunSoundVolume = 1.5f;
			// gunSound =
			// mGameScreen.getGame().getAssetManager().getSound("Knife Slash");
		}
		// ensures the new gun bitmap is drawn
		setsCurrentBitmap();
	}

	/**
	 * Resets the orientation of the gun to 0 after a gun is fired
	 */
	public void reOrientateGun() {
		if (orientation != 0 && !isPlaying()) {
			if (orientation > 0)
				orientation--;
			else
				orientation++;
		}
	}

	/***
	 * Gets the gun type
	 * 
	 * @return The gun type
	 */
	public GunType getGunType() {
		return gunType;
	}

	/**
	 * Gets the array list of bullets
	 * 
	 * @return Array List of bullets
	 */
	public ArrayList<BulletSprite> getmBullets() {
		return mBullets;
	}
}