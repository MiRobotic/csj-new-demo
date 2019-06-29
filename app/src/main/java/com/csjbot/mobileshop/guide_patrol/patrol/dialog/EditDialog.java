package com.csjbot.mobileshop.guide_patrol.patrol.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.util.MaxLengthWatcher;
import com.csjbot.mobileshop.util.StrUtil;

/**
 * Created by 孙秀艳 on 2019/1/18.
 */

public class EditDialog extends Dialog {

    private EditText et_content;
    private TextView tv_title;
    private TextView tv_words;//还可以输入多少字数
    private TextView tv_max_length;
    private Button bt_yes;
    private Button bt_no;
    private Context mContext;
    private int length;
    private String title;
    private OnDialogClickListener mListener;

    public EditDialog(@NonNull Context context) {
        super(context, R.style.NewRetailDialog);
        mContext = context;
        initView();
    }

    public EditDialog(@NonNull Context context, int length) {
        super(context, R.style.NewRetailDialog);
        mContext = context;
        this.length = length;
        initView();
    }

    public EditDialog(@NonNull Context context, String title, int length) {
        super(context, R.style.NewRetailDialog);
        mContext = context;
        this.title = title;
        this.length = length;
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_edittext);
        setCanceledOnTouchOutside(false);

        et_content = findViewById(R.id.et_content);
        tv_title = findViewById(R.id.tv_title);
        tv_words = findViewById(R.id.tv_words);
        tv_max_length = findViewById(R.id.tv_max_length);
        bt_no = findViewById(R.id.bt_no);
        bt_yes = findViewById(R.id.bt_yes);

        bt_no.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.no();
            }
        });
        bt_yes.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.yes(et_content.getText().toString());
            }
        });
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width= ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.height=ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.gravity= Gravity.CENTER;
        dialogWindow.setAttributes(lp);
    }

    public void setStart() {
        tv_max_length.setText(length+"");
        et_content.addTextChangedListener(new MaxLengthWatcher(mContext, length, et_content, mContext.getString(R.string.limit_length)+length+mContext.getString(R.string.chars)));

        if (StrUtil.isBlank(et_content.getText().toString())) {
            et_content.setHint(mContext.getString(R.string.plz_input)+ title);
        }
    }

    public void setListener(OnDialogClickListener listener) {
        mListener = listener;
    }

    public void setContent(String text) {
        et_content.setText(text);
        if (text != null) {
            et_content.setSelection(text.length());
        }
    }

    public void setLength(int len) {
        length = len;
    }

    public void setTitle(String text) {
        this.title = text;
        tv_title.setText(text);
    }

    public void setYes(String text) {
        bt_yes.setText(text);
    }

    public void setNo(String text) {
        bt_no.setText(text);
    }

    public interface OnDialogClickListener {
        void yes(String content);
        void no();
    }
}

