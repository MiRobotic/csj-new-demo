package com.csjbot.mobileshop.feature.content.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.base.BaseFragment;
import com.csjbot.mobileshop.feature.content.contract.ContentLevelThreeContract;
import com.csjbot.mobileshop.feature.content.contract.ContentLevelThreePresenterImpl;
import com.csjbot.mobileshop.feature.content.listener.SpeakMessageListener;
import com.csjbot.mobileshop.feature.content.listener.VoiceControlListener;

import java.net.URLDecoder;

import butterknife.BindView;

/**
 * @author ShenBen
 * @date 2018/10/14 12:30
 * @email 714081644@qq.com
 */
public class ContentLevelThreeFragment extends BaseFragment implements ContentLevelThreeContract.View, VoiceControlListener {
    private static final String DATA = "DATA";
    @BindView(R.id.rv_title)
    RecyclerView rvTitle;
    @BindView(R.id.webView)
    WebView webView;

    private ContentLevelThreeContract.Presenter mPresenter;
    private SpeakMessageListener mListener;

    public static ContentLevelThreeFragment newInstance(String data) {
        ContentLevelThreeFragment fragment = new ContentLevelThreeFragment();
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
        return isPlus() ? R.layout.fragment_content_level_three_vertical : R.layout.fragment_content_level_three_vertical;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        new ContentLevelThreePresenterImpl(getContext(), this);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(false);
        settings.setDomStorageEnabled(true);
        //视频自动播放
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
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
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String data = bundle.getString(DATA);
            if (TextUtils.isEmpty(data)) {
                return;
            }
            mPresenter.loadData(data);
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
    public void setPresenter(ContentLevelThreeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public RecyclerView getRvTitle() {
        return rvTitle;
    }

    @Override
    public void loadUrl(String word, String data) {
        if (mListener != null) {
            mListener.speakMessage(word);
        }
        webView.loadData(Html.fromHtml(data).toString(), "text/html; charset=UTF-8", null);
    }

    @Override
    public boolean voiceControl(String text) {
        return mPresenter.voiceControl(text);
    }
}
