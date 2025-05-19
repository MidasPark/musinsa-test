package com.musinsa.test.util;

import java.text.NumberFormat;
import java.util.Locale;

public class PriceFormatter {

    // 인스턴스 생성 방지
    private PriceFormatter() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String KorFormat(int price) {
        return NumberFormat.getNumberInstance(Locale.KOREA).format(price);
    }
}