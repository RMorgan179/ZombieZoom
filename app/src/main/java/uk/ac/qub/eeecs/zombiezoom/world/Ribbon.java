package uk.ac.qub.eeecs.zombiezoom.world;

import android.graphics.Bitmap;
import android.graphics.Rect;
import uk.ac.qub.eeecs.zombiezoom.engine.ElapsedTime;
import uk.ac.qub.eeecs.zombiezoom.engine.graphics.GraphicsHelper;
import uk.ac.qub.eeecs.zombiezoom.engine.graphics.IGraphics2D;

/**
 * Game object superclass
 * 
 * @version 1.0
 */
public class Ribbon extends GameObject {

	// /////////////////////////////////////////////////////////////////////////
	// Properties
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Define the image and bound used to hold the background
	 */
	private Bitmap mRibbonBitmap;

	/**
	 * Reusable Rect's used to draw this sprite
	 */
	protected Rect sourceRect = new Rect();
	protected Rect screenRect = new Rect();

	/**
	 * Creates a new Ribbon
	 * 
	 * @param mRibbonBitmap
	 *            Bitmap which will be used to create the ribbon
	 * @param gameScreen
	 *            GameScreen to which this ribbon belongs
	 */
	public Ribbon(Bitmap mRibbonBitmap, GameScreen gameScreen) {
		super(mRibbonBitmap.getWidth() / 2, mRibbonBitmap.getHeight() / 2,
				mRibbonBitmap.getWidth(), mRibbonBitmap.getHeight(), gameScreen);
		this.mRibbonBitmap = mRibbonBitmap;
	}

	/**
	 * Creates a new Ribbon
	 * 
	 * @param ribbonWidth
	 *            Width of the ribbon
	 * @param ribbonHeight
	 *            Height of the ribbon
	 * @param mRibbonBitmap
	 *            Bitmap which will be used to create the ribbon
	 * @param gameScreen
	 *            GameScreen to which this ribbon belongs
	 */
	public Ribbon(float ribbonWidth, float ribbonHeight, Bitmap mRibbonBitmap,
			GameScreen gameScreen) {
		super(ribbonWidth / 2, ribbonHeight / 2, ribbonWidth, ribbonHeight,
				gameScreen);
		this.mRibbonBitmap = mRibbonBitmap;
	}

	/**
	 * Creates a new Ribbon
	 * 
	 * @param x
	 *            x location of the ribbon
	 * @param y
	 *            y location of the ribbon
	 * @param ribbonWidth
	 *            Width of the ribbon
	 * @param ribbonHeight
	 *            Height of the ribbon
	 * @param mRibbonBitmap
	 *            Bitmap which will be used to create the ribbon
	 * @param gameScreen
	 *            GameScreen to which this ribbon belongs
	 */
	public Ribbon(float x, float y, float ribbonWidth, float ribbonHeight,
			Bitmap mRibbonBitmap, GameScreen gameScreen) {
		super(x, y, ribbonWidth, ribbonHeight, gameScreen);
		this.mRibbonBitmap = mRibbonBitmap;
	}

	/**
	 * Update method
	 */
	public void update(ElapsedTime elapsedTime) {
	}

	/**
	 * Draw the background ribbon
	 * 
	 * @param canvas
	 *            Canvas object on which to draw
	 * @param screenViewport
	 *            Viewport on the canvas object to draw to
	 */
	public void drawRibbon(IGraphics2D graphics2D,
			LayerViewport mLayerViewport, ScreenViewport mScreenViewport) {

		// Based on the layer viewport's location, set the x position of
		// the first ribbon image that falls within the viewport.

		int ribbonInset = (int) (mLayerViewport.x - mLayerViewport.halfWidth)
				/ (int) (mBound.halfWidth * 2);
		mBound.x = ribbonInset * (mBound.halfWidth * 2) + mBound.halfWidth;

		// Draw this image
		GraphicsHelper.getSourceAndScreenRect(mBound, mRibbonBitmap.getWidth(),
				mRibbonBitmap.getHeight(), mLayerViewport, mScreenViewport,
				sourceRect, screenRect);
		graphics2D.drawBitmap(mRibbonBitmap, sourceRect, screenRect, null);

		// Check if we need to draw a second ribbon to fill-in the whole
		// viewport
		if (mBound.x + mBound.halfWidth < mLayerViewport.x
				+ mLayerViewport.halfWidth) {

			// If so, move the ribbon x's location on by one image worth and
			// draw
			mBound.x += mBound.halfWidth * 2;
			GraphicsHelper.getSourceAndScreenRect(mBound,
					mRibbonBitmap.getWidth(), mRibbonBitmap.getHeight(),
					mLayerViewport, mScreenViewport, sourceRect, screenRect);
			graphics2D.drawBitmap(mRibbonBitmap, sourceRect, screenRect, null);
		}
	}
}
