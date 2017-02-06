package engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.flappyfishgame.R;

import java.util.ArrayList;
import java.util.List;

public class MainMenuHandler implements SceneHolder.SceneHolderHandler {
    private DrawableScene mScene;
    private DrawableObject mMat;
    private DrawableObject mStartButton;
    private DrawableObject mExitButton;
    private boolean mIsStartPressed = false;
    private boolean mIsExitPressed = false;
    private GameListener mLevelListener;
    private Context mContext;

    private List<TextureTemplate> mTexturesToLoad = new ArrayList<>();

    public MainMenuHandler(final Context context, final GameListener levelListener) {
        mContext = context;
        mLevelListener = levelListener;
    }

    @Override
    public void processBeforeDraw(final int matrixLocation, final float[] matrix) {
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
            if (mStartButton.isInside(glX, glY)) {
                mStartButton.setState(Const.BUTTON_PRESSED_STATE);
                mIsStartPressed = true;
            } else if (mExitButton.isInside(glX, glY)) {
                mExitButton.setState(Const.BUTTON_PRESSED_STATE);
                mIsExitPressed = true;
            }
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            mStartButton.setState(Const.NORMAL_STATE);
            mExitButton.setState(Const.NORMAL_STATE);
            if (mIsStartPressed && mStartButton.isInside(glX, glY)) {
                mLevelListener.onStartButtonPressed();
                SoundPlayer.playClickSound(mContext);
            } else if (mIsExitPressed && mExitButton.isInside(glX, glY)) {
                SoundPlayer.playClickSound(mContext);
                ((AppCompatActivity)mContext).finish();
            }
            mIsStartPressed = false;
            mIsExitPressed = false;
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
        mScene = new DrawableScene(1);

        // Mat
        mMat = new DrawableObject(2, 2, DrawableObject.NORMAL_SPRITE);
        final Bitmap iconMat = Utils.getResizedBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.mat), Game.getScreenWidth(), Game.getScreenHeight());
        final TextureTemplate matTemplate = new TextureTemplate(Const.NORMAL_STATE, TextureTemplate.SIMPLE_TEXTURE,
                iconMat, mMat);
        mTexturesToLoad.add(matTemplate);
        mScene.addToLayer(0, mMat);

        // Start button.
        mStartButton = new DrawableObject(0.7f, 0.7f, DrawableObject.SQUARE_SPRITE);
        final Bitmap iconStartButton = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.start_button);
        final Bitmap iconStartButtonPressed = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.start_button_pressed);
        final TextureTemplate startButtonTemplate = new TextureTemplate(Const.NORMAL_STATE, TextureTemplate.SIMPLE_TEXTURE,
                iconStartButton, mStartButton);
        final TextureTemplate startButtonPressedTemplate = new TextureTemplate(Const.BUTTON_PRESSED_STATE, TextureTemplate.SIMPLE_TEXTURE,
                iconStartButtonPressed, mStartButton);
        mTexturesToLoad.add(startButtonTemplate);
        mTexturesToLoad.add(startButtonPressedTemplate);
        mScene.addToLayer(0, mStartButton);

        // Exit button.
        mExitButton = new DrawableObject(0.3f, 0.3f, DrawableObject.SQUARE_SPRITE);
        mExitButton.setX(0.6f);
        mExitButton.setY(0.8f);
        final Bitmap iconExitButton = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.exit_button);
        final Bitmap iconExitButtonPressed = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.exit_button_pressed);
        final TextureTemplate exitButtonTemplate = new TextureTemplate(Const.NORMAL_STATE, TextureTemplate.SIMPLE_TEXTURE,
                iconExitButton, mExitButton);
        final TextureTemplate exitButtonPressedTemplate = new TextureTemplate(Const.BUTTON_PRESSED_STATE, TextureTemplate.SIMPLE_TEXTURE,
                iconExitButtonPressed, mExitButton);
        mTexturesToLoad.add(exitButtonTemplate);
        mTexturesToLoad.add(exitButtonPressedTemplate);
        mScene.addToLayer(0, mExitButton);
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
