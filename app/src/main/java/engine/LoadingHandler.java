package engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;

import com.flappyfishgame.R;

import java.util.ArrayList;
import java.util.List;

public class LoadingHandler implements SceneHolder.SceneHolderHandler {
    private DrawableScene mScene;
    private DrawableObject mMat;
    private DrawableObject mLoadingBar;
    private Context mContext;

    private List<TextureTemplate> mTexturesToLoad = new ArrayList<>();

    public LoadingHandler(final Context context, final GameListener levelListener) {
        mContext = context;
    }

    @Override
    public void processBeforeDraw(final int matrixLocation, final float[] matrix) {
    }

    @Override
    public void processAfterDraw(final int matrixLocation, final float[] matrix) {
    }

    @Override
    public void processTouch(final MotionEvent e) {
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

        mMat = new DrawableObject(2, 2, DrawableObject.NORMAL_SPRITE);
        final Bitmap iconMat = Utils.getResizedBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.mat), Game.getScreenWidth(), Game.getScreenHeight());
        final TextureTemplate matTemplate = new TextureTemplate(Const.NORMAL_STATE, TextureTemplate.SIMPLE_TEXTURE,
                iconMat, mMat);
        mTexturesToLoad.add(matTemplate);
        mScene.addToLayer(0, mMat);

        mLoadingBar = new DrawableObject(0.25f, 0.25f, DrawableObject.SQUARE_SPRITE);
        mLoadingBar.animateLoop(Const.NORMAL_ANIMATION_STATE);
        final Bitmap iconLoadingBar = Utils.getResizedBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.loading), 800, 100);
        final TextureTemplate loadingBarTemplate = new TextureTemplate(Const.NORMAL_ANIMATION_STATE,
                TextureTemplate.ANIMATION_TEXTURE, iconLoadingBar, mLoadingBar);
        mTexturesToLoad.add(loadingBarTemplate);
        mScene.addToLayer(0, mLoadingBar);
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
