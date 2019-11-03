package uk.ac.qub.eeecs.game.sprites;

import java.util.ArrayList;

import uk.ac.qub.eeecs.game.ZombieZoomGameScreen;
import uk.ac.qub.eeecs.zombiezoom.util.CollisionDetector;
import uk.ac.qub.eeecs.zombiezoom.util.CollisionDetector.CollisionType;
import uk.ac.qub.eeecs.zombiezoom.world.GameScreen;
import uk.ac.qub.eeecs.zombiezoom.world.Sprite;

/**
 * Define a basic sprite
 * 
 * @version 1.0
 */
public class BulletSprite extends Sprite {

	public enum BulletType {
		Gun, Vomit
	}

	BulletType bulletType;
	float speed;

	/**
	 * Strength of gravity to apply along the y-axis
	 */
	float GRAVITY = ((ZombieZoomGameScreen) mGameScreen).GRAVITY;

	// /////////////////////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a new bullet sprite
	 * 
	 * @param x
	 *            Centre x location of the bullet
	 * @param y
	 *            Centre y location of the bullet
	 * @param orientation
	 *            Orientation of the bullet
	 * @param initialXVelocity
	 *            Initial velocity along the x axis
	 * @param bulletType
	 *            Whether the bullet is a Gun bullet or a Vomit bullet
	 * @param gameScreen
	 *            GameScreen to which this bullet belongs
	 */
	public BulletSprite(float x, float y, float orientation,
			float initialXVelocity, BulletType bulletType, GameScreen gameScreen) {
		super(x, y, gameScreen);
		this.bulletType = bulletType;
		this.orientation = orientation;

		switch (bulletType) {
		case Gun:
			setBitmapAndBoundingBox(x, y, gameScreen.getGame()
					.getAssetManager().getBitmap("Bullet"));
			speed = 500;
			break;
		case Vomit:
			setBitmapAndBoundingBox(x, y, gameScreen.getGame()
					.getAssetManager().getBitmap("VomitBullet"));
			speed = -350;
			acceleration.y = GRAVITY;
			break;
		}
		// places rotation point in the centre
		setRotationPosition();

		// using orientation and speed to give a direction to the bullet
		velocity.x = initialXVelocity
				+ (speed * (float) Math.cos(Math.toRadians(orientation)));
		velocity.y = -speed * (float) Math.sin(Math.toRadians(orientation));
	}

	// /////////////////////////////////////////////////////////////////////////
	// Methods: Collision Detection
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Check for and then resolve any collision between the bullet and the
	 * platforms.
	 * 
	 * @param platforms
	 *            Array of platforms to test for collision against
	 * @return true if there is a collision, otherwise false
	 * 
	 */
	public boolean checkForAndResolveCollisions(
			ArrayList<PlatformSprite> platforms) {

		CollisionType collisionType;

		// Consider each platform for a collision
		for (PlatformSprite platform : platforms) {
			collisionType = CollisionDetector.determineAndResolveCollision(
					this, platform);

			switch (collisionType) {
			case Top:
			case Bottom:
			case Left:
			case Right:
				return true;
			case None:
				break;
			default:
				break;
			}
		}
		return false;
	}

	/**
	 * Check for and then resolve any collision between the bullet and the
	 * player
	 * 
	 * @param playerSprite
	 *            Player to test for collision against
	 * @return true if there is a collision, otherwise false
	 */
	public boolean checkForAndResolveCollisions(PlayerSprite playerSprite) {
		CollisionType collisionType;

		// Player get hit by bat
		collisionType = CollisionDetector.determineAndResolveCollision(this,
				playerSprite);

		switch (collisionType) {
		case Top:
		case Bottom:
		case Left:
		case Right:
			return true;
		case None:
			break;
		default:
			break;
		}

		return false;
	}

	/**
	 * Check for and then resolve any collision between the bullets and bullets
	 * 
	 * @param bullets
	 *            Array of bullets to test for collision against bats
	 * @return true if there is a collision, otherwise false
	 */
	public boolean checkForAndResolveCollisionsWithAnotherBullet(
			ArrayList<BulletSprite> bullets) {
		CollisionType collisionType;

		// Checks if the bullet is hit by another bullet
		for (BulletSprite bullet : bullets) {
			collisionType = CollisionDetector.determineAndResolveCollision(
					this, bullet);

			switch (collisionType) {
			case Top:
			case Bottom:
			case Left:
			case Right:
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