package me.wcy.htmltext.sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import me.wcy.htmltext.HtmlImageLoader;
import me.wcy.htmltext.HtmlText;
import me.wcy.htmltext.OnTagClickListener;

public class MainActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String sample = getSample();
        HtmlText.from(sample)
                .setImageLoader(new HtmlImageLoader() {
                    @Override
                    public void loadImage(String url, final Callback callback) {
                        // Glide sample, you can also use other image loader
                        Glide.with(MainActivity.this)
                                .asBitmap()
                                .load(url)
                                .into(new CustomTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        callback.onLoadComplete(resource);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                    }

                                    @Override
                                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                        super.onLoadFailed(errorDrawable);
                                        callback.onLoadFailed();
                                    }
                                });
                    }

                    @Override
                    public Drawable getDefaultDrawable() {
                        return ContextCompat.getDrawable(MainActivity.this, R.drawable.image_placeholder_loading);
                    }

                    @Override
                    public Drawable getErrorDrawable() {
                        return ContextCompat.getDrawable(MainActivity.this, R.drawable.image_placeholder_fail);
                    }

                    @Override
                    public int getMaxWidth() {
                        return getTextWidth();
                    }

                    @Override
                    public boolean fitWidth() {
                        return true;
                    }
                })
                .setOnTagClickListener(new OnTagClickListener() {
                    @Override
                    public void onImageClick(View context, List<String> imageUrlList, int position) {
                        Toast.makeText(MainActivity.this, "image click, position: " + position + ", url: " + imageUrlList.get(position), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLinkClick(View context, String url) {
                        Toast.makeText(MainActivity.this, "url click: " + url, Toast.LENGTH_SHORT).show();
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            context.getContext().startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .into(textView);
    }

    private String getSample() {
        try {
            InputStream is = getAssets().open("sample.html");

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getTextWidth() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return dm.widthPixels - textView.getPaddingLeft() - textView.getPaddingRight();
    }
}
