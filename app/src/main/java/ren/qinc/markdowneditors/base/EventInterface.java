package ren.qinc.markdowneditors.base;


import ren.qinc.markdowneditors.event.RxEvent;

/**
 * Eventbus
 * Created by 沈钦赐 on 16/1/21.
 */
public interface EventInterface {
    /**
     * 绑定
     */
     void registerEvent();


    /**
     * 解绑
     */
    void unregisterEvent();

    /**
     * 根据需要重写，如果返回True，这代表该type的Event你是要接受的 会回调
     * Has need event boolean.
     *
     * @param type the type
     * @return the boolean
     */
    boolean  hasNeedEvent(int type);

    /**
     * 回调
     * @param e
     */
    void onEventMainThread(RxEvent e);
}
