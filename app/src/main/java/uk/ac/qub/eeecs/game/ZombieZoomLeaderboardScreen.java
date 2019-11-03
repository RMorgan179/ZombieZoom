package uk.ac.qub.eeecs.game;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import uk.ac.qub.eeecs.zombiezoom.Game;
import uk.ac.qub.eeecs.zombiezoom.engine.AssetStore;
import uk.ac.qub.eeecs.zombiezoom.engine.ElapsedTime;
import uk.ac.qub.eeecs.zombiezoom.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.zombiezoom.engine.input.Input;
import uk.ac.qub.eeecs.zombiezoom.engine.input.TouchEvent;
import uk.ac.qub.eeecs.zombiezoom.world.GameScreen;
import uk.ac.qub.eeecs.zombiezoom.world.ScreenViewport;

public class ZombieZoomLeaderboardScreen extends GameScreen {

	/**
	 * Define the trigger touch region for playing the 'game'
	 */
	private Rect mBackgroundBound;
	private Rect mGameTitleBound;
	private Rect mBackBound;

	private Input input;
	private AssetStore assetManager;
	private Bitmap background, gameTitle, back;

	private ScreenViewport mScreenViewport;

	private Paint paint = new Paint();

	private Leaderboard mLeaderboard;
	private String[][] scores;

	/**
	 * Create a simple leaderboard screen
	 * 
	 * @param game
	 *            Game to which this screen belongs
	 */
	public ZombieZoomLeaderboardScreen(Game game) {
		super("ZombieZoomLeaderboardScreen", game);

		paint.setColor(Color.WHITE);
		paint.setTextSize(40.0f);

		// Create the screen viewport, size it to provide a 3:2 aspect
		float aspectRatio = (float) mGame.getScreenWidth()
				/ (float) mGame.getScreenHeight();

		if (aspectRatio > 1.5f) { // 16:10/16:9
			// Create the screen viewport
			mScreenViewport = new ScreenViewport(0, 0, mGame.getScreenWidth(),
					mGame.getScreenHeight());
		} else { // 4:3
			// Create the screen viewport
			mScreenViewport = new ScreenViewport(0, 0, mGame.getScreenWidth(),
					mGame.getScreenHeight());
		}

		mLeaderboard = new Leaderboard(mGame);
		scores = mLeaderboard.getScores();

		assetManager = mGame.getAssetManager();
		assetManager.loadAndAddBitmap("LeaderboardBackground",
				"img/Background.png");
		assetManager.loadAndAddBitmap("LeaderboardTitle",
				"img/LeaderboardButton.png");
		assetManager.loadAndAddBitmap("BackButton", "img/BackButton.png");
		background = assetManager.getBitmap("LeaderboardBackground");
		gameTitle = assetManager.getBitmap("LeaderboardTitle");
		back = assetManager.getBitmap("BackButton");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.eeecs.zombiezoom.world.GameScreen#update(uk.ac.qub.eeecs.zombiezoom
	 * .engine.ElapsedTime)
	 */
	@Override
	public void update(ElapsedTime elapsedTime) {
		// process any touch events occuring since the update
		input = mGame.getInput();

		List<TouchEvent> touchEvents = input.getTouchEvents();
		if ((touchEvents.size() > 0)
				&& (touchEvents.get(0).type == TouchEvent.TOUCH_DOWN)) {
			// Just check the first touch event
			TouchEvent touchEvent = touchEvents.get(0);
			if (mBackBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
				// If the play game area has been touched then swap screens
				mGame.getScreenManager().removeScreen(this.getName());
				ZombieZoomMenuScreen zombieZoomMenuScreen = new ZombieZoomMenuScreen(
						this.mGame);
				// As it's the only added screen it will become active.
				mGame.getScreenManager().addScreen(zombieZoomMenuScreen);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.qub.eeecs.zombiezoom.world.GameScreen#draw(uk.ac.qub.eeecs.zombiezoom
	 * .engine.ElapsedTime,
	 * uk.ac.qub.eeecs.zombiezoom.engine.graphics.IGraphics2D)
	 */
	@Override
	public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2d) {
		if (mBackgroundBound == null && background != null) {
			int left = 0;
			int top = 0;
			int bottom = graphics2d.getSurfaceHeight();
			int right = graphics2d.getSurfaceWidth();
			mBackgroundBound = new Rect(left, top, right, bottom);
		}
		graphics2d.drawBitmap(assetManager.getBitmap("LeaderboardBackground"),
				null, mBackgroundBound, null);

		if (mGameTitleBound == null && gameTitle != null) {
			int left = (graphics2d.getSurfaceWidth() - gameTitle.getWidth()) / 2;
			int top = (graphics2d.getSurfaceHeight()) / 10;
			int bottom = top + gameTitle.getHeight();
			int right = left + gameTitle.getWidth();
			mGameTitleBound = new Rect(left, top, right, bottom);
		}
		graphics2d.drawBitmap(assetManager.getBitmap("LeaderboardTitle"), null,
				mGameTitleBound, null);

		if (mBackBound == null && back != null) {
			int left = 0;
			int top = graphics2d.getSurfaceHeight() - back.getHeight();
			int bottom = graphics2d.getSurfaceHeight();
			int right = back.getWidth();
			mBackBound = new Rect(left, top, right, bottom);
		}
		graphics2d.drawBitmap(assetManager.getBitmap("BackButton"), null,
				mBackBound, null);

		for (int scoreIdx = 0; scoreIdx < scores.length; scoreIdx++) {
			String temp = "Top score " + (scoreIdx + 1) + " = "
					+ scores[scoreIdx][1] + " (" + scores[scoreIdx][0] + ")";
			int initialValue = ((mScreenViewport.height - mGameTitleBound.bottom) / 8)
					+ mGameTitleBound.bottom + 40;
			int nextValue = ((mScreenViewport.height - mGameTitleBound.bottom) / 8);
			graphics2d.drawText(temp,
					mScreenViewport.width / 2 - (temp.length() * 10),
					initialValue + (scoreIdx * nextValue), paint);
		}

	}
}
