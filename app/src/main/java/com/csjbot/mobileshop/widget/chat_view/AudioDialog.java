package com.csjbot.mobileshop.widget.chat_view;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.csjbot.mobileshop.R;

import java.io.IOException;

/**
 * @author ShenBen
 * @date 2019/2/26 18:50
 * @email 714081644@qq.com
 */
public class AudioDialog extends Dialog {
    private ImageView ivDetail;
    private MediaPlayer mediaPlayer;

    public AudioDialog(@NonNull Context context) {
        this(context, R.style.NewRetailDialog);
    }

    public AudioDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setCancelable(false);
        setContentView(R.layout.layout_chat_audio_detail);
        Window dialogWindow = getWindow();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(MediaPlayer::start);
        mediaPlayer.setOnCompletionListener(mp -> dismiss());

        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER);
        }
        ivDetail = findViewById(R.id.iv_audio_detail);

        ImageButton button = findViewById(R.id.ib_close);
        button.setOnClickListener(v -> cancel());
    }

    @Override
    public void cancel() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();

        super.cancel();
    }

    public void setAudioUrl(String url) {
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            dismiss();
            e.printStackTrace();
        }

        Glide.with(getContext()).load(R.drawable.record_pic).listener(new RequestListener() {

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

        }).into(ivDetail);
    }

}
