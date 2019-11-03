package uk.ac.qub.eeecs.game.sprites;

import java.util.ArrayList;

import uk.ac.qub.eeecs.game.ZombieZoomGameScreen;
import uk.ac.qub.eeecs.game.ai.BatBehaviour;
import uk.ac.qub.eeecs.zombiezoom.engine.ElapsedTime;
import uk.ac.qub.eeecs.zombiezoom.util.CollisionDetector;
import uk.ac.qub.eeecs.zombiezoom.util.CollisionDetector.CollisionType;
import uk.ac.qub.eeecs.zombiezoom.world.Animation;
import uk.ac.qub.eeecs.zombiezoom.world.GameScreen;

public class BatSprite extends Animation {

	// /////////////////////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a new bat sprite
	 * 
	 * @param x
	 *            Centre x location of the bat
	 * @param y
	 *            Centre y location of the bat
	 * @param gameScreen
	 *            GameScreen to which the bat belongs
	 */
	public BatSprite(float x, float y, GameScreen gameScreen) {
		super(x, y, .5f, gameScreen.getGame().getAssetManager()
				.getBitmap("Bat"), 11, gameScreen);
		play(.35, true);
		setLoopBackwards();
		maxVelocity = 200;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Methods: Collision Detection
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Check for and then resolve any collision between the bat and the player
	 * 
	 * @param playerSprite
	 *            Player Sprite to test for collision against bats
	 * @return true if there is a collision, otherwise false
	 */
	public boolean checkForAndResolveCollisions(PlayerSprite playerSprite) {
		CollisionType collisionType;

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
	 * Check for and then resolve any collision between the bats and bullets
	 * 
	 * @param bullets
	 *            Array of bullets to test for collision against bats
	 * @return true if there is a collision, otherwise false
	 */
	public boolean checkForAndResolveCollisions(ArrayList<BulletSprite> bullets) {
		CollisionType collisionType;

		// Checks if bat is hit by a bullet and explodes if it does
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

	// /////////////////////////////////////////////////////////////////////////
	// Methods: Update
	// /////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.eeecs.zombiezoom.world.Animation#update(uk.ac.qub.eeecs.zombiezoom
	 * .engine.ElapsedTime)
	 */
	@Override
	public void update(ElapsedTime elapsedTime) {
		BatBehaviour.seek(this,
				((ZombieZoomGameScreen) mGameScreen).getPlayerSprite());
		super.update(elapsedTime);
	}
}