package engine;

import android.view.MotionEvent;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Drawable scene class.
 */
public class DrawableScene {
    // Layers list.
    private final List<DrawableLayer> mLayers = new ArrayList<>();

    /**
     * Drawable scene constructor.
     * @param layerCount layer count.
     */
    public DrawableScene(final int layerCount) {
        for (int i = 0; i < layerCount; i++) {
            mLayers.add(new DrawableLayer());
        }
    }

    /**
     * Puts scene to buffer.
     * @param startPos start pos in buffer.
     * @param vertexBuffer OpenGL vertex buffer.
     * @param textureCoordinates OpenGL texture coordinates.
     * @return new start pos in buffer.
     */
    public int putToBuffer(final int startPos, final FloatBuffer vertexBuffer, final FloatBuffer textureCoordinates) {
        int pos = startPos;
        for (final DrawableLayer layer : mLayers) {
            pos = layer.putToBuffer(pos, vertexBuffer, textureCoordinates);
        }
        return pos;
    }

    /**
     * Draws scene.
     * @param matrixLocation OpenGL matrix location.
     * @param modelMatrix OpenGL model matrix.
     */
    public void draw(final int matrixLocation, final float[] modelMatrix) {
        for (final DrawableLayer layer : mLayers) {
            layer.draw(matrixLocation, modelMatrix);
        }
    }

    /**
     * Adds object to layer.
     * @param layerNum layer number.
     * @param object object to add.
     */
    public void addToLayer(final int layerNum, final DrawableObject object) {
        if (-1 < layerNum && layerNum < mLayers.size()) {
            final DrawableLayer layer = mLayers.get(layerNum);
            layer.add(object);
        }
    }

    /**
     * Gets textures.
     * @return texture ids.
     */
    public int[] getTextures() {
        final List<Integer> textureIds = new ArrayList<>();
        for (final DrawableLayer layer : mLayers) {
            textureIds.addAll(layer.getTextureIds());
        }
        int count = 0;
        final int[] textures = new int[textureIds.size()];
        for (int i = 0; i < textureIds.size(); i++) {
            textures[count++] = textureIds.get(i);
        }
        return textures;
    }
}
