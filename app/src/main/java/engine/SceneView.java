package engine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * OpenGL GL Surface View.
 */
public class SceneView extends GLSurfaceView {
    private SceneGLRenderer mRenderer;

    /**
     * Scene view constructor.
     * @param context application context.
     */
    public SceneView(final Context context) {
        super(context);
        init();
    }

    /**
     * Scene view constructor.
     * @param context application context.
     * @param attrs attribute set.
     */
    public SceneView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public boolean onTouchEvent(final MotionEvent e) {
        mRenderer.touchEvent(e);
        return true;
    }

    /**
     * Initiates scene view.
     */
    private void init() {
        setEGLContextClientVersion(2);
        mRenderer = new SceneGLRenderer(getContext());
        setRenderer(mRenderer);
    }
}
