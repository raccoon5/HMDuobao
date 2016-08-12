package com.haomeng.duobao.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import com.haomeng.duobao.R;

public class DialogCommonUtils {
    private static Dialog dialog;

    public static void showDialog(Activity activity) {
        cancelDialog();

        final View view = LayoutInflater.from(activity).inflate(
                R.layout.dialog_common, null);

        dialog = new Dialog(activity, R.style.dialog);
        dialog.setContentView(view);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancelDialog();
            }
        });

        try {
            dialog.show();
        } catch (Exception e) {
        }
    }

    public static void cancelDialog() {
        if (dialog!=null && dialog.isShowing()) {
            dialog.cancel();
        }
    }
}
