package uk.ac.qub.eeecs.game;

import uk.ac.qub.eeecs.zombiezoom.Game;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Leaderboard {
	private Game mGame;

	/**
	 * Creates a leaderboard
	 * 
	 * @param game
	 *            Game to which the leaderboard belongs
	 */
	public Leaderboard(Game game) {
		mGame = game;
	}

	/**
	 * Gets the names and scores in the leaderboard
	 * 
	 * @return Array containing the names and scores
	 */
	public String[][] getScores() {
		loadTopScores();
		return mTopScores;
	}

	// ////////////////////////////////////
	// Top Scores
	// ////////////////////////////////////

	/**
	 * Number of top scores that will be stored
	 */
	private final int NUM_TOP_SCORES = 6;

	/**
	 * Array of top scores
	 */
	private String mTopScores[][] = new String[NUM_TOP_SCORES][2];

	/**
	 * Load the top scores from the SharedPreferences
	 */
	private void loadTopScores() {
		// Get the SharedPreferences for the app
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(mGame.getActivity()
						.getApplicationContext());

		// Retrieve the top scores
		for (int scoreIdx = 0; scoreIdx < NUM_TOP_SCORES; scoreIdx++) {
			String value = preferences
					.getString("Top" + (scoreIdx + 1), "" + 0);
			mTopScores[scoreIdx][0] = value;

			mTopScores[scoreIdx][1] = preferences.getString("Top"
					+ (scoreIdx + 1) + "1", "No Top Score");
		}
	}

	/**
	 * Saves a new highscore into the SharedPreferences
	 * 
	 * @param newScore
	 *            new score to be saved
	 * @param newName
	 *            name associated with the score
	 */
	public void savePoints(int newScore, String newName) {
		loadTopScores();

		int score = newScore, temp;
		String name = newName, tempName;
		for (int scoreIdx = 0; scoreIdx < NUM_TOP_SCORES; scoreIdx++) {
			int value = Integer.parseInt(mTopScores[scoreIdx][0]);
			if (score > value) {
				temp = Integer.parseInt(mTopScores[scoreIdx][0]);
				mTopScores[scoreIdx][0] = "" + score;
				score = temp;

				tempName = mTopScores[scoreIdx][1];
				mTopScores[scoreIdx][1] = name;
				name = tempName;
			}
		}

		// Get the shared preferences for the app
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(mGame.getActivity()
						.getApplicationContext());

		// Get an editor for updating the preferences
		Editor preferenceEditor = preferences.edit();

		// Store the top scores
		for (int scoreIdx = 0; scoreIdx < NUM_TOP_SCORES; scoreIdx++) {
			preferenceEditor.putString("Top" + (scoreIdx + 1),
					mTopScores[scoreIdx][0]);
			preferenceEditor.putString("Top" + (scoreIdx + 1) + "1",
					mTopScores[scoreIdx][1]);
		}

		preferenceEditor.commit();

	}

	/**
	 * Checks if a score is going to be in the leaderboard
	 * 
	 * @param newScore
	 *            score to be saved
	 * @return true if newScore is going to be in the leaderboard, otherwise
	 *         false
	 */
	public int promtName(int newScore) {
		loadTopScores();

		int score = newScore, temp;
		for (int scoreIdx = 0; scoreIdx < NUM_TOP_SCORES; scoreIdx++) {
			int value = Integer.parseInt(mTopScores[scoreIdx][0]);
			if (score > value) {
				temp = Integer.parseInt(mTopScores[scoreIdx][0]);
				mTopScores[scoreIdx][0] = "" + score;
				score = temp;
				return scoreIdx + 1;
			}
		}
		return -1;
	}

	/**
	 * Method which resets all top scores to be 0
	 */
	public void reset() {
		// Set all values equal to 0
		for (int scoreIdx = 0; scoreIdx < NUM_TOP_SCORES; scoreIdx++) {
			mTopScores[scoreIdx][0] = "" + 0;
			mTopScores[scoreIdx][1] = "No Top Score";
		}

		// Get the shared preferences for the app
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(mGame.getActivity()
						.getApplicationContext());

		// Get an editor for updating the preferences
		Editor preferenceEditor = preferences.edit();

		// Store the top scores
		for (int scoreIdx = 0; scoreIdx < NUM_TOP_SCORES; scoreIdx++) {
			preferenceEditor.putString("Top" + (scoreIdx + 1),
					mTopScores[scoreIdx][0]);
			preferenceEditor.putString("Top" + (scoreIdx + 1) + "1",
					mTopScores[scoreIdx][1]);
		}

		preferenceEditor.commit();
	}

}
