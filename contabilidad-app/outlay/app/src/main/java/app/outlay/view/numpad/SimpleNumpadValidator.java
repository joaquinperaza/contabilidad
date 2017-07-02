package app.outlay.view.numpad;

import android.text.TextUtils;

import app.outlay.impl.AppPreferences;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public class SimpleNumpadValidator implements NumpadValidator {
    @Override
    public boolean valid(String value,String curr) {
        if (TextUtils.isEmpty(value) || value.length() > 10) {
            return false;
        }
        try {String textAfter;


            if (curr.equals("a")){
                textAfter= value.substring(4,value.length());
                } else {
                textAfter= value.substring(2,value.length());
                }
            String finaltxt = textAfter.replace(",","");
            Double.valueOf(finaltxt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onInvalidInput(String value) {

    }
}
