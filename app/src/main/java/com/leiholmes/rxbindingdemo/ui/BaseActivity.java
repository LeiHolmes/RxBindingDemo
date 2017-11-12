package com.leiholmes.rxbindingdemo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Description:   基本Activity，主要用于收集订阅与取消订阅，ButterKnife的绑定与解绑
 * author         xulei
 * Date           2017/10/26 09:51
 */
public abstract class BaseActivity extends AppCompatActivity {
    public CompositeDisposable mCompositeDisposable;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mUnbinder = ButterKnife.bind(this);
        mCompositeDisposable = new CompositeDisposable();
        onViewCreated(savedInstanceState);
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
    public void clearDisposable() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearDisposable();
        mUnbinder.unbind();
    }

    protected abstract int getLayoutId();

    protected abstract void onViewCreated(Bundle savedInstanceState);
}
