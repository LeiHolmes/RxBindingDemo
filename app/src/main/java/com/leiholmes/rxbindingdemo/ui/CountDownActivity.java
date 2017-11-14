package com.leiholmes.rxbindingdemo.ui;

import android.os.Bundle;
import android.widget.Button;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.leiholmes.rxbindingdemo.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Description:   利用RxBinding2演示倒计时获取验证码的功能
 * author         xulei
 * Date           2017/10/26 17:07
 */
public class CountDownActivity extends BaseActivity {
    @BindView(R.id.btn_get_code)
    Button btnGetCode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_count_down;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        //点击事件
        addDisposable(RxView.clicks(btnGetCode)
                //1s防抖
                .throttleFirst(1, TimeUnit.SECONDS)
                //UI线程
                .observeOn(AndroidSchedulers.mainThread())
                //点击后设置为不可点击
                .doOnNext(o -> RxView.enabled(btnGetCode).accept(false))
                .subscribe(o -> {
                            //从0开始发射11个数字为：0-10依次输出，延时0s执行，每1s发射一次。
                            addDisposable(Flowable.intervalRange(0, 11, 0, 1, TimeUnit.SECONDS)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    //每次发射数字更新UI
                                    .doOnNext(aLong -> {
                                        RxTextView.text(btnGetCode).accept("重新获取(" + (10 - aLong) + ")");
                                    })
                                    //倒计时完毕更新UI，设置为可点击
                                    .doOnComplete(() -> {
                                        RxView.enabled(btnGetCode).accept(true);
                                        RxTextView.text(btnGetCode).accept("获取验证码");
                                    })
                                    .subscribe());
                        }
                )
        );
    }
}
