package engine;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Drawable object class.
 */
public class DrawableObject {
    // Sprite type.
    public static final int SQUARE_SPRITE = 0;
    public static final int NORMAL_SPRITE = 1;

    // Object id.
    private int mId;

    // X coordinate.
    private float mX;
    // Y coordinate.
    private float mY;
    // Width.
    private float mWidth;
    // Height.
    private float mHeight;

    // Vertex array.
    private float[] mVertices;
    // Buffer position.
    private int mBufferPos;

    // Drawable object textures.
    private Map<String, Integer> mTextures = new HashMap<>();
    // Animation parameters.
    private Map<String, List<Integer>> mAnimations = new HashMap<>();
    private int mAnimationIndex = 0;
    private long mAnimationTime = 0;
    private boolean mLooped = false;

    // Object states.
    private String mState = Const.EMPTY_STATE;
    private String mAnimationState = Const.EMPTY_STATE;

    // Objects properties.
    private boolean mVisible = true;

    // Synchronized semaphore.
    private final Object mLock = new Object();

    /**
     * Drawable object constructor.
     * @param width width.
     * @param height height.
     * @param type square type.
     */
    public DrawableObject(final float width, final float height, final int type) {
        synchronized (mLock) {
            mId = IdGenerator.getId();

            mWidth = width;
            mHeight = height;

            if (type == SQUARE_SPRITE) {
                mWidth*= Game.getScreenXFactor();
                mHeight*= Game.getScreenYFactor();
            }

            mVertices = new float[]{
                    -mWidth / 2, -mHeight / 2,
                    -mWidth / 2, mHeight / 2,
                    mWidth / 2, mHeight / 2,
                    mWidth / 2, -mHeight / 2
            };
        }
    }

    @Override
    public boolean equals(final Object obj) {
        final DrawableObject second = (DrawableObject)obj;
        return mId == second.mId;
    }

    /**
     * Puts object to buffers.
     * @param pos start pos in buffer.
     * @param buffer OpenGL vertex buffer.
     * @param textureCoordinates OpenGL texture coordinates.
     * @return new start pos in buffer.
     */
    public int putToBuffer(final int pos, final FloatBuffer buffer, final FloatBuffer textureCoordinates) {
        mBufferPos = pos;
        buffer.put(mVertices);
        textureCoordinates.put(Const.SQUARE_TEXTURE_COORDINATES);
        return pos + mVertices.length / 2;
    }

    /**
     * Sets "visible" flag.
     * @param visible true if object is visible.
     */
    public void setVisible(final boolean visible) {
        synchronized (mLock) {
            mVisible = visible;
        }
    }

    /**
     * Returns true if object is visible.
     * @return true if object is visible.
     */
    public boolean isVisible() {
        return mVisible;
    }

    /**
     * Draws the object.
     * @param matrixLocation OpenGL matrix location.
     * @param modelMatrix OpenGL model matrix.
     */
    public void draw(final int matrixLocation, final float[] modelMatrix) {
        synchronized (mLock) {
            if (mVisible) {
                Integer texture = mTextures.get(mState);
                if (!mAnimationState.equals(Const.EMPTY_STATE)) {
                    final List<Integer> animationTexture = mAnimations.get(mAnimationState);
                    if (animationTexture != null && 0 <= mAnimationIndex && mAnimationIndex < animationTexture.size()) {
                        texture = animationTexture.get(mAnimationIndex);
                        final long time = System.currentTimeMillis();
                        if (time - mAnimationTime > Const.ANIMATION_DELAY) {
                            mAnimationIndex++;
                            if (mAnimationIndex >= animationTexture.size()) {
                                mAnimationIndex = 0;
                                if (!mLooped) {
                                    stopAnimation();
                                }
                            }
                            mAnimationTime = time;
                        }
                    }
                }
                if (texture != null) {
                    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);

                    Matrix.setIdentityM(modelMatrix, 0);
                    Matrix.translateM(modelMatrix, 0, mX, mY, 0);
                    GLES20.glUniformMatrix4fv(matrixLocation, 1, false, modelMatrix, 0);
                    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, mBufferPos, 4);
                }
            }
        }
    }

    /**
     * Sets x.
     * @param x x to set.
     */
    public void setX(final float x) {
        synchronized (mLock) {
            mX = x;
        }
    }

    /**
     * Sets y.
     * @param y y to set.
     */
    public void setY(final float y) {
        synchronized (mLock) {
            mY = y;
        }
    }

    /**
     * Gets object x.
     * @return object x.
     */
    public float getX() {
        return mX;
    }

    /**
     * Gets object y.
     * @return object y.
     */
    public float getY() {
        return mY;
    }

    /**
     * Adds texture.
     * @param name texture name.
     * @param textureId texture id to set.
     */
    public void addTexture(final String name, final int textureId) {
        synchronized (mLock) {
            if (mTextures.isEmpty()) {
                mState = name;
            }
            mTextures.put(name, textureId);
        }
    }

    /**
     * Gets texture ids.
     * @return texture ids.
     */
    public List<Integer> getTextureIds() {
        final List<Integer> textureIds = new ArrayList<>();
        for (final String texture : mTextures.keySet()) {
            textureIds.add(mTextures.get(texture));
        }
        for (final String animation : mAnimations.keySet()) {
            textureIds.addAll(mAnimations.get(animation));
        }
        return textureIds;
    }

    /**
     * Adds object animation.
     * @param animationName animation name.
     * @param animationTextureIds list of animation textures.
     */
    public void addAnimation(final String animationName, final List<Integer> animationTextureIds) {
        synchronized (mLock) {
            mAnimations.put(animationName, animationTextureIds);
        }
    }

    /**
     * Stops animation.
     */
    public void stopAnimation() {
        synchronized (mLock) {
            mAnimationState = Const.EMPTY_STATE;
            mLooped = false;
            mAnimationIndex = 0;
        }
    }

    /**
     * Starts animation with looping.
     * @param animationName animation name.
     */
    public void animateLoop(final String animationName) {
        synchronized (mLock) {
            if (!animationName.equals(mAnimationState)) {
                mAnimationIndex = 0;
            }
            mAnimationState = animationName;
            mLooped = true;
        }
    }

    /**
     * Animate one time.
     * @param animationName animation name.
     */
    public void animate(final String animationName) {
        synchronized (mLock) {
            mState = animationName;
            mLooped = false;
            mAnimationIndex = 0;
        }
    }

    /**
     * Sets state.
     * @param name name of state.
     */
    public void setState(final String name) {
        synchronized (mLock) {
            mState = name;
        }
    }

    /**
     * Returns true if point is inside of object.
     * @param x point x.
     * @param y point y.
     * @return true if point is inside of object.
     */
    public boolean isInside (final float x, final float y) {
        return mX - mWidth / 2 <= x && x <= mX + mWidth / 2 &&
                mY - mHeight / 2 <= y && y <= mY + mHeight / 2;
    }
}
