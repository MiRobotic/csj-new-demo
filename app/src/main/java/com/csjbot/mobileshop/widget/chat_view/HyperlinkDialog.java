package com.csjbot.mobileshop.widget.chat_view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.service.ChatService;
import com.csjbot.mobileshop.global.Constants;

import java.io.File;

/**
 * @author ShenBen
 * @date 2019/2/26 18:50
 * @email 714081644@qq.com
 */
public class HyperlinkDialog extends Dialog {
    private WebView webView;
    private View webview_loading_layout;
    private ImageView chat_hyperlink_load_gif;
    private ImageView  chat_view_loading_logo;
    public HyperlinkDialog(@NonNull Context context) {
        this(context, R.style.NewRetailDialog);
    }

    public HyperlinkDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setCancelable(false);
        setContentView(R.layout.layout_chat_hyperlink_detail);
        Window dialogWindow = getWindow();

        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER);
        }
        webView = findViewById(R.id.webview_hyperlink_detail);
        webview_loading_layout = findViewById(R.id.webview_loading_layout);
        chat_hyperlink_load_gif = findViewById(R.id.chat_hyperlink_load_gif);
        chat_view_loading_logo = findViewById(R.id.chat_view_loading_logo);


        WebSettings webSettings = webView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:54.0) Gecko/20100101 Firefox/54.0");

        ImageButton button = findViewById(R.id.ib_close);
        button.setOnClickListener(v -> cancel());

        setOnShowListener(dialog -> {
            Intent disCardListing = new Intent(ChatService.CHAT_SERVICE_SHOW);
            disCardListing.putExtra("isShowing", true);
            context.sendBroadcast(disCardListing);
        });

        setOnDismissListener(dialog -> {
            Intent disCardListing = new Intent(ChatService.CHAT_SERVICE_SHOW);
            disCardListing.putExtra("isShowing", false);
            context.sendBroadcast(disCardListing);
        });
    }

    @Override
    public void cancel() {
        super.cancel();
    }

    public void setWebViewUrl(String url) {
        if (!(url.startsWith("http://") || url.startsWith("https://"))) {
            url = "http://" + url;
        }

        setLogoBySd(Constants.Path.LOGO_PATH + Constants.Path.LOGO_FILE_NAME);

        webview_loading_layout.setVisibility(View.VISIBLE);
        Glide.with(getContext()).load(R.drawable.chat_video_load_gif)
                .listener(requestListener).into(chat_hyperlink_load_gif);

        CsjlogProxy.getInstance().info("setWebViewUrl " + url);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webview_loading_layout.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {


            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        this.show();
    }

    private void setLogoBySd(String filePath) {
        if (chat_view_loading_logo != null) {
            chat_view_loading_logo.setImageDrawable(null);
            if (new File(filePath).exists()) {
                Bitmap bmp = BitmapFactory.decodeFile(filePath);
                chat_view_loading_logo.setImageBitmap(bmp);
            } else {
                chat_view_loading_logo.setImageResource(R.drawable.logo_only);
            }
        }
    }

    private RequestListener requestListener = new RequestListener() {

        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
            if (resource instanceof GifDrawable) {
                //加载一次
                ((GifDrawable) resource).setLoopCount(GifDrawable.LOOP_FOREVER);
            }
            return false;
        }

    };
}
