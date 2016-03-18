package com.bocai.boc_tongxing.myrxjavaexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bocai.boc_tongxing.rxjavalib.rxutils.LogSub;
import com.bocai.boc_tongxing.rxjavalib.rxutils.Rx;
import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.fernandocejas.frodo.annotation.RxLogSubscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> list = new ArrayList<>();
        list.add("item1");
        list.add("item2");
        list.add("item3");

        //Observable.just("hello").subscribe(s -> print(s));

        //Observable.from(list).subscribe(s -> print(s));

        //Observable.just("data").repeat(5).subscribe(s -> print(s));

        /*Observable.just("data").repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Void> observable) {
                return Observable.interval(5, TimeUnit.SECONDS);
            }
        }).subscribe(s -> print(s));*/

        //Observable.range(10, 20).map(i -> String.valueOf(i)).subscribe(s -> print(s));

        /*Observable.just("sss").subscribe(new Subject<String,String>() {
            @Override
            public boolean hasObservers() {
                return false;
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Observable.empty(); //创建一个不发射任何数据但是正常终止的Observable
                Observable.never(); //Observable.never();创建一个不发射数据也不终止的Observable
                Observable.error(new Throwable("xxx")); //创建一个不发射数据以一个错误终止的Observable
            }
        });*/

        /*Observable<String> o1 = Observable.just("h1", "h2");
        Observable<String> o2 = Observable.from(list);
        Observable.zip(o1, o2, new Func2<String, String, String>() {
            @Override
            public String call(String s, String s2) {
                return s + s2;
            }
        }).subscribe(s -> print(s));
        Observable.just("a1", "a2").zipWith(o2, new Func2<String, String, String>() {
            @Override
            public String call(String s, String s2) {
                return s + s2;
            }
        }).subscribe(s -> print(s));
        Observable.merge(o1, o2).subscribe(s -> print(s));*/

        Observable.just("hello").map(s -> Integer.valueOf(s)).subscribe(new LogSub<Integer>(s -> {
        }));
        Observable.just("hello").map(s -> Integer.valueOf(s)).subscribe(Rx.sub(s -> {
        }));

        Observable<String> memory = Observable.just("memory:" + new Random().nextBoolean());
        Observable<String> disk = Observable.just("disk:" + new Random().nextBoolean());
        Observable<String> net = Observable.just("net:" + new Random().nextBoolean());
        Observable.concat(memory, disk, net).first(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return Boolean.valueOf(s.substring(s.indexOf(":") + 1));
            }
        }).subscribe(Rx.sub(s -> Log.i("zhk", "res:" + s)));

        getIntegerObs().subscribe(new MySubscribe<Integer>());

        String value = "hello";
        Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return Observable.just(value);
            }
        });


    }

    @RxLogObservable
    public Observable<Integer> getIntegerObs() {
        return getObsTest().map(s -> "XOOEEEE" + s + "GOGOGO")
                .map(s -> "--->" + s + "<-----")
                .map(s -> Integer.valueOf(s));
    }

    @RxLogObservable
    private Observable<String> getObsTest() {
        return Observable.just("hello world !");
    }

    @RxLogSubscriber
    class MySubscribe<T> extends Subscriber<T> {

        @Override
        public void onCompleted() {
            Log.i("Frodo", "onCompleted : ");
        }

        @Override
        public void onError(Throwable e) {
            Log.e("Frodo", "onError : " + e.getMessage());
        }

        @Override
        public void onNext(T t) {
            Log.i("Frodo", "onNext : " + t.toString());
        }
    }

    private void print(String s) {
        Log.i("Z-MainActivity", "onCreate: " + s);
    }
}
