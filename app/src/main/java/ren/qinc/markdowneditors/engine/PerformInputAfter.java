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

package ren.qinc.markdowneditors.engine;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import ren.qinc.markdowneditors.utils.Check;

/**
 * 输入监听
 * Created by 沈钦赐 on 16/6/24.
 */
public class PerformInputAfter {

    private final Editable editable;
    private boolean flag = false;

    public static void start(@NonNull EditText editText) {
        new PerformInputAfter(editText);
    }

    private PerformInputAfter(@NonNull EditText editText) {
        Check.CheckNull(editText, "EditText不能为空");
        editable = editText.getText();
        editText.addTextChangedListener(new Watcher());
    }

    private class Watcher implements TextWatcher {

        /**
         * Before text changed.
         *
         * @param s     the s
         * @param start the start 起始光标
         * @param count the count 选择数量
         * @param after the after 替换增加的文字数
         */
        @Override
        public final void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (flag) return;
            int end = start + count;
            if (end > start && end <= s.length()) {
                CharSequence charSequence = s.subSequence(start, end);
                if (charSequence.length() > 0) {
                    onSubText(s, charSequence, start);

                }
            }
        }

        /**
         * On text changed.
         *
         * @param s      the s
         * @param start  the start 起始光标
         * @param before the before 选择数量
         * @param count  the count 添加的数量
         */
        @Override
        public final void onTextChanged(CharSequence s, int start, int before, int count) {
            if (flag) return;
            int end = start + count;
            if (end > start) {
                CharSequence charSequence = s.subSequence(start, end);
                if (charSequence.length() > 0) {
                    onAddText(s, charSequence, start);
                }
            }
        }

        @Override
        public final void afterTextChanged(Editable s) {
            if (flag) return;
        }

    }

    private void onAddText(CharSequence source, CharSequence charSequence, int start) {
        flag = true;
        if ("\n".equals(charSequence.toString())) {
            //用户输入回车
            performAddEnter(editable, source, start);


        }
        flag = false;
    }

    private void onSubText(CharSequence source, CharSequence charSequence, int start) {
        flag = true;
        //操作代码

        flag = false;
    }

    /**
     * 处理回车操作
     *
     * @param editable
     * @param source
     * @param start
     */
    private void performAddEnter(Editable editable, CharSequence source, int start) {
        //获取回车之前的字符
        String tempStr = source.subSequence(0, start).toString();
        //查找最后一个回车
        int lastEnter = tempStr.lastIndexOf(10);
        if (lastEnter > 0) {
            //最后一个回车到输入回车之间的字符
            tempStr = tempStr.substring(lastEnter + 1, start);
        }

        String mString = tempStr.trim();
        String startSpace = getStartChar(tempStr, ' ');

        if (mString.startsWith("* ") && mString.length() > 2) {//* 开头
            editable.insert(start + 1, startSpace + "* ");
        } else if (mString.startsWith("1. ") && mString.length() > 3) {//1. 开头
            editable.insert(start + 1, startSpace + "1. ");
        } else if (mString.length() > 1) {
            editable.insert(start + 1, startSpace);
        }

    }

    /**
     * 获取开头的字符
     *
     * @param target
     * @param startChar
     * @return
     */
    private String getStartChar(String target, char startChar) {
        StringBuilder sb = new StringBuilder();
        char[] chars = target.toCharArray();
        for (char aChar : chars) {
            if (aChar == startChar) {
                sb.append(startChar);
            } else {
                break;
            }
        }
        return sb.toString();

    }
}
