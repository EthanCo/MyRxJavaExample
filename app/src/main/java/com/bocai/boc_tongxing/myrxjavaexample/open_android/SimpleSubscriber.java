package com.bocai.boc_tongxing.myrxjavaexample.open_android;

import rx.Subscriber;

/**
 * @Description TODO
 * Created by EthanCo on 2016/3/18.
 */
public abstract class SimpleSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }
}
