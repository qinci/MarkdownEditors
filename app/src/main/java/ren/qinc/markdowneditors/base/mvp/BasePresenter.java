package ren.qinc.markdowneditors.base.mvp;

import ren.qinc.markdowneditors.model.DataManager;
import rx.subscriptions.CompositeSubscription;


/**
 * presenter类的父类，提供Presenter接口的实现
 * <p>
 * W * Created by 沈钦赐 on 16/1/17.
 */
public class BasePresenter<T extends IMvpView> implements IPresenter<T> {

    private T mMvpView;

    protected DataManager mDataManager;

    /**
     * 用来保存 每个Presenter的所有订阅（请求），onDestory（detachView）或者subscribe的onCompleted中取消订阅
     * 自己维护生命周期，防止内存泄露
     */
    public CompositeSubscription mCompositeSubscription;


    @Override
    public void attachView(T mvpView) {
        this.mMvpView = mvpView;
        this.mCompositeSubscription = new CompositeSubscription();
        this.mDataManager = DataManager.getInstance();
    }

    @Override
    public void detachView() {
        this.mCompositeSubscription.unsubscribe();
        this.mCompositeSubscription = null;
        this.mMvpView = null;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public T getMvpView() {
        return mMvpView;
    }


    protected void callFailure(int errorCode, String message, int flag) {
        if (getMvpView() == null) return;
        getMvpView().onFailure(errorCode, message, flag);
    }

    protected void callShowProgress(String message, boolean canBack, int flag) {
        if (getMvpView() == null) return;
        getMvpView().showWait(message, canBack, flag);
    }

    protected void callHideProgress( int flag) {
        if (getMvpView() == null) return;
        getMvpView().hideWait(flag);
    }

    protected void callOtherSuccess(int flag){
        if (getMvpView() == null) return;
        getMvpView().otherSuccess(flag);
    }


}
