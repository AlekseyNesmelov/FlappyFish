package engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.flappyfishgame.R;

import java.util.ArrayList;
import java.util.List;

public class UpBarHandler implements SceneHolder.SceneHolderHandler {
    private DrawableScene mScene;
    private DrawableObject mMat;
    private List<DrawableObject> mHearts;
    private DrawableObject mCoin;
    private DrawableObject mBackButton;
    private DrawableObject mKeyButton;
    private DrawableObject mRedScreen;
    private boolean mIsBackPressed = false;
    private boolean mIsKeyPressed = false;
    private GameListener mLevelListener;
    private Context mContext;

    private List<TextureTemplate> mTexturesToLoad = new ArrayList<>();

    private long mRedScreenScreenTimeCount = 0;

    /**
     * Up bar handler constructor.
     * @param context application context.
     * @param levelListener level listener.
     */
    public UpBarHandler(final Context context, final GameListener levelListener) {
        mContext = context;
        mLevelListener = levelListener;
    }

    @Override
    public void processBeforeDraw(final int matrixLocation, final float[] matrix) {
        long time = System.currentTimeMillis();
        if (mRedScreen.isVisible() && time - mRedScreenScreenTimeCount > Const.RED_SCREEN_DELAY) {
            mRedScreen.setVisible(false);
        }
    }

    @Override
    public void processAfterDraw(int matrixLocation, float[] matrix) {
    }

    @Override
    public void processTouch(final MotionEvent e) {
        final float x = e.getX();
        final float y = e.getY();
        final float glX = Game.getXByScreenX(x);
        final float glY = Game.getYByScreenY(y);
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            if (mBackButton.isInside(glX, glY)) {
                mBackButton.setState(Const.BUTTON_PRESSED_STATE);
                mIsBackPressed = true;
            } else if (mKeyButton.isInside(glX, glY)) {
                mKeyButton.setState(Const.BUTTON_PRESSED_STATE);
                mIsKeyPressed = true;
            }
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            mBackButton.setState(Const.NORMAL_STATE);
            mKeyButton.setState(Const.NORMAL_STATE);
            if (mIsBackPressed && mBackButton.isInside(glX, glY)) {
                SoundPlayer.playClickSound(mContext);
                mLevelListener.onReturnToMenu();
            } else if (mIsKeyPressed && mKeyButton.isInside(glX, glY)) {
                SoundPlayer.playClickSound(mContext);
                mLevelListener.onKeyPressed();
            }
            mIsBackPressed = false;
            mIsKeyPressed = false;
        }
    }

    /**
     * Sets lives count.
     * @param livesCount lives count tot set.
     * @param showFail true if needs to show fail screen animation.
     */
    public void setLivesCount(final int livesCount, final boolean showFail) {
        if (showFail) {
            mRedScreenScreenTimeCount = System.currentTimeMillis();
            mRedScreen.setVisible(true);
            SoundPlayer.playMistakeSound(mContext);
        }
        for (int i = Const.MIN_LIVES_COUNT; i < livesCount; i++) {
            mHearts.get(i).setVisible(true);
        }
        for (int i = livesCount; i < Const.MAX_LIVES_COUNT; i++) {
            mHearts.get(i).setVisible(false);
        }
    }

    @Override
    public void processLevelThread() {
    }

    @Override
    public void stopLevelThread() {
    }

    @Override
    public void init() {
        mScene = new DrawableScene(2);

        // Red screen.
        mRedScreen = new DrawableObject(2f, 2f, DrawableObject.NORMAL_SPRITE);
        mRedScreen.setVisible(false);
        final Bitmap iconRedScreen = Utils.getResizedBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.red_screen), Game.getScreenWidth(), Game.getScreenHeight());
        final TextureTemplate redScreenTemplate = new TextureTemplate(Const.NORMAL_STATE, TextureTemplate.SIMPLE_TEXTURE,
                iconRedScreen, mRedScreen);
        mTexturesToLoad.add(redScreenTemplate);
        mScene.addToLayer(1, mRedScreen);

        // Mat.
        mMat = new DrawableObject(2f, 0.2f, DrawableObject.NORMAL_SPRITE);
        mMat.setY(0.9f);
        final Bitmap iconMat = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.up_bar);
        final TextureTemplate matTemplate = new TextureTemplate(Const.NORMAL_STATE, TextureTemplate.SIMPLE_TEXTURE,
                iconMat, mMat);
        mTexturesToLoad.add(matTemplate);
        mScene.addToLayer(0, mMat);

        // Back button.
        mBackButton = new DrawableObject(0.18f, 0.18f, DrawableObject.SQUARE_SPRITE);
        mBackButton.setX(0.85f);
        mBackButton.setY(0.9f);
        final Bitmap iconBackButton = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.back);
        final Bitmap iconBackButtonPressed = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.back_pressed);
        final TextureTemplate backButtonTemplate = new TextureTemplate(Const.NORMAL_STATE, TextureTemplate.SIMPLE_TEXTURE,
                iconBackButton, mBackButton);
        final TextureTemplate backButtonPressedTemplate = new TextureTemplate(Const.BUTTON_PRESSED_STATE, TextureTemplate.SIMPLE_TEXTURE,
                iconBackButtonPressed, mBackButton);
        mTexturesToLoad.add(backButtonTemplate);
        mTexturesToLoad.add(backButtonPressedTemplate);
        mScene.addToLayer(0, mBackButton);

        // Key button.
        mKeyButton = new DrawableObject(0.18f, 0.18f, DrawableObject.SQUARE_SPRITE);
        mKeyButton.setX(0.55f);
        mKeyButton.setY(0.9f);
        final Bitmap iconKeyButton = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.key);
        final Bitmap iconKeyButtonPressed = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.key_pressed);
        final TextureTemplate keyButtonTemplate = new TextureTemplate(Const.NORMAL_STATE, TextureTemplate.SIMPLE_TEXTURE,
                iconKeyButton, mKeyButton);
        final TextureTemplate keyButtonPressedTemplate = new TextureTemplate(Const.BUTTON_PRESSED_STATE, TextureTemplate.SIMPLE_TEXTURE,
                iconKeyButtonPressed, mKeyButton);
        mTexturesToLoad.add(keyButtonTemplate);
        mTexturesToLoad.add(keyButtonPressedTemplate);
        mScene.addToLayer(0, mKeyButton);

        // Hearts.
        mHearts = new ArrayList<>();
        final Bitmap iconHeart = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.heart);
        float curX = -0.85f;
        for (int i = 0; i < (Const.MAX_LIVES_COUNT - Const.MIN_LIVES_COUNT); i++) {
            final DrawableObject heart = new DrawableObject(0.18f, 0.18f, DrawableObject.SQUARE_SPRITE);
            heart.setX(curX);
            heart.setY(0.9f);
            curX+= 0.18f;
            mHearts.add(heart);
            mScene.addToLayer(0, heart);
        }
        final TextureTemplate heartTemplate = new TextureTemplate(Const.NORMAL_STATE, TextureTemplate.SIMPLE_TEXTURE,
                iconHeart, mHearts);
        mTexturesToLoad.add(heartTemplate);

        // Coins.
        final int textSize = 70;
        final Paint textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);
        textPaint.setARGB(255, 0, 0, 0);

        mCoin = new DrawableObject(0.18f, 0.18f, DrawableObject.SQUARE_SPRITE);
        mCoin.setX(0.2f);
        mCoin.setY(0.9f);
        final Bitmap iconCoin = Utils.getResizedBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.coin), 200, 200);
        final Canvas canvas = new Canvas(iconCoin);
        canvas.drawText(Game.getCoinCount() + "", 30, 130, textPaint);
        final TextureTemplate coinTemplate = new TextureTemplate(Const.NORMAL_STATE, TextureTemplate.SIMPLE_TEXTURE,
                iconCoin, mCoin);
        mTexturesToLoad.add(coinTemplate);
        mScene.addToLayer(0, mCoin);
    }

    @Override
    public List<TextureTemplate> getTexturesToLoad() {
       return mTexturesToLoad;
    }

    @Override
    public DrawableScene getScene() {
        return mScene;
    }
}

