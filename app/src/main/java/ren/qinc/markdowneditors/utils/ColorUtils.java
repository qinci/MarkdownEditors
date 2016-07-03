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

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;

/**
 * 颜色相关工具
 * Created by 沈钦赐 on 16/1/31.
 */
public class ColorUtils {

    /**
     * 改变color值的透明度
     *
     * @param RGBValues rgb颜色值
     * @param alpha     透明度 0-255
     * @return the alpha color
     */
    public static int getAlphaColor(@ColorInt int RGBValues, @IntRange(from = 0, to = 255) int alpha) {
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        red = red < 0 ? 0 : red;
        green = green < 0 ? 0 : green;
        blue = blue < 0 ? 0 : blue;
        return Color.argb(alpha, red, green, blue);

    }
}
