public class GameEngine {
    static final int MODE_TITLE = 0;
    static final int MODE_BJ = 1;
    private final int mode;
    private TitleEngine titleEngine;
    private BjEngine bjEngine;
    public GameEngine(int mode) {
        this.mode = mode;

        switch (this.mode) {
            case MODE_TITLE -> titleEngine = new TitleEngine();
            case MODE_BJ -> bjEngine = new BjEngine();
            default -> {
            }
        }
    }

    public Object getEngine() {
        return switch (mode) {
            // case MODE_TITLE -> titleEngine;
            case MODE_BJ -> bjEngine;
            default -> false;
        };
    }
}
