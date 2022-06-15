package com.queenieeq.mymoney.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.queenieeq.mymoney.firebase.models.Currency;
import com.queenieeq.mymoney.firebase.models.User;

public class CurrencyHelper {
    public static String formatCurrency(Currency currency, long money) {
        long absMoney = Math.abs(money);
        return (currency.left ? (currency.symbol + (currency.space ? " " : "")): "") +
                (money < 0 ? "-" : "") +
                (absMoney / 100) + "," +
                (absMoney % 100 < 10 ? "0" : "") +
                (absMoney % 100)  +
                (currency.left ? "" : ((currency.space ? " " : "") + currency.symbol));
    }
//    public static void printCurrency(double currencyAmount, String outputCurrency) {
//        Locale locale;
//
//        if (outputCurrency.equals("Yen")) {
//            locale = new Locale("jp", "JP");
//        } else if(outputCurrency.equals("Euros")) {
//            locale = new Locale("de", "DE");
//        } else if (outputCurrency.equals("Dollars")) {
//            locale = new Locale("en", "US");
//        } else {
//            locale = new Locale("en", "US");
//        }
//
//        Currency currency = Currency.getInstance(locale);
//        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
//
//        System.out.println(currency.getDisplayName() + ": " + numberFormat.format(currencyAmount));
//
//    }
//}
    public static void setupAmountEditText(EditText editText, User user) {
        editText.setText(CurrencyHelper.formatCurrency(user.currency,0), TextView.BufferType.EDITABLE);
        editText.addTextChangedListener(new TextWatcher() {
            private String current = "";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (!charSequence.toString().equals(current)) {
                    editText.removeTextChangedListener(this);
                    current = CurrencyHelper.formatCurrency(user.currency,convertAmountStringToLong(charSequence));
                    editText.setText(current);
                    editText.setSelection(current.length() -
                            (user.currency.left ? 0 : (user.currency.symbol.length() + (user.currency.space ? 1 : 0))));

                    editText.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public static long convertAmountStringToLong(CharSequence s) {
        String cleanString = s.toString().replaceAll("[^0-9]", "");
        return Long.valueOf(cleanString);
    }
}
