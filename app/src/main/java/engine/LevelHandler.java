package engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;

import com.flappyfishgame.R;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LevelHandler implements SceneHolder.SceneHolderHandler {
    private DrawableScene mScene;
    private DrawableObject mMat;
    private DrawableObject mPenguinRed;
    private GameListener mLevelListener;
    private Context mContext;
    private boolean mGameIsBroken = false;
    private float mFishX = -0.5f;
    private float mFishY = 0;
    private boolean mIsScreenTouched = false;
    private boolean mIsFirstTouched = false;

    private List<TextureTemplate> mTexturesToLoad = new CopyOnWriteArrayList<>();

    /**
     * Level 1 handler.
     * @param context application context.
     * @param levelListener level listener.
     */
    public LevelHandler(final Context context, final GameListener levelListener) {
        mContext = context;
        mLevelListener = levelListener;
    }

    @Override
    public void processBeforeDraw(final int matrixLocation, final float[] matrix) {
    }

    @Override
    public void processAfterDraw(final int matrixLocation, final float[] matrix) {
    }

    @Override
    public void processTouch(final MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            mIsFirstTouched = true;
            mIsScreenTouched = true;
            mPenguinRed.animateLoop(Const.DRAG_ANIMATION);
            SoundPlayer.playWaterBubblesSound(mContext);
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            mIsScreenTouched = false;
            mPenguinRed.animateLoop(Const.NORMAL_ANIMATION_STATE);
        }
    }

    @Override
    public void processLevelThread() {
        while (!mGameIsBroken) {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
            }
            if (mIsFirstTouched) {
                float angle = -1;
                if (mIsScreenTouched) {
                    angle = 0.7f;
                }
                final float newX = Const.CENTER_X + (mFishX - Const.CENTER_X) * (float) Math.cos(angle * Const.ANGLE_FACTOR) -
                        (mFishY - Const.CENTER_Y) * (float) Math.sin(angle * Const.ANGLE_FACTOR);
                final float newY = Const.CENTER_Y + (mFishY - Const.CENTER_Y) * (float) Math.cos(angle * Const.ANGLE_FACTOR) +
                        (mFishX - Const.CENTER_X) * (float) Math.sin(angle * Const.ANGLE_FACTOR);
                if (-0.9f < newY && newY < 0.75f) {
                    mFishX = newX;
                    mFishY = newY;
                    mPenguinRed.setX(mFishX);
                    mPenguinRed.setY(mFishY);
                }
                if (-0.85f > newY) {
                    mFishX = -0.5f;
                    mFishY = 0;
                    mPenguinRed.setX(mFishX);
                    mPenguinRed.setY(mFishY);
                    mLevelListener.onLevelMistake();
                }
            }
        }
    }

    @Override
    public void stopLevelThread() {
        mGameIsBroken = true;
    }

    @Override
    public void init() {
        mScene = new DrawableScene(1);

        // Mat
        mMat = new DrawableObject(2, 2, DrawableObject.NORMAL_SPRITE);
        final Bitmap iconMat = (BitmapFactory.decodeResource(mContext.getResources(), R.drawable.level_mat));
        final TextureTemplate matTemplate = new TextureTemplate(Const.NORMAL_STATE, TextureTemplate.SIMPLE_TEXTURE,
                iconMat, mMat);
        mTexturesToLoad.add(matTemplate);
        mScene.addToLayer(0, mMat);

        // Red penguin.
        mPenguinRed = new DrawableObject(0.25f, 0.25f, DrawableObject.SQUARE_SPRITE);
        mPenguinRed.setX(mFishX);
        mPenguinRed.setY(mFishY);
        final Bitmap iconPenguinRed = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.penguin_red);
        final Bitmap normalAnimationPenguinRed = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.penguin_red_animation1);
        final Bitmap dragAnimationPenguinRed = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.penguin_red_animation2);
        final TextureTemplate penguinRedTemplate = new TextureTemplate(Const.NORMAL_STATE, TextureTemplate.SIMPLE_TEXTURE,
                iconPenguinRed, mPenguinRed);
        final TextureTemplate penguinRedNormalAnimationTemplate = new TextureTemplate(Const.NORMAL_ANIMATION_STATE,
                TextureTemplate.ANIMATION_TEXTURE, normalAnimationPenguinRed, mPenguinRed);
        final TextureTemplate penguinRedDragAnimationTemplate = new TextureTemplate(Const.DRAG_ANIMATION,
                TextureTemplate.ANIMATION_TEXTURE, dragAnimationPenguinRed, mPenguinRed);
        mTexturesToLoad.add(penguinRedTemplate);
        mTexturesToLoad.add(penguinRedNormalAnimationTemplate);
        mTexturesToLoad.add(penguinRedDragAnimationTemplate);
        mScene.addToLayer(0, mPenguinRed);
    }

    @Override
    public DrawableScene getScene() {
        return mScene;
    }

    @Override
    public List<TextureTemplate> getTexturesToLoad() {
        return mTexturesToLoad;
    }
}
