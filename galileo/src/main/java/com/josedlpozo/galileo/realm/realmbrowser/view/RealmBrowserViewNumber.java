/*
 * The MIT License (MIT)
 *
 * Original Work: Copyright (c) 2015 Danylyk Dmytro
 *
 * Modified Work: Copyright (c) 2015 Rottmann, Jonas
 *
 * Modified Work: Copyright (c) 2018 vicfran
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.josedlpozo.galileo.realm.realmbrowser.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import com.josedlpozo.galileo.R;
import com.josedlpozo.galileo.realm.realmbrowser.helper.Utils;
import io.realm.DynamicRealmObject;
import io.realm.RealmObjectSchema;
import java.lang.reflect.Field;

import static android.graphics.PorterDuff.Mode.SRC_ATOP;
import static android.support.v4.content.ContextCompat.getColor;
import static android.support.v4.content.ContextCompat.getDrawable;

class RealmBrowserViewNumber extends RealmBrowserViewField {
    private boolean isInputValid = true;
    private EditText fieldEditText;

    public RealmBrowserViewNumber(Context context, @NonNull RealmObjectSchema realmObjectSchema, @NonNull Field field) {
        super(context, realmObjectSchema, field);
        if (!Utils.isNumber(getField())) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void inflateViewStub() {
        ViewStub stub = (ViewStub) findViewById(R.id.realm_browser_stub);
        stub.setLayoutResource(R.layout.realm_browser_fieldview_edittext);
        stub.inflate();
    }

    @Override
    public void initViewStubView() {
        fieldEditText = (EditText) findViewById(R.id.realm_browser_field_edittext);
        fieldEditText.setMaxLines(1);
        fieldEditText.addTextChangedListener(createTextWatcher());
        if (Utils.isDouble(getField()) || Utils.isFloat(getField())) {
            fieldEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        } else {
            fieldEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        }
    }

    @Override
    public Object getValue() {
        if (getRealmObjectSchema().isNullable(getField().getName()) && isFieldIsNullCheckBoxChecked()) {
            return null;
        }

        String value = fieldEditText.getText().toString();
        if (value.isEmpty()) {
            value = "0";
        }

        if (Utils.isByte(getField())) {
            return Byte.parseByte(value);
        } else if (Utils.isShort(getField())) {
            return Short.parseShort(value);
        } else if (Utils.isInteger(getField())) {
            return Integer.parseInt(value);
        } else if (Utils.isLong(getField())) {
            return Long.parseLong(value);
        } else if (Utils.isDouble(getField())) {
            return Double.parseDouble(value);
        } else if (Utils.isFloat(getField())) {
            return Float.parseFloat(value);
        }
        return null;
    }

    @Override
    public void toggleAllowInput(boolean allow) {
        fieldEditText.setEnabled(allow);
    }

    @Override
    public boolean isInputValid() {
        return isInputValid;
    }

    @Override
    public void setRealmObject(@NonNull DynamicRealmObject realmObject) {
        if (Utils.isNumber(getField())) {
            fieldEditText.setText(realmObject.get(getField().getName()).toString());
        } else {
            throw new IllegalArgumentException();
        }
    }


    private TextWatcher createTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @SuppressWarnings("ResultOfMethodCallIgnored")
            @Override
            public void afterTextChanged(final Editable s) {
                try {
                    if (!s.toString().isEmpty()) {
                        if (Utils.isByte(getField())) {
                            Byte.parseByte(s.toString());
                        } else if (Utils.isShort(getField())) {
                            Short.parseShort(s.toString());
                        } else if (Utils.isInteger(getField())) {
                            Integer.parseInt(s.toString());
                        } else if (Utils.isLong(getField())) {
                            Long.parseLong(s.toString());
                        } else if (Utils.isDouble(getField())) {
                            Double.parseDouble(s.toString());
                        } else if (Utils.isFloat(getField())) {
                            Float.parseFloat(s.toString());
                        }
                    }
                    getFieldInfoImageView().setVisibility(GONE);
                    isInputValid = true;
                    RealmBrowserViewNumber.this.setBackgroundColor(getColor(getContext(), android.R.color.transparent));
                } catch (NumberFormatException e) {
                    isInputValid = false;
                    getFieldInfoImageView().setVisibility(VISIBLE);
                    getFieldInfoImageView().setImageDrawable(getDrawable(getContext(), R.drawable.realm_browser_ic_warning_black_24dp));
                    getFieldInfoImageView().setColorFilter(getColor(getContext(), R.color.realm_browser_error), SRC_ATOP);
                    getFieldInfoImageView().setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Snackbar.make(RealmBrowserViewNumber.this, s.toString() + " does not fit data type " + getField().getType().getSimpleName(), Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    RealmBrowserViewNumber.this.setBackgroundColor(getColor(getContext(), R.color.realm_browser_error_light));
                }
            }
        };
    }
}
