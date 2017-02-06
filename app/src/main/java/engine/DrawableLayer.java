package engine;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Drawable layer class.
 */
public class DrawableLayer {
    // Layer objects.
    private final List<DrawableObject> mObjects = new ArrayList<>();

    /**
     * Adds object to layer.
     * @param object object to add.
     */
    public void add(final DrawableObject object) {
        mObjects.add(object);
    }

    /**
     * Puts layer to buffers.
     * @param startPos start pos in buffer.
     * @param vertexBuffer OpenGL vertex buffer.
     * @param textureCoordinates OpenGL texture coordinates.
     * @return new start pos in buffer.
     */
    public int putToBuffer(final int startPos, final FloatBuffer vertexBuffer, final FloatBuffer textureCoordinates) {
        int pos = startPos;
        for (final DrawableObject object : mObjects) {
            pos = object.putToBuffer(pos, vertexBuffer, textureCoordinates);
        }
        return pos;
    }

    /**
     * Draws layer.
     * @param matrixLocation OpenGL matrix location.
     * @param modelMatrix OpenGL model matrix.
     */
    public void draw(final int matrixLocation, final float[] modelMatrix) {
        for (final DrawableObject object : mObjects) {
            object.draw(matrixLocation, modelMatrix);
        }
    }

    /**
     * Gets texture ids of layer.
     * @return texture ids of layer.
     */
    public List<Integer> getTextureIds() {
        final List<Integer> textureIds = new ArrayList<>();
        for (final DrawableObject obj : mObjects) {
            textureIds.addAll(obj.getTextureIds());
        }
        return textureIds;
    }
}
