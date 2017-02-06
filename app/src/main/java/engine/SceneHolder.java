package engine;

import android.view.MotionEvent;

import java.nio.FloatBuffer;
import java.util.List;

/**
 * Scene holder class.
 */
public class SceneHolder {
    // Scene holder thread.
    private SceneHolderThread mSceneHolderThread = new SceneHolderThread();
    // Scene holder handler.
    private SceneHolderHandler mSceneHolderHandler;
    // Scene.
    private DrawableScene mScene;

    /**
     * Scene holder constructor.
     * @param levelHandler level handler.
     */
    public SceneHolder(final SceneHolderHandler levelHandler) {
        levelHandler.init();
        mSceneHolderHandler = levelHandler;
        mScene = levelHandler.getScene();
    }

    /**
     * Touch event.
     * @param e motion event.
     */
    public void touchEvent(final MotionEvent e) {
        mSceneHolderHandler.processTouch(e);
    }

    /**
     * Starts level thread.
     */
    public void startLevelThread() {
        mSceneHolderThread.start();
    }

    /**
     * Stops level thread.
     */
    public void stopLevelThread() {
        mSceneHolderHandler.stopLevelThread();
    }

    /**
     * Puts scene holder to buffer.
     * @param startPos start pos in buffer.
     * @param vertexData vertex buffer.
     * @param textureCoordinates texture coordinates.
     * @return new pos in buffer.
     */
    public int putToBuffer(final int startPos, final FloatBuffer vertexData, final FloatBuffer textureCoordinates) {
        return mScene.putToBuffer(startPos, vertexData, textureCoordinates);
    }

    /**
     * Draws scene holder.
     * @param matrixLocation matrix location.
     * @param matrix model matrix.
     */
    public void draw(final int matrixLocation, final float[] matrix) {
        mSceneHolderHandler.processBeforeDraw(matrixLocation, matrix);
        mScene.draw(matrixLocation, matrix);
        mSceneHolderHandler.processAfterDraw(matrixLocation, matrix);
    }

    /**
     * Scene holder thread.
     */
    private class SceneHolderThread extends Thread{
        @Override
        public void run() {
            mSceneHolderHandler.processLevelThread();
        }
    }

    /**
     * Scene holder handler interface.
     */
    public interface SceneHolderHandler {
        /**
         * Action before draw.
         * @param matrixLocation matrix location.
         * @param matrix model matrix.
         */
        void processBeforeDraw(final int matrixLocation, final float[] matrix);

        /**
         * Action after draw.
         * @param matrixLocation matrix location.
         * @param matrix model matrix.
         */
        void processAfterDraw(final int matrixLocation, final float[] matrix);

        /**
         * Touch action.
         * @param e motion event.
         */
        void processTouch(final MotionEvent e);

        /**
         * Body of level thread.
         */
        void processLevelThread();

        /**
         * Stops level thread.
         */
        void stopLevelThread();

        /**
         * Initialize.
         */
        void init();

        /**
         * Gets drawable scene.
         * @return scene.
         */
        DrawableScene getScene();

        /**
         * Gets textures to load.
         * @return textures to load.
         */
        List<TextureTemplate> getTexturesToLoad();
    }
}
