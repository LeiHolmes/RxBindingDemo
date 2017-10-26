package com.leiholmes.rxbindingdemo.ui;

import android.os.Bundle;
import android.util.Log;
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
    private List<String> list;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rx_adapter_view;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        initData();
        itemClicks();
        itemLongClicks();
        itemSelections();
    }

    private void initData() {
        //假数据
        list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("LeiHolmes:" + i);
        }
        //adapter适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        adapter.addAll(list);
        lvList.setAdapter(adapter);
    }

    /**
     * ListView或GridView等item点击事件
     */
    private void itemClicks() {
        RxAdapterView.itemClicks(lvList)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(integer -> Toast.makeText(RxAdapterViewActivity.this, "点击了第" + integer + "条：" + list.get(integer), Toast.LENGTH_SHORT).show());
        //需要详细点击信息的可使用itemClickEvents(AdapterView view)
    }

    /**
     * ListView或GridView等item长点击事件
     */
    private void itemLongClicks() {
        RxAdapterView.itemLongClicks(lvList)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(integer -> Toast.makeText(RxAdapterViewActivity.this, "长点击了第" + integer + "条：" + list.get(integer), Toast.LENGTH_SHORT).show());
        //需要详细长点击信息的可使用itemLongClickEvents(AdapterView view)
    }

    /**
     * 条目被选中的事件
     * 其条目下需要有可被selected的控件才会触发
     */
    private void itemSelections() {
        RxAdapterView.itemSelections(lvList)
                .subscribe(integer -> Log.e("rx_binding_test", "itemSelections：" + integer));
        //需要详细选中信息的可使用selectionEvents(AdapterView view)
    }
}
