package com.nidjo123.treasury.util;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;

/**
 * This class contains useful methods for handling currency, for example properly formatting BigDecimal into String and so on.
 */
public class CurrencyUtil {

    private static NumberFormat formatter = NumberFormat.getCurrencyInstance();

    static {
        formatter.setCurrency(Currency.getInstance("HRK"));
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
    }

    /**
     * Returns new instance of TextFormatter with default value of zero for formatting BigDecimals into currency representation.
     *
     * @return Returns new instance of TextFormatter for formatting BigDecimals into currency representation.
     */
    public static TextFormatter currencyFormatter() {
        return currencyFormatter(BigDecimal.ZERO);
    }

    /**
     * Returns new instance of TextFormatter with default value for formatting BigDecimals into currency representation.
     *
     * @param defaultValue default value for the formatter.
     * @return Returns new instance of TextFormatter with default value for formatting BigDecimals into currency representation.
     */
    public static TextFormatter  currencyFormatter(BigDecimal defaultValue) {
        StringConverter<BigDecimal> stringConverter = new StringConverter<BigDecimal>() {
            @Override
            public String toString(BigDecimal object) {
                return CurrencyUtil.format(object).toString();
            }

            @Override
            public BigDecimal fromString(String string) {
                return new BigDecimal(string);
            }
        };

        return new TextFormatter(stringConverter, defaultValue);
    }

    /**
     * Returns {@link NumberFormat} instance that correctly formats HRK currency.
     *
     * @return Returns {@link NumberFormat} instance that correctly formats HRK currency.
     */
    public static NumberFormat getHRKFormatter() {
        return formatter;
    }

    /**
     * Returns String containing properly formatted amount of money.
     *
     * @param x a number.
     * @return Returns properly formatted amount of money.
     */
    public static String format(Number x) {
        return formatter.format(x);
    }

    /**
     * Returns String containing properly formatted amount of money.
     *
     * @param x a {@link BigDecimal}.
     * @return Returns properly formatted amount of money.
     */
    public static String format(BigDecimal x) {
        return formatter.format(x);
    }
}
