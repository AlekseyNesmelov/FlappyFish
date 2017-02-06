package engine;

/**
 * Game events listener.
 */
public interface GameListener {
    /**
     * Level loading completed.
     */
    void onLevelLoaded();

    /**
     * Level mistake.
     */
    void onLevelMistake();

    /**
     * Return to menu from level.
     */
    void onReturnToMenu();

    /**
     * Start button pressed.
     */
    void onStartButtonPressed();

    /**
     * Menu loaded.
     */
    void onMenuLoaded();

    /**
     * Key pressed.
     */
    void onKeyPressed();
}
