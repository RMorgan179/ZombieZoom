package uk.ac.qub.eeecs.game.sprites;

import uk.ac.qub.eeecs.zombiezoom.world.Animation;
import uk.ac.qub.eeecs.zombiezoom.world.GameScreen;

public class Explosion extends Animation {

	// /////////////////////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Creates an explosion
	 * 
	 * @param x
	 *            Centre x location of the explosion
	 * @param y
	 *            Centre y location of the explosion
	 * @param gameScreen
	 *            GameScreen to which the explosion belongs
	 */
	public Explosion(float x, float y, GameScreen gameScreen) {
		super(x, y, .3f, gameScreen.getGame().getAssetManager()
				.getBitmap("Explosion"), 48, gameScreen);
		play(2.25, false);
	}

	/**
	 * Creates an explosion
	 * 
	 * @param x
	 *            Centre x location of the explosion
	 * @param y
	 *            Centre y location of the explosion
	 * @param scale
	 *            Scale of the explosion
	 * @param gameScreen
	 *            GameScreen to which this explosion belongs
	 */
	public Explosion(float x, float y, float scale, GameScreen gameScreen) {
		super(x, y, scale, gameScreen.getGame().getAssetManager()
				.getBitmap("Explosion"), 48, gameScreen);
		play(2.25, false);
	}
}