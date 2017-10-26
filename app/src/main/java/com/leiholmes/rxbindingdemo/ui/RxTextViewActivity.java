package com.leiholmes.rxbindingdemo.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;
import com.jakewharton.rxbinding2.widget.TextViewBeforeTextChangeEvent;
import com.jakewharton.rxbinding2.widget.TextViewEditorActionEvent;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.leiholmes.rxbindingdemo.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Description:   RxBinding2中RxTextView演示Activity
 * author         xulei
 * Date           2017/10/25 13:50
 */
public class RxTextViewActivity extends BaseActivity {
    @BindView(R.id.et_rx_text_view)
    EditText etRxTextView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rx_text_view;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        textChanges();
//        beforeTextChangeEvents();
//        afterTextChangeEvents();
        editorActions();
    }

    /**
     * TextView内容变化事件
     * textChanges只返回改变之后的CharSequence
     * textChangeEvents返回的TextViewTextChangeEvent中包含start，before，text, count等详细信息
     */
    private void textChanges() {
        //textChanges
        addDisposable(RxTextView.textChanges(etRxTextView)
                //项目场景中若需要即时根据改变内容请求服务器检索的话，可使用debounce进行限流，就能避免因为用户输入速度过快导致多次请求服务器了。
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                //CharSequence转换为String
                .map(CharSequence::toString)
                .subscribe(s -> {
                    Log.e("rx_binding_test", "textChanges:etRxTextView内容变化了:" + s);
                    Toast.makeText(RxTextViewActivity.this, "etRxTextView内容变化了", Toast.LENGTH_SHORT).show();
                }));
        //textChangeEvents
//        addDisposable(RxTextView.textChangeEvents(etRxTextView)
//                .subscribe(textViewTextChangeEvent -> {
//                    Log.e("rx_binding_test", "textChanges:etRxTextView内容变化了:" + "before:" +
//                            textViewTextChangeEvent.before() + ",start:" + textViewTextChangeEvent.start() +
//                            ",text:" + textViewTextChangeEvent.text() + ",count:" + textViewTextChangeEvent.count());
//                    Toast.makeText(RxTextViewActivity.this, "etRxTextView内容变化了", Toast.LENGTH_SHORT).show();
//                }));
    }

    /**
     * TextView内容变化前的事件
     */
    private void beforeTextChangeEvents() {
        addDisposable(RxTextView.beforeTextChangeEvents(etRxTextView)
                .subscribe(
                        textViewBeforeTextChangeEvent -> {
                            Log.e("rx_binding_test", "beforeTextChangeEvents:etRxTextView内容变化前:" + textViewBeforeTextChangeEvent.text());
                            Toast.makeText(RxTextViewActivity.this, "etRxTextView内容变化前", Toast.LENGTH_SHORT).show();
                        }));
    }

    /**
     * TextView内容变化后的事件
     */
    private void afterTextChangeEvents() {
        addDisposable(RxTextView.afterTextChangeEvents(etRxTextView)
                .subscribe(textViewAfterTextChangeEvent -> {
                    Log.e("rx_binding_test", "afterTextChangeEvents:etRxTextView内容变化后");
                    Toast.makeText(RxTextViewActivity.this, "etRxTextView内容变化后", Toast.LENGTH_SHORT).show();
                }));
    }

    /**
     * 输入完成点击回车键事件
     * editorActions相当于简化版原来的setOnEditorActionListener
     * editorActionEvents额外返回当前View，ActionId，KeyEvent等信息
     * 走两次的解决办法：KeyEvent包含down和up事件，所以走两次。根据KeyEvent加入一种判断即可
     */
    private void editorActions() {
        addDisposable(RxTextView.editorActions(etRxTextView)
                .subscribe(
                        integer -> {
                            Log.e("rx_binding_test", "editorActions:输入完毕，点击回车:");
                            Toast.makeText(RxTextViewActivity.this, "输入完毕，点击回车", Toast.LENGTH_SHORT).show();
                        }));
        addDisposable(RxTextView.editorActionEvents(etRxTextView)
                .subscribe(
                        textViewEditorActionEvent -> {
                            KeyEvent keyEvent = textViewEditorActionEvent.keyEvent();
                            //解决走两次问题
                            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                                Log.e("rx_binding_test", "editorActionEvents:输入完毕，点击回车:" + textViewEditorActionEvent.keyEvent());
                                Toast.makeText(RxTextViewActivity.this, "输入完毕，点击回车", Toast.LENGTH_SHORT).show();
                            }
                        }));
    }
}
