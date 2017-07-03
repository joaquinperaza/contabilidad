package app.peraza.core.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Bogdan Melnychuk on 2/10/16.
 */
public final class NumberUtils {
    public static String formatAmount(Double amount) {
        return String.format("%.0f", amount);
    }

    public static String formatAmount(BigDecimal amount) {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("###.#",simbolos);
        return formateador.format(amount);

    }
}
