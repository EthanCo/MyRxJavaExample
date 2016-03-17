package com.bocai.boc_tongxing.myrxjavaexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import rx.Observable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable.just("hello").subscribe(s -> {
            Toast.makeText(MainActivity.this, "subscribe:" + s, Toast.LENGTH_SHORT).show();
        });
    }
}
