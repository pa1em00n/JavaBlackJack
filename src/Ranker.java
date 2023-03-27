public class Ranker {
    private int rank;
    private final String name;
    private final int win;
    private final int lose;
    private final int money;

    public Ranker(int rank, String name, int win, int lose, int money) {
        this.rank = rank;
        this.name = name;
        this.win = win;
        this.lose = lose;
        this.money = money;
    }
    public int getRank() { return rank; }
    public String getName() { return name; }
    public int getWin() { return win; }
    public int getLose() { return lose; }
    public int getMoney() { return money; }
    public void rankDown() { ++rank; }
}
