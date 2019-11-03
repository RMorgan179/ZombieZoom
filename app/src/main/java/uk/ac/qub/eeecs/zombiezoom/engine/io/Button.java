package uk.ac.qub.eeecs.zombiezoom.engine.io;

import uk.ac.qub.eeecs.zombiezoom.engine.ElapsedTime;
import uk.ac.qub.eeecs.zombiezoom.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.zombiezoom.engine.input.Input;
import uk.ac.qub.eeecs.zombiezoom.engine.input.TouchHandler;
import uk.ac.qub.eeecs.zombiezoom.util.BoundingBox;
import uk.ac.qub.eeecs.zombiezoom.world.GameScreen;
import uk.ac.qub.eeecs.zombiezoom.world.LayerViewport;
import uk.ac.qub.eeecs.zombiezoom.world.ScreenViewport;
import uk.ac.qub.eeecs.zombiezoom.world.Sprite;

/**
 * Button that can detected if a touch event falls on it.
 * 
 * Important: It is assumed that the control is defined in terms of screen
 * coordinates (not layer coordinates).
 * 
 * @version 1.0
 */
public class Button extends Sprite {

	// /////////////////////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Create a new Button.
	 * 
	 * @param x
	 *            Centre y location of the control
	 * @param y
	 *            Centre x location of the control
	 * @param width
	 *            Width of the control
	 * @param height
	 *            Height of the control
	 * @param bitmap
	 *            Bitmap used to represent this control
	 * @param gameScreen
	 *            Gamescreen to which this control belongs
	 */
	public Button(float x, float y, float width, float height,
			String bitmapName, GameScreen gameScreen) {
		super(x, y, width, height, gameScreen.getGame().getAssetManager()
				.getBitmap(bitmapName), gameScreen);
	}

	// /////////////////////////////////////////////////////////////////////////
	// Methods
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Determine if this Button has been activated (touched).
	 * 
	 * @return boolean true if the control has been touched, false otherwise
	 */
	public boolean isActivated() {

		// Consider any touch events occurring in this update
		Input input = mGameScreen.getGame().getInput();

		// Check if any of the touch events were on this control
		BoundingBox bound = getBound();
		for (int idx = 0; idx < TouchHandler.MAX_TOUCHPOINTS; idx++) {
			if (input.existsTouch(idx)) {
				if (bound.contains(input.getTouchX(idx), input.getTouchY(idx))) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
			LayerViewport layerViewport, ScreenViewport screenViewport) {

		// Assumed to be in screen space so just draw the whole thing
		drawScreenRect.set((int) (position.x - mBound.halfWidth),
				(int) (position.y - mBound.halfWidth),
				(int) (position.x + mBound.halfWidth),
				(int) (position.y + mBound.halfHeight));

		graphics2D.drawBitmap(mBitmap, null, drawScreenRect, null);
	}
}
