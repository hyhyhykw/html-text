package me.wcy.htmltext;

import android.view.View;

import java.util.List;

public interface OnTagClickListener {
    /**
     * 图片被点击
     */
    void onImageClick(View widget, List<String> imageUrlList, int position);

    /**
     * 链接被点击
     */
    void onLinkClick(View widget, String url);
}