package ren.qinc.markdowneditors.base;


import com.kaopiz.kprogresshud.KProgressHUD;

/**
 *
 * Created by 沈钦赐 on 16/1/17.
 */
public interface WaitDialogInterface {

	/**
	 * 隐藏对话框
	 */
	void hideWaitDialog();


	/**
	 * 显示等待的对话框
	 * @param text
	 * @return
	 */
	KProgressHUD showWaitDialog(String text, boolean canBack);

}
