package com.leiholmes.rxbindingdemo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.view.ViewScrollChangeEvent;

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
    @BindView(R.id.btn_draw)
    Button btnDraw;
    @BindView(R.id.view_canvas)
    View viewCanvas;
    @BindView(R.id.btn_scroll_layout)
    Button btnScrollLayout;
    @BindView(R.id.btn_scroll)
    Button btnScroll;
    private CompositeDisposable mCompositeDisposable;
    private int x;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_view);
        ButterKnife.bind(this);
        mCompositeDisposable = new CompositeDisposable();
        click();
        draws();
        layoutChange();
        scrollChange();
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

    private void draws() {
        //点击btnDraw调用viewCanvas的绘制
        addDisposable(RxView.clicks(btnDraw)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        //模拟让viewCanvas绘制,这个方法不好用
//                        viewCanvas.getViewTreeObserver().dispatchOnDraw();
                    }
                }));
        //当viewCanvas绘制时触发
        addDisposable(RxView.draws(viewCanvas)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.e("rx_binding_test", "draws:viewCanvas绘制了");
                        Toast.makeText(RxViewActivity.this, "viewCanvas绘制了", Toast.LENGTH_SHORT).show();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void scrollChange() {
        //点击btnScroll模拟让btnScrollLayout滑动
        addDisposable(RxView.clicks(btnScroll)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        x += 10;
                        if (x == 100) {
                            x = 0;
                        }
                        btnScrollLayout.scrollTo(x, 0);
                    }
                }));
        //btnScrollLayout滑动时触发
        addDisposable(RxView.scrollChangeEvents(btnScrollLayout)
                .subscribe(new Consumer<ViewScrollChangeEvent>() {
                    @Override
                    public void accept(ViewScrollChangeEvent viewScrollChangeEvent) throws Exception {
                        Log.e("rx_binding_test", "scrollChangeEvents:btnScrollLayout滑动了:" + viewScrollChangeEvent.toString());
                        Toast.makeText(RxViewActivity.this, "btnScrollLayout滑动了", Toast.LENGTH_SHORT).show();
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
