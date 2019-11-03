package uk.ac.qub.eeecs.zombiezoom.world;

import uk.ac.qub.eeecs.zombiezoom.engine.ElapsedTime;
import uk.ac.qub.eeecs.zombiezoom.util.BoundingBox;
import android.graphics.Bitmap;
import android.graphics.Point;

public class Animation extends Sprite {

	// ////////////////////////////////////////////////////////////////////
	// Animation Properties
	// ////////////////////////////////////////////////////////////////////

	/**
	 * Bitmap holding the frames of this animation
	 */
	private Bitmap animationFrames;

	/**
	 * Sets the frames width
	 * 
	 * @param frameWidth
	 *            Width of the frame
	 */
	public void setFrameWidth(int frameWidth) {
		this.frameWidth = frameWidth;
		mBound.halfWidth = frameWidth * scale / 2.0f;
	}

	/**
	 * Sets the frames height
	 * 
	 * @param frameHeight
	 *            Height of the frame
	 */
	public void setFrameHeight(int frameHeight) {
		this.frameHeight = frameHeight;
		mBound.halfHeight = frameHeight * scale / 2.0f;
	}

	/**
	 * Width and height of each frame of the animation
	 */
	private int frameWidth;
	/**
	 * loopBackwards determines whether or not the animation returns the first
	 * frame (i.e. false) or moves backwards through the frames (i.e. true)
	 * default is false
	 */
	private boolean loopBackwards = false;

	public void setLoopBackwards() {
		loopBackwards = true;
	}

	/**
	 * if moveRight is true then the next frame is to the right, if it is false
	 * then the next frame is to the left default is true
	 */
	private boolean moveRight = true;

	/**
	 * Sets the Frame Count
	 * 
	 * @param frameCount
	 *            The number of frames
	 */
	public void setFrameCount(int frameCount) {
		this.frameCount = frameCount;
		this.currentFrame = frameCount - 1;
	}

	/**
	 * Sets the Frame Count and Current Frame
	 * 
	 * @param frameCount
	 *            The number of frames
	 * @param currentFrame
	 *            The current frame
	 */
	public void setFrameCount(int frameCount, int currentFrame) {
		this.frameCount = frameCount;
		this.currentFrame = currentFrame;
	}

	/**
	 * Sets the Current Row
	 * 
	 * @param currentRow
	 *            The current row of the animation
	 */
	public void setCurrentRow(int currentRow) {
		this.currentRow = currentRow;
	}

	/**
	 * Height of each frame
	 */
	private int frameHeight;

	/**
	 * Number of frames in the animation
	 */
	private int frameCount;

	/**
	 * scale determines the size
	 */
	private float scale;

	/**
	 * Index of the current frame of animation
	 */
	private int currentFrame;

	/**
	 * Gets the current frame
	 * 
	 * @return currentFrame
	 */
	public int getCurrentFrame() {
		return currentFrame;
	}

	/**
	 * Index of the current frame of animation
	 */
	private int currentRow;

	/**
	 * Display period for each frame alongside a frame timer
	 */
	private double frameTimer;
	private double framePeriod;

	/**
	 * Boolean flag determining if the animation should loop
	 */
	private boolean isLooping = false;

	/**
	 * Boolean flag determining if the animation is currently playing
	 */
	private boolean isPlaying = false;

	// ////////////////////////////////////////////////////////////////////
	// Animation Constructor and Methods
	// ////////////////////////////////////////////////////////////////////

	/**
	 * Create a new animation
	 * 
	 * @param gameScreen
	 *            Gamescreen to which this animation belongs (assumed to be of
	 *            equal width)
	 */
	public Animation(GameScreen gameScreen) {
		super(gameScreen);
	}

	/**
	 * Create a new animation
	 * 
	 * @param x
	 *            Centre x location of the animation
	 * @param y
	 *            Centre y location of the animation
	 * @param scale
	 *            of the animation
	 * @param animationFrames
	 *            Bitmap holding the frames of the animation
	 * @param frameCount
	 *            Number of horizontal frames in the animation
	 * @param gameScreen
	 *            Gamescreen to which this animation belongs (assumed to be of
	 *            equal width)
	 */
	public Animation(float x, float y, float scale, Bitmap animationFrames,
			int frameCount, GameScreen gameScreen) {
		super(x, y, gameScreen);
		this.setAnimationFrames(animationFrames);
		this.frameCount = frameCount;
		this.currentRow = 0;
		this.scale = scale;

		frameHeight = animationFrames.getHeight();
		frameWidth = animationFrames.getWidth() / frameCount;
		mBound = new BoundingBox(x, y, frameWidth * scale / 2.0f, frameHeight
				* scale / 2.0f);
		// sets initial frame
		mBitmap = Bitmap.createBitmap(animationFrames, 0, currentRow
				* frameHeight, frameWidth, frameHeight);
	}

	/**
	 * Create a new animation
	 * 
	 * @param x
	 *            Centre x location of the animation
	 * @param y
	 *            Centre y location of the animation
	 * @param scale
	 *            of the animation
	 * @param currentRow
	 *            the index of the selected animation
	 * @param numberOfRows
	 *            number of animations
	 * @param animationFrames
	 *            Bitmap holding the frames of the animation
	 * @param frameCount
	 *            Number of horizontal frames in the animation
	 * @param gameScreen
	 *            Gamescreen to which this animation belongs (assumed to be of
	 *            equal width)
	 */
	public Animation(float x, float y, float scale, int currentRow,
			int numberOfRows, Bitmap animationFrames, int frameCount,
			GameScreen gameScreen) {
		super(x, y, gameScreen);
		this.setAnimationFrames(animationFrames);
		this.frameCount = frameCount;
		this.currentRow = currentRow;
		this.scale = scale;

		frameHeight = animationFrames.getHeight() / numberOfRows;
		frameWidth = animationFrames.getWidth() / frameCount;
		mBound = new BoundingBox(x, y, frameWidth * scale / 2.0f, frameHeight
				* scale / 2.0f);
		// sets initial frame
		mBitmap = Bitmap.createBitmap(animationFrames, 0, currentRow
				* frameHeight, frameWidth, frameHeight);
	}

	/**
	 * Create a new animation
	 * 
	 * @param x
	 *            Centre x location of the animation
	 * @param y
	 *            Centre y location of the animation
	 * @param scale
	 *            of the animation
	 * @param currentRow
	 *            the index of the selected animation
	 * @param numberOfRows
	 *            number of animations
	 * @param animationFrames
	 *            Bitmap holding the frames of the animation
	 * @param frameCount
	 *            Number of horizontal frames in the animation
	 * @param rotX
	 *            x location the animation rotates about
	 * @param rotY
	 *            y location the animation rotates about
	 * @param gameScreen
	 *            Gamescreen to which this animation belongs (assumed to be of
	 *            equal width)
	 */
	public Animation(float x, float y, float scale, int currentRow,
			int numberOfRows, Bitmap animationFrames, int frameCount, int rotX,
			int rotY, GameScreen gameScreen) {
		super(x, y, gameScreen);
		this.setAnimationFrames(animationFrames);
		this.frameCount = frameCount;
		this.currentRow = currentRow;
		this.scale = scale;

		frameHeight = animationFrames.getHeight() / numberOfRows;
		frameWidth = animationFrames.getWidth() / frameCount;
		mBound = new BoundingBox(x, y, frameWidth * scale / 2.0f, frameHeight
				* scale / 2.0f);
		// sets initial frame
		mBitmap = Bitmap.createBitmap(animationFrames, 0, currentRow
				* frameHeight, frameWidth, frameHeight);
		rotationPosition = new Point(rotX, rotY);
	}

	/**
	 * Create a new animation
	 * 
	 * @param x
	 *            Centre x location of the animation
	 * @param y
	 *            Centre y location of the animation
	 * @param frameWidth
	 *            Width of the animation
	 * @param frameHeight
	 *            Height of the animation
	 * @param scale
	 *            of the animation
	 * @param currentRow
	 *            the index of the selected animation
	 * @param numberOfRows
	 *            number of animations
	 * @param animationFrames
	 *            Bitmap holding the frames of the animation
	 * @param frameCount
	 *            Number of horizontal frames in the animation
	 * @param gameScreen
	 *            Gamescreen to which this animation belongs (assumed to be of
	 *            equal width)
	 */
	public Animation(float x, float y, int frameWidth, int frameHeight,
			float scale, int currentRow, int numberOfRows,
			Bitmap animationFrames, int frameCount, GameScreen gameScreen) {
		super(x, y, gameScreen);
		this.setAnimationFrames(animationFrames);
		this.frameCount = frameCount;
		this.currentRow = currentRow;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.scale = scale;

		mBound = new BoundingBox(x, y, frameWidth * scale / 2.0f, frameHeight
				* scale / 2.0f);
		// sets initial frame
		mBitmap = Bitmap.createBitmap(animationFrames, 0, currentRow
				* frameHeight, frameWidth, frameHeight);
	}

	/**
	 * Create a new animation
	 * 
	 * @param x
	 *            Centre x location of the animation
	 * @param y
	 *            Centre y location of the animation
	 * @param frameWidth
	 *            Width of the animation
	 * @param frameHeight
	 *            Height of the animation
	 * @param scale
	 *            of the animation
	 * @param currentRow
	 *            the index of the selected animation
	 * @param numberOfRows
	 *            number of animations
	 * @param animationFrames
	 *            Bitmap holding the frames of the animation
	 * @param frameCount
	 *            Number of horizontal frames in the animation
	 * @param rotX
	 *            x location the animation rotates about
	 * @param rotY
	 *            y location the animation rotates about
	 * @param gameScreen
	 *            Gamescreen to which this animation belongs (assumed to be of
	 *            equal width)
	 */
	public Animation(float x, float y, int frameWidth, int frameHeight,
			float scale, int currentRow, int numberOfRows,
			Bitmap animationFrames, int frameCount, int rotX, int rotY,
			GameScreen gameScreen) {
		super(x, y, gameScreen);
		this.setAnimationFrames(animationFrames);
		this.frameCount = frameCount;
		this.currentRow = currentRow;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.scale = scale;

		mBound = new BoundingBox(x, y, frameWidth * scale / 2.0f, frameHeight
				* scale / 2.0f);
		// sets initial frame
		mBitmap = Bitmap.createBitmap(animationFrames, 0, currentRow
				* frameHeight, frameWidth, frameHeight);
		rotationPosition = new Point(rotX, rotY);
	}

	/**
	 * Create a new animation
	 * 
	 * @param x
	 *            Centre x location of the animation
	 * @param y
	 *            Centre y location of the animation
	 * @param frameWidth
	 *            Width of the animation
	 * @param frameHeight
	 *            Height of the animation
	 * @param scale
	 *            of the animation
	 * @param numberOfRows
	 *            number of animations
	 * @param animationFrames
	 *            Bitmap holding the frames of the animation
	 * @param rotX
	 *            x location the animation rotates about
	 * @param rotY
	 *            y location the animation rotates about
	 * @param gameScreen
	 *            Gamescreen to which this animation belongs (assumed to be of
	 *            equal width)
	 */
	public Animation(float x, float y, int frameWidth, int frameHeight,
			float scale, int numberOfRows, Bitmap animationFrames, int rotX,
			int rotY, GameScreen gameScreen) {
		super(x, y, gameScreen);
		this.setAnimationFrames(animationFrames);
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.scale = scale;

		mBound = new BoundingBox(x, y, frameWidth * scale / 2.0f, frameHeight
				* scale / 2.0f);
		// sets initial frame
		mBitmap = Bitmap.createBitmap(animationFrames, 0, currentRow
				* frameHeight, frameWidth, frameHeight);
		rotationPosition = new Point(rotX, rotY);
	}

	/**
	 * Trigger playback of this animation
	 * 
	 * @param animationPeriod
	 *            Period over which the animation should play
	 * @param loop
	 *            True if the animation should play repeatedly
	 */
	public void play(double animationPeriod, boolean loop) {
		framePeriod = animationPeriod / (double) frameCount;
		currentFrame = -1;
		isLooping = loop;

		isPlaying = true;
	}

	/**
	 * Set the time the animation will last for
	 * 
	 * @param animationPeriod
	 *            The time which the animation lasts
	 */
	public void setAnimationPeriod(double animationPeriod) {
		this.framePeriod = animationPeriod / (double) frameCount;
	}

	/**
	 * Update which frame of the animation should be displayed
	 * 
	 * @param elapsedTime
	 *            Elapsed time since the last update
	 */
	@Override
	public void update(ElapsedTime elapsedTime) {
		if (!isPlaying)
			return;

		// If this is the first time update has been called since the
		// play method was called then reset the current frame and timer
		if (currentFrame == -1) {
			currentFrame = 0;
			frameTimer = 0.0;

		}

		// Update the amount of time accumulated against this frame
		frameTimer += elapsedTime.stepTime;

		// If the frame display duration has been exceeded then try to
		// go on to the next frame, looping or stopping if the end of
		// the animation has been reached.
		if (frameTimer >= framePeriod) {
			if (moveRight)
				currentFrame++;
			else
				currentFrame--;

			if (currentFrame >= frameCount) {
				if (isLooping) {
					if (loopBackwards) {
						moveRight = false;
						currentFrame--;
					} else {
						currentFrame = 0;
					}
				} else {
					currentFrame = frameCount - 1;
					isPlaying = false;
				}
			} else if (currentFrame == 0) {
				moveRight = true;
			}
			setsCurrentBitmap();

			// 'Reset' the frame timer
			frameTimer -= framePeriod;
		}
		super.update(elapsedTime);
	}

	/**
	 * Sets new frame bitmap
	 */
	public void setsCurrentBitmap() {
		// sets new frame bitmap
		mBitmap = Bitmap
				.createBitmap(getAnimationFrames(), currentFrame * frameWidth,
						currentRow * frameHeight, frameWidth, frameHeight);
	}

	/**
	 * Determines whether the animation is looping
	 * 
	 * @return true if animation is looping, otherwise false
	 */
	public boolean isLooping() {
		return isLooping;
	}

	/**
	 * Determines whether the animation is playing
	 * 
	 * @return true if animation is playing, otherwise false
	 */
	public boolean isPlaying() {
		return isPlaying;
	}

	public Bitmap getAnimationFrames() {
		return animationFrames;
	}

	public void setAnimationFrames(Bitmap animationFrames) {
		this.animationFrames = animationFrames;
	}
}