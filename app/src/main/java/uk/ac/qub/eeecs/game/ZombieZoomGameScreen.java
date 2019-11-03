package uk.ac.qub.eeecs.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import uk.ac.qub.eeecs.game.sprites.BatSprite;
import uk.ac.qub.eeecs.game.sprites.BombSprite;
import uk.ac.qub.eeecs.game.sprites.BulletSprite;
import uk.ac.qub.eeecs.game.sprites.BulletSprite.BulletType;
import uk.ac.qub.eeecs.game.sprites.CoinSprite;
import uk.ac.qub.eeecs.game.sprites.CoinSprite.CoinType;
import uk.ac.qub.eeecs.game.sprites.Explosion;
import uk.ac.qub.eeecs.game.sprites.GodModeSprite;
import uk.ac.qub.eeecs.game.sprites.Gun.GunType;
import uk.ac.qub.eeecs.game.sprites.HealthPackSprite;
import uk.ac.qub.eeecs.game.sprites.PlatformSprite;
import uk.ac.qub.eeecs.game.sprites.PlayerSprite;
import uk.ac.qub.eeecs.game.sprites.PlayerSprite.PlayerState;
import uk.ac.qub.eeecs.game.sprites.PointsCounter;
import uk.ac.qub.eeecs.game.sprites.ZombieSprite;
import uk.ac.qub.eeecs.game.sprites.ZombieSprite.ZombieState;
import uk.ac.qub.eeecs.zombiezoom.Game;
import uk.ac.qub.eeecs.zombiezoom.engine.AssetStore;
import uk.ac.qub.eeecs.zombiezoom.engine.ElapsedTime;
import uk.ac.qub.eeecs.zombiezoom.engine.graphics.GraphicsHelper;
import uk.ac.qub.eeecs.zombiezoom.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.zombiezoom.engine.input.Input;
import uk.ac.qub.eeecs.zombiezoom.engine.input.TouchEvent;
import uk.ac.qub.eeecs.zombiezoom.engine.io.Button;
import uk.ac.qub.eeecs.zombiezoom.util.BoundingBox;
import uk.ac.qub.eeecs.zombiezoom.util.MoveableObjectsHelper;
import uk.ac.qub.eeecs.zombiezoom.world.GameScreen;
import uk.ac.qub.eeecs.zombiezoom.world.Gender;
import uk.ac.qub.eeecs.zombiezoom.world.LayerViewport;
import uk.ac.qub.eeecs.zombiezoom.world.Ribbon;
import uk.ac.qub.eeecs.zombiezoom.world.ScreenViewport;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.widget.EditText;

/**
 * Simple sample game world with some sprites
 * 
 * @version 1.0
 */
public class ZombieZoomGameScreen extends GameScreen {
	public enum GameState {
		Ready, Running, Paused, GameOver
	}

	GameState state = GameState.Ready;

	/**
	 * Define viewports for this layer and the associated screen projection
	 */
	private ScreenViewport mScreenViewport;
	private LayerViewport mLayerViewport;
	private float screenScale;
	private Ribbon background;
	private String name;

	/**
	 * Defines the initial x, y position of the player
	 */
	public final float PLAYERINITIALX = 50.0f;
	public final float PLAYERINITIALY = 33.0f;

	/**
	 * values which are needed to calculate score
	 */
	public final int ZOMBIE_VALUE = 10;
	public final int BAT_VALUE = 8;
	public final int COIN_VALUE = 5;

	/**
	 * Strength of gravity to apply along the y-axis
	 */
	public float GRAVITY = -800.0f;

	/**
	 * Defines the number of platforms which are generated every time created
	 * world is run
	 */
	public final int NUMOFPLATFORMS = 10;

	/**
	 * Define an array of sprites to populate the game world
	 */
	private ArrayList<PlatformSprite> mPlatforms = new ArrayList<PlatformSprite>();
	private ArrayList<CoinSprite> mCoins = new ArrayList<CoinSprite>();
	private ArrayList<BombSprite> mBombs = new ArrayList<BombSprite>();
	private ArrayList<BatSprite> mBats = new ArrayList<BatSprite>();
	private ArrayList<ZombieSprite> mZombies = new ArrayList<ZombieSprite>();
	private ArrayList<Explosion> mExplosion = new ArrayList<Explosion>();
	private ArrayList<PointsCounter> mPoints = new ArrayList<PointsCounter>();
	private ArrayList<HealthPackSprite> mPotion = new ArrayList<HealthPackSprite>();
	private ArrayList<BulletSprite> mVomitBullets = new ArrayList<BulletSprite>();
	private ArrayList<GodModeSprite> mPickUps = new ArrayList<GodModeSprite>();

	/**
	 * Gets the Array of Vomit Bullets
	 * 
	 * @return Array of Vomit Bullets
	 */
	public ArrayList<BulletSprite> getmVomitBullets() {
		return mVomitBullets;
	}

	/**
	 * Gets the player sprite
	 * 
	 * @return playerSprite
	 */
	public PlayerSprite getPlayerSprite() {
		return playerSprite;
	}

	/**
	 * Score values
	 */
	private int distance = 0;
	private int coinWallet = 0;
	private int zombieKillCount = 0;
	private int batKillCount = 0;

	private PlayerSprite playerSprite;
	private Button pauseButton, resumeButton, quitButton;

	/**
	 * Purse variables
	 */
	private Purse mPurse;
	private boolean coinsSaved = false;

	/**
	 * Leaderboard to save high scores
	 */
	private Leaderboard mLeaderboard;
	private boolean scoreSaved = false;

	/**
	 * Health Bar
	 */
	private Bitmap healthBarBackground;
	private Bitmap healthBarsBitmap;
	private Bitmap coinBitmap;
	private Bitmap backBitmap;
	private Rect healthBarBackgroundBound;
	private Rect healthBarsBound;
	private Rect coinRect;
	private Rect mBackBound;

	/**
	 * The controlLine is used as a control indicator, i.e. if you touch to the
	 * left of the control line you jump, where as if you touch to the right of
	 * the control line you rotate the gun
	 */
	private float controlLine;
	private Point previousTouchPoint = new Point(0, 0);
	private float numberOfVomitBullets = 0;

	/**
	 * Used to calculate jump velocity
	 */
	private float tempTouchDownY;
	// prevents the gun reorienting when true
	private boolean currentlyaiming = false;

	// generates a number between 0.3 and 0.7 depending on the players distance
	private float probabilityBasedOnPlayersDistance = 0.3f;
	// after 5000m the prob will always be 0.3f
	private float maxDistanceBeforeAlwaysMinProbability = 1000;

	private Random random = new Random();
	private Paint paint = new Paint();

	/**
	 * Create a simple game world
	 * 
	 * @param game
	 *            Game to which this screen belongs
	 */
	public ZombieZoomGameScreen(Game game) {
		super("ZombieZoomGameScreen", game);

		float aspectRatio = ((ZombieZoomGame) game).getAspectRatio();

		if (aspectRatio > 1.5f) { // 16:10/16:9
			// Create the layer viewport
			mLayerViewport = new LayerViewport(160 * aspectRatio, 160,
					160 * aspectRatio, 160);
		} else { // 3:2
			// Create the layer viewport
			mLayerViewport = new LayerViewport(240, 160, 240, 160);
		}
		// Create the screen viewport
		mScreenViewport = new ScreenViewport(0, 0, mGame.getScreenWidth(),
				mGame.getScreenHeight());
		// sets screenScale
		screenScale = mScreenViewport.width / mLayerViewport.getWidth();
		paint.setColor(Color.WHITE);
		paint.setTextSize(10 * screenScale);

		// Create a new leaderboard
		mLeaderboard = new Leaderboard(game);

		// Load in the image assets used by this layer
		AssetStore assetManager = mGame.getAssetManager();
		assetManager.loadAndAddBitmap("Background", "img/Background.png");
		assetManager.loadAndAddBitmap("Platform", "img/Platform.png");
		assetManager.loadAndAddBitmap("Fallable Platform",
				"img/FallablePlatform.png");
		assetManager.loadAndAddBitmap("Gun", "img/Gun.png");
		assetManager.loadAndAddBitmap("Bullet", "img/Bullet.png");
		assetManager.loadAndAddBitmap("VomitBullet", "img/VomitBullet.png");
		assetManager.loadAndAddBitmap("Coin", "img/Coin.png");
		assetManager.loadAndAddBitmap("PurpleCoin", "img/PurpleCoin.png");
		assetManager.loadAndAddBitmap("SingleCoin", "img/SingleCoin.png");
		assetManager.loadAndAddBitmap("Player Torso", "img/Player Torso.png");
		assetManager.loadAndAddBitmap("Running Animation",
				"img/RunningAnimation.png");
		assetManager.loadAndAddBitmap("Death Animation",
				"img/deathAnimation.png");
		assetManager.loadAndAddBitmap("Pause", "img/Pause.png");
		assetManager.loadAndAddBitmap("Resume", "img/Resume.png");
		assetManager.loadAndAddBitmap("Quit", "img/Quit.png");
		assetManager.loadAndAddBitmap("Health Bar", "img/Healthbar.png");
		assetManager.loadAndAddBitmap("Health Bars", "img/Bars.png");
		assetManager.loadAndAddBitmap("Bomb", "img/skullball.png");
		assetManager.loadAndAddBitmap("Explosion", "img/Explode.png");
		assetManager.loadAndAddBitmap("Bat", "img/Bat.png");
		assetManager.loadAndAddBitmap("Zombie", "img/WalkingZombieSprite.png");
		assetManager.loadAndAddBitmap("Potion", "img/HealthPotion.png");
		assetManager.loadAndAddBitmap("plus5Points", "img/plusFive.png");
		assetManager.loadAndAddBitmap("plus10Points", "img/plusTen.png");
		assetManager.loadAndAddBitmap("plus8Points", "img/PlusEight.png");
		assetManager.loadAndAddBitmap("BackButton", "img/BackButton.png");
		assetManager.loadAndAddBitmap("GodModeOn", "img/CollectGodMode.png");
		backBitmap = assetManager.getBitmap("BackButton");

		// Create the space background
		background = new Ribbon(
				assetManager.getBitmap("Background").getWidth() * 1.5f,
				assetManager.getBitmap("Background").getHeight(),
				assetManager.getBitmap("Background"), this);

		// Creates the buttons
		pauseButton = new Button(mScreenViewport.left + (20.0f * screenScale),
				(20.0f * screenScale), (35.0f * screenScale),
				(35.0f * screenScale), "Pause", this);
		resumeButton = new Button(mScreenViewport.left
				+ (mScreenViewport.width / 2) - (75 * screenScale),
				(mScreenViewport.height / 2) + (25 * screenScale),
				(125 * screenScale), (50 * screenScale), "Resume", this);
		quitButton = new Button(mScreenViewport.left
				+ (mScreenViewport.width / 2) + (75 * screenScale),
				(mScreenViewport.height / 2) + (25 * screenScale),
				(125 * screenScale), (50 * screenScale), "Quit", this);

		// Create the initial platforms
		for (int idx = 0; idx < NUMOFPLATFORMS; idx++) {
			PlatformSprite platform = new PlatformSprite(this);

			// Check how we get the screen size here
			BoundingBox bound = platform.getBound();
			platform.setPosition(bound.halfWidth + (idx * bound.getWidth()),
					bound.halfHeight);
			mPlatforms.add(platform);
		}

		if (((ZombieZoomGame) mGame).mCoinHoarderModeEnabled) {
			playerSprite = new PlayerSprite(PLAYERINITIALX,
					PLAYERINITIALY + 10, 1.25f, this);
		} else {
			playerSprite = new PlayerSprite(PLAYERINITIALX, PLAYERINITIALY,
					.7f, this);
		}

		// Load in the test zombie
		mZombies.add(new ZombieSprite(PLAYERINITIALX * 10, PLAYERINITIALY, this));

		// Load in the test GodModeSprite
		// mPickUps.add(new GodModeSprite(PLAYERINITIALX * 12, PLAYERINITIALY *
		// 3,
		// this));

		mPurse = new Purse(game);

		healthBarBackground = assetManager.getBitmap("Health Bar");
		healthBarsBitmap = assetManager.getBitmap("Health Bars");
		coinBitmap = assetManager.getBitmap("SingleCoin");

		// The controlLine is used as a control indicator, i.e. if you touch to
		// the left of the control line you jump,
		// where as if you touch to the right of the control line you rotate the
		// gun

		controlLine = playerSprite.getBound().x * screenScale * 2;
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
		Input input = mGame.getInput();

		List<TouchEvent> touchEvents = input.getTouchEvents();
		switch (state) {
		case Ready:
			updateReady(touchEvents);
			break;
		case Running:
			updateRunning(touchEvents, elapsedTime);
			break;
		case Paused:
			updatePaused(touchEvents);
			break;
		case GameOver:
			updateGameOver(touchEvents, elapsedTime);
			break;
		}
	}

	/**
	 * Updates method for when game state is ready
	 * 
	 * @param touchEvents
	 *            List of touches
	 */
	private void updateReady(List<TouchEvent> touchEvents) {
		if ((touchEvents.size() > 0)
				&& (touchEvents.get(0).type == TouchEvent.TOUCH_DOWN))
			state = GameState.Running;
	}

	/**
	 * Update method for when game state is running
	 * 
	 * @param touchEvents
	 *            List of touches
	 * @param elapsedTime
	 *            Elapsed time for the time
	 */
	private void updateRunning(List<TouchEvent> touchEvents,
			ElapsedTime elapsedTime) {
		distance = (int) (playerSprite.position.x / 50);
		int len = touchEvents.size();
		float moveDistance = 0.0f;
		for (int i = 0; i < len; i++) {
			TouchEvent e = touchEvents.get(i);
			if (e.x <= controlLine) {
				if (e.type == TouchEvent.TOUCH_DOWN) {
					// does rocket jump if the player is currently jumping, the
					// gun is not already shooting, the gun is a shotgun and the
					// player is not falling
					if (playerSprite.isJumping()
							&& !playerSprite.gunSprite.isPlaying()
							&& playerSprite.gunSprite.getGunType() == GunType.Shotgun
							&& playerSprite.velocity.y > 0) {
						// the gun is placed at 90 decrees to carry out a rocket
						// jump
						playerSprite.gunSprite.setOrientation(90);
						playerSprite.gunSprite.fireGun(playerSprite.position.x,
								playerSprite.position.y,
								playerSprite.velocity.x, this);
						playerSprite.velocity.y += playerSprite.ROCKET_JUMP_VELOCITY;
						playerSprite.runningAnimation.play(
								MoveableObjectsHelper.calculateTime(0,
										playerSprite.velocity.y, GRAVITY),
								false);
					} else {
						// presses the pause Button and ensures that the user
						// does not accidentally touch pause
						if (pauseButton.isActivated()) {
							state = GameState.Paused;
						}
						// the current y position that has been touched is
						// stored to calculate moveDistance later
						else
							tempTouchDownY = e.y;
					}
					// if the player is not already jumping the player will jump
				} else if (!playerSprite.isJumping()) {
					moveDistance = tempTouchDownY - e.y;
					if (e.type == TouchEvent.TOUCH_UP
							|| moveDistance > playerSprite.MAX_JUMP_VELOCITY
									- playerSprite.MIN_JUMP_VELOCITY) {
						// ensures the user has to swipe up in order jump
						if (moveDistance > 0)
							playerSprite.setJumpingState(moveDistance);
						// resets to 0
						tempTouchDownY = 0;
					}
				}
				// aims and/or shoots the gun
			} else {
				playerSprite.rotateGun(e.x, e.y, mLayerViewport, screenScale);
				currentlyaiming = true;
				// only allows the user to shoot one bullet at a time
				if (e.type == TouchEvent.TOUCH_UP
						&& !playerSprite.gunSprite.isPlaying()) {
					// may be used to draw a bullet trajectory line
					previousTouchPoint = new Point((int) e.x, (int) e.y);
					currentlyaiming = false;

					playerSprite.gunSprite.fireGun(playerSprite.position.x,
							playerSprite.position.y, playerSprite.velocity.x,
							this);
				}
			}
		}

		// Checks and removes platforms that are no longer needed
		if (mLayerViewport.x - mLayerViewport.halfWidth > mPlatforms.get(0)
				.getBound().x + mPlatforms.get(0).getBound().halfWidth)
			mPlatforms.remove(0);

		// Checks to see if more platforms need to be spawned
		if (mPlatforms.size() <= NUMOFPLATFORMS)
			createWorld();

		if (currentlyaiming == false)
			playerSprite.gunSprite.reOrientateGun();
		playerSprite.update(elapsedTime, mPlatforms);

		for (PlatformSprite platform : mPlatforms) {
			if (platform.isFallable())
				platform.update(elapsedTime);
		}

		CoinSprite coin;
		Iterator<CoinSprite> coinIterator = mCoins.iterator();
		while (coinIterator.hasNext()) {
			coin = coinIterator.next();
			coin.update(elapsedTime);
			// removes all coins which leave the viewport
			if (coin.getBound().getRight() < mLayerViewport.getLeft())
				coinIterator.remove();
			// if player interacts with a coin
			if (coin.checkForAndResolveCollisions(playerSprite,
					playerSprite.gunSprite.getmBullets(), mPlatforms)) {

				try {
					coinIterator.remove();
				} catch (IllegalStateException e) {
					Log.e("ZZ", "Coin could not be removed because " + e);
				}
				if (coin.coinType == CoinType.Yellow) {
					coinWallet++;
					mPoints.add(new PointsCounter(coin.getBound().getLeft()
							+ ((coin.getBound().getRight() - coin.getBound()
									.getLeft()) / 2), ((coin.getBound()
							.getBottom() + (coin.getBound().getTop() - coin
							.getBound().getBottom()) / 2)) + 10, 0.5f, mGame
							.getAssetManager().getBitmap("plus5Points"), 6,
							this));

				} else if (coin.coinType == CoinType.Purple) {
					coinWallet += 2;
					mPoints.add(new PointsCounter(coin.getBound().getLeft()
							+ ((coin.getBound().getRight() - coin.getBound()
									.getLeft()) / 2), ((coin.getBound()
							.getBottom() + (coin.getBound().getTop() - coin
							.getBound().getBottom()) / 2)) + 10, 0.5f, mGame
							.getAssetManager().getBitmap("plus10Points"), 6,
							this));
				}
			}
		}

		// Load in the GodModeSprite around every 500 score points
		// there can onlybe one pickup on the screen at a time
		// if (mPickUps.size() < 1 && !((ZombieZoomGame) mGame).mGodModeEnabled
		// && getScore() % 500 > 495) {
		if (mPickUps.size() < 1 && !((ZombieZoomGame) mGame).mGodModeEnabled
				&& getScore() % 100 > 95) {

			mPickUps.add(new GodModeSprite(mLayerViewport.getRight(),
					playerSprite.position.y + (mLayerViewport.halfHeight / 2),
					this));
		}

		GodModeSprite pickUp;
		Iterator<GodModeSprite> pickUpIterator = mPickUps.iterator();
		while (pickUpIterator.hasNext()) {
			pickUp = pickUpIterator.next();
			pickUp.update(elapsedTime);
			// removes all pickUps which leave the viewport
			if (pickUp.getBound().getRight() < mLayerViewport.getLeft())
				pickUpIterator.remove();
			// if player interacts with a pickUp
			if (pickUp.checkForAndResolveCollisions(playerSprite,
					playerSprite.gunSprite.getmBullets(), mPlatforms)) {

				try {
					pickUpIterator.remove();
				} catch (IllegalStateException e) {
					Log.e("ZZ", "PickUp could not be removed because " + e);
				}
			}
		}

		HealthPackSprite potion;
		Iterator<HealthPackSprite> potionIterator = mPotion.iterator();
		while (potionIterator.hasNext()) {
			potion = potionIterator.next();
			potion.update(elapsedTime);
			if (potion.getBound().getRight() < mLayerViewport.getLeft())
				try {
					potionIterator.remove();
				} catch (Exception e) {

				}
			if (potion.checkForAndResolveCollisions(playerSprite)) {
				potionIterator.remove();
				if (playerSprite.health < 10) {
					playerSprite.health = playerSprite.health + 5;
				} else {
					playerSprite.health = 15;
				}
			}
		}

		BombSprite bomb;
		Iterator<BombSprite> bombIterator = mBombs.iterator();
		while (bombIterator.hasNext()) {
			bomb = bombIterator.next();
			bomb.update(elapsedTime);
			// Removes all bombs which leave the viewport
			if (bomb.getBound().getRight() < mLayerViewport.getLeft())
				try {
					bombIterator.remove();
				} catch (Exception e) {

				}
			// If player interacts with a bomb
			if (bomb.checkForAndResolveCollisions(playerSprite)) {
				try {
					bombIterator.remove();
					mExplosion.add(new Explosion(bomb.getBound().getLeft()
							+ ((bomb.getBound().getRight() - bomb.getBound()
									.getLeft()) / 2), bomb.getBound()
							.getBottom()
							+ ((bomb.getBound().getTop() - bomb.getBound()
									.getBottom()) / 2), this));
				} catch (Exception e) {

				}
			}

			// If bullet interacts with a bomb
			if (bomb.checkForAndResolveCollisions(playerSprite.gunSprite
					.getmBullets())) {
				try {
					bombIterator.remove();
					mExplosion.add(new Explosion(bomb.getBound().getLeft()
							+ ((bomb.getBound().getRight() - bomb.getBound()
									.getLeft()) / 2), bomb.getBound()
							.getBottom()
							+ ((bomb.getBound().getTop() - bomb.getBound()
									.getBottom()) / 2), this));
				} catch (Exception e) {

				}
			}
		}

		Explosion explosion;
		Iterator<Explosion> explosionIterator = mExplosion.iterator();
		while (explosionIterator.hasNext()) {
			explosion = explosionIterator.next();
			explosion.update(elapsedTime);
		}

		BatSprite bats;
		Iterator<BatSprite> batsIterator = mBats.iterator();
		while (batsIterator.hasNext()) {
			bats = batsIterator.next();
			if (bats.getBound().getLeft() < mLayerViewport.getRight()) {

				bats.update(elapsedTime);
				// Removes all bats which leave the viewport
				if (bats.getBound().getRight() < mLayerViewport.getLeft())
					try {
						batsIterator.remove();
					} catch (Exception e) {

					}
				// If player interacts with a bat
				if (bats.checkForAndResolveCollisions(playerSprite)) {
					try {
						batsIterator.remove();
						playerSprite.takeAwayHealth(1);
					} catch (Exception e) {

					}
				}
				if (bats.checkForAndResolveCollisions(playerSprite.gunSprite
						.getmBullets())) {
					try {
						batsIterator.remove();
						batKillCount++;
						mPoints.add(new PointsCounter(bats.getBound().getLeft()
								+ ((bats.getBound().getRight() - bats
										.getBound().getLeft()) / 2),
								((bats.getBound().getBottom() + (bats
										.getBound().getTop() - bats.getBound()
										.getBottom()) / 2)) + 10, 0.5f, mGame
										.getAssetManager().getBitmap(
												"plus8Points"), 6, this));
					} catch (Exception e) {

					}
				}
			}
		}

		BulletSprite bullet;
		Iterator<BulletSprite> bulletIterator = playerSprite.gunSprite
				.getmBullets().iterator();
		while (bulletIterator.hasNext()) {
			bullet = bulletIterator.next();
			bullet.update(elapsedTime);
			// removes all bullet which leave the viewport or hit a platform
			if (bullet.checkForAndResolveCollisions(mPlatforms)
					|| !GraphicsHelper.determineSpriteFallsWithinLayerViewport(
							bullet, mLayerViewport)) {
				try {
					bulletIterator.remove();
				} catch (Exception e) {

				}
			}
		}

		ZombieSprite zombie;
		Iterator<ZombieSprite> zombieIterator = mZombies.iterator();

		BulletSprite vomitBullet;
		Iterator<BulletSprite> vomitBulletIterator = mVomitBullets.iterator();
		while (vomitBulletIterator.hasNext()) {
			vomitBullet = vomitBulletIterator.next();
			vomitBullet.update(elapsedTime);
			// removes all vomit bullets when they leave the viewport or
			// hit a platform or hit by the player's bullet
			if (vomitBullet
					.checkForAndResolveCollisionsWithAnotherBullet(playerSprite.gunSprite
							.getmBullets())
					|| vomitBullet.checkForAndResolveCollisions(mPlatforms)
					|| !GraphicsHelper.determineSpriteFallsWithinLayerViewport(
							vomitBullet, mLayerViewport)) {
				try {
					vomitBulletIterator.remove();
				} catch (Exception e) {

				}
			}
			// If player is hit by vomit
			if (vomitBullet.checkForAndResolveCollisions(playerSprite)) {
				try {
					vomitBulletIterator.remove();
					playerSprite.takeAwayHealth(1);
					;
				} catch (IllegalStateException e) {
					Log.e("ZZ", "Vomit could not be removed because " + e);
				}
			}
		}

		while (zombieIterator.hasNext()) {
			zombie = zombieIterator.next();
			zombie.update(elapsedTime);

			// this fires the vomit at the correct frame
			if (zombie.state == ZombieState.Attack) {
				// resets numberOfVomitBullets
				if (zombie.getCurrentFrame() == 0)
					numberOfVomitBullets = 0;

				if (zombie.getGender() == Gender.Male
						&& zombie.getCurrentFrame() == 24) {
					if (numberOfVomitBullets == 0)
						mVomitBullets.add(new BulletSprite(zombie.position.x,
								zombie.position.y,
								zombie.getVomitOrientation() - 5,
								zombie.velocity.x, BulletType.Vomit, this));
					else if (numberOfVomitBullets == 1)
						mVomitBullets.add(new BulletSprite(zombie.position.x,
								zombie.position.y,
								zombie.getVomitOrientation() + 5,
								zombie.velocity.x, BulletType.Vomit, this));
					numberOfVomitBullets++;
				} else if (numberOfVomitBullets < 1
						&& zombie.getGender() == Gender.Female
						&& zombie.getCurrentFrame() == 12) {
					mVomitBullets.add(new BulletSprite(zombie.position.x,
							zombie.position.y, zombie.getVomitOrientation(),
							zombie.velocity.x, BulletType.Vomit, this));
					numberOfVomitBullets++;
				}
			}

			// makes the zombie move once it enters the viewport
			if (zombie.getBound().getLeft() < mLayerViewport.getRight()) {
				// makes the zombie move once if just entered the viewport
				if (zombie.state == ZombieState.OfScreen) {
					zombie.setWalkState();
				}

				if (zombie.state != ZombieState.Attack
						&& zombie.position.x < (playerSprite.position.x + 400)) {
					zombie.setAttackState();
				}

			}

			// Removes a zombie if it leaves the left of the viewport
			if (zombie.getBound().getRight() < mLayerViewport.getLeft())
				try {
					zombieIterator.remove();
				} catch (Exception e) {

				}

			// If zombie interacts with the player remove 3 life points from
			// player
			if (zombie.checkForAndResolveCollisions(playerSprite, mPlatforms)) {
				zombieIterator.remove();
				playerSprite.takeAwayHealth(3);
			}
			// If the zombie collides with a bullet add 1 kill to the
			// zombieKillCount
			if (zombie.checkForAndResolveCollisions(playerSprite.gunSprite
					.getmBullets())) {
				try {
					zombieIterator.remove();
					zombieKillCount++;
					mPoints.add(new PointsCounter(zombie.getBound().getLeft()
							+ ((zombie.getBound().getRight() - zombie
									.getBound().getLeft()) / 2),
							((zombie.getBound().getBottom() + (zombie
									.getBound().getTop() - zombie.getBound()
									.getBottom()) / 2)) + 10, 0.5f, mGame
									.getAssetManager()
									.getBitmap("plus10Points"), 6, this));
					if (random.nextFloat() < 0.2f) {
						mPotion.add(new HealthPackSprite(zombie.position.x,
								zombie.position.y, .5f, this));
					}
				} catch (Exception e) {

				}

			}
		}

		// keep viewport on player
		mLayerViewport.x = playerSprite.position.x + mLayerViewport.halfWidth
				- PLAYERINITIALX;

		PointsCounter pointCounter;
		Iterator<PointsCounter> pointsIterator = mPoints.iterator();
		while (pointsIterator.hasNext()) {
			pointCounter = pointsIterator.next();
			pointCounter.update(elapsedTime);
		}

		// Player gets a shotgun after collecting 25 coins
		if (playerSprite.gunSprite.getGunType() != GunType.Shotgun
				&& coinWallet == 10)
			playerSprite.gunSprite.setGun(GunType.Shotgun);

		// Check if Player is Dead
		if (playerSprite.position.y + playerSprite.getBound().halfHeight < 0
				|| playerSprite.health <= 0) {
			playerSprite.state = PlayerState.Dead;
			state = GameState.GameOver;
			((ZombieZoomGame) mGame).mCoinHoarderModeEnabled = false;
			((ZombieZoomGame) mGame).mGodModeEnabled = false;
			((ZombieZoomGame) mGame).mBulletModeEnabled = false;
		}

	}

	/**
	 * Creates the game world
	 */
	public void createWorld() {
		// there is a 10% chance that a sequence of fallable platforms are
		// spawned
		if (random.nextFloat() < 0.1f) {
			// generates a line of fallable platforms
			for (int idx = 0; idx < random.nextInt(NUMOFPLATFORMS / 2); idx++) {
				PlatformSprite platform = new PlatformSprite(this);
				// Check how we get the screen size here
				BoundingBox bound = platform.getBound();
				platform.setFallable(true);
				platform.setPosition(mPlatforms.get(mPlatforms.size() - 1)
						.getBound().x + bound.getWidth(),
						mPlatforms.get(mPlatforms.size() - 1).position.y);
				mPlatforms.add(platform);
			}
		} else {
			for (int idx = 0; idx < NUMOFPLATFORMS; idx++) {
				PlatformSprite platform = new PlatformSprite(this);
				// Check how we get the screen size here
				BoundingBox bound = platform.getBound();

				// generates a number between 0.3 and 0.7 depending on the
				// players distance after 5000m the probability will always be
				// 0.3f
				if (distance < maxDistanceBeforeAlwaysMinProbability)
					probabilityBasedOnPlayersDistance = 0.7f - ((distance / maxDistanceBeforeAlwaysMinProbability) * 0.4f);

				// the further distance the player has travel the more likely
				// a gap will follow a platform rather than another platform
				if (random.nextFloat() < probabilityBasedOnPlayersDistance) {
					// next platform is at the same height as the last platform
					platform.setPosition(mPlatforms.get(mPlatforms.size() - 1)
							.getBound().x + bound.getWidth(),
							mPlatforms.get(mPlatforms.size() - 1).position.y);

					// Load in a coin a fourth of the time
					if (random.nextFloat() < 0.5f)
						mCoins.add(new CoinSprite(
								platform.getBound().getLeft(), platform
										.getBound().y + 50, this));
					// Load in the zombie randomly
					if (random.nextFloat() < .4f)
						mZombies.add(new ZombieSprite(platform.getBound()
								.getRight(),
								platform.getBound().getTop() + 100, this));

				}
				// generates a gap between the platforms
				else {
					float maxYheight;
					if (playerSprite.gunSprite.getGunType() == GunType.Pistol) {
						maxYheight = playerSprite.MAX_Y_HEIGHT;
					} else {
						maxYheight = playerSprite.MAX_Y_HEIGHT_WITH_ROCKET_JUMP;
					}

					float generatedHeight = playerSprite.MIN_Y_HEIGHT
							+ random.nextFloat()
							* (maxYheight - playerSprite.MIN_Y_HEIGHT);

					float jumpPeriod = MoveableObjectsHelper.calculateTime(0,
							playerSprite.MAX_JUMP_VELOCITY, GRAVITY);
					float fallPeriod = MoveableObjectsHelper
							.calculateTimeWithAnInitalVelocityOfZero(maxYheight
									- generatedHeight, GRAVITY);

					float xDistanceDuringJump = 0.99f * MoveableObjectsHelper
							.calculateDisplacementWithTime(
									playerSprite.velocity.x, jumpPeriod);
					float generatedGap = (random.nextFloat() + (fallPeriod / jumpPeriod))
							* xDistanceDuringJump;

					if (random.nextFloat() < 0.2f)
						platform.setFallable(true);
					else
						platform.setFallable(false);

					if ((mLayerViewport.halfHeight * 1.5) > mPlatforms
							.get(mPlatforms.size() - 1).position.y
							&& (mPlatforms.get(mPlatforms.size() - 1).position.y < PLAYERINITIALY - 20 || random
									.nextFloat() > 0.5f)) {
						// next platform is above the last platform
						platform.setPosition(
								mPlatforms.get(mPlatforms.size() - 1)
										.getBound().x
										+ bound.getWidth()
										+ generatedGap,
								mPlatforms.get(mPlatforms.size() - 1).position.y
										- generatedHeight);

						// this ensure that the platform does not spawn above
						// the viewport
						if (platform.getBound().y > mLayerViewport.getHeight()
								- bound.halfHeight
								- (playerSprite.getBound().getHeight() * 3))
							platform.setPosition(platform.getBound().x,
									mLayerViewport.getHeight()
											- bound.halfHeight
											- (playerSprite.getBound()
													.getHeight() * 3));

						// coins are more likely to at the start of the game,
						// as the player's distance moved increases the chance
						// of a bomb appearing becomes more likely
						if (random.nextFloat() < probabilityBasedOnPlayersDistance)

							mCoins.add(new CoinSprite(platform.getBound()
									.getLeft() - (generatedGap / 2),
									platform.position.y
											+ playerSprite.getBound()
													.getHeight(), this));

						else
							mBombs.add(new BombSprite(platform.getBound()
									.getLeft() - (generatedGap / 2),
									platform.position.y
											+ playerSprite.getBound()
													.getHeight(), this));
					} else {
						// next platform is below the last platform
						platform.setPosition(
								mPlatforms.get(mPlatforms.size() - 1)
										.getBound().x
										+ bound.getWidth()
										+ generatedGap,
								mPlatforms.get(mPlatforms.size() - 1).position.y
										+ generatedHeight);

						// this lowers the last platform was fallable
						if (mPlatforms.get(mPlatforms.size() - 1).isFallable())
							platform.setPosition(platform.getBound().x,
									platform.getBound().y
											+ platform.getBound().getHeight());

						// this ensure that the platform does not spawn below
						// the viewport
						if (platform.getBound().y < bound.halfHeight)
							platform.setPosition(platform.getBound().x,
									bound.halfHeight);

						// coins are more likely to at the start of the game,
						// as the player's distance moved increases the chance
						// of a bomb appearing becomes more likely
						if (random.nextFloat() < probabilityBasedOnPlayersDistance)
							mCoins.add(new CoinSprite(platform.getBound()
									.getLeft() - (generatedGap / 2), mPlatforms
									.get(mPlatforms.size() - 1).position.y
									+ playerSprite.getBound().getHeight()
									- playerSprite.MIN_Y_HEIGHT, this));
						else
							mBombs.add(new BombSprite(platform.getBound()
									.getLeft() - (generatedGap / 2), mPlatforms
									.get(mPlatforms.size() - 1).position.y
									+ playerSprite.getBound().getHeight()
									- playerSprite.MIN_Y_HEIGHT, this));
					}
					// Load in the zombie randomly
					if (random.nextFloat() < probabilityBasedOnPlayersDistance * 0.75f)
						mZombies.add(new ZombieSprite(platform.position.x,
								platform.position.y
										+ platform.getBound().halfHeight * 3,
								this));

					// Load in the bats randomly
					if (random.nextFloat() < probabilityBasedOnPlayersDistance * 0.5f)
						mBats.add(new BatSprite(
								platform.position.x,
								mLayerViewport.getHeight()
										- random.nextInt((int) mLayerViewport.halfHeight),
								this));
				}
				mPlatforms.add(platform);
			}
		}
	}

	/**
	 * Update method for when the game is paused
	 * 
	 * @param touchEvents
	 *            List of touches
	 */
	private void updatePaused(List<TouchEvent> touchEvents) {
		if (resumeButton.isActivated()) {
			state = GameState.Running;
		} else if (quitButton.isActivated()) {
			// If the play game area has been touched then swap screens
			mGame.getScreenManager().removeScreen(this.getName());
			ZombieZoomMenuScreen zombieZoomMenuScreen = new ZombieZoomMenuScreen(
					this.mGame);
			// As it's the only added screen it will become active.
			mGame.getScreenManager().addScreen(zombieZoomMenuScreen);
		}
	}

	/**
	 * Update method for when the game is over
	 * 
	 * @param touchEvents
	 *            List of touches
	 * @param elapsedTime
	 *            Elapsed time for the game
	 */
	private void updateGameOver(List<TouchEvent> touchEvents,
			ElapsedTime elapsedTime) {
		if (coinsSaved == false) {
			coinsSaved = true;
			mPurse.addToPurse(coinWallet);
		}
		if (playerSprite.deathAnimation.isPlaying())
			playerSprite.update(elapsedTime, mPlatforms);
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
				scoreSaved = false;
			} else {
				mGame.getScreenManager().removeScreen(this.getName());
				ZombieZoomGameScreen zombieZoomGameScreen = new ZombieZoomGameScreen(
						this.mGame);
				// As it's the only added screen it will become active.
				mGame.getScreenManager().addScreen(zombieZoomGameScreen);
				scoreSaved = false;
				return;
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
	public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
		background.drawRibbon(graphics2D, mLayerViewport, mScreenViewport);

		for (int idx = 0; idx < mPlatforms.size(); idx++) {
			if (mLayerViewport.intersects(mPlatforms.get(idx).getBound())) {
				mPlatforms.get(idx).draw(elapsedTime, graphics2D,
						mLayerViewport, mScreenViewport);
			}
		}

		String temp = "Score : " + getScore();
		graphics2D.drawText(temp,
				(mScreenViewport.width / 2) - (paint.measureText(temp) / 2),
				paint.getTextSize(), paint);

		if (coinRect == null && coinBitmap != null) {
			int left = (int) ((((mScreenViewport.width / 2) - (paint
					.measureText(temp) / 2))) + paint.measureText(temp) + 15);
			int top = 0;
			int bottom = top + coinBitmap.getHeight();
			int right = left + coinBitmap.getWidth();
			coinRect = new Rect(left, top, right, bottom);
		}
		graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("SingleCoin"),
				null, coinRect, null);
		String coinString = "" + coinWallet;
		graphics2D.drawText(coinString, coinRect.right, paint.getTextSize(),
				paint);

		// first draws any existing bullets in mBullets and then the player
		for (BulletSprite b : playerSprite.gunSprite.getmBullets()) {
			b.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
		}

		playerSprite.draw(elapsedTime, graphics2D, mLayerViewport,
				mScreenViewport);
		switch (state) {
		case Ready:
			drawReadyUI(graphics2D);
			break;
		case Running:
			drawRunningUI(elapsedTime, graphics2D);
			break;
		case Paused:
			drawPausedUI(elapsedTime, graphics2D);
			break;
		case GameOver:
			drawGameOverUI(graphics2D);
			break;
		}

		// Draw the health bar
		if (healthBarBackgroundBound == null && healthBarBackground != null) {
			int left = graphics2D.getSurfaceWidth()
					- healthBarBackground.getWidth();
			int top = 0;
			int bottom = healthBarBackground.getHeight();
			int right = graphics2D.getSurfaceWidth();
			healthBarBackgroundBound = new Rect(left, top, right, bottom);
		}
		graphics2D.drawBitmap(healthBarBackground, null,
				healthBarBackgroundBound, null);

		float startingPoint = (graphics2D.getSurfaceWidth()
				- (healthBarBackground.getWidth() / 2) - (healthBarsBitmap
				.getWidth() * 7.5f));
		for (int i = 0; i < playerSprite.health; i++) {
			if (healthBarsBound == null && healthBarsBitmap != null) {
				int left = (int) startingPoint
						+ (healthBarsBitmap.getWidth() * i);
				int top = (healthBarBackground.getHeight() / 2)
						- (healthBarsBitmap.getHeight() / 2);
				int bottom = (healthBarBackground.getHeight() / 2)
						+ (healthBarsBitmap.getHeight() / 2);
				int right = left + healthBarsBitmap.getWidth();
				healthBarsBound = new Rect(left, top, right, bottom);
			}
			graphics2D
					.drawBitmap(healthBarsBitmap, null, healthBarsBound, null);
			healthBarsBound = null;
		}
	}

	/**
	 * Gets the score for the game
	 * 
	 * @return Calculated score
	 */
	private int getScore() {
		return (int) (distance + (coinWallet * COIN_VALUE)
				+ (batKillCount * BAT_VALUE) + (zombieKillCount * ZOMBIE_VALUE));
	}

	/**
	 * Draw method when game state is ready
	 * 
	 * @param graphics2D
	 *            Graphics instance used to draw the screen
	 */
	public void drawReadyUI(IGraphics2D graphics2D) {
		String temp = "Ready? (Touch Screen)";
		graphics2D.drawText(temp,
				(mScreenViewport.width / 2) - (paint.measureText(temp) / 2),
				(mScreenViewport.height / 2), paint);
		// draw control line
		drawDashedStraightVerticalLine(25, controlLine, 0, controlLine,
				mScreenViewport.height * 0.2f, graphics2D, paint);
	}

	/**
	 * Draws a straight dashed vertical line
	 * 
	 * @param numOfLines
	 *            Number of lines to be drawn
	 * @param startX
	 *            Start x location
	 * @param startY
	 *            Start y location
	 * @param stopX
	 *            End x location
	 * @param stopY
	 *            End y location
	 * @param graphics2D
	 * @param paint
	 *            Paint parameters
	 */
	public void drawDashedStraightVerticalLine(float numOfLines, float startX,
			float startY, float stopX, float stopY, IGraphics2D graphics2D,
			Paint paint) {
		float length = mScreenViewport.height * (1 / ((numOfLines * 2) - 1));
		int i = 0;
		do {
			graphics2D.drawLine(controlLine, length * 2 * i, controlLine,
					(length * 2 * i) + length, paint);
			i++;
		} while (i <= numOfLines);
	}

	/**
	 * Draw method when game state is running
	 * 
	 * @param elapsedTime
	 *            Elapsed Time information for the frame
	 * @param graphics2D
	 *            Graphics instance used to draw the screen
	 */
	public void drawRunningUI(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
		for (int idx = 0; idx < mCoins.size(); idx++) {
			if (mLayerViewport.intersects(mCoins.get(idx).getBound())) {
				mCoins.get(idx).draw(elapsedTime, graphics2D, mLayerViewport,
						mScreenViewport);
			}
		}

		for (int idx = 0; idx < mPickUps.size(); idx++) {
			if (mLayerViewport.intersects(mPickUps.get(idx).getBound())) {
				mPickUps.get(idx).draw(elapsedTime, graphics2D, mLayerViewport,
						mScreenViewport);
			}
		}

		for (int idx = 0; idx < mPotion.size(); idx++) {
			if (mLayerViewport.intersects(mPotion.get(idx).getBound())) {
				mPotion.get(idx).draw(elapsedTime, graphics2D, mLayerViewport,
						mScreenViewport);
			}
		}

		for (int idx = 0; idx < mBombs.size(); idx++) {
			if (mLayerViewport.intersects(mBombs.get(idx).getBound())) {
				mBombs.get(idx).draw(elapsedTime, graphics2D, mLayerViewport,
						mScreenViewport);
			}
		}

		for (int idx = 0; idx < mExplosion.size(); idx++) {
			if (mLayerViewport.intersects(mExplosion.get(idx).getBound())) {
				mExplosion.get(idx).draw(elapsedTime, graphics2D,
						mLayerViewport, mScreenViewport);
			}
		}

		for (int idx = 0; idx < mBats.size(); idx++) {
			if (mLayerViewport.intersects(mBats.get(idx).getBound())) {
				mBats.get(idx).draw(elapsedTime, graphics2D, mLayerViewport,
						mScreenViewport);
			}
		}

		for (int idx = 0; idx < mZombies.size(); idx++) {
			if (mLayerViewport.intersects(mZombies.get(idx).getBound())) {
				mZombies.get(idx).draw(elapsedTime, graphics2D, mLayerViewport,
						mScreenViewport);
			}
			// first draws any existing bullets in mVomitBullets
			for (BulletSprite b : mVomitBullets) {
				b.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
			}
		}

		for (int idx = 0; idx < mPoints.size(); idx++) {
			if (mLayerViewport.intersects(mPoints.get(idx).getBound())) {
				mPoints.get(idx).draw(elapsedTime, graphics2D, mLayerViewport,
						mScreenViewport);
			}
		}

		pauseButton.draw(elapsedTime, graphics2D, mLayerViewport,
				mScreenViewport);

		// draw control line
		drawDashedStraightVerticalLine(25, controlLine, 0, controlLine,
				mScreenViewport.height * 0.2f, graphics2D, paint);
	}

	/**
	 * Draw method when game state is paused
	 * 
	 * @param elapsedTime
	 *            Elapsed Time information for the frame
	 * @param graphics2D
	 *            Graphics instance used to draw the screen
	 */
	public void drawPausedUI(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
		resumeButton.draw(elapsedTime, graphics2D, mLayerViewport,
				mScreenViewport);
		quitButton.draw(elapsedTime, graphics2D, mLayerViewport,
				mScreenViewport);
	}

	/**
	 * Draw method when game state is over
	 * 
	 * @param graphics2D
	 *            Graphics instance used to draw the screen
	 */
	public void drawGameOverUI(IGraphics2D graphics2D) {
		String temp = "Game Over";
		graphics2D.drawText(temp,
				(mScreenViewport.width / 2) - (paint.measureText(temp) / 2),
				(mScreenViewport.height / 2), paint);

		temp = "Tap screen to try again";
		graphics2D.drawText(temp,
				(mScreenViewport.width / 2) - (paint.measureText(temp) / 2),
				(mScreenViewport.height / 2) + 30, paint);

		if (mBackBound == null && backBitmap != null) {
			int left = 0;
			int top = graphics2D.getSurfaceHeight() - backBitmap.getHeight();
			int bottom = graphics2D.getSurfaceHeight();
			int right = backBitmap.getWidth();
			mBackBound = new Rect(left, top, right, bottom);
		}
		graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("BackButton"),
				null, mBackBound, null);

		if (scoreSaved == false) {
			scoreSaved = true;

			int scoreLocation = mLeaderboard.promtName(getScore());
			if (scoreLocation != -1) {
				final EditText txtUrl = new EditText(mGame.getActivity());
				new AlertDialog.Builder(mGame.getActivity())
						.setMessage(
								"Congratulations, your score will be Top Score "
										+ scoreLocation
										+ " in the leaderboard. Enter your name:")
						.setView(txtUrl)
						.setPositiveButton("Submit Name",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										name = txtUrl.getText().toString();
										mLeaderboard.savePoints(getScore(),
												name);
										scoreSaved = true;
									}
								})
						.setNegativeButton("Anonymous",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										mLeaderboard.savePoints(getScore(),
												"Unknown");
										scoreSaved = true;
									}
								}).show();
			}
		}
	}

	@Override
	public void pause() {
		state = GameState.Paused;
		super.pause();
	}
	
	@Override
	public void resume() {
		super.resume();
	}
}