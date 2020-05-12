package com.reactlibrary;

import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class MyLibraryModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private static final String RN_CLASS = "RNToastSnackbar";

    private static final String DURATION_TOAST_SHORT = "TOAST_SHORT";
    private static final String DURATION_TOAST_LONG = "TOAST_LONG";
    private static final String DURATION_SNACKBAR_INDEFINITE = "SNACKBAR_INDEFINITE";
    private static final String DURATION_SNACKBAR_SHORT = "SNACKBAR_SHORT";
    private static final String DURATION_SNACKBAR_LONG = "SNACKBAR_LONG";

    // toast gravity static
    static class ToastGravity {
        static final String BOTTOM = "BOTTOM";
        static final String TOP = "TOP";
        static final String LEFT = "LEFT";
        static final String RIGHT = "RIGHT";
    }

    public MyLibraryModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    // Methods

    /**
     * Show Toast ReactNative method
     * @param options Toast options
     */
    @ReactMethod
    public void showToast(ReadableMap options) {
        String message = getOptionValue(options, "message", "");
        int duration = getOptionValue(options, "duration", Toast.LENGTH_SHORT);
        String gravityType = getOptionValue(options, "gravity", "");

        // gravity
        int gravity;
        switch (gravityType) {
            case ToastGravity.TOP:
                gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                break;
            case ToastGravity.LEFT:
                gravity = Gravity.START | Gravity.CENTER_VERTICAL;
                break;
            case ToastGravity.RIGHT:
                gravity = Gravity.END | Gravity.CENTER_VERTICAL;
                break;
            default:
                gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                break;
        }

        int yOffset = gravityType.equals("") ? 90 : 0;

        Toast toast = Toast.makeText(reactContext, message, duration);
        if (options.hasKey("gravity")) {
            toast.setGravity(gravity, 0, yOffset);
        }

        // custom view
        if (options.hasKey("backgroundColor") || options.hasKey("textColor")) {
            LayoutInflater inflater = LayoutInflater.from(reactContext);
            View view = inflater.inflate(R.layout.toast_layout, null);
            CardView toastBg = ((View) view).findViewById(R.id.toast_container);
            TextView toastText = ((View) view).findViewById(R.id.tv_msg);
            toastText.setText(message);

            int backgroundColor = getOptionValue(options, "backgroundColor", Color.BLACK);
            int textColor = getOptionValue(options, "textColor", Color.WHITE);

            toastBg.setCardBackgroundColor(backgroundColor);
            toastText.setTextColor(textColor);

            toast.setView(view);
        }

        toast.show();
    }

    /**
     * Show Snackbar ReactNative method
     * @param options Snackbar options
     */
    @ReactMethod
    public void showSnackbar(ReadableMap options, Callback callback) {
        ViewGroup view;

        try {
            view = getCurrentActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (view == null) return;

        if (!view.hasWindowFocus()) {
            // The view is not focused, we should get all the modal views in the screen.
            ArrayList<View> modals = recursiveLoopChildren(view, new ArrayList<View>());

            for (View modal : modals) {
                if (modal == null) continue;

                displaySnackbar(modal, options, callback);
            }

            return;
        }

        displaySnackbar(view, options, callback);
    }
    private void displaySnackbar(View view, ReadableMap options, final Callback callback) {
        String message = getOptionValue(options, "message", "");
        int duration = getOptionValue(options, "duration", Snackbar.LENGTH_SHORT);
        int textColor = getOptionValue(options, "textColor", Color.WHITE);
        boolean rtl = getOptionValue(options, "rtl", false);

        Snackbar snackbar = Snackbar.make(view, message, duration);
        View snackbarView = snackbar.getView();

        // RTL support
        if (rtl && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            snackbarView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            snackbarView.setTextDirection(View.TEXT_DIRECTION_RTL);
        }

        // background color
        if (options.hasKey("backgroundColor")) {
            snackbarView.setBackgroundColor(options.getInt("backgroundColor"));
        }

        // text color
        TextView snackbarText = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        snackbarText.setTextColor(textColor);

        // action
        if (options.hasKey("action")) {
            ReadableMap actionOption = options.getMap("action");
            String actionText = getOptionValue(actionOption, "text", "");
            int actionTextColor = getOptionValue(actionOption, "textColor", Color.parseColor("#479ed7"));

            View.OnClickListener onClickListener = new View.OnClickListener() {
                boolean callbackCalled = false;

                @Override
                public void onClick(View view) {
                    if (callbackCalled) return;

                    callbackCalled = true;
                    callback.invoke();
                }
            };

            snackbar.setAction(actionText, onClickListener);
            snackbar.setActionTextColor(actionTextColor);
        }

        snackbar.show();
    }

    // Module name in javascript to expose
    @NonNull
    @Override
    public String getName() {
        return RN_CLASS;
    }

    // Constants in javascript to expose
    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DURATION_TOAST_SHORT, Toast.LENGTH_SHORT);
        constants.put(DURATION_TOAST_LONG, Toast.LENGTH_LONG);
        constants.put(DURATION_SNACKBAR_SHORT, Snackbar.LENGTH_SHORT);
        constants.put(DURATION_SNACKBAR_LONG, Snackbar.LENGTH_LONG);
        constants.put(DURATION_SNACKBAR_INDEFINITE, Snackbar.LENGTH_INDEFINITE);
        return constants;
    }

    // Helpers
    private ArrayList<View> recursiveLoopChildren(ViewGroup view, ArrayList<View> modals) {
        if (view.getClass().getSimpleName().equalsIgnoreCase("ReactModalHostView")) {
            modals.add(view.getChildAt(0));
        }

        for (int i = view.getChildCount() - 1; i >= 0; i--) {
            final View child = view.getChildAt(i);

            if (child instanceof ViewGroup) {
                recursiveLoopChildren((ViewGroup) child, modals);
            }
        }

        return modals;
    }

    private String getOptionValue(ReadableMap option, String key, String fallBack) {
        return option.hasKey(key) ? option.getString(key) : fallBack;
    }
    private int getOptionValue(ReadableMap option, String key, int fallBack) {
        return option.hasKey(key) ? option.getInt(key) : fallBack;
    }
    private boolean getOptionValue(ReadableMap option, String key, boolean fallBack) {
        return option.hasKey(key) ? option.getBoolean(key) : fallBack;
    }
}
