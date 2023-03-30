import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;

class NumberFormatterFactory extends DefaultFormatterFactory {
    private static final NumberFormatter numberFormatter = new NumberFormatter();
    static {
        numberFormatter.setValueClass(Integer.class);
        ((NumberFormat) numberFormatter.getFormat()).setGroupingUsed(false);
    }
    public NumberFormatterFactory() { super(numberFormatter, numberFormatter, numberFormatter); }
}