package uk.ac.qub.eeecs.zombiezoom.engine.input;

import java.util.List;

import android.content.Context;
import android.view.View;

/**
 * Touch, key, accelerometer and compass input support
 * 
 * @version 1.0
 */
public class Input {

	/**
	 * Define the different handlers that are responsible for managing the
	 * different types of input
	 */
	private TouchHandler mTouchHandler;

	/**
	 * Create a new input manager for the specified content view
	 * 
	 * @param context
	 *            Context within which this handler will operate
	 * @param view
	 *            View that this handler will collect input from
	 */
	public Input(Context context, View view) {
		mTouchHandler = new TouchHandler(view);
	}

	// /////////////////////////////////////////////////////////////////////////
	// Touch Input Events //
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Determine if there is an ongoing touch event for the specified pointer ID
	 * 
	 * @param pointerId
	 *            Touch pointer ID to test for
	 * @return true if there is an ongoing touch event, otherwise false
	 */
	public boolean existsTouch(int pointerId) {
		return mTouchHandler.existsTouch(pointerId);
	}

	/**
	 * Get the x-coordinate for the specified pointer ID.
	 * 
	 * A value of Float.NaN is returned if the pointer ID does not exist
	 * 
	 * @param pointerId
	 *            Touch pointer ID to retrieve
	 * @return x touch location
	 */
	public float getTouchX(int pointerId) {
		return mTouchHandler.getTouchX(pointerId);
	}

	/**
	 * Get the y-coordinate for the specified pointer ID.
	 * 
	 * A value of Float.NaN is returned if the pointer ID does not exist
	 * 
	 * @param pointerId
	 *            Touch pointer ID to retrieve
	 * @return y touch location
	 */
	public float getTouchY(int pointerId) {
		return mTouchHandler.getTouchY(pointerId);
	}

	/**
	 * Return a list of captured touch events occurring for this update tick.
	 * 
	 * @return List of captured touch events
	 */
	public List<TouchEvent> getTouchEvents() {
		return mTouchHandler.getTouchEvents();
	}

	/**
	 * Resets the accumulators
	 */
	public void resetAccumulators() {
		mTouchHandler.resetAccumulator();
	}
}
