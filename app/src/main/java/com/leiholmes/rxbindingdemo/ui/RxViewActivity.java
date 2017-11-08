package com.leiholmes.rxbindingdemo.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.view.ViewScrollChangeEvent;
import com.leiholmes.rxbindingdemo.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * Description:   RxBinding2中RxView演示Activity
 * author         xulei
 * Date           2017/10/25 09:51
 */
public class RxViewActivity extends BaseActivity {
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
    private int x;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rx_view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        clicks();
        draws();
        drags();
        layoutChanges();
        scrollChange();
    }

    /**
     * 点击事件
     */
    private void clicks() {
        //2s防抖点击
        addDisposable(RxView.clicks(btnClick)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    Log.e("rx_binding_test", "clicks:点击了按钮：两秒内防抖");
                    Toast.makeText(RxViewActivity.this, "点击了按钮：两秒内防抖", Toast.LENGTH_SHORT).show();
                }));
        //长点击
        addDisposable(RxView.longClicks(btnClick)
                .subscribe(o -> {
                    Log.e("rx_binding_test", "longClicks:长点击了按钮");
                    Toast.makeText(RxViewActivity.this, "长点击了按钮", Toast.LENGTH_SHORT).show();
                }));
    }

    /**
     * 绘制事件
     */
    private void draws() {
        //点击btnDraw调用viewCanvas的绘制
        addDisposable(RxView.clicks(btnDraw)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    //模拟让viewCanvas绘制,这个方法不好用
                    //viewCanvas.getViewTreeObserver().dispatchOnDraw();
                }));
        //当viewCanvas绘制时触发
        addDisposable(RxView.draws(viewCanvas)
                .subscribe(o -> {
                    Log.e("rx_binding_test", "draws:viewCanvas绘制了");
                    Toast.makeText(RxViewActivity.this, "viewCanvas绘制了", Toast.LENGTH_SHORT).show();
                }));
    }

    /**
     * 拖拽事件
     */
    private void drags() {
        //当btnDraw被拖拽时触发
        addDisposable(RxView.drags(btnDraw)
                .subscribe(o -> {
                    Log.e("rx_binding_test", "drags:btnDraw被拖拽了");
                    Toast.makeText(RxViewActivity.this, "btnDraw被拖拽了", Toast.LENGTH_SHORT).show();
                }));
    }
    /**
     * 布局改变事件
     */
    private void layoutChanges() {
        //点击btnChange改变btn_layout的布局,防抖2s
        addDisposable(RxView.clicks(btnChange)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> btnLayout.layout(btnLayout.getLeft() - 20, btnLayout.getTop(),
                        btnLayout.getRight() - 20, btnLayout.getBottom())
                ));
        //btn_layout布局改变时触发
        addDisposable(RxView.layoutChanges(btnLayout)
                .subscribe(o -> {
                    Log.e("rx_binding_test", "layoutChanges:btnLayout布局改变了");
                    Toast.makeText(RxViewActivity.this, "btnLayout布局改变了", Toast.LENGTH_SHORT).show();
                }));
    }

    /**
     * 滑动变化事件
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void scrollChange() {
        //点击btnScroll模拟让btnScrollLayout滑动
        addDisposable(RxView.clicks(btnScroll)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    x += 10;
                    if (x == 100) {
                        x = 0;
                    }
                    btnScrollLayout.scrollTo(x, 0);
                }));
        //btnScrollLayout滑动时触发
        addDisposable(RxView.scrollChangeEvents(btnScrollLayout)
                .subscribe(viewScrollChangeEvent -> {
                    Log.e("rx_binding_test", "scrollChangeEvents:btnScrollLayout滑动了:" + viewScrollChangeEvent.toString());
                    Toast.makeText(RxViewActivity.this, "btnScrollLayout滑动了", Toast.LENGTH_SHORT).show();
                }));
    }
}
