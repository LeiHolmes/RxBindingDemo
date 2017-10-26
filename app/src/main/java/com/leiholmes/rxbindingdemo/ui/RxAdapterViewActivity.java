package com.leiholmes.rxbindingdemo.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.leiholmes.rxbindingdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * Description:   RxBinding2中RxAdapterView演示Activity
 * author         xulei
 * Date           2017/10/26 09:49
 */
public class RxAdapterViewActivity extends BaseActivity {

    @BindView(R.id.lv_list)
    ListView lvList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rx_adapter_view;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        //假数据
        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("LeiHolmes:" + i);
        }
        //adapter适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        adapter.addAll(list);
        lvList.setAdapter(adapter);
        //ListView或GridView等item点击事件
        RxAdapterView.itemClicks(lvList)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Toast.makeText(RxAdapterViewActivity.this, "点击了第" + integer + "条：" + list.get(integer), Toast.LENGTH_SHORT).show();
                    }
                });
        //ListView或GridView等item长点击事件
        RxAdapterView.itemLongClicks(lvList)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Toast.makeText(RxAdapterViewActivity.this, "长点击了第" + integer + "条：" + list.get(integer), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
