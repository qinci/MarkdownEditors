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

import android.os.CountDownTimer;

/**
 * 倒计时封装(后面定时保存用)
 * Created by 沈钦赐 on 16/1/5.
 */
public class Countdown extends CountDownTimer {
    private CountdownListener listener;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public Countdown(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    /**
     * 每秒回调
     * Instantiates a new Countdown.
     *
     * @param s 秒数
     */
    public Countdown(long s, CountdownListener listener) {
        this(s * 1000, 1000);
        this.listener = listener;
    }

    public void setListener(CountdownListener listener) {
        this.listener = listener;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (listener != null) {
            listener.onUpdate(millisUntilFinished);
        }

    }

    @Override
    public void onFinish() {
        if (listener != null) {
            listener.onFinish();
        }

    }


    /**
     * 取消回调
     * Set cancel.
     */
    public void setCancel() {
        super.cancel();
        if (listener != null) {
            listener.onCancel();
        }
    }

    interface CountdownListener {
        void onUpdate(long millis);

        void onFinish();

        void onCancel();
    }
}
