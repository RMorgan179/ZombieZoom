package uk.ac.qub.eeecs.game.sprites;

import android.graphics.Bitmap;
import uk.ac.qub.eeecs.zombiezoom.world.Animation;
import uk.ac.qub.eeecs.zombiezoom.world.GameScreen;

public class PointsCounter extends Animation {

	// /////////////////////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a new Points Counter Animation
	 * 
	 * @param x
	 *            Centre x location of the animation
	 * @param y
	 *            Centre y location of the animation
	 * @param pointsBitmap
	 *            Bitmap holding the frames of the animation
	 * @param frameCount
	 *            Number of horizontal frames in the animation
	 * @param gameScreen
	 *            GameScreen to which this animation belongs
	 */
	public PointsCounter(float x, float y, Bitmap pointsBitmap, int frameCount,
			GameScreen gameScreen) {
		super(x, y, 1, pointsBitmap, frameCount, gameScreen);
		play(0.75, false);
	}

	/**
	 * Creates a new Points Counter Animation
	 * 
	 * @param x
	 *            Centre x location of the animation
	 * @param y
	 *            Centre y location of the animation
	 * @param scale
	 *            of the animation
	 * @param pointsBitmap
	 *            Bitmap holding the frames of the animation
	 * @param frameCount
	 *            Number of horizontal frames in the animation
	 * @param gameScreen
	 *            GameScreen to which this animation belongs
	 */
	public PointsCounter(float x, float y, float scale, Bitmap pointsBitmap,
			int frameCount, GameScreen gameScreen) {
		super(x, y, scale, pointsBitmap, frameCount, gameScreen);
		play(0.75, false);
	}
}