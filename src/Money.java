
public class Money {
    // member
    private int value;
    private final int max = 99999;
    private final int min = 0;

    public Money() {
        // for dealer
        this.value = 0;
    }

    public Money(int initValue) {
        this.value = initValue;
        normalize(this.value);
    }

    public void addition(int value) {
        this.value += value;
        normalize(this.value);
    }

    public void subtraction(int value) {
        this.value -= value;
        normalize(this.value);
    }

    private int normalize(int currentValue) {
        int fixedValue = currentValue;
        if (fixedValue > max)
            fixedValue = max;
        if (min > fixedValue)
            fixedValue = min;
        return fixedValue;
    }

    public int getValue() {
        return value;
    }
}
