package com.josedlpozo.galileo.realm.realmbrowser.helper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class DataHolder {

    public static final String DATA_HOLDER_KEY_FIELD = "field";
    public static final String DATA_HOLDER_KEY_OBJECT = "obj";
    public static final String DATA_HOLDER_KEY_CONFIG = "config";
    public static final String DATA_HOLDER_KEY_CLASS = "class";

    private static final DataHolder instance = new DataHolder();

    @NonNull
    public static DataHolder getInstance() {
        return instance;
    }

    private DataHolder() {
    }

    private Map<String, SoftReference<Object>> data = new HashMap<>();

    public void save(@NonNull String id, @Nullable Object object) {
        data.put(id, new SoftReference<>(object));
        Timber.d("DataHolder.save(%s, %s)", id, object);
    }

    @Nullable
    public Object retrieve(@NonNull String id) {
        SoftReference<Object> objectWeakReference = data.get(id);
        Object o = objectWeakReference.get();
        Timber.d("DataHolder.retrieve(%s) = %s", id, o.toString());
        return o;
    }
}
