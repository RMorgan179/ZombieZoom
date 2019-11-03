package uk.ac.qub.eeecs.zombiezoom.world;

import uk.ac.qub.eeecs.zombiezoom.engine.ElapsedTime;
import uk.ac.qub.eeecs.zombiezoom.util.Vector2;

/**
 * Game object superclass
 * 
 * @version 1.0
 */
public class MoveableGameObject extends GameObject {

	// /////////////////////////////////////////////////////////////////////////
	// Default values
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Default maximum acceleration and velocity
	 */
	public static float DEFAULT_MAX_ACCELERATION = Float.MAX_VALUE;
	public static float DEFAULT_MAX_VELOCITY = Float.MAX_VALUE;

	// /////////////////////////////////////////////////////////////////////////
	// Properties
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Acceleration and velocity of the sprite, alongside maximum values.
	 * Position is inherited from game object.
	 */
	public Vector2 velocity = new Vector2();
	public Vector2 acceleration = new Vector2();

	public float maxAcceleration = DEFAULT_MAX_ACCELERATION;
	public float maxVelocity = DEFAULT_MAX_VELOCITY;

	/**
	 * Creates a Moveable Game Object
	 * 
	 * @param gameScreen
	 *            GameScreen to which this moveable game object belongs
	 */
	public MoveableGameObject(GameScreen gameScreen) {
		super(gameScreen);
	}

	/**
	 * Creates a Moveable Game Object
	 * 
	 * @param x
	 *            x location of the moveable game object
	 * @param y
	 *            y location of the moveable game object
	 * @param gameScreen
	 *            GameScreen to which this moveable game object belongs
	 */
	public MoveableGameObject(float x, float y, GameScreen gameScreen) {
		super(x, y, gameScreen);
	}

	/**
	 * Update the game object
	 * 
	 * @param elapsedTime
	 *            Elapsed time information
	 */
	public void update(ElapsedTime elapsedTime) {
		float dt = (float) elapsedTime.stepTime;

		// Ensure the maximum acceleration isn't exceeded
		if (acceleration.lengthSquared() > maxAcceleration * maxAcceleration) {
			acceleration.normalise();
			acceleration.multiply(maxAcceleration);
		}

		// Update the velocity using the acceleration and ensure the
		// maximum velocity has not been exceeded
		velocity.add(acceleration.x * dt, acceleration.y * dt);

		if (velocity.lengthSquared() > maxVelocity * maxVelocity) {
			velocity.normalise();
			velocity.multiply(maxVelocity);
		}

		// Update the position using the velocity
		position.add(velocity.x * dt, velocity.y * dt);
		mBound.x = position.x;
		mBound.y = position.y;
	}
}
