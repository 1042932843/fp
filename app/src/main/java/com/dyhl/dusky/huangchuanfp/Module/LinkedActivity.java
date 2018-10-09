package com.dyhl.dusky.huangchuanfp.Module;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;


public class LinkedActivity extends BaseActivity {

    private EditText etLinked, etDesc;
    public static final int REQUEST_CODE_EDIT_LINKED = 2001;//编辑文本

    @BindView(R.id.txt_title)
    TextView apptitle;

    @OnClick(R.id.img_back)
    public void back() {
        finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_linked;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        etLinked = (EditText) findViewById(R.id.et_txteditor_linked);
        etDesc = (EditText) findViewById(R.id.et_txteditor_desc);
        initWidget();
    }

    @Override
    public void initToolBar() {

    }

    public void onOK(View view) {
        if (TextUtils.isEmpty(etLinked.getText().toString())) {
            ToastUtil.ShortToast("请输入正确的链接");
            return;
        }
        Intent intent = new Intent();
//        linkContent.setTitle(TextUtils.isEmpty(etDesc.getText().toString()) ? getString(R.string.link_address) : etDesc.getText().toString());
//        linkContent.setLink(etLinked.getText().toString());
        intent.putExtra("txt", TextUtils.isEmpty(etDesc.getText().toString()) ? "点击此处跳转" : etDesc.getText().toString());
        intent.putExtra("link", etLinked.getText().toString());

        this.setResult(REQUEST_CODE_EDIT_LINKED, intent);
        finish();
    }

    private void initWidget() {
        setAppTitle("插入链接");
    }

    public void setAppTitle(String title) {
        Log.d("reg", "title:" + title);
        apptitle.setText(title);
    }
}
