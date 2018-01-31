package falezza.fabio.ransomoid.utils;

/**
 * Created by fabio on 31/01/18.
 */

public class Util {
    private static Util instance;

    private Util() {
    }

    public Util getInstance() {
        if (instance == null) {
            instance = new Util();
        }
        return instance;
    }
}
