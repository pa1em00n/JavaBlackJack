
public class Money {
    // member
    private int value;

    /* コンストラクタ */
    public Money() { this.value = 0; }
    public Money(int initValue) { normalize(this.value = initValue); }

    /* 増減 */
    public void addition(int mod) { normalize(value += mod); }
    public void subtraction(int mod) { normalize(value -= mod); }

    /* 正規範囲に修正 */
    private void normalize(int currentValue) {
        final int max = 99999;
        final int min = 0;
        int fixedValue = currentValue;
        if (fixedValue > max)
            fixedValue = max;
        if (min > fixedValue)
            fixedValue = min;
        value = fixedValue;
    }

    // setter
        ;
    // getter
    public int getValue() { return value; }
}
