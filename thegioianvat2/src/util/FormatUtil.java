package util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatUtil {
    private static final DecimalFormat moneyFormat = new DecimalFormat("#,###");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final Locale localeVN = new Locale("vi", "VN");
    private static final NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);

    public static String formatCurrency(double amount) {
        return currencyVN.format(amount).replace("₫", "VNĐ");
    }

    public static String formatVND(double amount) {
        return formatCurrency(amount);
    }

    public static String formatMoney(double amount) {
        return moneyFormat.format(amount);
    }

    public static String formatDate(Date date) {
        if (date == null) return "";
        return dateFormat.format(date);
    }
}