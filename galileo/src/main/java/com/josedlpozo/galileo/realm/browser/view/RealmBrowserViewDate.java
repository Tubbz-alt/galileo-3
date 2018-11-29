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
package com.josedlpozo.galileo.realm.browser.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.josedlpozo.galileo.R;
import com.josedlpozo.galileo.realm.browser.helper.Utils;
import io.realm.DynamicRealmObject;
import io.realm.RealmObjectSchema;
import java.lang.reflect.Field;
import java.util.Date;

import static android.graphics.PorterDuff.Mode.SRC_ATOP;
import static android.support.v4.content.ContextCompat.getColor;
import static android.support.v4.content.ContextCompat.getDrawable;

class RealmBrowserViewDate extends RealmBrowserViewField {

    private TextView textView;
    private EditText editText;
    private ImageView infoImageView;
    private Button buttonPicker;
    private Button buttonNow;
    private Date newDateValue;

    public RealmBrowserViewDate(Context context, @NonNull RealmObjectSchema realmObjectSchema, @NonNull Field field) {
        super(context, realmObjectSchema, field);
        if (!Utils.INSTANCE.isDate(getField())) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void inflateViewStub() {
        ViewStub stub = findViewById(R.id.realm_browser_stub);
        stub.setLayoutResource(R.layout.realm_browser_fieldview_date);
        stub.inflate();
    }

    @Override
    public void initViewStubView() {
        textView = findViewById(R.id.realm_browser_field_date_textview);
        editText = findViewById(R.id.realm_browser_field_date_edittext);
        buttonPicker = findViewById(R.id.realm_browser_field_date_button_picker);
        buttonNow = findViewById(R.id.realm_browser_field_date_button_now);
        infoImageView = findViewById(R.id.realm_browser_field_date_infoimageview);

        editText.addTextChangedListener(createTextWatcher());

        buttonPicker.setOnClickListener(view -> Toast.makeText(getContext(), "TODO", Toast.LENGTH_SHORT).show());
        buttonNow.setOnClickListener(view -> {
            newDateValue = new Date(System.currentTimeMillis());
            editText.setText(String.valueOf(newDateValue.getTime()));
            textView.setText(newDateValue.toString());
        });
        infoImageView.setOnClickListener(
            view -> Snackbar.make(RealmBrowserViewDate.this, "Time in milliseconds since epoch.", Snackbar.LENGTH_SHORT).show());
    }

    @Override
    public Object getValue() {
        if (isFieldIsNullCheckBoxChecked()) {
            return null;
        } else {
            return newDateValue;
        }
    }

    @Override
    public void toggleAllowInput(boolean allow) {
        textView.setEnabled(allow);
        textView.setVisibility(allow ? VISIBLE : GONE);
        editText.setVisibility(allow ? VISIBLE : GONE);
        infoImageView.setVisibility(allow ? VISIBLE : GONE);
        buttonPicker.setVisibility(allow ? VISIBLE : GONE);
        buttonNow.setVisibility(allow ? VISIBLE : GONE);
    }

    @Override
    public boolean isInputValid() {
        return validateInput(editText.getText().toString());
    }

    @Override
    public void setRealmObject(@NonNull DynamicRealmObject realmObject) {
        if (Utils.INSTANCE.isDate(getField())) {
            if (realmObject.getDate(getField().getName()) == null) {
                updateFieldIsNullCheckBoxValue(true);
            } else {
                editText.setText(String.valueOf(realmObject.getDate(getField().getName()).getTime()));
                textView.setText(realmObject.getDate(getField().getName()).toString());
            }
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
                validateInput(s.toString());
            }
        };
    }

    private boolean validateInput(final String s) {
        try {
            if (!s.isEmpty()) {
                newDateValue = new Date(Long.parseLong(s));
                textView.setText(newDateValue.toString());
            }
            getFieldInfoImageView().setVisibility(GONE);
            RealmBrowserViewDate.this.setBackgroundColor(getColor(getContext(), android.R.color.transparent));
            return true;
        } catch (NumberFormatException e) {
            getFieldInfoImageView().setVisibility(VISIBLE);
            getFieldInfoImageView().setImageDrawable(getDrawable(getContext(), R.drawable.realm_browser_ic_warning_black_24dp));
            getFieldInfoImageView().setColorFilter(getColor(getContext(), R.color.realm_browser_error), SRC_ATOP);
            getFieldInfoImageView().setOnClickListener(
                v -> Snackbar.make(RealmBrowserViewDate.this, s + " does not fit data type " + getField().getType().getSimpleName(), Snackbar.LENGTH_SHORT).show());
            RealmBrowserViewDate.this.setBackgroundColor(getColor(getContext(), R.color.realm_browser_error_light));
            return false;
        }
    }
}