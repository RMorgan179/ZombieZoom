package uk.ac.qub.eeecs.zombiezoom.world;

import uk.ac.qub.eeecs.zombiezoom.engine.ElapsedTime;
import uk.ac.qub.eeecs.zombiezoom.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.zombiezoom.util.BoundingBox;
import uk.ac.qub.eeecs.zombiezoom.util.Vector2;

/**
 * Game object superclass
 * 
 * @version 1.0
 */
public abstract class GameObject {

	/**
	 * Game screen to which this game object belongs
	 */
	protected GameScreen mGameScreen;

	/**
	 * Bounding box for this game object
	 */
	protected BoundingBox mBound = new BoundingBox();

	/**
	 * Return the bounding box for this game object.
	 * 
	 * Note: The values within the bounding box should not be modified.
	 * 
	 * @return The bounding box
	 */
	public BoundingBox getBound() {
		// Ensure the bound is centred on the sprite's current location
		mBound.x = position.x;
		mBound.y = position.y;
		return mBound;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Properties
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Position of this game object
	 */
	public Vector2 position = new Vector2();

	/**
	 * Create a new game object
	 * 
	 * @param gameScreen
	 *            Gamescreen to which this object belongs
	 */
	public GameObject(GameScreen gameScreen) {
		mGameScreen = gameScreen;
	}

	/**
	 * Create a new game object
	 * 
	 * @param x
	 *            x location of the object
	 * @param y
	 *            y location of the object
	 * @param gameScreen
	 *            Gamescreen to which this object belongs
	 */
	public GameObject(float x, float y, GameScreen gameScreen) {
		mGameScreen = gameScreen;

		mBound.x = position.x = x;
		mBound.y = position.y = y;
	}

	/**
	 * Create a new game object
	 * 
	 * @param x
	 *            x location of the object
	 * @param y
	 *            y location of the object
	 * @param width
	 *            width used for bounding box
	 * @param height
	 *            height used for bounding box
	 * @param gameScreen
	 *            Gamescreen to which this object belongs
	 */
	public GameObject(float x, float y, float width, float height,
			GameScreen gameScreen) {
		mGameScreen = gameScreen;

		mBound.x = position.x = x;
		mBound.y = position.y = y;
		mBound.halfHeight = height / 2;
		mBound.halfWidth = width / 2;
	}

	/**
	 * Set the position of the game object
	 * 
	 * @param x
	 *            x-location of the game object
	 * @param y
	 *            y-location of the game object
	 */
	public void setPosition(float x, float y) {

		mBound.x = position.x = x;
		mBound.y = position.y = y;
	}

	/**
	 * Update the game object
	 * 
	 * @param elapsedTime
	 *            Elapsed time information
	 */
	public void update(ElapsedTime elapsedTime) {
	}

	/**
	 * Draw the game object
	 * 
	 * @param elapsedTime
	 *            Elapsed time information
	 * @param graphics2D
	 *            Graphics instance
	 * @param layerViewport
	 *            Game layer viewport
	 * @param screenViewport
	 *            Screen viewport
	 */
	public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
			LayerViewport layerViewport, ScreenViewport screenViewport) {
	}
}
