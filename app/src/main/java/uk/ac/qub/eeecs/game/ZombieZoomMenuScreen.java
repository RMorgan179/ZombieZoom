package uk.ac.qub.eeecs.game;

import java.util.List;

import uk.ac.qub.eeecs.zombiezoom.Game;
import uk.ac.qub.eeecs.zombiezoom.engine.AssetStore;
import uk.ac.qub.eeecs.zombiezoom.engine.ElapsedTime;
import uk.ac.qub.eeecs.zombiezoom.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.zombiezoom.engine.input.Input;
import uk.ac.qub.eeecs.zombiezoom.engine.input.TouchEvent;
import uk.ac.qub.eeecs.zombiezoom.world.GameScreen;
import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Define a very basic menu screen with a single touch area
 * 
 * @version 1.0
 */
public class ZombieZoomMenuScreen extends GameScreen {

	/**
	 * Define the trigger touch region for playing the 'game'
	 */
	private Rect mPlayGameBound;
	private Rect mBackgroundBound;
	private Rect mOptionsBound;
	private Rect mUpgradesBound;
	private Rect mLeaderboardsBound;

	private Input input;
	private AssetStore assetManager;
	private Bitmap playGame, background, options, upgrades, leaderboards;
	private final float BUTTON_SCALE = 0.8f;

	/**
	 * Create a simple menu screen
	 * 
	 * @param game
	 *            Game to which this screen belongs
	 */
	public ZombieZoomMenuScreen(Game game) {
		super("ZombieZoomMenuScreen", game);

		// Load in the bitmap used on the menu screen
		assetManager = mGame.getAssetManager();
		assetManager.loadAndAddBitmap("MenuBackground",
				"img/ZombieZoomMenuBackground.png");
		assetManager.loadAndAddBitmap("PlayGame", "img/PlayGameButton.png");
		assetManager.loadAndAddBitmap("Options", "img/OptionsButton.png");
		assetManager.loadAndAddBitmap("Upgrades", "img/UpgradesButton.png");
		assetManager.loadAndAddBitmap("Leaderboards",
				"img/LeaderboardButton.png");

		playGame = assetManager.getBitmap("PlayGame");
		background = assetManager.getBitmap("MenuBackground");
		options = assetManager.getBitmap("Options");
		upgrades = assetManager.getBitmap("Upgrades");
		leaderboards = assetManager.getBitmap("Leaderboards");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.eeecs.gage.world.GameScreen#update(uk.ac.qub.eeecs.gage.engine
	 * .ElapsedTime)
	 */
	@Override
	public void update(ElapsedTime elapsedTime) {

		// Proess any touch events occurring since the update
		input = mGame.getInput();

		List<TouchEvent> touchEvents = input.getTouchEvents();
		if ((touchEvents.size() > 0)
				&& (touchEvents.get(0).type == TouchEvent.TOUCH_DOWN)) {
			// Just check the first touch event
			TouchEvent touchEvent = touchEvents.get(0);
			if (mPlayGameBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
				// If the play game area has been touched then swap screens
				mGame.getScreenManager().removeScreen(this.getName());
				ZombieZoomGameScreen zombieZoomGameScreen = new ZombieZoomGameScreen(
						this.mGame);
				// As it's the only added screen it will become active.
				mGame.getScreenManager().addScreen(zombieZoomGameScreen);
			}
			// need to change this to options menu screen when created
			if (mOptionsBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
				// If the play game area has been touched then swap screens
				mGame.getScreenManager().removeScreen(this.getName());
				ZombieZoomOptionsScreen zombieZoomOptionsScreen = new ZombieZoomOptionsScreen(
						this.mGame);
				// As it's the only added screen it will become active.
				mGame.getScreenManager().addScreen(zombieZoomOptionsScreen);
			}
			if (mUpgradesBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
				// If the play game area has been touched then swap screens
				mGame.getScreenManager().removeScreen(this.getName());
				ZombieZoomUpgradesScreen zombieZoomUpgradesScreen = new ZombieZoomUpgradesScreen(
						this.mGame);
				// As it's the only added screen it will become active.
				mGame.getScreenManager().addScreen(zombieZoomUpgradesScreen);
			}
			if (mLeaderboardsBound.contains((int) touchEvent.x,
					(int) touchEvent.y)) {
				// If the play game area has been touched then swap screens
				mGame.getScreenManager().removeScreen(this.getName());
				ZombieZoomLeaderboardScreen zombieZoomLeaderboardScreen = new ZombieZoomLeaderboardScreen(
						this.mGame);
				// As it's the only added screen it will become active.
				mGame.getScreenManager().addScreen(zombieZoomLeaderboardScreen);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.eeecs.gage.world.GameScreen#draw(uk.ac.qub.eeecs.gage.engine
	 * .ElapsedTime, uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D)
	 */
	@Override
	public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

		if (mBackgroundBound == null && background != null) {
			int left = 0;
			int top = 0;
			int bottom = graphics2D.getSurfaceHeight();
			int right = graphics2D.getSurfaceWidth();
			mBackgroundBound = new Rect(left, top, right, bottom);

		}
		graphics2D.drawBitmap(assetManager.getBitmap("MenuBackground"), null,
				mBackgroundBound, null);

		if (mPlayGameBound == null && playGame != null) {
			float left = (graphics2D.getSurfaceWidth() - (playGame.getWidth() * BUTTON_SCALE)) / 2;
			float top = graphics2D.getSurfaceHeight() / 3;
			float bottom = top + (playGame.getHeight() * BUTTON_SCALE);
			float right = left + (playGame.getWidth() * BUTTON_SCALE);
			mPlayGameBound = new Rect((int) left, (int) top, (int) right,
					(int) bottom);
		}

		graphics2D.drawBitmap(assetManager.getBitmap("PlayGame"), null,
				mPlayGameBound, null);

		if (mOptionsBound == null && options != null) {
			float left = mPlayGameBound.left;
			float top = mPlayGameBound.top + (160 * BUTTON_SCALE);
			float bottom = top + (options.getHeight() * BUTTON_SCALE);
			float right = left + (options.getWidth() * BUTTON_SCALE);
			mOptionsBound = new Rect((int) left, (int) top, (int) right,
					(int) bottom);
		}

		graphics2D.drawBitmap(assetManager.getBitmap("Options"), null,
				mOptionsBound, null);

		if (mUpgradesBound == null && options != null) {
			float left = mOptionsBound.left;
			float top = mOptionsBound.top + (160 * BUTTON_SCALE);
			float bottom = top + (upgrades.getHeight() * BUTTON_SCALE);
			float right = left + (upgrades.getWidth() * BUTTON_SCALE);
			mUpgradesBound = new Rect((int) left, (int) top, (int) right,
					(int) bottom);
		}

		graphics2D.drawBitmap(assetManager.getBitmap("Upgrades"), null,
				mUpgradesBound, null);

		if (mLeaderboardsBound == null && options != null) {
			float left = mUpgradesBound.left;
			float top = mUpgradesBound.top + (160 * BUTTON_SCALE);
			float bottom = top + (leaderboards.getHeight() * BUTTON_SCALE);
			float right = left + (leaderboards.getWidth() * BUTTON_SCALE);
			mLeaderboardsBound = new Rect((int) left, (int) top, (int) right,
					(int) bottom);
		}

		graphics2D.drawBitmap(assetManager.getBitmap("Leaderboards"), null,
				mLeaderboardsBound, null);
	}
}
