package uk.ac.qub.eeecs.zombiezoom.engine.graphics;

import uk.ac.qub.eeecs.zombiezoom.util.BoundingBox;
import uk.ac.qub.eeecs.zombiezoom.world.LayerViewport;
import uk.ac.qub.eeecs.zombiezoom.world.ScreenViewport;
import uk.ac.qub.eeecs.zombiezoom.world.Sprite;
import android.graphics.Bitmap;
import android.graphics.Rect;

public final class GraphicsHelper {

	/**
	 * Determine a source bitmap Rect and destination screen Rect if the
	 * specified sprite bound falls within the layer's viewport.
	 * 
	 * @param sprite
	 *            Sprite instance to be considered
	 * @param layerViewport
	 *            Layer viewport region to check the entity against
	 * @param screenViewport
	 *            Screen viewport region that will be used to draw the
	 * @param sourceRect
	 *            Output Rect holding the region of the bitmap to draw
	 * @param screenRect
	 *            Output Rect holding the region of the screen to draw to
	 * @return boolean true if the entity is visible, false otherwise
	 */
	public static final boolean getSourceAndScreenRect(Sprite sprite,
			LayerViewport layerViewport, ScreenViewport screenViewport,
			Rect sourceRect, Rect screenRect) {

		// Get the bounding box for the specified sprite
		BoundingBox entityBound = sprite.getBound();

		// Determine if the sprite falls within the layer viewport
		if (entityBound.getLeft() < layerViewport.getRight()
				&& entityBound.getRight() > layerViewport.getLeft()
				&& entityBound.getTop() < layerViewport.getBottom()
				&& entityBound.getBottom() > layerViewport.getTop()) {

			// Work out what region of the sprite is visible within the layer
			// viewport,

			float sourceX = Math.max(0.0f, layerViewport.getLeft()
					- entityBound.getLeft());
			float sourceY = Math.max(0.0f, entityBound.getBottom()
					- layerViewport.getBottom());

			float sourceWidth = ((entityBound.getWidth() - sourceX) - Math.max(
					0.0f, entityBound.getRight() - layerViewport.getRight()));
			float sourceHeight = ((entityBound.getHeight() - sourceY) - Math
					.max(0.0f, layerViewport.getTop() - entityBound.getTop()));

			// Determining the scale factor for mapping the bitmap onto this
			// Rect and set the sourceRect value.

			Bitmap spriteBitmap = sprite.getBitmap();

			float sourceScaleWidth = (float) spriteBitmap.getWidth()
					/ entityBound.getWidth();
			float sourceScaleHeight = (float) spriteBitmap.getHeight()
					/ entityBound.getHeight();

			sourceRect.set((int) (sourceX * sourceScaleWidth),
					(int) (sourceY * sourceScaleHeight),
					(int) ((sourceX + sourceWidth) * sourceScaleWidth),
					(int) ((sourceY + sourceHeight) * sourceScaleHeight));

			// Determine =which region of the screen viewport (relative to the
			// canvas) we will be drawing to.

			// Determine the x- and y-aspect rations between the layer and
			// screen viewports
			float screenXScale = (float) screenViewport.width
					/ layerViewport.getWidth();
			float screenYScale = (float) screenViewport.height
					/ layerViewport.getHeight();

			float screenX = screenViewport.left
					+ screenXScale
					* Math.max(0.0f,
							entityBound.getLeft() - layerViewport.getLeft());
			float screenY = screenViewport.top
					+ screenYScale
					* Math.max(0.0f,
							layerViewport.getBottom() - entityBound.getBottom());

			// Set the region to the canvas to which we will draw
			screenRect.set((int) screenX, (int) screenY,
					(int) (screenX + sourceWidth * screenXScale),
					(int) (screenY + sourceHeight * screenYScale));

			return true;
		}

		// Not visible
		return false;
	}

	/**
	 * Determine a source bitmap Rect and destination screen Rect if a specified
	 * entity falls within the layer's viewport.
	 * 
	 * @param entityBound
	 *            Bound of the entity to be considered
	 * @param entityBitmapWidth
	 *            Width of the entity
	 * @param entityBitmapHeight
	 *            Height of the entity
	 * @param layerViewport
	 *            Layer viewport region to check the entity against
	 * @param screenViewport
	 *            Screen viewport region that will be used to draw the
	 * @param sourceRect
	 *            Output Rect holding the region of the bitmap to draw
	 * @param screenRect
	 *            Output Rect holding the region of the screen to draw to
	 * @return boolean true if the entity is visible, false otherwise
	 */
	public static final boolean getSourceAndScreenRect(BoundingBox entityBound,
			float entityBitmapWidth, float entityBitmapHeight,
			LayerViewport layerViewport, ScreenViewport screenViewport,
			Rect sourceRect, Rect screenRect) {

		// Determine if the sprite falls within the layer viewport
		if (entityBound.getLeft() < layerViewport.getRight()
				&& entityBound.getRight() > layerViewport.getLeft()
				&& entityBound.getTop() < layerViewport.getBottom()
				&& entityBound.getBottom() > layerViewport.getTop()) {

			// Work out what region of the sprite is visible within the layer
			// viewport,

			float sourceX = Math.max(0.0f, layerViewport.getLeft()
					- entityBound.getLeft());
			float sourceY = Math.max(0.0f, entityBound.getBottom()
					- layerViewport.getBottom());

			float sourceWidth = ((entityBound.getWidth() - sourceX) - Math.max(
					0.0f, entityBound.getRight() - layerViewport.getRight()));
			float sourceHeight = ((entityBound.getHeight() - sourceY) - Math
					.max(0.0f, layerViewport.getTop() - entityBound.getTop()));

			// Determining the scale factor for mapping the bitmap onto this
			// Rect and set the sourceRect value.

			float sourceScaleWidth = (float) entityBitmapWidth
					/ entityBound.getWidth();
			float sourceScaleHeight = (float) entityBitmapHeight
					/ entityBound.getHeight();

			sourceRect.set((int) (sourceX * sourceScaleWidth),
					(int) (sourceY * sourceScaleHeight),
					(int) ((sourceX + sourceWidth) * sourceScaleWidth),
					(int) ((sourceY + sourceHeight) * sourceScaleHeight));

			// Determine =which region of the screen viewport (relative to the
			// canvas) we will be drawing to.

			// Determine the x- and y-aspect rations between the layer and
			// screen viewports
			float screenXScale = (float) screenViewport.width
					/ layerViewport.getWidth();
			float screenYScale = (float) screenViewport.height
					/ layerViewport.getHeight();

			float screenX = screenViewport.left
					+ screenXScale
					* Math.max(0.0f,
							entityBound.getLeft() - layerViewport.getLeft());
			float screenY = screenViewport.top
					+ screenYScale
					* Math.max(0.0f,
							layerViewport.getBottom() - entityBound.getBottom());

			// Set the region to the canvas to which we will draw
			screenRect.set((int) screenX, (int) screenY,
					(int) (screenX + sourceWidth * screenXScale),
					(int) (screenY + sourceHeight * screenYScale));

			return true;
		}

		// Not visible
		return false;
	}

	/**
	 * 
	 /** Determine if the sprite falls within the layer viewport
	 * 
	 * @param sprite
	 *            Sprite instance to be considered
	 * @param layerViewport
	 *            Layer viewport region to check the entity against
	 * @return boolean true if the entity is visible, false otherwise
	 */
	public static final boolean determineSpriteFallsWithinLayerViewport(
			Sprite sprite, LayerViewport layerViewport) {

		// Get the bounding box for the specified sprite
		BoundingBox entityBound = sprite.getBound();

		// Determine if the sprite falls within the layer viewport
		if (entityBound.getLeft() < layerViewport.getRight()
				&& entityBound.getRight() > layerViewport.getLeft()
				&& entityBound.getTop() < layerViewport.getBottom()
				&& entityBound.getBottom() > layerViewport.getTop()) {
			return true;
		}

		// Not visible
		return false;
	}
}