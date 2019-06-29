package com.csjbot.mobileshop.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.csjbot.mobileshop.R;
import com.iflytek.thridparty.G;

/**
 * Created by sunxiuyan on 2017/11/15.
 */

public class ProgressDialog extends Dialog {

    private TextView tvTitle;
    private ProgressBar progressBar;
    private TextView tvProgress;
    private Button btnCancel;
    private OnCancelListener mListener;

    private int mMax = 100;

    public ProgressDialog(@NonNull Context context) {
        this(context, R.style.NewRetailDialog);
    }

    public ProgressDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_progress);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        tvTitle = findViewById(R.id.tv_title);
        progressBar = findViewById(R.id.progressbar);
        tvProgress = findViewById(R.id.tv_progress);
        btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.cancel();
            }
        });
        progressBar.setMax(mMax);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setMaxProgress(int max) {
        mMax = max;
        progressBar.setMax(max);
    }

    public void setProgress(int progress) {
        progressBar.setProgress(progress);
        tvProgress.setText(progress + "%");
    }

    public void setCancelButtonGone() {
        btnCancel.setVisibility(View.GONE);
    }

    public void setOnCancelListener(OnCancelListener listener) {
        mListener = listener;
    }

    public interface OnCancelListener {
        void cancel();
    }
}
