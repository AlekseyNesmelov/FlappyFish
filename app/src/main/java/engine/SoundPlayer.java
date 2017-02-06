package engine;

import android.content.Context;
import android.media.MediaPlayer;

import com.flappyfishgame.R;

/**
 * Sound player class.
 */
public class SoundPlayer {
    private static MediaPlayer mp;
    private static MediaPlayer mp2;

    /**
     * Starts playing background music.
     * @param context application context.
     */
    public static void playBackgroundMusic(final Context context) {
        if (mp != null && mp.isPlaying()) {
            mp.stop();
        }
        mp = MediaPlayer.create(context, R.raw.water);
        mp.setLooping(true);
        mp.start();
    }

    /**
     * Stops playing background music.
     */
    public static void stopBackgroundMusic() {
        if (mp != null && mp.isPlaying()) {
            mp.stop();
        }
    }

    /**
     * Plays water bubbles sound.
     * @param context application context.
     */
    public static void playWaterBubblesSound(final Context context) {
        if (mp2 == null || mp2 != null && !mp2.isPlaying()) {
            mp2 = MediaPlayer.create(context, R.raw.water_bubbles);
            mp2.start();
        }
    }

    /**
     * Plays click sound.
     * @param context application context.
     */
    public static void playClickSound(final Context context) {
        MediaPlayer.create(context, R.raw.click).start();
    }

    /**
     * Plays mistake sound.
     * @param context application context.
     */
    public static void playMistakeSound(final Context context) {
        MediaPlayer.create(context, R.raw.mistake).start();
    }

    /**
     * Plays win sound.
     * @param context application context.
     */
    public static void playWinSound(final Context context) {
        MediaPlayer.create(context, R.raw.win).start();
    }
}
