package com.leiholmes.rxbindingdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:   入口Activity
 * author         xulei
 * Date           2017/10/25 09:51
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
                break;
            case R.id.btn_rx_compound:
                break;
        }
    }
}
