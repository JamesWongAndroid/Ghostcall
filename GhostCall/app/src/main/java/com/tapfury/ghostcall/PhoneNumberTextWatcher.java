package com.tapfury.ghostcall;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Ynott on 7/15/15.
 */
public class PhoneNumberTextWatcher implements TextWatcher {

    private static final String TAG = PhoneNumberTextWatcher.class.getSimpleName();
    private EditText editText;
    private boolean isDelete;

    public PhoneNumberTextWatcher(EditText eTxtPhone) {
        this.editText = eTxtPhone;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        String val = editable.toString();
        String a = "";
        String b = "";
        String c = "";
        if (val != null && val.length() > 0) {
            val = val.replace("-", "");
            if (val.length() >= 3) {
                a = val.substring(0, 3);
            } else if (val.length() < 3) {
                a = val.substring(0, val.length());
            }
            if (val.length() >= 6) {
                b = val.substring(3, 6);
                c = val.substring(6, val.length());
            } else if (val.length() > 3 && val.length() < 6) {
                b = val.substring(3, val.length());
            }
            StringBuffer stringBuffer = new StringBuffer();
            if (a != null && a.length() > 0) {
                stringBuffer.append(a);
                if (a.length() == 3) {
                    stringBuffer.append("-");
                }
            }
            if (b != null && b.length() > 0) {
                stringBuffer.append(b);
                if (b.length() == 3) {
                    stringBuffer.append("-");
                }
            }
            if (c != null && c.length() > 0) {
                stringBuffer.append(c);
            }
            editText.removeTextChangedListener(this);
            editText.setText(stringBuffer.toString());
            editText.setSelection(editText.getText().toString().length());
            editText.addTextChangedListener(this);
        } else {
            editText.removeTextChangedListener(this);
            editText.setText("");
            editText.addTextChangedListener(this);
        }
    }
}
