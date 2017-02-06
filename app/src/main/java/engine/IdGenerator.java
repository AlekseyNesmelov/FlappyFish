package engine;

/**
 * Id generator class.
 */
public class IdGenerator {
    // Id counter.
    private static int mIdCount = 0;

    /**
     * Generates new id.
     * @return generated id.
     */
    public static synchronized int getId() {
        return mIdCount++;
    }
}
