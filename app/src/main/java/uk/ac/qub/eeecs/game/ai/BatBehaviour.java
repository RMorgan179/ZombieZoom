package uk.ac.qub.eeecs.game.ai;

import uk.ac.qub.eeecs.game.sprites.PlayerSprite;
import uk.ac.qub.eeecs.zombiezoom.util.Vector2;
import uk.ac.qub.eeecs.zombiezoom.world.Animation;

public class BatBehaviour {

	/**
	 * Store an acceleration vector for the specified body towards the specified
	 * target Animation
	 * 
	 * @param playerSprite
	 *            Seeking Animation
	 * @param targetAnimation
	 *            Target Animation
	 */
	public static void seek(Animation targetAnimation, PlayerSprite playerSprite) {
		// Return if the body has reached the target
		if (targetAnimation.position.x == playerSprite.position.x
				&& targetAnimation.position.y == playerSprite.position.y) {
			targetAnimation.velocity.set(Vector2.Zero);
		} else {
			// Determine the seeking direction
			targetAnimation.velocity.set(playerSprite.position.x
					- targetAnimation.position.x, playerSprite.position.y
					- targetAnimation.position.y);
			targetAnimation.velocity.normalise();
			targetAnimation.velocity.multiply(targetAnimation.maxVelocity);
		}
	}
}
