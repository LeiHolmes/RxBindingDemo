package com.leiholmes.rxbindingdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;


public class MainActivity extends AppCompatActivity {
    private Button btnShake;
    private CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCompositeSubscription = new CompositeSubscription();
        initView();
        initBinding();
    }

    private void initView() {
        btnShake = (Button) findViewById(R.id.btn_shake);
    }

    private void initBinding() {
        //防抖点击
        addSubscription(RxView.clicks(btnShake)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e("rx_binding_test", "点击了按钮：两秒内防抖");
                        Toast.makeText(MainActivity.this, "点击了按钮：两秒内防抖", Toast.LENGTH_SHORT).show();
                    }
                }));
        //长点击
        addSubscription(RxView.longClicks(btnShake)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e("rx_binding_test", "长点击了按钮");
                        Toast.makeText(MainActivity.this, "长点击了按钮", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    /**
     * 添加订阅
     */
    public void addSubscription(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    /**
     * 取消所有订阅
     */
    public void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
        }
    }

    @Override
    protected void onDestroy() {
        unSubscribe();
        super.onDestroy();
    }
}
