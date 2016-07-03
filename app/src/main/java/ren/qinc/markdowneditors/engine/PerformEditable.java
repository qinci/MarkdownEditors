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

import android.view.View;
import android.widget.EditText;

import ren.qinc.markdowneditors.R;
import ren.qinc.markdowneditors.utils.Check;


/**
 * 格式相关操作
 * Created by 沈钦赐 on 16/6/22.
 */
public class PerformEditable implements View.OnClickListener {
    private EditText mEditText;

    public PerformEditable(EditText editText) {
        Check.CheckNull(editText, "EditText不能为空");
        this.mEditText = editText;
    }

    public void perform(int id, Object... param) {
        switch (id) {
            case R.id.id_shortcut_console://代码行
                performConsole();
                break;
            case R.id.id_shortcut_format_header_1://
                H(1);
                break;
            case R.id.id_shortcut_format_header_2://
                H(2);
                break;
            case R.id.id_shortcut_format_header_3://
                H(3);
                break;
            case R.id.id_shortcut_format_header_4://
                H(4);
                break;
            case R.id.id_shortcut_format_header_5://
                H(5);
                break;
            case R.id.id_shortcut_format_header_6://
                H(6);
                break;
            case R.id.id_shortcut_format_italic://斜体
                performItalic();
                break;
            case R.id.id_shortcut_format_bold://粗体
                performBold();
                break;
            case R.id.id_shortcut_list_bulleted://列表
                performList("* ");
                break;
            case R.id.id_shortcut_format_numbers://数字列表
                performList("1. ");
                break;
            case R.id.id_shortcut_format_quote://引用
                performQuote();
                break;
            case R.id.id_shortcut_format_strikethrough://字体删除线
                performStrikethrough();
                break;
            case R.id.id_shortcut_grid://插入表格
                performInsertTable(param);
                break;
            case R.id.id_shortcut_insert_link://插入链接
                performInsertLink(param);
                break;
            case R.id.id_shortcut_insert_photo://插入图片
                performInsertPhoto(param);
                break;
            case R.id.id_shortcut_minus://插入分割线
                performMinus();
                break;
            case R.id.id_shortcut_xml://xml
                performXML();
                break;
        }
    }


    private void performList(String tag) {
        String source = mEditText.getText().toString();
        int selectionStart = mEditText.getSelectionStart();
        int selectionEnd = mEditText.getSelectionEnd();
        String substring = source.substring(0, selectionStart);
        int line = substring.lastIndexOf(10);


        if (line != -1) {
            selectionStart = line + 1;
        } else {
            selectionStart = 0;
        }
        substring = source.substring(selectionStart, selectionEnd);

        String[] split = substring.split("\n");
        StringBuffer stringBuffer = new StringBuffer();

        if (split != null && split.length > 0)
            for (String s : split) {
                if (s.length() == 0 && stringBuffer.length() != 0) {
                    stringBuffer.append("\n");
                    continue;
                }
                if (!s.trim().startsWith(tag)) {
                    //不是 空行或者已经是序号开头
                    if (stringBuffer.length() > 0) stringBuffer.append("\n");
                    stringBuffer.append(tag).append(s);
                } else {
                    if (stringBuffer.length() > 0) stringBuffer.append("\n");
                    stringBuffer.append(s);
                }
            }

        if (stringBuffer.length() == 0) {
            stringBuffer.append(tag);
        }
        mEditText.getText().replace(selectionStart, selectionEnd, stringBuffer.toString());
        mEditText.setSelection(stringBuffer.length() + selectionStart);
        completed();
    }

    @Override
    public void onClick(View v) {
        if (mEditText == null) return;
        perform(v.getId());
    }

    /**
     * H.
     *
     * @param level the level
     */
    public final void H(int level) {
        String source = mEditText.getText().toString();
        int selectionStart = mEditText.getSelectionStart();
        int selectionEnd = mEditText.getSelectionEnd();
        StringBuilder result = new StringBuilder();
        String substring = source.substring(selectionStart, selectionEnd);
        if (!hasNewLine(source, selectionStart))
            result.append("\n");
        for (int i = 0; i < level; i++) {
            result.append("#");
        }
        result.append(" ").append(substring);

        mEditText.getText().replace(selectionStart, selectionEnd, result.toString());
        mEditText.setSelection(selectionStart + result.length());
        completed();
    }

    private void performInsertTable(Object... param) {
        int row;
        int column;
        int i;
        if (param == null || param.length < 2) {
            row = 3;
            column = 3;
        } else {
            try {
                row = Integer.parseInt(param[0].toString());
                column = Integer.parseInt(param[1].toString());
            } catch (NumberFormatException e) {
                row = 3;
                column = 3;
            }
        }
        String source = mEditText.getText().toString();
        int selectionStart = mEditText.getSelectionStart();
        StringBuilder stringBuilder = new StringBuilder();

        if (!hasNewTwoLine(source, selectionStart)) {
            if (hasNewLine(source, selectionStart))
                stringBuilder.append("\n");
            else
                stringBuilder.append("\n\n");
        }
        stringBuilder.append("|");
        for (i = 0; i < column; i++) {
            stringBuilder.append(" Header |");
        }
        stringBuilder.append("\n|");
        for (i = 0; i < column; i++) {
            stringBuilder.append(":----------:|");
        }
        stringBuilder.append("\n");
        for (int i2 = 0; i2 < row; i2++) {
            stringBuilder.append("|");
            for (i = 0; i < column; i++) {
                stringBuilder.append("            |");
            }
            stringBuilder.append("\n");
        }
        String result = stringBuilder.toString();
        mEditText.getText().insert(selectionStart, result);
        mEditText.setSelection(selectionStart + result.length());
        completed();
    }


    private void performInsertPhoto(Object[] param) {
        Object[] temp = param;
        if (param == null) param = new Object[]{""};
        String source = mEditText.getText().toString();
        int selectionStart = mEditText.getSelectionStart();

        String result;
        if (hasNewLine(source, selectionStart)) {
            result = "![image](" + param[0] + ")";
        } else {
            result = "\n" + "![image](" + param[0] + ")";
        }
        mEditText.getText().insert(selectionStart, result);
        if (temp == null || temp[0].toString().length() == 0)
            mEditText.setSelection(result.length() + selectionStart - 1);
        else
            mEditText.setSelection(result.length() + selectionStart);
        completed();
    }

    /**
     * 插入链接
     * Perform insert link.
     *
     * @param param the param
     */
    private void performInsertLink(Object[] param) {
        int selectionStart = mEditText.getSelectionStart();
        String result;
        if (param == null || param.length == 0) {
            result = "[]()\n";
            mEditText.getText().insert(selectionStart, result);
            mEditText.setSelection(selectionStart + 1);
        } else if (param.length == 1) {
            result = "[" + param[0] + "](" + param[0] + ")\n";
            mEditText.getText().insert(selectionStart, result);
            mEditText.setSelection(selectionStart + result.length());
        } else {
            result = "[" + param[0] + "](" + param[1] + ")\n";
            mEditText.getText().insert(selectionStart, result);
            mEditText.setSelection(selectionStart + result.length());
        }

        completed();
    }

    private void performItalic() {
        String source = mEditText.getText().toString();
        int selectionStart = mEditText.getSelectionStart();
        int selectionEnd = mEditText.getSelectionEnd();
        String substring = source.substring(selectionStart, selectionEnd);
        String result = " *" + substring + "* ";
        mEditText.getText().replace(selectionStart, selectionEnd, result);
        mEditText.setSelection(result.length() + selectionStart - 2);
        completed();
    }

    private void performBold() {
        String source = mEditText.getText().toString();
        int selectionStart = mEditText.getSelectionStart();
        int selectionEnd = mEditText.getSelectionEnd();
        String substring = source.substring(selectionStart, selectionEnd);
        String result = " **" + substring + "** ";
        mEditText.getText().replace(selectionStart, selectionEnd, result);
        mEditText.setSelection(result.length() + selectionStart - 3);
        completed();
    }

    private void performConsole() {
        String source = mEditText.getText().toString();
        int selectionStart = mEditText.getSelectionStart();

        int selectionEnd = mEditText.getSelectionEnd();
        String substring = source.substring(selectionStart, selectionEnd);

        String result;
        if (hasNewLine(source, selectionStart))
            result = "```\n" + substring + "\n```\n";
        else
            result = "\n```\n" + substring + "\n```\n";

        mEditText.getText().replace(selectionStart, selectionEnd, result);
        mEditText.setSelection(result.length() + selectionStart - 5);
        completed();
    }

    private void performXML() {
        String source = mEditText.getText().toString();
        int selectionStart = mEditText.getSelectionStart();
        int selectionEnd = mEditText.getSelectionEnd();
        String substring = source.substring(selectionStart, selectionEnd);
        String result = " `" + substring + "` ";
        mEditText.getText().replace(selectionStart, selectionEnd, result);
        mEditText.setSelection(result.length() + selectionStart - 2);
        completed();
    }

    private void performStrikethrough() {
        String source = mEditText.getText().toString();
        int selectionStart = mEditText.getSelectionStart();
        int selectionEnd = mEditText.getSelectionEnd();
        String substring = source.substring(selectionStart, selectionEnd);

        String result = " ~~" + substring + "~~ ";
        mEditText.getText().replace(selectionStart, selectionEnd, result);
        mEditText.setSelection(result.length() + selectionStart - 3);
        completed();
    }

    public void performQuote() {
        String source = mEditText.getText().toString();
        int selectionStart = mEditText.getSelectionStart();
        int selectionEnd = mEditText.getSelectionEnd();
        String substring = source.substring(selectionStart, selectionEnd);

        String result;
        if (hasNewLine(source, selectionStart)) {
            result = "> " + substring;
        } else {
            result = "\n> " + substring;

        }
        mEditText.getText().replace(selectionStart, selectionEnd, result);
        mEditText.setSelection(result.length() + selectionStart);
        completed();
    }

    /**
     * 插入横向
     * Perform minus.
     */
    public void performMinus() {
        String source = mEditText.getText().toString();
        int selectionStart = mEditText.getSelectionStart();

        String result;
        if (hasNewLine(source, selectionStart)) {
            //已经单独一行
            result = "-------\n";
        } else {
            //同一行，加上换行
            result = "\n-------\n";
        }
        mEditText.getText().replace(selectionStart, selectionStart, result);
        mEditText.setSelection(result.length() + selectionStart);
        completed();
    }

    protected void completed() {

    }

    private boolean hasNewLine(String source, int selectionStart) {
        if (source.isEmpty()) return true;
        source = source.substring(0, selectionStart);
        return source.charAt(source.length() - 1) == 10;
    }

    private boolean hasNewTwoLine(String source, int selectionStart) {
        source = source.substring(0, selectionStart);
        return source.length() >= 2 && source.charAt(source.length() - 1) == 10 && source.charAt(source.length() - 2) == 10;
    }

    private boolean isEmptyLine(String source, int selectionStart) {
        if (source.isEmpty()) return true;
        if (selectionStart == source.length()) return hasNewLine(source, selectionStart);

        String startStr = source.substring(0, selectionStart);
        //最后一行
        return source.charAt(startStr.length() - 1) == 10 && source.charAt(startStr.length()) == 10;

    }


}
