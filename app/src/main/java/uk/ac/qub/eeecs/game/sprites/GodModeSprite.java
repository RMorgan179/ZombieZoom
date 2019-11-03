package uk.ac.qub.eeecs.game.sprites;

import java.util.ArrayList;

import uk.ac.qub.eeecs.game.ZombieZoomGame;
import uk.ac.qub.eeecs.game.ZombieZoomGameScreen;
import uk.ac.qub.eeecs.zombiezoom.util.CollisionDetector;
import uk.ac.qub.eeecs.zombiezoom.util.CollisionDetector.CollisionType;
import uk.ac.qub.eeecs.zombiezoom.world.Animation;
import uk.ac.qub.eeecs.zombiezoom.world.GameScreen;

/**
 * Define a basic sprite
 * 
 * @version 1.0
 */
public class GodModeSprite extends Animation {

	private float GRAVITY = ((ZombieZoomGameScreen) mGameScreen).GRAVITY;

	// /////////////////////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a pickup sprite
	 * 
	 * @param x
	 *            Centre x location of the pickup
	 * @param y
	 *            Centre y location of the pickup
	 * @param gameScreen
	 *            GameScreen to which this pickup belongs
	 */
	public GodModeSprite(float x, float y, GameScreen gameScreen) {
		super(x, y, .1f, gameScreen.getGame().getAssetManager()
				.getBitmap("GodModeOn"), 6, gameScreen);

		play(1.25, true);

	}

	// /////////////////////////////////////////////////////////////////////////
	// Methods: Collision Detection
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Check for and then resolve any collision between the pickup and the
	 * player, bullets and platforms.
	 * 
	 * @param playerSprite
	 *            playerSprite tests collision against coin
	 * @param bullets
	 *            Array of bullets to test for collision against coin
	 * @param platforms
	 *            Array of platforms to test for collision against coin
	 * @return true if there is a collision, otherwise false
	 */
	public boolean checkForAndResolveCollisions(PlayerSprite playerSprite,
			ArrayList<BulletSprite> bullets, ArrayList<PlatformSprite> platforms) {

		CollisionType collisionType;

		// Player picks up pickup
		collisionType = CollisionDetector.determineAndResolveCollision(this,
				playerSprite);

		switch (collisionType) {
		case Top:
		case Bottom:
		case Left:
		case Right:
			((ZombieZoomGame) mGameScreen.getGame()).mGodModeEnabled = true;
			playerSprite.health = 15;
			return true;
		case None:
			break;
		default:
			break;
		}

		// Checks if pickup is hit by a bullet and applies gravity to the pickup
		// if
		// it does
		for (BulletSprite bullet : bullets) {
			collisionType = CollisionDetector.determineAndResolveCollision(
					this, bullet);

			switch (collisionType) {
			case Top:
			case Bottom:
			case Left:
			case Right:
				acceleration.y = GRAVITY;
			case None:
				break;
			default:
				break;
			}
		}

		// Ensures pickup does not fall through any platforms when the pickup is
		// falling
		if (acceleration.y == GRAVITY) {
			for (PlatformSprite platform : platforms) {
				collisionType = CollisionDetector.determineAndResolveCollision(
						this, platform);

				switch (collisionType) {
				case Top:
					velocity.y = 0.0f;
					break;
				default:
					break;
				}
			}
		}

		return false;
	}
}