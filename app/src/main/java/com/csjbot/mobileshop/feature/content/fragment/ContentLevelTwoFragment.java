package com.csjbot.mobileshop.feature.content.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.base.BaseFragment;
import com.csjbot.mobileshop.feature.content.adapter.ContentLevelTwoAdapter;
import com.csjbot.mobileshop.feature.content.bean.ChildContentLevelTwoBean;
import com.csjbot.mobileshop.feature.content.listener.SpeakMessageListener;
import com.csjbot.mobileshop.feature.content.listener.VoiceControlListener;
import com.csjbot.mobileshop.feature.content.manager.TopLayoutManager;
import com.csjbot.mobileshop.model.http.bean.rsp.ContentInfoBean;
import com.csjbot.mobileshop.util.GsonUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * @author ShenBen
 * @date 2018/10/14 12:30
 * @email 714081644@qq.com
 */
public class ContentLevelTwoFragment extends BaseFragment implements VoiceControlListener {
    private static final String DATA = "DATA";
    private static final String TAG = "ContentLevelTwoFragment";
    @BindView(R.id.rv_title)
    RecyclerView rvTitle;
    @BindView(R.id.webView)
    WebView webView;
    private List<ChildContentLevelTwoBean> mList;
    private ContentLevelTwoAdapter mAdapter;
    private int mCurrentPosition = 0;

    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;
    private SpeakMessageListener mListener;

    public static ContentLevelTwoFragment newInstance(String data) {
        ContentLevelTwoFragment fragment = new ContentLevelTwoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (SpeakMessageListener) context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_content_level_two_vertical;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        mList = new ArrayList<>();

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(false);
        settings.setDomStorageEnabled(true);
        //视频自动播放
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //调整图片至适合webview的大小
        settings.setUseWideViewPort(true);
        //缩放至屏幕的大小
        settings.setLoadWithOverviewMode(true);
        //设置默认编码
        settings.setDefaultTextEncodingName("utf-8");
        //设置自动加载图片
        settings.setLoadsImagesAutomatically(true);
        settings.setAllowFileAccess(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });

        if (isPlus()) {
            mAdapter = new ContentLevelTwoAdapter(R.layout.item_content_level_two, mList);
        } else {
            mAdapter = new ContentLevelTwoAdapter(R.layout.item_content_level_two, mList);
        }

        LinearLayoutManager manager = new TopLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvTitle.setLayoutManager(manager);
        rvTitle.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            updateSelectedIndex(position);
        });
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String data = bundle.getString(DATA);
            if (TextUtils.isEmpty(data)) {
                return;
            }
            List<ContentInfoBean.ChildrenBeanX> children = GsonUtils.jsonToObject(data,
                    new TypeToken<List<ContentInfoBean.ChildrenBeanX>>() {
                    }.getType());
            ChildContentLevelTwoBean twoBean;
            for (ContentInfoBean.ChildrenBeanX child : children) {
                if (child.isHomeShow()) {
                    twoBean = new ChildContentLevelTwoBean(child.getName(), child.getDetails(), child.getWords(), child.getSequence());
                    mList.add(twoBean);
                }
            }
            if (!mList.isEmpty()) {
                Collections.sort(mList);
                mList.get(0).setChecked(true);
                if (mListener != null) {
                    mListener.speakMessage(mList.get(0).getSpeakWords());
                }
                mAdapter.setNewData(mList);
                webView.loadData(Html.fromHtml(mList.get(0).getContentUrl()).toString(), "text/html; charset=UTF-8", null);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
        webView.resumeTimers();
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
        webView.pauseTimers();
    }

    @Override
    public void onDestroyView() {
        if (webView != null) {
            webView.clearCache(true);
            webView.clearHistory();
            webView.clearFormData();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean voiceControl(String text) {
        if (!mList.isEmpty() && !TextUtils.isEmpty(text)) {
            int size = mList.size();
            for (int i = 0; i < size; i++) {
                if (text.contains(mList.get(i).getTitle())) {
                    updateSelectedIndex(i);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 更新列表选中项
     *
     * @param position
     */
    private void updateSelectedIndex(int position) {
        if (mCurrentPosition == position) {
            return;
        }
        mList.get(mCurrentPosition).setChecked(false);
        mAdapter.notifyItemChanged(mCurrentPosition);
        ChildContentLevelTwoBean bean = mList.get(position);
        bean.setChecked(true);
        mAdapter.notifyItemChanged(position);
        mCurrentPosition = position;
        if (mListener != null) {
            mListener.speakMessage(bean.getSpeakWords());
        }
        webView.loadData(Html.fromHtml(bean.getContentUrl()).toString(), "text/html; charset=UTF-8", null);
        rvTitle.smoothScrollToPosition(position);
    }
}
