package uk.ac.qub.eeecs.game.sprites;

import java.util.ArrayList;

import uk.ac.qub.eeecs.game.ZombieZoomGame;
import uk.ac.qub.eeecs.game.ZombieZoomGameScreen;
import uk.ac.qub.eeecs.zombiezoom.engine.ElapsedTime;
import uk.ac.qub.eeecs.zombiezoom.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.zombiezoom.util.CollisionDetector;
import uk.ac.qub.eeecs.zombiezoom.util.CollisionDetector.CollisionType;
import uk.ac.qub.eeecs.zombiezoom.util.MoveableObjectsHelper;
import uk.ac.qub.eeecs.zombiezoom.world.Animation;
import uk.ac.qub.eeecs.zombiezoom.world.GameScreen;
import uk.ac.qub.eeecs.zombiezoom.world.LayerViewport;
import uk.ac.qub.eeecs.zombiezoom.world.MoveableGameObject;
import uk.ac.qub.eeecs.zombiezoom.world.ScreenViewport;
import uk.ac.qub.eeecs.zombiezoom.world.Sprite;
import android.graphics.Bitmap;
import android.graphics.Point;

/**
 * Define a basic sprite
 * 
 * @version 1.0
 */
public class PlayerSprite extends MoveableGameObject {
	public enum PlayerState {
		Running, Jumping, Dead
	}

	// /////////////////////////////////////////////////////////////////////////
	// Properties
	// /////////////////////////////////////////////////////////////////////////

	public Animation runningAnimation, deathAnimation;
	public Gun gunSprite;
	private Sprite playerTorsoSprite;
	// stores the most recent platform the Player stood on
	private PlatformSprite currentPlatform;
	private float playerScale;
	public PlayerState state = PlayerState.Running;
	public int health = 15;

	/**
	 * Defines the initial x, y position of the player
	 */
	public final float MIN_JUMP_VELOCITY = 175.0f;
	public final float MAX_JUMP_VELOCITY = 325.0f;
	public final float ROCKET_JUMP_VELOCITY = 200.0f;
	/**
	 * constant translate values
	 */
	public final Point TRANSLATE_TORSO = new Point(1, 9);
	public final Point TRANSLATE_GUN = new Point(19, 7);
	public final Point TRANSLATE_RUNNING = new Point(2, -11);

	/**
	 * Defines the y heights
	 */
	public final float MAX_Y_HEIGHT_WITH_ROCKET_JUMP;
	public final float MAX_Y_HEIGHT;
	public final float MIN_Y_HEIGHT;

	/**
	 * Acceleration with which the player can move along the x-axis
	 */
	float RUN_ACCELERATION = .5f;

	/**
	 * Maximum velocity of the player along the x-axis
	 */
	float MAX_X_VELOCITY = 450.0f;

	/**
	 * Initial velocity of the player along the x-axis
	 */
	float INITIAL_X_VELOCITY = 225.0f;

	/**
	 * Strength of gravity to apply along the y-axis
	 */
	float GRAVITY = ((ZombieZoomGameScreen) mGameScreen).GRAVITY;

	// /////////////////////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a new player
	 * 
	 * @param x
	 *            x location of the player
	 * @param y
	 *            y location of the player
	 * @param playerScale
	 *            Scale of the player
	 * @param gameScreen
	 *            GameScreen to which this player belongs
	 */
	public PlayerSprite(float x, float y, float playerScale,
			GameScreen gameScreen) {
		super(x, y, gameScreen);
		this.playerScale = playerScale;

		Bitmap tempBM = gameScreen.getGame().getAssetManager()
				.getBitmap("Player Torso");
		playerTorsoSprite = new Sprite(position.x, position.y,
				tempBM.getWidth() * playerScale, tempBM.getHeight()
						* playerScale, tempBM, gameScreen);

		gunSprite = new Gun(position.x, position.y, playerScale, gameScreen);

		tempBM = gameScreen.getGame().getAssetManager()
				.getBitmap("Running Animation");
		runningAnimation = new Animation(position.x, position.y, playerScale,
				0, 2, tempBM, 11, gameScreen);

		tempBM = gameScreen.getGame().getAssetManager()
				.getBitmap("Death Animation");
		deathAnimation = new Animation(position.x, position.y, playerScale,
				tempBM, 10, gameScreen);
		deathAnimation.play(.65, false);

		updateSpritePositions();

		// Indicate that playback should commence
		runningAnimation.play(1.2f, true);
		// sets default velocity
		velocity.x = INITIAL_X_VELOCITY;

		mBound.halfHeight = playerTorsoSprite.getBound().halfHeight
				+ runningAnimation.getBound().halfHeight;
		mBound.halfWidth = playerTorsoSprite.getBound().halfWidth;

		// calculates the Y distances which will be used to generate platform
		// locations
		MAX_Y_HEIGHT_WITH_ROCKET_JUMP = 0.99f * MoveableObjectsHelper
				.calculateDisplacementWithAcceleration(MAX_JUMP_VELOCITY
						+ (ROCKET_JUMP_VELOCITY / 2), 0, GRAVITY);
		MAX_Y_HEIGHT = 0.99f * MoveableObjectsHelper
				.calculateDisplacementWithAcceleration(MAX_JUMP_VELOCITY, 0,
						GRAVITY);
		MIN_Y_HEIGHT = 0.99f * MoveableObjectsHelper
				.calculateDisplacementWithAcceleration(MIN_JUMP_VELOCITY, 0,
						GRAVITY);

		// Apply gravity to the y-axis acceleration
		acceleration.y = GRAVITY;

		// set an appropriate positive x-acceleration
		acceleration.x = RUN_ACCELERATION;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Methods: General
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Sets the players state to jumping
	 * 
	 * @param moveDistance
	 *            Distance the player can move
	 */
	public void setJumpingState(float moveDistance) {
		// Checks If the player wants to jump from a still or falling platform
		if (velocity.y == 0.0f
				|| (currentPlatform != null && currentPlatform.isFallable() && velocity.y > currentPlatform.velocity.y - 5)) {
			state = PlayerState.Jumping;
			velocity.y = MIN_JUMP_VELOCITY + moveDistance;

			// ensure velocity does not exceed max jump velocity
			if (velocity.y > MAX_JUMP_VELOCITY)
				velocity.y = MAX_JUMP_VELOCITY;
			currentPlatform = null;
			runningAnimation.setFrameWidth(33);
			runningAnimation.setFrameHeight(20);
			runningAnimation.setCurrentRow(1);
			runningAnimation.setFrameCount(6, 0);
			runningAnimation
					.play(MoveableObjectsHelper.calculateTime(0, velocity.y,
							GRAVITY), false);
		}
	}

	/**
	 * Sets the players state to running
	 */
	public void setRunningState() {
		state = PlayerState.Running;
		runningAnimation.setCurrentRow(0);
		runningAnimation.setFrameCount(11, 5);
		runningAnimation
				.play(1.2 - (0.4 * (velocity.x - INITIAL_X_VELOCITY) / MAX_X_VELOCITY),
						true);
	}

	/**
	 * Rotates the players gun
	 * 
	 * @param touchX
	 *            x position of touch
	 * @param touchY
	 *            y position of touch
	 * @param layerViewport
	 *            LayerViewport which the player belongs
	 * @param screenScale
	 *            Scale of the screen
	 */
	public void rotateGun(float touchX, float touchY,
			LayerViewport layerViewport, float screenScale) {
		gunSprite.setOrientation((float) Math.toDegrees(Math.atan2(touchX
				- (position.x - layerViewport.getLeft()) * screenScale,
				((layerViewport.getHeight() - position.y) * screenScale)
						- touchY)) - 90);
	}

	/**
	 * Decreases the players health
	 * 
	 * @param minusHealth
	 *            Amount to decrease players health by
	 */
	public void takeAwayHealth(int minusHealth) {
		if (!((ZombieZoomGame) mGameScreen.getGame()).mGodModeEnabled)
			health -= minusHealth;
	}

	/**
	 * Updates the sprites positions
	 */
	public void updateSpritePositions() {
		playerTorsoSprite.setPosition(position.x
				+ (TRANSLATE_TORSO.x * playerScale), position.y
				+ (TRANSLATE_TORSO.y * playerScale));
		runningAnimation.setPosition(position.x
				+ (TRANSLATE_RUNNING.x * playerScale), position.y
				+ (TRANSLATE_RUNNING.y * playerScale));
		gunSprite.setPosition(position.x + (TRANSLATE_GUN.x * playerScale),
				position.y + (TRANSLATE_GUN.y * playerScale));
	}

	/**
	 * Checks if the player is jumping
	 * 
	 * @return true if player is jumping, otherwise false
	 */
	public boolean isJumping() {
		if (state == PlayerState.Jumping)
			return true;
		else
			return false;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Methods: Collision Detection
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Check for and then resolve any collision between the player and the
	 * platforms.
	 * 
	 * @param platforms
	 *            Array of platforms to test for collision against
	 * @return true if there is a collision, otherwise false
	 */
	private void checkForAndResolveCollisions(
			ArrayList<PlatformSprite> platforms) {

		CollisionType collisionType;

		// Consider for the first 5 platforms as they are the only ones the
		// player can collide with
		for (int i = 0; i < 5; i++) {
			collisionType = CollisionDetector.determineAndResolveCollision(
					this, platforms.get(i));

			switch (collisionType) {
			case Top:
				velocity.y = 0.0f;
				if (platforms.get(i).isFallable())
					platforms.get(i).acceleration.y = platforms.get(i)
							.getFallingVelocity();
				if (state == PlayerState.Dead)
					velocity.x *= 0.95f;
				currentPlatform = platforms.get(i);
				break;
			case Bottom:
				velocity.y = 0.0f;
				break;
			case Right:
				velocity.x *= 0.99f;
				break;
			case None:
				if (velocity.x < INITIAL_X_VELOCITY
						&& state != PlayerState.Dead)
					velocity.x = INITIAL_X_VELOCITY;
				break;
			default:
				break;
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// Methods: Update / Draw
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Update method for the player
	 * 
	 * @param elapsedTime
	 *            Elapsed Time information for the frame
	 * @param mPlatforms
	 *            Array List of platforms
	 */
	public void update(ElapsedTime elapsedTime,
			ArrayList<PlatformSprite> mPlatforms) {
		// makes the players running animation faster as velocity.x gets larger
		if (state == PlayerState.Running) {
			runningAnimation
					.setAnimationPeriod(1.2 - (0.4 * (velocity.x - INITIAL_X_VELOCITY) / MAX_X_VELOCITY));
		}
		if (!runningAnimation.isPlaying() && state != PlayerState.Dead) {
			setRunningState();
		}

		// Call the sprite's update method to apply the defined
		// accelerations and velocities to provide a new position
		// and orientation.
		super.update(elapsedTime);

		// The player is constrained by a maximum x-velocity,
		// but not a y-velocity. Make sure we have not exceeded this.
		if (Math.abs(velocity.x) > MAX_X_VELOCITY)
			velocity.x = Math.signum(velocity.x) * MAX_X_VELOCITY;

		// Check that our new position has not collided by one of the
		// defined platforms. If so, then removing any overlap and
		// ensure a valid velocity.
		checkForAndResolveCollisions(mPlatforms);

		// updates playerTorsoSprite, runningAnimation and gunSprite positions
		updateSpritePositions();

		// updates the running/jumping Animation
		runningAnimation.update(elapsedTime);
		gunSprite.update(elapsedTime);
		if (state == PlayerState.Dead) {
			deathAnimation.setPosition(position.x, position.y);
			deathAnimation.update(elapsedTime);
		}
	}

	/**
	 * Draw method for the player
	 */
	public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
			LayerViewport layerViewport, ScreenViewport screenViewport) {
		if (state == PlayerState.Dead) {
			deathAnimation.draw(elapsedTime, graphics2D, layerViewport,
					screenViewport);
		} else {
			playerTorsoSprite.draw(elapsedTime, graphics2D, layerViewport,
					screenViewport);
			runningAnimation.draw(elapsedTime, graphics2D, layerViewport,
					screenViewport);
			gunSprite.draw(elapsedTime, graphics2D, layerViewport,
					screenViewport);
		}
	}
}