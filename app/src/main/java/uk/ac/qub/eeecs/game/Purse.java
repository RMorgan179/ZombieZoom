package uk.ac.qub.eeecs.game;

import uk.ac.qub.eeecs.zombiezoom.Game;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Purse {
	private int purseTotal;
	Game mGame;

	// /////////////////////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a new Purse
	 * 
	 * @param game
	 *            Game to which the purse belongs
	 */
	public Purse(Game game) {
		mGame = game;
		purseTotal = getPurseTotal();
	}

	/**
	 * Creates a new Purse
	 * 
	 * @param aTotal
	 *            Initial value for purse total
	 * @param game
	 *            Game to which the purse belongs
	 */
	public Purse(int aTotal, Game game) {
		purseTotal = aTotal;
		mGame = game;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Methods: Public
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Sets a new Purse Total and saves it
	 * 
	 * @param purseTotal
	 *            Value to be stored
	 */
	public void setPurseTotal(int purseTotal) {
		this.purseTotal = purseTotal;
		savePurse();
	}

	/**
	 * Adds a number of coins to Purse and saves it
	 * 
	 * @param aValue
	 *            Number of coins to be added
	 */
	public void addToPurse(int aValue) {
		loadPurse();
		purseTotal += aValue;
		savePurse();
	}

	/**
	 * Removes a number of coins from Purse and saves it
	 * 
	 * @param aValue
	 *            Number of coins to be removed
	 */
	public void removeFromPurse(int aValue) {
		loadPurse();
		purseTotal -= aValue;
		savePurse();
	}

	/**
	 * Gets the purse total
	 * 
	 * @return purseTotal
	 */
	public int getPurseTotal() {
		loadPurse();
		return purseTotal;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Properties: Private, Using SharedPreferences
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Loads the purse total from SharedPreferences
	 */
	private void loadPurse() {
		// Get the SharedPreferences for the app
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(mGame.getActivity()
						.getApplicationContext());

		purseTotal = preferences.getInt("purseTotal", 0);
	}

	/**
	 * Saves the purse total to SharedPreferences
	 */
	private void savePurse() {
		// Get the shared preferences for the app
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(mGame.getActivity()
						.getApplicationContext());

		// Get an editor for updating the preferences
		Editor preferenceEditor = preferences.edit();

		// Store the purse value
		preferenceEditor.putInt("purseTotal", purseTotal);

		preferenceEditor.commit();
	}
}
