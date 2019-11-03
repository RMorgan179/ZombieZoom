package uk.ac.qub.eeecs.game.sprites;

import uk.ac.qub.eeecs.zombiezoom.world.GameScreen;
import uk.ac.qub.eeecs.zombiezoom.world.Animation;
import uk.ac.qub.eeecs.zombiezoom.engine.audio.Sound;
import uk.ac.qub.eeecs.zombiezoom.util.CollisionDetector;
import uk.ac.qub.eeecs.zombiezoom.util.CollisionDetector.CollisionType;

public class HealthPackSprite extends Animation {
	public Sound HealthSFX;

	// /////////////////////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a health pack
	 * 
	 * @param x
	 *            Centre x location of the health pack
	 * @param y
	 *            Centre y location of the health pack
	 * @param scale
	 *            Scale of the health pack
	 * @param gameScreen
	 *            GameScreen to which this health pack belongs
	 */
	public HealthPackSprite(float x, float y, float scale, GameScreen gameScreen) {
		super(x, y, scale, gameScreen.getGame().getAssetManager()
				.getBitmap("Potion"), 11, gameScreen);
		play(1.25, true);

		HealthSFX = mGameScreen.getGame().getAssetManager()
				.getSound("HealthSFX");
	}

	// /////////////////////////////////////////////////////////////////////////
	// Methods: Collision Detection
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Check for and then resolve any collision between the health pack and the
	 * player
	 * 
	 * @param playerSprite
	 *            Player sprite to be tests against for collisions
	 * @return true if there is a collision, otherwise false
	 */
	public boolean checkForAndResolveCollisions(PlayerSprite playerSprite) {

		CollisionType collisionType;

		// Player picks up coin
		collisionType = CollisionDetector.determineAndResolveCollision(this,
				playerSprite);

		switch (collisionType) {
		case Top:
		case Bottom:
		case Left:
		case Right:
			HealthSFX.play();

			// if (playerSprite.health < 14 ){
			// playerSprite.health = playerSprite.health + 1;
			// }

			return true;
		case None:
			break;
		default:
			break;
		}
		return false;

	}
}