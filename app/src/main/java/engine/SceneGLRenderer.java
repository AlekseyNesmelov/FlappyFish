package engine;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * OpenGL renderer class.
 */
public class SceneGLRenderer implements GLSurfaceView.Renderer {
    private Game mGame;
    private Context mContext;
    private FloatBuffer mVertexData;
    private FloatBuffer mTextureCoordinates;
    private float[] mMatrix = new float[16];
    private int mMatrixLocation;
    private int mPositionLocation;
    private int mATextureLocation;
    private int mUTextureUnitLocation;
    private int mProgramId;

    private final Object mLock = new Object();

    /**
     * Gl renderer constructor.
     * @param context application context.
     */
    public SceneGLRenderer(final Context context) {
        mContext = context;
    }

    @Override
    public void onDrawFrame(final GL10 arg0) {
        synchronized (mLock) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            mGame.checkState(mVertexData, mTextureCoordinates);
            mGame.draw(mMatrixLocation, mMatrix);
        }
    }

    @Override
    public void onSurfaceCreated(final GL10 arg0, final EGLConfig arg1) {
        GLES20.glClearColor(1f, 1f, 1f, 1f);
        final int vertexShaderId = Shader.createShader(GLES20.GL_VERTEX_SHADER, Shader.VERTEX_SHADER);
        final int fragmentShaderId = Shader.createShader(GLES20.GL_FRAGMENT_SHADER, Shader.FRAGMENT_SHADER);
        mProgramId = Shader.createProgram(vertexShaderId, fragmentShaderId);
        GLES20.glUseProgram(mProgramId);
        prepareBuffers();
        bindData();
        mGame = new Game(mContext, mLock);
    }

    @Override
    public void onSurfaceChanged(final GL10 arg0, final int width, final int height) {
        GLES20.glViewport(0, 0, width, height);
        mGame.recalculateProjection(width, height);
        mGame.setUpGame();
    }

    /**
     * Touch event.
     * @param e motion event.
     */
    public void touchEvent(final MotionEvent e) {
        mGame.touchEvent(e);
    }

    /**
     * Prepares buffers.
     */
    private void prepareBuffers() {
        mVertexData = ByteBuffer
                .allocateDirect(Const.VERTEX_BUFFER_SIZE * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        mTextureCoordinates = ByteBuffer
                .allocateDirect(Const.TEXTURE_BUFFER_SIZE * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
    }

    /**
     * Binds shader data.
     */
    private void bindData(){
        mPositionLocation = GLES20.glGetAttribLocation(mProgramId, "a_Position");
        mMatrixLocation = GLES20.glGetUniformLocation(mProgramId, "u_Matrix");
        mATextureLocation = GLES20.glGetAttribLocation(mProgramId, "a_Texture");
        mUTextureUnitLocation = GLES20.glGetUniformLocation(mProgramId, "u_TextureUnit");
        mPositionLocation = GLES20.glGetAttribLocation(mProgramId, "a_Position");

        mVertexData.position(0);
        GLES20.glVertexAttribPointer(mPositionLocation, 2, GLES20.GL_FLOAT,
                false, 0, mVertexData);
        GLES20.glEnableVertexAttribArray(mPositionLocation);

        mTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(mATextureLocation, 2, GLES20.GL_FLOAT,
                false, 0, mTextureCoordinates);
        GLES20.glEnableVertexAttribArray(mATextureLocation);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUniform1i(mUTextureUnitLocation, 0);
    }
}

