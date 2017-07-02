package app.peraza.core.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Bogdan Melnychuk on 2/10/16.
 */
public final class NumberUtils {
    public static String formatAmount(Double amount) {
        return String.format("%.2f", amount);
    }

    public static String formatAmount(BigDecimal amount) {
        return NumberFormat.getNumberInstance(Locale.US).format(amount);

    }
}
