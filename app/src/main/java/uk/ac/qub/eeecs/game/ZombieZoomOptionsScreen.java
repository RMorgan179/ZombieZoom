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
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.widget.Toast;

public class ZombieZoomOptionsScreen extends GameScreen {

	/**
	 * Define the trigger touch region for playing the 'game'
	 */
	private Rect mBackgroundBound;
	private Rect mGameTitleBound;
	private Rect mBackBound;
	private Rect mPlaySound;
	private Rect mDeleteBound;
	private Rect mAspectRatio169;
	private Rect mAspectRatio32;

	private Input input;
	private AssetStore assetManager;
	private Bitmap background, gameTitle, back, musicOff, deleteHighScores,
			aspectRatio169, aspectRatio32;

	private boolean musicPlaying;

	private Paint paint = new Paint();

	private Leaderboard mLeaderboard;
	private boolean showToast = false;
	private boolean cancelToast = false;
	private String toastText;

	/**
	 * Create a simple leaderboard screen
	 * 
	 * @param game
	 *            Game to which this screen belongs
	 */
	public ZombieZoomOptionsScreen(Game game) {
		super("ZombieZoomOptionsscreen", game);

		musicPlaying = ((ZombieZoomGame) mGame).getMusic().getAllowedToPlay();
		mLeaderboard = new Leaderboard(game);
		paint.setColor(Color.WHITE);
		paint.setTextSize(40.0f);

		assetManager = mGame.getAssetManager();
		assetManager
				.loadAndAddBitmap("OptionsBackground", "img/Background.png");
		assetManager.loadAndAddBitmap("OptionsTitle", "img/OptionsButton.png");
		assetManager.loadAndAddBitmap("BackButton", "img/BackButton.png");
		assetManager.loadAndAddBitmap("MusicOn", "img/SoundOn.png");
		assetManager.loadAndAddBitmap("MusicOff", "img/SoundOff.png");
		assetManager.loadAndAddBitmap("DeleteIcon", "img/DeleteIcon.png");
		assetManager.loadAndAddBitmap("AspectRatio16:9",
				"img/AspectRatio169.png");
		assetManager
				.loadAndAddBitmap("AspectRatio3:2", "img/AspectRatio32.png");

		background = assetManager.getBitmap("OptionsBackground");
		gameTitle = assetManager.getBitmap("OptionsTitle");
		back = assetManager.getBitmap("BackButton");
		musicOff = assetManager.getBitmap("MusicOff");
		deleteHighScores = assetManager.getBitmap("DeleteIcon");
		aspectRatio169 = assetManager.getBitmap("AspectRatio16:9");
		aspectRatio32 = assetManager.getBitmap("AspectRatio3:2");

	}

	/**
	 * Method which changes the flag on each sound allowing it to play
	 * 
	 * @param value
	 *            Value deciding whether sound can be played or not
	 */
	private void setAllowedToPlay(boolean value) {
		((ZombieZoomGame) mGame).getMusic().setAllowedToPlay(value);
		mGame.getAssetManager().getSound("Pistol").setAllowedToPlay(value);
		mGame.getAssetManager().getSound("Shotgun").setAllowedToPlay(value);
		mGame.getAssetManager().getSound("Acquire Shotgun")
				.setAllowedToPlay(value);
		mGame.getAssetManager().getSound("CoinSFX").setAllowedToPlay(value);
		mGame.getAssetManager().getSound("HealthSFX").setAllowedToPlay(value);
		mGame.getAssetManager().getSound("ZombieGroan").setAllowedToPlay(value);
		mGame.getAssetManager().getSound("HealthSFX").setAllowedToPlay(value);
		mGame.getAssetManager().getSound("BombSFX").setAllowedToPlay(value);
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

			if (mPlaySound.contains((int) touchEvent.x, (int) touchEvent.y)) {
				try {
					if (musicPlaying == true) {
						if (toastText != null) {
							cancelToast = true;
						}
						musicPlaying = false;
						setAllowedToPlay(false);
						((ZombieZoomGame) mGame).getMusic().pause();
						toastText = "Sound is turned off";
						showToast = true;
					} else if (musicPlaying == false) {
						if (toastText != null) {
							cancelToast = true;
						}
						musicPlaying = true;
						setAllowedToPlay(true);
						mGame.getMusic().play();
						toastText = "Sound is turned on";
						showToast = true;
					}
				} catch (Exception e) {
					Log.e("ZZ", "Turn off music not work because " + e);
				}
			}

			if (mDeleteBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
				try {
					if (toastText != null) {
						cancelToast = true;
					}
					mLeaderboard.reset();
					toastText = "All high scores have been deleted";
					showToast = true;
				} catch (Exception e) {
					Log.e("ZZ", "Delete high scores was not successful " + e);
				}
			}

			if (mAspectRatio169
					.contains((int) touchEvent.x, (int) touchEvent.y)) {
				try {

					if (toastText != null) {
						cancelToast = true;
					}
					((ZombieZoomGame) mGame).setAspectRatio((float) 16 / 9);
					toastText = "Game is now running in 16:9.";
					showToast = true;
				} catch (Exception e) {
					Log.e("ZZ", "Game failed to run in 16:9" + e);
				}
			}
			if (mAspectRatio32.contains((int) touchEvent.x, (int) touchEvent.y)) {
				try {

					if (toastText != null) {
						cancelToast = true;
					}
					((ZombieZoomGame) mGame).setAspectRatio((float) 3 / 2);
					toastText = "Game is now running in 3:2.";
					showToast = true;
				} catch (Exception e) {
					Log.e("ZZ", "Game failed to run in 3:2" + e);
				}
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
		graphics2d.drawBitmap(assetManager.getBitmap("OptionsBackground"),
				null, mBackgroundBound, null);

		if (mGameTitleBound == null && gameTitle != null) {
			int left = (graphics2d.getSurfaceWidth() - gameTitle.getWidth()) / 2;
			int top = (graphics2d.getSurfaceHeight()) / 10;
			int bottom = top + gameTitle.getHeight();
			int right = left + gameTitle.getWidth();
			mGameTitleBound = new Rect(left, top, right, bottom);
		}
		graphics2d.drawBitmap(assetManager.getBitmap("OptionsTitle"), null,
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

		if (mPlaySound == null && musicOff != null) {
			int left = (graphics2d.getSurfaceWidth() - musicOff.getWidth()) / 4;
			int top = (graphics2d.getSurfaceHeight()) / 8 * 3;
			int bottom = top + musicOff.getHeight();
			int right = left + musicOff.getWidth();
			mPlaySound = new Rect(left, top, right, bottom);
		}

		if (musicPlaying == true) {
			graphics2d.drawBitmap(assetManager.getBitmap("MusicOn"), null,
					mPlaySound, null);
		} else {
			graphics2d.drawBitmap(assetManager.getBitmap("MusicOff"), null,
					mPlaySound, null);
		}

		graphics2d.drawText("Sound On/Off: ",
				graphics2d.getSurfaceWidth() / 25 * 4,
				graphics2d.getSurfaceHeight() / 8 * 3, paint);

		if (mAspectRatio169 == null && aspectRatio169 != null) {
			int left = (graphics2d.getSurfaceWidth() - aspectRatio169
					.getWidth()) / 25 * 5;
			int top = (graphics2d.getSurfaceHeight()) / 6 * 4;
			int bottom = top + aspectRatio169.getHeight();
			int right = left + aspectRatio169.getWidth();
			mAspectRatio169 = new Rect(left, top, right, bottom);
		}
		graphics2d.drawBitmap(assetManager.getBitmap("AspectRatio16:9"), null,
				mAspectRatio169, null);

		if (mAspectRatio32 == null && aspectRatio32 != null) {
			int left = (graphics2d.getSurfaceWidth() - aspectRatio32.getWidth()) / 10 * 5;
			int top = (graphics2d.getSurfaceHeight()) / 6 * 4;
			int bottom = top + aspectRatio32.getHeight();
			int right = left + aspectRatio32.getWidth();
			mAspectRatio32 = new Rect(left, top, right, bottom);
		}
		graphics2d.drawBitmap(assetManager.getBitmap("AspectRatio3:2"), null,
				mAspectRatio32, null);

		if (mDeleteBound == null && deleteHighScores != null) {
			int left = (graphics2d.getSurfaceWidth() - deleteHighScores
					.getWidth()) / 3 * 2;
			int top = (graphics2d.getSurfaceHeight()) / 8 * 3;
			int bottom = top + deleteHighScores.getHeight();
			int right = left + deleteHighScores.getWidth();
			mDeleteBound = new Rect(left, top, right, bottom);
		}

		graphics2d.drawBitmap(assetManager.getBitmap("DeleteIcon"), null,
				mDeleteBound, null);

		graphics2d.drawText("Aspect Ratios: (Changes game screen)",
				graphics2d.getSurfaceWidth() / 25 * 4,
				graphics2d.getSurfaceHeight() / 8 * 5, paint);

		graphics2d.drawText("Delete High Scores?",
				graphics2d.getSurfaceWidth() / 25 * 12,
				graphics2d.getSurfaceHeight() / 8 * 3, paint);

		if (showToast == true) {
			showToast = false;
			Toast.makeText(mGame.getActivity().getApplicationContext(),
					toastText, Toast.LENGTH_SHORT).show();
		}

		if (cancelToast == true) {
			cancelToast = false;
			Toast.makeText(mGame.getActivity().getApplicationContext(),
					toastText, Toast.LENGTH_SHORT).cancel();
		}
	}
}
