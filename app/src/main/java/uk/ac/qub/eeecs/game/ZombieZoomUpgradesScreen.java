package uk.ac.qub.eeecs.game;

import java.util.List;

import uk.ac.qub.eeecs.zombiezoom.Game;
import uk.ac.qub.eeecs.zombiezoom.engine.AssetStore;
import uk.ac.qub.eeecs.zombiezoom.engine.ElapsedTime;
import uk.ac.qub.eeecs.zombiezoom.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.zombiezoom.engine.input.Input;
import uk.ac.qub.eeecs.zombiezoom.engine.input.TouchEvent;
import uk.ac.qub.eeecs.zombiezoom.engine.io.Button;
import uk.ac.qub.eeecs.zombiezoom.world.GameScreen;
import uk.ac.qub.eeecs.zombiezoom.world.ScreenViewport;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
//import android.graphics.Color;
//import android.graphics.Paint;

public class ZombieZoomUpgradesScreen extends GameScreen {
	// real values
	// public final int COST_OF_GOD_MODE = 50;
	// public final int COST_OF_COIN_HOARDER_MODE = 25;
	// public final int COST_OF_BULLET_MODE = 40;

	// test values
	public final int COST_OF_GOD_MODE = 10;
	public final int COST_OF_COIN_HOARDER_MODE = 8;
	public final int COST_OF_BULLET_MODE = 5;

	/**
	 * Define the trigger touch region for playing the 'game'
	 */
	private Rect mBackgroundBound;
	private Rect mGameTitleBound;
	private Rect mBackBound;

	private Button mGodButton;
	private Button mCoinHoarderButton;
	private Button mBulletButton;

	private Input input;
	private AssetStore assetManager;
	private Bitmap background, gameTitle, backIcon;

	private ScreenViewport mScreenViewport;

	private Purse mPurse;
	private int coins;

	private Paint paint = new Paint();

	/**
	 * Create a simple upgrade screen
	 * 
	 * @param game
	 *            Game to which this screen belongs
	 */
	public ZombieZoomUpgradesScreen(Game game) {
		super("ZombieZoomUpgradescreen", game);

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

		assetManager = mGame.getAssetManager();
		assetManager.loadAndAddBitmap("UpgradesBackground",
				"img/Background.png");
		assetManager
				.loadAndAddBitmap("UpgradesTitle", "img/UpgradesButton.png");
		assetManager.loadAndAddBitmap("BackButton", "img/BackButton.png");
		assetManager.loadAndAddBitmap("GodButton", "img/GodMode.png");
		assetManager.loadAndAddBitmap("CoinHoarderButton",
				"img/CoinHoarderMode.png");
		assetManager.loadAndAddBitmap("BulletButton", "img/BulletMode.png");

		background = assetManager.getBitmap("UpgradesBackground");
		gameTitle = assetManager.getBitmap("UpgradesTitle");
		backIcon = assetManager.getBitmap("BackButton");

		mGodButton = new Button((mScreenViewport.width / 2) - 350,
				(mScreenViewport.height / 2), 150, 200, "GodButton", this);
		mCoinHoarderButton = new Button((mScreenViewport.width / 2),
				(mScreenViewport.height / 2), 150, 200, "CoinHoarderButton",
				this);
		mBulletButton = new Button((mScreenViewport.width / 2) + 350,
				(mScreenViewport.height / 2), 150, 200, "BulletButton", this);

		mPurse = new Purse(game);
		coins = mPurse.getPurseTotal();
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
			if (touchEvent.type == TouchEvent.TOUCH_DOWN) {
				if (mBackBound.contains((int) touchEvent.x, (int) touchEvent.y)) {
					mPurse.setPurseTotal(coins);
					// If the play game area has been touched then swap screens
					mGame.getScreenManager().removeScreen(this.getName());
					ZombieZoomMenuScreen zombieZoomMenuScreen = new ZombieZoomMenuScreen(
							this.mGame);
					// As it's the only added screen it will become active.
					mGame.getScreenManager().addScreen(zombieZoomMenuScreen);
				}

				if (mGodButton.isActivated()) {
					if (((ZombieZoomGame) mGame).mGodModeEnabled == false) {
						if (coins >= COST_OF_GOD_MODE) {
							coins = coins - COST_OF_GOD_MODE;
							((ZombieZoomGame) mGame).mGodModeEnabled = true;
						}
					} else {
						coins = coins + COST_OF_GOD_MODE;
						((ZombieZoomGame) mGame).mGodModeEnabled = false;
					}
				}

				if (mCoinHoarderButton.isActivated()) {
					if (((ZombieZoomGame) mGame).mCoinHoarderModeEnabled == false) {
						if (coins >= COST_OF_COIN_HOARDER_MODE) {
							coins = coins - COST_OF_COIN_HOARDER_MODE;
							((ZombieZoomGame) mGame).mCoinHoarderModeEnabled = true;
						}
					} else {
						coins = coins + COST_OF_COIN_HOARDER_MODE;
						((ZombieZoomGame) mGame).mCoinHoarderModeEnabled = false;
					}
				}

				if (mBulletButton.isActivated()) {
					if (((ZombieZoomGame) mGame).mBulletModeEnabled == false) {
						if (coins >= COST_OF_BULLET_MODE) {
							coins = coins - COST_OF_BULLET_MODE;
							((ZombieZoomGame) mGame).mBulletModeEnabled = true;
						}
					} else {
						coins = coins + COST_OF_BULLET_MODE;
						((ZombieZoomGame) mGame).mBulletModeEnabled = false;
					}
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
		graphics2d.drawBitmap(assetManager.getBitmap("UpgradesBackground"),
				null, mBackgroundBound, null);

		if (mGameTitleBound == null && gameTitle != null) {
			int left = (graphics2d.getSurfaceWidth() - gameTitle.getWidth()) / 2;
			int top = (graphics2d.getSurfaceHeight()) / 10;
			int bottom = top + gameTitle.getHeight();
			int right = left + gameTitle.getWidth();
			mGameTitleBound = new Rect(left, top, right, bottom);
		}
		graphics2d.drawBitmap(assetManager.getBitmap("UpgradesTitle"), null,
				mGameTitleBound, null);

		if (mBackBound == null && backIcon != null) {
			int left = 0;
			int top = graphics2d.getSurfaceHeight() - backIcon.getHeight();
			int bottom = graphics2d.getSurfaceHeight();
			int right = backIcon.getWidth();
			mBackBound = new Rect(left, top, right, bottom);

		}
		graphics2d.drawBitmap(assetManager.getBitmap("BackButton"), null,
				mBackBound, null);

		graphics2d.drawText("Coin Amount: " + coins,
				graphics2d.getSurfaceWidth() / 25 * 5,
				graphics2d.getSurfaceHeight() / 3, paint);

		mGodButton.draw(elapsedTime, graphics2d, null, mScreenViewport);
		mCoinHoarderButton.draw(elapsedTime, graphics2d, null, mScreenViewport);
		mBulletButton.draw(elapsedTime, graphics2d, null, mScreenViewport);

		graphics2d.drawText("GOD MODE", mGodButton.getBound().getLeft() - 35,
				mGodButton.getBound().getBottom() + 50, paint);
		graphics2d
				.drawText(COST_OF_GOD_MODE + " ", mGodButton.getBound()
						.getLeft() - 35,
						mGodButton.getBound().getBottom() + 100, paint);

		graphics2d.drawText("COIN HOARDER ", mCoinHoarderButton.getBound()
				.getLeft() - 35,
				mCoinHoarderButton.getBound().getBottom() + 50, paint);
		graphics2d.drawText(COST_OF_COIN_HOARDER_MODE + " ", mCoinHoarderButton
				.getBound().getLeft() - 35, mCoinHoarderButton.getBound()
				.getBottom() + 100, paint);

		graphics2d.drawText("SUPER GUN",
				mBulletButton.getBound().getLeft() - 35, mBulletButton
						.getBound().getBottom() + 50, paint);
		graphics2d.drawText(COST_OF_BULLET_MODE + "", mBulletButton.getBound()
				.getLeft() - 35, mBulletButton.getBound().getBottom() + 100,
				paint);

		paint.setColor(Color.GREEN);
		if (((ZombieZoomGame) mGame).mGodModeEnabled) {
			graphics2d.drawText("[ENABLED]",
					mGodButton.getBound().getLeft() - 25, mGodButton.getBound()
							.getBottom() + 160, paint);
		}
		if (((ZombieZoomGame) mGame).mCoinHoarderModeEnabled) {
			graphics2d.drawText("[ENABLED]", mCoinHoarderButton.getBound()
					.getLeft() - 25, mGodButton.getBound().getBottom() + 160,
					paint);
		}
		if (((ZombieZoomGame) mGame).mBulletModeEnabled) {
			graphics2d.drawText("[ENABLED]",
					mBulletButton.getBound().getLeft() - 25, mGodButton
							.getBound().getBottom() + 160, paint);
		}
		paint.setColor(Color.WHITE);
	}
}
