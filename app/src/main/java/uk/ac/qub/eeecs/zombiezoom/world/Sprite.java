package uk.ac.qub.eeecs.zombiezoom.world;

import uk.ac.qub.eeecs.zombiezoom.engine.ElapsedTime;
import uk.ac.qub.eeecs.zombiezoom.engine.graphics.GraphicsHelper;
import uk.ac.qub.eeecs.zombiezoom.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.zombiezoom.util.BoundingBox;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Simple sprite class
 * 
 * @version 1.0
 */
public class Sprite extends MoveableGameObject {

	// /////////////////////////////////////////////////////////////////////////
	// Properties
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Bitmap used to render this sprite
	 */
	protected Bitmap mBitmap;

	/**
	 * Reusable Rect's used to draw this sprite
	 */
	protected Rect drawSourceRect = new Rect();
	protected Rect drawScreenRect = new Rect();

	/**
	 * Orientation alongside angular velocity and acceleration, with maximum
	 * values.
	 */
	protected float orientation;

	/**
	 * Gets the sprite's orientation
	 * 
	 * @return Orientation of the sprite
	 */
	public float getOrientation() {
		return orientation;
	}

	/**
	 * Sets the sprite's orientation
	 * 
	 * @param orientation
	 *            Orientation of the sprite
	 */
	public void setOrientation(float orientation) {
		this.orientation = orientation;
	}

	/**
	 * Get's the rotation position of the sprite
	 * 
	 * @return The sprite's rotation position
	 */
	public Point getRotationPosition() {
		return rotationPosition;
	}

	/**
	 * Places point in centre if there are no parameters
	 */
	public void setRotationPosition() {
		this.rotationPosition = new Point((int) mBound.halfWidth,
				(int) mBound.halfHeight);
	}

	/**
	 * Places rotation point
	 * 
	 * @param rotationPosition
	 *            Point to be used for rotation
	 */
	public void setRotationPosition(Point rotationPosition) {
		this.rotationPosition = rotationPosition;
	}

	/**
	 * Position of rotation point
	 */
	protected Point rotationPosition;

	/**
	 * Internal matrix use to support draw requests
	 */
	protected Matrix drawMatrix = new Matrix();

	// /////////////////////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Create a new Sprite
	 * 
	 * @param gameScreen
	 *            Gamescreen to which this sprite belongs
	 */
	public Sprite(GameScreen gameScreen) {
		super(gameScreen);
		mBound = new BoundingBox();
	}

	/**
	 * Create a new sprite. The size will be set to that of the associated
	 * bitmap
	 * 
	 * @param centerX
	 *            Centre y location of the sprite
	 * @param centerY
	 *            Centre x location of the sprite
	 * @param gameScreen
	 *            Gamescreen to which this sprite belongs
	 */
	public Sprite(float x, float y, GameScreen gameScreen) {
		super(x, y, gameScreen);
	}

	/**
	 * Create a new sprite. The size will be set to that of the associated
	 * bitmap
	 * 
	 * @param centerX
	 *            Centre y location of the sprite
	 * @param centerY
	 *            Centre x location of the sprite
	 * @param bitmap
	 *            Bitmap used to represent this sprite
	 * @param gameScreen
	 *            Gamescreen to which this sprite belongs
	 */
	public Sprite(float x, float y, Bitmap bitmap, GameScreen gameScreen) {
		super(x, y, gameScreen);
		setBitmapAndBoundingBox(x, y, bitmap);
	}

	/**
	 * Create a new sprite. The size will be set to that of the associated
	 * bitmap
	 * 
	 * @param centerX
	 *            Centre y location of the sprite
	 * @param centerY
	 *            Centre x location of the sprite
	 * @param bitmap
	 *            Bitmap used to represent this sprite
	 */
	public void setBitmapAndBoundingBox(float x, float y, Bitmap bitmap) {
		mBitmap = bitmap;
		mBound = new BoundingBox(x, y, bitmap.getWidth() / 2.0f,
				bitmap.getHeight() / 2.0f);
	}

	/**
	 * Create a new sprite.
	 * 
	 * @param centerX
	 *            Centre y location of the sprite
	 * @param centerY
	 *            Centre x location of the sprite
	 * @param width
	 *            Width of the sprite
	 * @param height
	 *            Height of the sprite
	 * @param bitmap
	 *            Bitmap used to represent this sprite
	 * @param gameScreen
	 *            Gamescreen to which this sprite belongs
	 */
	public Sprite(float x, float y, float width, float height, Bitmap bitmap,
			GameScreen gameScreen) {
		super(gameScreen);

		position.x = x;
		position.y = y;
		mBitmap = bitmap;

		mBound = new BoundingBox(x, y, width / 2.0f, height / 2.0f);
	}

	/**
	 * Create a new sprite.
	 * 
	 * @param centerX
	 *            Centre y location of the sprite
	 * @param centerY
	 *            Centre x location of the sprite
	 * @param width
	 *            Width of the sprite
	 * @param height
	 *            Height of the sprite
	 * @param bitmap
	 *            Bitmap used to represent this sprite
	 * @param rotX
	 *            x location the animation rotates about
	 * @param rotY
	 *            y location the animation rotates about
	 * @param gameScreen
	 *            Gamescreen to which this sprite belongs
	 */
	public Sprite(float x, float y, float width, float height, Bitmap bitmap,
			float rotX, float rotY, GameScreen gameScreen) {
		super(gameScreen);

		position.x = x;
		position.y = y;
		mBitmap = bitmap;

		mBound = new BoundingBox(x, y, width / 2.0f, height / 2.0f);
		rotationPosition = new Point((int) rotX, (int) rotY);
	}

	// /////////////////////////////////////////////////////////////////////////
	// Configuration Methods
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Return the bitmap used for this sprite.
	 * 
	 * @return Bitmap associated with this sprite.
	 */
	public Bitmap getBitmap() {
		return mBitmap;
	}

	/**
	 * Set the position of the sprite
	 * 
	 * @param x
	 *            x-location of the sprite
	 * @param y
	 *            y-location of the sprite
	 */
	public void setPosition(float x, float y) {
		mBound.x = position.x = x;
		mBound.y = position.y = y;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Update and Draw
	// /////////////////////////////////////////////////////////////////////////

	@Override
	public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
			LayerViewport layerViewport, ScreenViewport screenViewport) {
		if (GraphicsHelper.getSourceAndScreenRect(this, layerViewport,
				screenViewport, drawSourceRect, drawScreenRect)) {
			// if picture does not rotate
			if (rotationPosition == null) {
				graphics2D.drawBitmap(mBitmap, drawSourceRect, drawScreenRect,
						null);
			}
			// if picture does rotate
			else {
				float scaleX = (float) drawScreenRect.width()
						/ (float) drawSourceRect.width();
				float scaleY = (float) drawScreenRect.height()
						/ (float) drawSourceRect.height();

				// Build an appropriate transformation matrix
				drawMatrix.reset();
				drawMatrix.postRotate(orientation, rotationPosition.x,
						rotationPosition.y);
				drawMatrix.postScale(scaleX, scaleY);
				drawMatrix.postTranslate(drawScreenRect.left,
						drawScreenRect.top);
				// Draw the image
				graphics2D.drawBitmap(mBitmap, drawMatrix, null);
			}
		}
	}
}
