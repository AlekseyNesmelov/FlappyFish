package engine;

/**
 * Game constants.
 */
public class Const {
    // Vertex buffer size;
    public static final int VERTEX_BUFFER_SIZE = 60000;
    // Texture buffer size.
    public static final int TEXTURE_BUFFER_SIZE = 60000;

    // Square texture coordinates.
    public static final float[] SQUARE_TEXTURE_COORDINATES = new float[] {0, 1, 0, 0, 1, 0, 1, 1};

    // State numbers.
    public static final int STATE_MAIN_MENU = 0;
    public static final int STATE_SCORE = 1;
    public static final int STATE_LOADING = 2;
    public static final int STATE_LEVEL = 3;

    // Empty drawable object state.
    public static final String EMPTY_STATE = "";
    // Drag drawable object state.
    public static final String DRAG_ANIMATION = "drag";
    // Normal state.
    public static final String NORMAL_STATE = "normal";
    // Normal state animation.
    public static final String NORMAL_ANIMATION_STATE = "normal_animation";
    // Button pressed state.
    public static final String BUTTON_PRESSED_STATE = "press";
    // Full state.
    public static final String FULL_STATE = "full";

    // Max lives count.
    public static final int MAX_LIVES_COUNT = 3;
    // Min lives count.
    public static final int MIN_LIVES_COUNT = 0;

    // Delay between animation frames.
    public static final int ANIMATION_DELAY = 150;

    // Delay of red screen.
    public static final int RED_SCREEN_DELAY = 500;

    public static final float FISH_MOVE_RADIUS = 1;
    public static final float CENTER_X = -2;
    public static final float CENTER_Y = 0;
    public static final float ANGLE_FACTOR = (float)Math.PI / 180;
}
