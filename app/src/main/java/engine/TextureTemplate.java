package engine;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Animation template structure.
 */
public class TextureTemplate {
    // Simple texture type.
    public static final int SIMPLE_TEXTURE = 0;
    // Animation texture type.
    public static final int ANIMATION_TEXTURE = 1;
    // Texture name.
    private String mName;
    // Bitmap to load texture.
    private Bitmap mBitmap;
    // Texture owners.
    private List<DrawableObject> mOwners;
    // Type of template.
    private int mType;

    /**
     * Texture template constructor.
     * @param name animation name.
     * @param type type of template.
     * @param bitmap bitmap to load texture.
     * @param owners animation owners.
     */
    public TextureTemplate(final String name, final int type,
                           final Bitmap bitmap, final List<DrawableObject> owners) {
        mName = name;
        mType = type;
        mBitmap = bitmap;
        mOwners = owners;
    }

    /**
     * Texture template constructor.
     * @param name animation name.
     * @param type type of template.
     * @param bitmap bitmap to load texture.
     * @param owner animation owner.
     */
    public TextureTemplate(final String name, final int type,
                           final Bitmap bitmap, final DrawableObject owner) {
        mName = name;
        mType = type;
        mBitmap = bitmap;
        mOwners = new ArrayList<>();
        mOwners.add(owner);
    }

    /**
     * Gets texture name.
     * @return texture name.
     */
    public String getName() {
        return mName;
    }

    /**
     * Gets texture type.
     * @return texture type.
     */
    public int getType() {
        return mType;
    }

    /**
     * Gets texture bitmap.
     * @return texture bitmap.
     */
    public Bitmap getBitmap() {
        return mBitmap;
    }

    /**
     * Gets texture owners.
     * @return texture owners.
     */
    public List<DrawableObject> getOwners() {
        return mOwners;
    }
}
