package ren.qinc.markdowneditors.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.Bind;
import ren.qinc.markdowneditors.R;
import ren.qinc.markdowneditors.base.BaseToolbarActivity;


/**
 * 单张图片查看(待使用)
 * The type Common image details activity.
 * Created by 沈钦赐 on 2015/12/20.
 */
public class CommonImageDetailsActivity extends BaseToolbarActivity {

    public static final String EXTRA_IMAGE_URL = "image_url";
    public static final String EXTRA_IMAGE_TITLE = "image_title";
    public static final String TRANSIT_PIC = "picture";

    protected static ImageView imageView;
    @Bind(R.id.tv_title)
    protected TextSwitcher textSwitcher;

    String imageUrl, title;


    /**
     * Show image.
     *
     * @param context  the context
     * @param imageUrl the image url
     */
    public static void showImage(Context context, @NonNull String imageUrl) {
        if (context == null) return;
        showImage(context, imageView, imageUrl, null);
    }

    /**
     * Show image.
     *
     * @param context  the context
     * @param imageUrl the image url
     * @param title    the title
     */
    public static void showImage(Context context, @NonNull String imageUrl, String title) {
        if (context == null) return;
        showImage(context, imageView, imageUrl, title);
    }

    /**
     * Show image.
     *
     * @param context       the context activity的context
     * @param sharedElement the shared element
     * @param imageUrl      the image url 图片url
     * @param title         the title 标题  可为null
     */
    public static void showImage(Context context, View sharedElement, @NonNull String imageUrl, @Nullable String title) {
        if (context == null) return;
        Intent intent = new Intent(context, CommonImageDetailsActivity.class);
        intent.putExtra(CommonImageDetailsActivity.EXTRA_IMAGE_URL, imageUrl);
        intent.putExtra(CommonImageDetailsActivity.EXTRA_IMAGE_TITLE, title);
        if (context instanceof Activity && sharedElement != null) {
            ActivityOptionsCompat optionsCompat
                    = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    (Activity) context, sharedElement, CommonImageDetailsActivity.TRANSIT_PIC);
            try {
                ActivityCompat.startActivity((Activity) context, intent,
                        optionsCompat.toBundle());
                //界面共享该图片元素
            } catch (IllegalArgumentException e) {
                context.startActivity(intent);//如果异常 直接启动
            }
        } else {
            context.startActivity(intent);//如果异常 直接启动
        }


    }


    public static Intent newIntent(Context context, String url, String title) {
        Intent intent = new Intent(context, CommonImageDetailsActivity.class);
        intent.putExtra(CommonImageDetailsActivity.EXTRA_IMAGE_URL, url);
        intent.putExtra(CommonImageDetailsActivity.EXTRA_IMAGE_TITLE, title);
        return intent;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        imageView = (ImageView) findViewById(R.id.picture);
        imageView.setOnClickListener(this::switchover);

        parseIntent();
        // init image view
        ViewCompat.setTransitionName(imageView, TRANSIT_PIC);//activity共享元素,有动画效果
        Glide.with(mContext)
                .load(imageUrl).placeholder(R.drawable.ic_null)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存原始尺寸和其他尺寸
                .into(imageView);
//        Glide.with(context)
//                .load(imageUrl)
//                .asBitmap()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存原始尺寸和其他尺寸
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
//
//                    }
//                });


        // set up app bar
        setAppBarAlpha(0.7f);

        //init mTextSwitcher
        textSwitcher.setFactory(() -> {
            TextView textView = new TextView(this);
            textView.setTextAppearance(this, R.style.WebTitle);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.postDelayed(() -> textView.setSelected(true), 1738);
            return textView;
        });
        textSwitcher.setInAnimation(this, android.R.anim.fade_in);
        textSwitcher.setOutAnimation(this, android.R.anim.fade_out);
        if (title != null) setTitle(title);
    }

    public void switchover(View view) {
        hideOrShowToolbar();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_common_single_picture;
    }

    @Override
    public void initData() {

    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        textSwitcher.setText(title);
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }


    private void parseIntent() {
        imageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        title = getIntent().getStringExtra(EXTRA_IMAGE_TITLE);
        if (title == null && imageUrl != null) {
            try {
                int start = imageUrl.lastIndexOf("/");
                int end = imageUrl.lastIndexOf(".");
                title = imageUrl.substring(start, end);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picture, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                break;
            case R.id.action_save:
                saveImageToGallery();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveImageToGallery() {
        //保存图片
    }

    @Override
    protected void onDestroy() {
        imageView = null;
        super.onDestroy();
    }
}