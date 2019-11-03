package uk.ac.qub.eeecs.game.sprites;

import java.util.ArrayList;

import uk.ac.qub.eeecs.zombiezoom.engine.audio.Sound;
import uk.ac.qub.eeecs.zombiezoom.util.CollisionDetector;
import uk.ac.qub.eeecs.zombiezoom.util.CollisionDetector.CollisionType;
import uk.ac.qub.eeecs.zombiezoom.world.Animation;
import uk.ac.qub.eeecs.zombiezoom.world.GameScreen;

public class BombSprite extends Animation {
	private Sound bombSound;

	// /////////////////////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a new bomb sprite
	 * 
	 * @param x
	 *            Centre x location of the bomb
	 * @param y
	 *            Centre y location of the bomb
	 * @param gameScreen
	 *            GameScreen to which the bomb belongs
	 */
	public BombSprite(float x, float y, GameScreen gameScreen) {
		super(x, y, .3f, gameScreen.getGame().getAssetManager()
				.getBitmap("Bomb"), 24, gameScreen);
		play(2.0f, true);
		gameScreen.getGame().getAssetManager()
				.loadAndAddSound("BombSFX", "sfx/BombSFX.mp3");
		bombSound = gameScreen.getGame().getAssetManager().getSound("BombSFX");
	}

	/**
	 * Creates a new bomb sprite
	 * 
	 * @param x
	 *            Centre x location of the bomb
	 * @param y
	 *            Centre y location of the bomb
	 * @param scale
	 *            Scale of the bomb
	 * @param gameScreen
	 *            GameScreen to which the bomb belongs
	 */
	public BombSprite(float x, float y, float scale, GameScreen gameScreen) {
		super(x, y, scale, gameScreen.getGame().getAssetManager()
				.getBitmap("Bomb"), 24, gameScreen);
		play(2.0f, true);
		gameScreen.getGame().getAssetManager()
				.loadAndAddSound("BombSFX", "sfx/BombSFX.mp3");
		bombSound = gameScreen.getGame().getAssetManager().getSound("BombSFX");
	}

	float generatedFloat;

	// /////////////////////////////////////////////////////////////////////////
	// Methods: General
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Method which determines how loud the explosion is The explosion is louder
	 * if the player sprite is closer to the bomb
	 * 
	 * @param playerSprite
	 *            Player Sprite
	 */
	public void explode(PlayerSprite playerSprite) {
		generatedFloat = (position.x - playerSprite.position.x) / 150;
		// makes explosion SFX louder the closer the player is to the bomb
		if (generatedFloat > 0.25f)
			generatedFloat = 0.25f;
		bombSound.play(0.45f - generatedFloat);
	}

	// /////////////////////////////////////////////////////////////////////////
	// Methods: Collision Detection
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Check for and then resolve any collision between the bomb and the player
	 * 
	 * @param playerSprite
	 *            playerSprite tests collision against bomb
	 * @return true if there is a collision, otherwise false
	 */
	public boolean checkForAndResolveCollisions(PlayerSprite playerSprite) {
		CollisionType collisionType;

		// Player picks up bomb
		collisionType = CollisionDetector.determineAndResolveCollision(this,
				playerSprite);

		switch (collisionType) {
		case Top:
		case Bottom:
		case Left:
		case Right:
			playerSprite.takeAwayHealth(1);
			explode(playerSprite);
			return true;
		case None:
			break;
		default:
			break;
		}

		return false;
	}

	/**
	 * Check for and resolve any collision between the bomb and bullets
	 * 
	 * @param bullets
	 *            Array list of bullets to test collision against bomb
	 * @return true if there is a collision, otherwise false
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