package com.csjbot.mobileshop.widget.language;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.global.CsjLanguage;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingwc on 2018/4/4.
 */

public class LanguageView extends FrameLayout {

    private Context mContext;

    private RecyclerView rvLanguage;
    private List<LanguageBean> mList;
    private LanguageAdapter mAdapter;

    private LanguageClickListener mLanguageClickLlistener;
    private int mIndex = 0;

    public LanguageView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public LanguageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LanguageView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.layout_language, this, true);
        rvLanguage = findViewById(R.id.rv_language);
        mList = new ArrayList<>();
        mAdapter = new LanguageAdapter();

        List<CsjLanguage.ConstantsLanguageBean> supportLanguages = CsjLanguage.getSupportLanguages();
        for (CsjLanguage.ConstantsLanguageBean bean : supportLanguages) {
            mList.add(new LanguageBean(bean.getShowLanguage(), bean.getLanguageDef()));
        }
        LinearLayoutManager manager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rvLanguage.setLayoutManager(manager);
        rvLanguage.setHasFixedSize(true);
        rvLanguage.setAdapter(mAdapter);

        String language = SharedPreUtil.getString(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGE_NAME_KEY, mList.get(0).getLanguage());
        for (int i = 0; i < mList.size(); i++) {
            LanguageBean bean = mList.get(i);
            if (TextUtils.equals(language, bean.getLanguage())) {
                mIndex = i;
                mList.get(i).setChecked(true);
                break;
            }
        }

        mAdapter.setNewData(mList);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mLanguageClickLlistener != null) {
//                if (mIndex == position) {
//                    return;
//                }
                mList.get(position).setChecked(true);
                mAdapter.notifyItemChanged(position);
                mList.get(mIndex).setChecked(false);
                mAdapter.notifyItemChanged(mIndex);
                mIndex = position;
                LanguageBean bean = mList.get(position);
                int languageDef = bean.getLanguageDef();
                SharedPreUtil.putString(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGE_NAME_KEY, bean.getLanguage());
                mLanguageClickLlistener.onLanguageClickListener(languageDef);
            }
        });

    }

    public void setLanguageClickListener(LanguageClickListener listener) {
        this.mLanguageClickLlistener = listener;
    }

    public interface LanguageClickListener {
        void onLanguageClickListener(int language);

    }
}
