package me.wcy.htmltext.span;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import java.util.List;

import me.wcy.htmltext.OnTagClickListener;

/**
 * Created by hzwangchenyan on 2017/5/5.
 */
public class ImageClickSpan extends ClickableSpan {
    private OnTagClickListener listener;
    private final List<String> imageUrls;
    private final int position;

    public ImageClickSpan(List<String> imageUrls, int position) {
        this.imageUrls = imageUrls;
        this.position = position;
    }

    public void setListener(OnTagClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View widget) {
        if (listener != null) {
            listener.onImageClick(widget, imageUrls, position);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
    }
}
