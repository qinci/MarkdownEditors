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

package ren.qinc.markdowneditors.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 安全的状态保存回调 来至nuuneoi（感谢）
 */
public class BaseStatedFragment extends Fragment {
    private Bundle savedState;

    private static final String SAVE_KEY = "SAVE_KEY_131231231239";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!restoreStateFromArguments()) {
            onFirstLaunched();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 保存状态
        saveStateToArguments();
    }

    public BaseStatedFragment() {
        super();
    }

    /**
     * On first launched.首次启动初始化，第二次如果没有销毁的话，直接在onRestoreState恢复数据就行了
     */
    protected void onFirstLaunched() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存状态
        saveStateToArguments();
    }

    ////////////////////
    // Don't Touch !!
    ////////////////////

    private void saveStateToArguments() {
        if (getView() != null)
            savedState = saveState();
        if (savedState != null) {
            Bundle b = getArguments();
            if (b != null) {
                b.putBundle(SAVE_KEY, savedState);
            }

        }
    }

    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        if (b == null) {
            return false;
        }
        savedState = b.getBundle(SAVE_KEY);
        if (savedState == null) {
            return false;
        }
        restoreState();
        return true;
    }

    private void restoreState() {
        if (savedState != null) {
            onRestoreState(savedState);
        }
    }


    private Bundle saveState() {
        Bundle state = new Bundle();
        // For Example
        //state.putString("text", tv1.getText().toString());
        onSaveState(state);
        return state;
    }

    /**
     * 子类重写，用于恢复状态保存，不用空判断
     * On restore state.
     *
     * @param savedInstanceState the saved instance state
     */
    protected void onRestoreState(Bundle savedInstanceState) {
        // For Example
        //tv1.setText(savedState.getString("text"));
    }

    protected void onSaveState(Bundle outState) {

    }
}
