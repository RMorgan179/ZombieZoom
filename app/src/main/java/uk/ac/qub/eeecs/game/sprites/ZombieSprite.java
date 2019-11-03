package uk.ac.qub.eeecs.game.sprites;

import java.util.ArrayList;
import java.util.Random;

import uk.ac.qub.eeecs.game.ZombieZoomGameScreen;
import uk.ac.qub.eeecs.zombiezoom.engine.audio.Sound;
import uk.ac.qub.eeecs.zombiezoom.util.CollisionDetector;
import uk.ac.qub.eeecs.zombiezoom.util.CollisionDetector.CollisionType;
import uk.ac.qub.eeecs.zombiezoom.world.Animation;
import uk.ac.qub.eeecs.zombiezoom.world.GameScreen;
import uk.ac.qub.eeecs.zombiezoom.world.Gender;

public class ZombieSprite extends Animation {
	public enum ZombieState {
		OfScreen, Walk, Attack
	}

	// /////////////////////////////////////////////////////////////////////////
	// Properties
	// /////////////////////////////////////////////////////////////////////////

	public ZombieState state = ZombieState.OfScreen;
	private Gender gender;

	private Sound ZombieGroan;
	private float vomitOrientation;
	private float X_VELOCITY = -50.0f;
	private Random random = new Random();

	/**
	 * Strength of gravity to apply along the y-axis
	 */
	float GRAVITY = ((ZombieZoomGameScreen) mGameScreen).GRAVITY;

	// /////////////////////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a new ZombieSprite
	 * 
	 * @param x
	 *            Centre x location of the zombie
	 * @param y
	 *            Centre y location of the zombie
	 * @param gameScreen
	 *            GameScreen to which this zombie belongs
	 */
	public ZombieSprite(float x, float y, GameScreen gameScreen) {
		super(x, y, 34, 43, 1.0f, 0, 4, gameScreen.getGame().getAssetManager()
				.getBitmap("Zombie"), 7, gameScreen);

		// Apply gravity to the y-axis acceleration
		acceleration.y = GRAVITY;

		play(1.5, true);
		ZombieGroan = mGameScreen.getGame().getAssetManager()
				.getSound("ZombieGroan");

		// 50/50 gender generator
		if (random.nextInt(2) == 0)
			gender = Gender.Male;
		else
			gender = Gender.Female;

		setWalkState();
	}

	// /////////////////////////////////////////////////////////////////////////
	// Methods: General
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Sets the zombie to attack state
	 */
	public void setAttackState() {
		state = ZombieState.Attack;
		velocity.x = 0;
		if (gender == Gender.Male) {
			setCurrentRow(3);
			setFrameCount(29);
			setFrameWidth(50);
			setAnimationPeriod(1.4f);
			// this repositions the animation so there is a more smooth
			// transition to the attack state
			position.x -= 12;
		} else {
			setCurrentRow(1);
			setFrameCount(19);
			setFrameWidth(48);
			setAnimationPeriod(1.0f);
		}
		setsCurrentBitmap();
		// minimum vomitOrientation is 15 and maximum vomitOrientation is 45
		setVomitOrientation(15 + new Random().nextInt(30));
		;
	}

	/**
	 * Sets the vomits orientation to the specified orientation
	 * 
	 * @param vomitOrientation
	 *            Orientation of the vomit
	 */
	public void setVomitOrientation(float vomitOrientation) {
		this.vomitOrientation = vomitOrientation;
	}

	/**
	 * Sets the zombie to a walking state
	 */
	public void setWalkState() {
		state = ZombieState.Walk;
		velocity.x = X_VELOCITY;
		if (gender == Gender.Male) {
			setCurrentRow(2);
			setFrameCount(8);
			setFrameWidth(31);
		} else {
			setCurrentRow(0);
			setFrameCount(7);
			setFrameWidth(34);
		}
		setsCurrentBitmap();
		setAnimationPeriod(1.5f);
	}

	/**
	 * Returns the orientation of the vomit
	 * 
	 * @return vomitOrientation
	 */
	public float getVomitOrientation() {
		return vomitOrientation;
	}

	/**
	 * Returns the gender
	 * 
	 * @return gender
	 */
	public Gender getGender() {
		return gender;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Methods: Collision Detection
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Checks if the zombie collides with the player
	 * 
	 * @param playerSprite
	 *            Player to be tested for collision
	 * @param platforms
	 *            ArrayList of platforms
	 * @return true if there is a collision, otherwise false
	 */
	public boolean checkForAndResolveCollisions(PlayerSprite playerSprite,
			ArrayList<PlatformSprite> platforms) {
		CollisionType collisionType;

		// Checks if the zombie collides with the player and plays a groan if it
		// does
		collisionType = CollisionDetector.determineAndResolveCollision(this,
				playerSprite);

		switch (collisionType) {
		case Top:

		case Bottom:
		case Left:
		case Right:
			if (random.nextFloat() > 0.7) {
				ZombieGroan.play(.25f);
			}
			return true;
		case None:
			break;
		default:
			break;
		}

		// Consider each platform for a collision
		for (PlatformSprite platform : platforms) {
			collisionType = CollisionDetector.determineAndResolveCollision(
					this, platform);

			switch (collisionType) {
			case Top:
				velocity.y = 0.0f;
				break;
			case Bottom:
				velocity.y = 0.0f;
				break;
			case Right:
				velocity.x = 0.0f;
				break;
			case None:
				break;
			default:
				break;
			}
		}
		return false;
	}

	/**
	 * Checks if zombie is hit by a bullet and plays groan if it does
	 * 
	 * @param bullets
	 *            ArrayList of bullets
	 * @return True if there is a collision, otherwise false
	 */
	public boolean checkForAndResolveCollisions(ArrayList<BulletSprite> bullets) {

		CollisionType collisionType;

		for (BulletSprite bullet : bullets) {
			collisionType = CollisionDetector.determineAndResolveCollision(
					this, bullet);

			switch (collisionType) {
			case Top:
			case Bottom:
			case Left:
			case Right:
				if (random.nextFloat() > 0.4) {
					ZombieGroan.play(.25f);
				}
				return true;
			case None:
				break;
			default:
				break;
			}
		}
		return false;
	}
}