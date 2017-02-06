package engine;

import android.opengl.GLES20;

/**
 * Represents a shader program.
 */
public class Shader {
    public static final String VERTEX_SHADER =
            "attribute vec4 a_Position;\n" +
                    "uniform mat4 u_Matrix;\n" +
                    "attribute vec2 a_Texture;\n" +
                    "varying vec2 v_Texture;\n" +
                    "void main()\n" +
                    "{\n" +
                    "    gl_Position = u_Matrix * a_Position;\n" +
                    "    v_Texture = a_Texture;\n" +
                    "}";
    public static final String FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "uniform sampler2D u_TextureUnit;\n" +
                    "varying vec2 v_Texture;\n" +
                    "void main()\n" +
                    "{\n" +
                    "    gl_FragColor = texture2D(u_TextureUnit, v_Texture);\n" +
                    "}";

    /**
     * Creates shader program.
     * @param vertexShaderId OpenGL id of a vertex shader program.
     * @param fragmentShaderId OpenGL id of a fragment shader program.
     * @return OpenGL id of the shader program.
     */
    public static int createProgram(final int vertexShaderId, final int fragmentShaderId) {
        final int programId = GLES20.glCreateProgram();
        if (programId == 0) {
            return 0;
        }
        GLES20.glAttachShader(programId, vertexShaderId);
        GLES20.glAttachShader(programId, fragmentShaderId);
        GLES20.glLinkProgram(programId);
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(programId);
            return 0;
        }
        return programId;
    }

    /**
     * Creates shader.
     * @param type type of a shader.
     * @param shaderText text of a shader program.
     * @return OpenGL id of shader.
     */
    public static int createShader(final int type, final String shaderText) {
        final int shaderId = GLES20.glCreateShader(type);
        if (shaderId == 0) {
            return 0;
        }
        GLES20.glShaderSource(shaderId, shaderText);
        GLES20.glCompileShader(shaderId);
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == 0) {
            GLES20.glDeleteShader(shaderId);
            return 0;
        }
        return shaderId;
    }

    /**
     * Constructor is private.
     */
    private Shader() {
    }
}
