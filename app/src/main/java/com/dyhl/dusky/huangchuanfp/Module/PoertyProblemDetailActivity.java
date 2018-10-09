package com.dyhl.dusky.huangchuanfp.Module;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Adapter.ProblemR2Adapter;
import com.dyhl.dusky.huangchuanfp.Adapter.ProblemR1Adapter;
import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Design.FullyLinearLayoutManager;
import com.dyhl.dusky.huangchuanfp.Design.QAItem.AItem;
import com.dyhl.dusky.huangchuanfp.Design.QAItem.QItem;
import com.dyhl.dusky.huangchuanfp.Design.imagePicker.ImagePickerAdapter;
import com.dyhl.dusky.huangchuanfp.Design.imagePicker.SelectDialog;
import com.dyhl.dusky.huangchuanfp.Design.keyEditText.KeyEditText;
import com.dyhl.dusky.huangchuanfp.Module.entity.Answer;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.ProblemDetail;
import com.dyhl.dusky.huangchuanfp.Module.entity.Question;
import com.dyhl.dusky.huangchuanfp.Net.ApiConstants;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class PoertyProblemDetailActivity extends BaseActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener{
    String userid;
    String username;
    String loginname;
    int totalcount;
    protected ProgressDialog dialog;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private String code;
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.eidt_layout)
    CardView eidt_layout;

    @OnClick(R.id.img_back)
    public void back(){
        finish();
    }

    @BindView(R.id.txt_title)
    TextView apptitle;

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.all)
    TextView all;

    @BindView(R.id.value)
    KeyEditText value_edit;
    @OnClick(R.id.send)
    public void Send(){
        String v=value_edit.getText().toString();

        if(TextUtils.isEmpty(v)){
           ToastUtil.ShortToast("请输入内容后发送");
           return;
        }
        if(!TextUtils.isEmpty(username)&&username.equals(loginname)){
            question(v);
        }else{
            answer(v);
        }
    }

    ArrayList<Question> listR1;
    @BindView(R.id.recyclerView1)
    LinearLayout recyclerView1;

    ArrayList<Answer> listR2;
    @BindView(R.id.recyclerView2)
    LinearLayout recyclerView2;

    @BindView(R.id.publics)
    TextView publics;
    @OnClick(R.id.publics)
    public void set(){
        if(code.length()<=6){
            setPublic();
        }else{
            //ToastUtil.ShortToast("您没有操作权限");
        }
    }
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.obj)
    TextView obj;
    String num;

    int pageSize=20;
    int currentPageR1=1;
    int currentPageR2=1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_poertyproblemdetail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        initWidget();
        Intent it=getIntent();
        code= PreferenceUtil.getStringPRIVATE("permissions", UserState.NA);
        userid= PreferenceUtil.getStringPRIVATE("id", "");
        loginname=PreferenceUtil.getStringPRIVATE("name", "");
        num=it.getStringExtra("num");
        selImageList=new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, 0);
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        listR1=new ArrayList<>();
        listR2=new ArrayList<>();

    }

    @SuppressLint("CheckResult")
    public void question(String v){
        String value=v;
        dialog.show();
        RetrofitHelper.getPovertyProblemAPI()
                .Question(num,value)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    dialog.dismiss();
                    switch (state){
                        case "0":
                            hideKeyboard();
                            value_edit.setText("");
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            Question question= JSON.parseObject(apiMsg.getResult(), Question.class);
                            QItem qItem=new QItem(this);
                            qItem.setData(question);
                            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            recyclerView1.addView(qItem,0,params);
                            break;
                        case "-1":
                        case "-2":

                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;
                    }
                }, throwable -> {
                    dialog.dismiss();
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });

    }

    @SuppressLint("CheckResult")
    public void answer(String v){
        String value=v;
        dialog.show();
        RetrofitHelper.getPovertyProblemAPI()
                .Answer(num,value,userid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    dialog.dismiss();
                    switch (state){
                        case "0":
                            hideKeyboard();
                            value_edit.setText("");
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            Answer answer= JSON.parseObject(apiMsg.getResult(), Answer.class);
                            AItem aItem=new AItem(this);
                            aItem.setData(answer);
                            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            recyclerView2.addView(aItem,0,params);
                            totalcount=totalcount+1;
                            all.setText("全部回复 "+totalcount);
                            if("0".equals(totalcount)){
                                all.setText("全部回复 "+totalcount+" (暂时没有回复)");
                            }
                            break;
                        case "-1":
                        case "-2":

                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;
                    }
                }, throwable -> {
                    dialog.dismiss();
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });
    }

    @SuppressLint("CheckResult")
    public void getQuestionList(){
        RetrofitHelper.getPovertyProblemAPI()
                .getQuestionList(num,currentPageR1,pageSize)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            JSONObject obj = new JSONObject(apiMsg.getResult());
                            final JSONArray jsonArray = obj.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String o = jsonArray.getString(i);
                                Question question= JSON.parseObject(o, Question.class);
                                QItem qItem=new QItem(this);
                                qItem.setData(question);
                                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                recyclerView1.addView(qItem,params);
                            }
                            currentPageR1 += 1;
                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;
                    }

                    dialog.dismiss();
                }, throwable -> {
                    dialog.dismiss();
                    //tip.setHint("没有查询到相关信息");
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });
    }
    @SuppressLint("CheckResult")
    public void getAnswerList(){
        RetrofitHelper.getPovertyProblemAPI()
                .getAnswerList(num,currentPageR2,pageSize)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            JSONObject obj = new JSONObject(apiMsg.getResult());
                            final JSONArray jsonArray = obj.getJSONArray("data");
                            totalcount=obj.getInt("totalCount");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String o = jsonArray.getString(i);
                                Answer answer= JSON.parseObject(o, Answer.class);
                                AItem aItem=new AItem(this);
                                aItem.setData(answer);
                                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                recyclerView2.addView(aItem,params);
                            }
                            all.setText("全部回复 "+totalcount);
                            if("0".equals(totalcount)){
                                all.setText("全部回复 "+totalcount+" (暂时没有回复)");
                            }
                            currentPageR2 += 1;
                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;
                    }

                    dialog.dismiss();
                }, throwable -> {
                    dialog.dismiss();
                    //tip.setHint("没有查询到相关信息");
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });
    }

    @Override
    public void initToolBar() {

    }
    @SuppressLint("CheckResult")
    @Override
    public void loadData() {
        dialog = new ProgressDialog(PoertyProblemDetailActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("请求中...");
        dialog.show();
        RetrofitHelper.getPovertyProblemAPI()
                .getDetail(num)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            ProblemDetail problemDetail=JSON.parseObject(apiMsg.getResult(),ProblemDetail.class);
                            if(problemDetail!=null){
                                name.setText(problemDetail.getResponsible());
                                username=problemDetail.getResponsible();
                                time.setText(problemDetail.getInputtime());
                                content.setText(problemDetail.getValue());
                                obj.setText(problemDetail.getName());
                                if(!TextUtils.isEmpty(username)&&username.equals(loginname)){
                                    eidt_layout.setVisibility(View.GONE);
                                }else{
                                    eidt_layout.setVisibility(View.VISIBLE);
                                }
                                if(!TextUtils.isEmpty(problemDetail.getPublics())){
                                    publics.setText(" "+problemDetail.getPublics());
                                    if(code.length()<=6){
                                        Drawable drawable = getResources().getDrawable(R.drawable.ic_action_down);
                                        publics.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
                                    }else{
                                        if("公开".equals(problemDetail.getPublics())){
                                            Drawable drawable2 = getResources().getDrawable(R.drawable.pub);
                                            publics.setCompoundDrawablesWithIntrinsicBounds(drawable2,null,null,null);
                                        }else{
                                            Drawable drawable3 = getResources().getDrawable(R.drawable.pri);
                                            publics.setCompoundDrawablesWithIntrinsicBounds(drawable3,null,null,null);
                                        }
                                    }


                                }

                                String pics=problemDetail.getPictures();
                                if(pics!=null){
                                    String[] array = pics.split(",");
                                    for(int i=0;i<array.length;i++){
                                        if (!TextUtils.isEmpty(array[i])){
                                            ImageItem imageItem=new ImageItem();
                                            imageItem.path= ApiConstants.Base_URL+array[i];
                                            selImageList.add(imageItem);
                                        }
                                    }
                                    adapter.setMaxImgCount(selImageList.size());
                                    adapter.setImages(selImageList);
                                }


                            }

                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;
                    }

                    dialog.dismiss();
                }, throwable -> {
                    dialog.dismiss();
                    //tip.setHint("没有查询到相关信息");
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });
        getQuestionList();
        getAnswerList();
    }


    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                intentPreview.putExtra(ImagePicker.EXTRA_CAN_DELETE, false);//设置不能点击删除，因为是作展示用的
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }


    private void initWidget() {
        setAppTitle("问题详情");
    }

    public void setAppTitle(String title) {
        Log.d("reg", "title:" + title);
        apptitle.setText(title);
    }


    public void setPublic(){
        List<String> names = new ArrayList<>();
        names.add("公开");
        names.add("屏蔽");
        showDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        RetrofitHelper.getCommonServiceAPI()
                                .setupPublics(1,num,"公开")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(bean -> {
                                    String a=bean.string();
                                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                                    String state = apiMsg.getState();
                                    switch (state){
                                        case "0":
                                            publics.setText("公开");
                                            Drawable drawable = getResources().getDrawable(R.drawable.pub);
                                            publics.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);

                                            ToastUtil.ShortToast(apiMsg.getMessage());
                                            break;
                                        case "-1":
                                        case "-2":
                                            ToastUtil.ShortToast(apiMsg.getMessage());
                                            break;
                                    }
                                }, throwable -> {
                                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                                });

                        break;
                    case 1:
                        RetrofitHelper.getCommonServiceAPI()
                                .setupPublics(1,num,"屏蔽")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(bean -> {
                                    String a=bean.string();
                                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                                    String state = apiMsg.getState();
                                    switch (state){
                                        case "0":
                                            publics.setText("屏蔽");
                                            Drawable drawable = getResources().getDrawable(R.drawable.pri);
                                            publics.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
                                            ToastUtil.ShortToast(apiMsg.getMessage());
                                            break;
                                        case "-1":
                                        case "-2":
                                            ToastUtil.ShortToast(apiMsg.getMessage());
                                            break;
                                    }
                                }, throwable -> {
                                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                                });
                        break;
                    default:
                        break;
                }

            }
        }, names);
    }


    SelectDialog dialogs;
    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {

        dialogs = new SelectDialog((Activity) this, R.style
                    .transparentFrameWindowStyle,
                    listener, names);

            if (!this.isFinishing()) {
                dialogs.show();
            }

        return dialogs;
    }

    private void hideKeyboard() {
       InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
        if (this.getCurrentFocus().getWindowToken() != null) {
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
   }
   }
}
}
