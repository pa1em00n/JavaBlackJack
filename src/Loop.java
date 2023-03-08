@SuppressWarnings("ALL")
public class Loop implements Runnable {
    private final GameWindow window;
    private final GameEngine engine;
    public Loop() {
        window = new GameWindow();
        engine = new GameEngine(GameEngine.MODE_TITLE);
    }

    @Override
    public void run() {
        while (true) {
            try {
                /* method */
                window.redraw(engine);
                /* wait */
                Thread.sleep(16);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

}
