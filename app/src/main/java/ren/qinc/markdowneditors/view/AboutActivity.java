/*
 * Copyright 2016. SHENQINCI(沈钦赐)<946736079@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ren.qinc.markdowneditors.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import ren.qinc.markdowneditors.AppContext;
import ren.qinc.markdowneditors.R;
import ren.qinc.markdowneditors.base.BaseToolbarActivity;
import ren.qinc.markdowneditors.base.BaseWebActivity;
import ren.qinc.markdowneditors.utils.SystemBarUtils;
import ren.qinc.markdowneditors.utils.SystemUtils;

/**
 * about
 * Created by 沈钦赐 on 16/6/30.
 */
public class AboutActivity extends BaseToolbarActivity {
    @Bind(R.id.version)
    TextView version;
    @Bind(R.id.description)
    TextView description;
    private static final String MAIL = "mailto:qq@qinc.me";

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    public static void startAboutActivity(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        version.setText(String.format(getString(R.string.version_string), SystemUtils.getAppVersion(mContext)));
        String fromAssets = SystemUtils.getAssertString(mContext.getApplicationContext(), "description.txt");
        if (TextUtils.isEmpty(fromAssets)) {
            description.setText("MarkdownEditors");
        } else {

            description.setText(fromAssets);
        }
    }

    @Override
    protected void initStatusBar() {
        SystemBarUtils.tintStatusBar(this, getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.contact_me, R.id.ad_contact_me})
    protected void contackMe(View v) {
        String subject = null;
        switch (v.getId()) {
            case R.id.ad_contact_me:
                subject = "广告联系";
                break;
            default:
                subject = "MarkdownEditor用户";
                break;
        }

        Uri uri = Uri.parse(MAIL);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        //intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
        intent.putExtra(Intent.EXTRA_SUBJECT, subject); // 主题
        // intent.putExtra(Intent.EXTRA_TEXT, "这是邮件的正文部分"); // 正文
        try {
            startActivity(intent);
        } catch (Exception e) {
            AppContext.showSnackbar(getWindow().getDecorView(), "找不到邮箱应用!");
        }
    }


    @OnClick(R.id.about_github)
    protected void openSource() {
        BaseWebActivity.loadUrl(this, "https://github.com/qinci/MarkdownEditors", "源码地址");
    }

    @NonNull
    @Override
    protected String getTitleString() {
        return this.getString(R.string.about);
    }
}
