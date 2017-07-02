package app.peraza.view.numpad;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import app.peraza.R;
import app.peraza.impl.AppPreferences;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public class NumpadView extends LinearLayout implements View.OnClickListener {
    public interface NumpadClickListener {
        boolean onNumberClicked(int value);

        boolean onClearClicked();

        boolean onDecimalClicked();

        boolean onClearLongClicked();
    }

    private NumpadClickListener numpadClickListener;
    private NumpadEditable attachedEditable;
    private NumpadValidator validator;

    public NumpadView(Context context) {
        super(context);
        init();
    }

    public NumpadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NumpadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NumpadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View parent = inflater.inflate(app.peraza.R.layout.view_numpad, this, true);

        parent.findViewById(app.peraza.R.id.btn1).setOnClickListener(this);
        parent.findViewById(app.peraza.R.id.btn2).setOnClickListener(this);
        parent.findViewById(app.peraza.R.id.btn3).setOnClickListener(this);
        parent.findViewById(app.peraza.R.id.btn4).setOnClickListener(this);
        parent.findViewById(app.peraza.R.id.btn5).setOnClickListener(this);
        parent.findViewById(app.peraza.R.id.btn6).setOnClickListener(this);
        parent.findViewById(app.peraza.R.id.btn7).setOnClickListener(this);
        parent.findViewById(app.peraza.R.id.btn8).setOnClickListener(this);
        parent.findViewById(app.peraza.R.id.btn9).setOnClickListener(this);
        parent.findViewById(app.peraza.R.id.btn0).setOnClickListener(this);
        parent.findViewById(app.peraza.R.id.btnDecimal).setOnClickListener(this);
        View clearButton = parent.findViewById(app.peraza.R.id.btnClear);
        clearButton.setOnClickListener(this);
        clearButton.setOnLongClickListener(v -> {
            if (numpadClickListener != null) {
                numpadClickListener.onClearLongClicked();
            }
            if (attachedEditable != null) {
                attachedEditable.setText("");
            }
            return true;
        });
    }

    public void setNumpadClickListener(NumpadClickListener numpadClickListener) {
        this.numpadClickListener = numpadClickListener;
    }

    public void attachEditable(NumpadEditable attachedEditable, NumpadValidator validator) {
        this.attachedEditable = attachedEditable;
        this.validator = validator;
    }

    public void attachEditable(NumpadEditable attachedEditable) {
        attachEditable(attachedEditable, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.peraza.R.id.btn0:
                onNumberClicked(0);
                break;
            case app.peraza.R.id.btn1:
                onNumberClicked(1);
                break;
            case app.peraza.R.id.btn2:
                onNumberClicked(2);
                break;
            case app.peraza.R.id.btn3:
                onNumberClicked(3);
                break;
            case app.peraza.R.id.btn4:
                onNumberClicked(4);
                break;
            case app.peraza.R.id.btn5:
                onNumberClicked(5);
                break;
            case app.peraza.R.id.btn6:
                onNumberClicked(6);
                break;
            case app.peraza.R.id.btn7:
                onNumberClicked(7);
                break;
            case app.peraza.R.id.btn8:
                onNumberClicked(8);
                break;
            case app.peraza.R.id.btn9:
                onNumberClicked(9);
                break;
            case app.peraza.R.id.btnClear:
                onClearClicked();
                break;
            case app.peraza.R.id.btnDecimal:
                onDecimalCLicked();
                break;
        }
    }

    private void onNumberClicked(int i) {
        if (numpadClickListener != null) {
            numpadClickListener.onNumberClicked(i);
        }
        if (attachedEditable != null) {
            String textBefore = attachedEditable.getText();
            String curr= new AppPreferences(getContext()).getCurrency();
            if(textBefore.equals("")){

                String label;
                if (curr.equals("a")){label= getContext().getString(R.string.label_currencya);} else {label= getContext().getString(R.string.label_currencyb);}
                textBefore=label+" ";

            }
try {
    if (curr.equals("a") && Integer.valueOf(textBefore.substring(4, textBefore.length())) == 0) {
        textBefore = "USD ";
    } else if (curr.equals("b") && Integer.valueOf(textBefore.substring(2, textBefore.length())) == 0) {
        textBefore = "$ ";
    }
} catch (Exception e){}


            updateAttachedView(textBefore + i);
        }
    }

    private void onDecimalCLicked() {
        if (numpadClickListener != null) {
            numpadClickListener.onDecimalClicked();
        }
        if (attachedEditable != null && attachedEditable.getText().length()>1) {
            String textBefore = attachedEditable.getText();
            String textAfter;
            String curr= new AppPreferences(getContext()).getCurrency();
            String newC;
            if (curr.equals("a")){
                textAfter= textBefore.substring(3,textBefore.length());
                newC="b";} else {
                textAfter= textBefore.substring(1,textBefore.length());
                newC="a";}
            new AppPreferences(getContext()).setCurrency(newC);
            String label;

            if (newC.equals("a")){label= getContext().getString(R.string.label_currencya);} else {label= getContext().getString(R.string.label_currencyb);}


            updateAttachedView(label+textAfter);

        }
    }

    private void onClearClicked() {
        if (numpadClickListener != null) {
            numpadClickListener.onClearClicked();
        }
        if (attachedEditable != null) {
            String textBefore = attachedEditable.getText();
            int min;
            String curr= new AppPreferences(getContext()).getCurrency();

            if (curr.equals("a")){
                min= 4;
            } else {
                min = 2;
            }
            if (textBefore.length() > min) {
                String textAfter = textBefore.substring(0, textBefore.length() - 1);
                updateAttachedView(textAfter, true);
            }
        }
    }

    private void updateAttachedView(String str) {
        this.updateAttachedView(str, true);
    }

    private void updateAttachedView(String str, boolean useValidator) {
        if(useValidator && validator != null) {
            String curr= new AppPreferences(getContext()).getCurrency();
            if (validator.valid(str,curr)) {
                String textAfter;
                String textBefore;
                if (curr.equals("a")){
                    textAfter= str.substring(4,str.length());
                    textBefore = str.substring(0,4);
                } else {
                    textAfter= str.substring(2,str.length());
                    textBefore = str.substring(0,2);
                }
                String finaltxtafter = textAfter.replace(".","");
                Integer number = Integer.valueOf(finaltxtafter);

                String str3 = textBefore.concat(String.format("%,d", number));
                String str2 = str3.replaceAll(",",".");

                Log.e("tryadded", str2);
                if (validator.valid(str2,curr)){
                    Log.e("added", str2);
                attachedEditable.setText(str2);}

            } else {

                validator.onInvalidInput(str);
                try {boolean textAfter;


                    if (curr.equals("a")){
                        textAfter= str.length()==4;
                    } else {
                        textAfter= str.length()==2;
                    }

                    if(textAfter){
                        updateAttachedView(str+"0");
                    }

                } catch (Exception e) {

                }


            }
        } else {
            attachedEditable.setText(str);
            Log.e("added2", str);
        }
    }
}
