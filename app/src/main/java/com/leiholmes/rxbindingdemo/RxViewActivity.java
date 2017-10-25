package com.leiholmes.rxbindingdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Description:   RxBinding2中RxView演示Activity
 * author         xulei
 * Date           2017/10/25 09:51
 */
public class RxViewActivity extends AppCompatActivity {
    @BindView(R.id.btn_click)
    Button btnClick;
    @BindView(R.id.btn_layout)
    Button btnLayout;
    @BindView(R.id.btn_change)
    Button btnChange;
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_view);
        ButterKnife.bind(this);
        mCompositeDisposable = new CompositeDisposable();
        click();
        layoutChange();
    }

    private void click() {
        //2s防抖点击
        addDisposable(RxView.clicks(btnClick)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.e("rx_binding_test", "clicks:点击了按钮：两秒内防抖");
                        Toast.makeText(RxViewActivity.this, "点击了按钮：两秒内防抖", Toast.LENGTH_SHORT).show();
                    }
                }));
        //长点击
        addDisposable(RxView.longClicks(btnClick)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.e("rx_binding_test", "longClicks:长点击了按钮");
                        Toast.makeText(RxViewActivity.this, "长点击了按钮", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void layoutChange() {
        //点击btnChange改变btn_layout的布局,防抖2s
        addDisposable(RxView.clicks(btnChange)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        btnLayout.layout(btnLayout.getLeft() - 20, btnLayout.getTop(),
                                btnLayout.getRight() - 20, btnLayout.getBottom());
                    }
                }));
        //btn_layout布局改变时触发
        addDisposable(RxView.layoutChanges(btnLayout)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.e("rx_binding_test", "layoutChanges:btnLayout布局改变了");
                        Toast.makeText(RxViewActivity.this, "btnLayout布局改变了", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    /**
     * 添加订阅
     */
    public void addDisposable(Disposable mDisposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(mDisposable);
    }

    /**
     * 取消所有订阅
     */
    public void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    @Override
    protected void onDestroy() {
        unSubscribe();
        super.onDestroy();
    }
}
