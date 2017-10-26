package com.leiholmes.rxbindingdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.leiholmes.rxbindingdemo.R;

import butterknife.OnClick;

/**
 * Description:   入口Activity
 * author         xulei
 * Date           2017/10/25 09:51
 */
public class MainActivity extends BaseActivity {
    
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {

    }

    @OnClick({R.id.btn_rx_view, R.id.btn_rx_text_view, R.id.btn_rx_adapter_view, R.id.btn_rx_compound})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_rx_view:
                startActivity(new Intent(this, RxViewActivity.class));
                break;
            case R.id.btn_rx_text_view:
                startActivity(new Intent(this, RxTextViewActivity.class));
                break;
            case R.id.btn_rx_adapter_view:
                startActivity(new Intent(this, RxAdapterViewActivity.class));
                break;
            case R.id.btn_rx_compound:
                startActivity(new Intent(this, RxCompoundButtonActivity.class));
                break;
        }
    }
}
