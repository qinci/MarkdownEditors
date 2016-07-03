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
package ren.qinc.markdowneditors.utils;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 检测类
 * The type Check.
 * Created by 沈钦赐（部分方法收集网上）
 */
public class Check {
    //邮件验证
    private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    //手机号验证
    private final static Pattern phoner = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");


    public static boolean isEmpty(CharSequence str) {
        return isNull(str) || str.length() == 0;
    }

    public static boolean isEmpty(Object[] os) {
        return isNull(os) || os.length == 0;
    }

    public static boolean isEmpty(Collection<?> l) {
        return isNull(l) || l.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> m) {
        return isNull(m) || m.isEmpty();
    }

    /**
     * 判断int是否<0
     *
     * @param i the
     * @return the boolean
     */
    public static boolean isVain(int i) {
        return i < 0;
    }

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static void CheckNull(Object o, String message) {
        if (o == null) throw new IllegalStateException(message);
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     * ViewRoot
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * Is mobile no boolean.
     *
     * @param phone the phone
     * @return the boolean
     * @description 判断手机号格式是否正确
     */
    public static boolean isPhoneNo(String phone) {
        // 支持13X，14X，15X，17X，18X
        Matcher m = phoner.matcher(phone);
        return m.matches();
    }

    /**
     * 判断是否包含有特殊符号
     *
     * @param str the str
     * @return boolean boolean
     */
    public static boolean isConSpeCharacters(String str) {
        if (str.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() == 0) {
            // 不包含特殊字符
            return false;
        }
        return true;
    }

}