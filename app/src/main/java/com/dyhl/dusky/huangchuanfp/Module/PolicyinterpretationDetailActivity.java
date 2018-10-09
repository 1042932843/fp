package com.dyhl.dusky.huangchuanfp.Module;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Design.imagePicker.SelectDialog;
import com.dyhl.dusky.huangchuanfp.Design.keyEditText.KeyEditText;
import com.dyhl.dusky.huangchuanfp.Module.entity.Annuncement;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.Exp;
import com.dyhl.dusky.huangchuanfp.Net.ApiConstants;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.richeditor.RichEditor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.dyhl.dusky.huangchuanfp.Module.LinkedActivity.REQUEST_CODE_EDIT_LINKED;


public class PolicyinterpretationDetailActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener {
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    Tiny.FileCompressOptions options;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 1;//允许选择图片最大数
    List<File> files;
    protected ProgressDialog dialog;
    @BindView(R.id.txt_right0)
    TextView txt_right0;
    @OnClick(R.id.txt_right0)
    public void choiceLevel(){
        if (code.length() <= 6) {
            //创建弹出式菜单对象（最低版本11）
            PopupMenu popup = new PopupMenu(this, txt_right0);//第二个参数是绑定的那个view
            //获取菜单填充器
            MenuInflater inflater = popup.getMenuInflater();
            //填充菜单
            inflater.inflate(R.menu.menu, popup.getMenu());
            //绑定菜单项的点击事件
            popup.setOnMenuItemClickListener(this);
            //显示(这一行代码不要忘记了)
            popup.show();
        } else {
            //ToastUtil.ShortToast("您没有操作权限");
        }

    }
    int level=2;
    //弹出式菜单的单击事件处理
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.xianji:
                //Toast.makeText(this, "县级", Toast.LENGTH_SHORT).show();
                level=0;
                txt_right0.setText("[县级]");
                break;
            case R.id.zhenji:
                //Toast.makeText(this, "镇级", Toast.LENGTH_SHORT).show();
                level=1;
                txt_right0.setText("[镇级]");
                break;
            case R.id.cunji:
                //Toast.makeText(this, "村级", Toast.LENGTH_SHORT).show();
                level=2;
                txt_right0.setText("[村级]");
                break;
            default:
                break;
        }
        return false;
    }

    @BindView(R.id.editor)
    RichEditor mEditor;

    @OnClick(R.id.img_back)
    public void back() {
        finish();
    }

    @BindView(R.id.txt_title)
    TextView apptitle;

    @BindView(R.id.title)
    KeyEditText title;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.from)
    TextView from;
    @BindView(R.id.publics)
    TextView publics;

    @OnClick(R.id.publics)
    public void set() {
        if (code.length() <= 6) {
            setPublic();
        } else {
            //ToastUtil.ShortToast("您没有操作权限");
        }
    }


    @BindView(R.id.edit_h)
    HorizontalScrollView edit_h;
    @BindView(R.id.info)
    RelativeLayout info;
    @BindView(R.id.txt_right)
    TextView txt_right;

    @OnClick(R.id.txt_right)
    public void push() {
        if ("发布".equals(txt_right.getText().toString())){
            if(TextUtils.isEmpty(title.getText().toString())){
                ToastUtil.ShortToast("请编辑标题后提交");
                return;
            }
            if(TextUtils.isEmpty(mEditor.getHtml())){
                ToastUtil.ShortToast("请编辑内容后提交");
                return;
            }
            if ("-10086".equals(id)) {
                baocun();
            }else{
                xiugai(id);
            }

        }else{
            txt_right.setText("发布");
            edit_h.setVisibility(true ? View.VISIBLE : View.GONE);
            //设置编辑器是否可用
            mEditor.setInputEnabled(true);
            title.setEnabled(true);
        }
    }

    String id;
    String type;
    String code;

    @Override
    public int getLayoutId() {
        return R.layout.activity_annuncementdetail_html;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initViews(Bundle savedInstanceState) {
        initWidget();

        dialog = new ProgressDialog(PolicyinterpretationDetailActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("请求中...");
        Intent it = getIntent();
        selImageList = new ArrayList<>();
        options = new Tiny.FileCompressOptions();
        files=new ArrayList<>();
        id = it.getStringExtra("id");
        type=it.getStringExtra("type");
        code = PreferenceUtil.getStringPRIVATE("permissions", UserState.NA);

        //初始化编辑高度
        mEditor.setEditorHeight(400);
        //初始化字体大小
        mEditor.setEditorFontSize(32);
        //初始化字体颜色
        //mEditor.setEditorFontColor(Color.BLACK);
        //mEditor.setEditorBackgroundColor(Color.BLUE);

        //初始化内边距
        mEditor.setPadding(0, 12, 0, 10);
        //设置编辑框背景，可以是网络图片
        // mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        // mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);

        txt_right.setText("编辑");
        txt_right.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        title.setEnabled(true);
        txt_right.setVisibility(true&&"notice".equals(type) ? View.VISIBLE : View.GONE);



        mEditor.getSettings().setDisplayZoomControls(true);
// 设置可以支持缩放
        mEditor.getSettings().setSupportZoom(true);
        //mEditor.setInitialScale(200);//为25%，最小缩放等级
// 设置出现缩放工具
        mEditor.getSettings().setBuiltInZoomControls(true);
        //不显示webview缩放按钮
        mEditor.getSettings().setDisplayZoomControls(false);
//扩大比例的缩放
        mEditor.getSettings().setUseWideViewPort(true);
//自适应屏幕
        //mEditor.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mEditor.getSettings().setLoadWithOverviewMode(true);
        mEditor.setInputEnabled(false);
        title.setEnabled(false);
        if ("-10086".equals(id)) {
            info.setVisibility(View.GONE);
            txt_right.setText("发布");
            txt_right0.setVisibility(View.VISIBLE);
            //设置默认显示语句
            mEditor.setPlaceholder("在此输入和编辑内容...");

            edit_h.setVisibility(true ? View.VISIBLE : View.GONE);
            //设置编辑器是否可用
            mEditor.setInputEnabled(true);
            title.setEnabled(true);
            if (code.length() <= 6) {
                txt_right0.setVisibility(View.VISIBLE);
            }else{
                txt_right0.setVisibility(View.GONE);
            }
        } else {
            txt_right0.setVisibility(View.GONE);
            txt_right.setVisibility(View.GONE);
            info.setVisibility(View.VISIBLE);
        }
        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSubscript();
            }
        });

        findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSuperscript();
            }
        });

        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });

        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBlockquote();
            }
        });

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setNumbers();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgpick();
            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(PolicyinterpretationDetailActivity.this, LinkedActivity.class), REQUEST_CODE_EDIT_LINKED);


            }
        });
        findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertTodo();
            }
        });
    }

    @Override
    public void initToolBar() {

    }

    @SuppressLint("CheckResult")
    @Override
    public void loadData() {
        if("-10086".equals(id)){
            return;
        }
        dialog.show();
        RetrofitHelper.getPolicyServiceAPI()
                .getNewsDetail(id)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a = bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a, ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state) {
                        case "0":
                            Annuncement annuncement = JSON.parseObject(apiMsg.getResult(), Annuncement.class);
                            title.setText(annuncement.getTitle());
                            time.setText(annuncement.getInputtime());
                            if (!TextUtils.isEmpty(annuncement.getSource())) {
                                from.setText("来源：" + annuncement.getSource());
                            }
                            if (!TextUtils.isEmpty(annuncement.getPublics())) {
                                publics.setText(" " + annuncement.getPublics());
                                if (code.length() <= 6) {
                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_action_down);
                                    publics.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                                } else {
                                    if ("公开".equals(annuncement.getPublics())) {
                                        Drawable drawable2 = getResources().getDrawable(R.drawable.pub);
                                        publics.setCompoundDrawablesWithIntrinsicBounds(drawable2, null, null, null);
                                    } else {
                                        Drawable drawable3 = getResources().getDrawable(R.drawable.pri);
                                        publics.setCompoundDrawablesWithIntrinsicBounds(drawable3, null, null, null);
                                    }
                                }


                            }

                            String html=annuncement.getValue();
                            String html2="<html>\n" +
                                    "<head>\n" +
                                    "\t<title></title>\n" +
                                    "</head>\n" +
                                    "<meta name=\"viewport\" content=\"user-scalable=yes\" />\n" +
                                    "<meta name=\"mobileOptimized\" content=\"width\">"+
                                    "<meta name=\"handheldFriendly\" content=\"true\">"+
                                    "<body>\n" +
                                    "<h1 style=\"margin: 0cm 0cm 0.0001pt; line-height: 26pt; text-align: center;\"><span style=\"font-size:100px;mso-bidi-font-size:22.0pt;font-family:方正小标宋简体;\n" +
                                    "mso-hansi-font-family:宋体;color:black\">习近平总书记关于脱贫攻坚工作的<a name=\"_Toc11629_WPSOffice_Level1\"><span style=\"mso-bookmark:_Toc8441\"><span lang=\"EN-US\"><o:p></o:p></span></span></a></span></h1>\n" +
                                    "\n" +
                                    "<h1 style=\"margin: 0cm 0cm 0.0001pt; line-height: 26pt; text-align: center;\"><span style=\"mso-bookmark:_Toc11629_WPSOffice_Level1\"><span style=\"mso-bookmark:_Toc8441\"><span style=\"font-size:\n" +
                                    "19.0pt;mso-bidi-font-size:22.0pt;font-family:方正小标宋简体;mso-hansi-font-family:\n" +
                                    "宋体;color:black\">重 要 论 述</span></span></span><span lang=\"EN-US\" style=\"font-size:19.0pt;mso-bidi-font-size:22.0pt;font-family:方正小标宋简体;\n" +
                                    "mso-hansi-font-family:宋体;color:black\"><o:p></o:p></span></h1>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"line-height:30.0pt\"><span lang=\"EN-US\" style=\"font-size:14.0pt;mso-bidi-font-size:16.0pt;font-family:宋体;mso-bidi-font-family:\n" +
                                    "仿宋_GB2312;color:black\"><o:p>&nbsp;</o:p></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●全面建成小康社会，最艰巨最繁重的任务在农村、特别是在贫困地区。<b>没有农村的小康，特别是没有贫困地区的小康，就没有全面建成小康社会。</b>大家要深刻理解这句话的含义。因此，要提高对做好扶贫开发工作重要性的认识，增强做好扶贫开发工作的责任感和使命感。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;摘自《在河北省阜平县考察扶贫开发工作时的讲话》（<span lang=\"EN-US\">2012</span>年<span lang=\"EN-US\">12</span>月<span lang=\"EN-US\">29</span>日、<span lang=\"EN-US\">30</span>日），《做焦裕禄式的县委书记》，中央文献出版社<span lang=\"EN-US\">2015</span>年版，第<span lang=\"EN-US\">16</span>页<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●深入推进扶贫开发，帮助困难群众特别是革命老区、贫困山区困难群众早日脱贫致富，<b>到<span lang=\"EN-US\">2020</span>年稳定实现扶贫对象不愁吃、不愁穿，保障其义务教育、基本医疗、住房，是中央确定的目标。</b><span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;摘自《在河北省阜平县考察扶贫开发工作时的讲话》（<span lang=\"EN-US\">2012</span>年<span lang=\"EN-US\">12</span>月<span lang=\"EN-US\">29</span>日、<span lang=\"EN-US\">30</span>日），《做焦裕禄式的县委书记》，中央文献出版社<span lang=\"EN-US\">2015</span>年版，第<span lang=\"EN-US\">16</span>页<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●扶贫要实事求是，因地制宜。要精准扶贫，切忌喊口号，也不要定好高骛远的目标。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;摘自习近平在湖南湘西花垣县十八洞村考察座谈会上的讲话（<span lang=\"EN-US\">2013</span>年<span lang=\"EN-US\">11</span>月）<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●国家级扶贫开发工作重点县就是要把减少扶贫对象作为首要任务，坚定信心，找准路子，<b>加快转变扶贫开发方式，实行精准扶贫。</b>既不能一味等靠、无所作为，也不能违背规律、盲目蛮干，甚至搞劳民伤财的&ldquo;形象工程&rdquo;。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;摘自习近平在中央经济工作会议上的讲话（<span lang=\"EN-US\">2013</span>年<span lang=\"EN-US\">12</span>月<span lang=\"EN-US\">23</span>日）<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span lang=\"EN-US\" style=\"font-size:16.0pt;font-family:宋体;mso-bidi-font-family:宋体;color:black\">&nbsp;</span><span style=\"font-size:16.0pt;font-family:宋体;mso-bidi-font-family:宋体;color:black\">●要加强监管，杜绝截留、挪用和贪污扶贫资金，真正把资金用到扶贫对象上，帮助贫困群众早日过上小康生活。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">要进一步增强责任感和紧迫感，坚持科学规划、分类指导，实施精准扶贫，增强内生动力。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;习近平在河北省委有关报告上的批示（<span lang=\"EN-US\">2014</span>年<span lang=\"EN-US\">2</span>月<span lang=\"EN-US\">25</span>日）<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●精准扶贫，就是要对扶贫对象实行精细化管理，对扶贫资源实行精确化配置，对扶贫对象实行精准化扶持，确保扶贫资源真正用在扶贫对象身上、真正用在贫困地区。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;习近平在参加十二届全国人大二次会议贵州代表团审议时的讲话（<span lang=\"EN-US\">2014</span>年<span lang=\"EN-US\">3</span>月<span lang=\"EN-US\">7</span>日）<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●抓好教育是扶贫开发的根本大计，要让贫困家庭的孩子都能接受公平的有质量的教育，起码学会一项有用的技能，不要让孩子输在起跑线上，尽力阻断贫困代际传递。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;习近平在中央经济工作会议上的讲话（<span lang=\"EN-US\">2014</span>年<span lang=\"EN-US\">12</span>月<span lang=\"EN-US\">9</span>日）<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●要以更加明确的目标、更加有力的举措、更加有效的行动，深入实施精准扶贫、精准脱贫，项目安排和资金使用都要提高精准度，扶到点上、根上，让贫困群众真正得到实惠。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;习近平在云南调研时强调（<span lang=\"EN-US\">2015</span>年<span lang=\"EN-US\">1</span>月<span lang=\"EN-US\">19</span>日至<span lang=\"EN-US\">21</span>日）<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●要帮助贫困地区群众提高身体素质、文化素质、就业能力，努力阻止因病致贫、因病返贫，打开孩子们通过学习成长、青壮年通过多渠道就业改变命运的扎实通道，坚决阻止贫困现象代际传递。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;习近平到广西代表团参加审议时强调（<span lang=\"EN-US\">2015</span>年<span lang=\"EN-US\">3</span>月<span lang=\"EN-US\">8</span>日）<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●要采取超常举措，拿出过硬办法，按照精准扶贫、精准脱贫要求，用一套政策组合拳，确保在既定时间节点打赢扶贫开发攻坚战。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;习近平在浙江召开华东<span lang=\"EN-US\">7</span>省市党委主要负责同志座谈会强调（<span lang=\"EN-US\">2015</span>年<span lang=\"EN-US\">5</span>月<span lang=\"EN-US\">27</span>日）<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●扶贫开发是全党全社会的共同责任，要动员和凝聚全社会力量广泛参与。要坚持专项扶贫、行业扶贫、社会扶贫等多方力量、多种举措有机结合和互为支撑的&ldquo;三位一体&rdquo;大扶贫格局，强化举措，扩大成果。要健全东西部协作、党政机关定点扶贫机制，各部门要积极完成所承担的定点扶贫任务，东部地区要加大对西部地区的帮扶力度，国有企业要承担更多扶贫开发任务。要广泛调动社会各界参与扶贫开发积极性，鼓励、支持、帮助各类非公有制、社会组织、个人自愿采取包干方式参与扶贫。、<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">扶贫开发贵在精准，重在精准，成败之举在于精准。各地都要在扶持对象精准、项目安排精准、资金使用精准、措施到户精准、因村派人（第一书记）精准、脱贫成效精准上想办法、出实招、见真效。要坚持因人因地施策，因贫困原因施策，因贫困类型施策，区别不同情况，做到对症下药、精准滴灌、靶向治疗，不搞大水漫灌、走马观花、大而化之。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;习近平在部分省区市扶贫攻坚与&ldquo;十三五&rdquo;时期经济社会发展座谈会上的讲话（<span lang=\"EN-US\">2015</span>年<span lang=\"EN-US\">6</span>月<span lang=\"EN-US\">18</span>日）<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●到<span lang=\"EN-US\">2020</span>年全面建成小康社会，最艰巨的任务在贫困地区，我们必须补上这个短板。扶贫必扶智。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;摘自习近平给&ldquo;国培计划（<span lang=\"EN-US\">2014</span>）&rdquo;北师大贵州研修班参训教师的回信（<span lang=\"EN-US\">2015</span>年<span lang=\"EN-US\">9</span>月<span lang=\"EN-US\">9</span>日）<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●消除贫困、改善民生、逐步实现共同富裕，是社会主义的本质要求，是我们党的重要使命。全面建成小康社会，是我们对全国人民的庄严承诺。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">越是进行脱贫攻坚战，越是要加强和改善党的领导。各级党委和政府必须坚定信心、勇于担当，把脱贫职责扛在肩上，把脱贫任务抓在手上。各级领导干部要保持顽强的工作作风和拼劲，满腔热情做好脱贫攻坚工作。脱贫攻坚任务重的地区党委和政府要把脱贫攻坚作为&ldquo;十三五&rdquo;期间头等大事和第一民生工程来抓，坚持以脱贫攻坚统揽经济社会发展全局。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">中央和国家机关各部门要把脱贫攻坚作为分内职责，加强对本部门本行业脱贫攻坚的组织领导，运用部门职能和行业资源做好工作，做到扶贫项目优先安排、扶贫资金优先保障、扶贫工作优先对接、扶贫措施优先落实。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;摘自习近平在中央扶贫开发工作会议上重要讲话（<span lang=\"EN-US\">2015</span>年<span lang=\"EN-US\">11</span>月<span lang=\"EN-US\">27</span>日至<span lang=\"EN-US\">28</span>日）<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●扶贫开发到了攻克最后堡垒的阶段，所面对的多数是贫中之贫、困中之困，需要以更大的决心、更明确的思路、更精准的举措抓工作。要坚持时间服从质量，科学确定脱贫时间，不搞层层加码。要真扶贫、扶真贫、真脱贫。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;习近平在主持召开东西部扶贫协作座谈会并发表重要讲话（<span lang=\"EN-US\">2016</span>年<span lang=\"EN-US\">7</span>月<span lang=\"EN-US\">20</span>日）<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●脱贫攻坚工作要实打实干，拿出&ldquo;绣花&rdquo;的功夫， 发扬钉钉子精神，一切工作都要落实到为贫困群众解决实际问题上，防止形式主义，不能搞花拳绣腿，不能搞繁文缛节，不能做表面文章。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">扶贫工作必须务实，脱贫过程必须扎实，脱贫结果必须真实。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;习近平在山西太原市主持召开深度贫困地区脱贫攻坚座谈会并发表重要讲话（<span lang=\"EN-US\">2017</span>年<span lang=\"EN-US\">6</span>月<span lang=\"EN-US\">23</span>日）<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●致富不致富<span lang=\"EN-US\">,</span>关键看干部。在脱贫攻坚战场上，基层干部在宣讲扶贫政策、整合扶贫资源、分配扶贫资金、推动扶贫项目落实等方面具有关键作用。要采取双向挂职、两地培训等方式，加大对西部地区干部特别是基层干部、贫困村创业致富带头人的培训力度，帮助西部地区提高当地人才队伍能力和水平，打造一支留得住、能战斗、带不走的人才队伍。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;习近平在主持召开东西部扶贫协作座谈会并发表重要讲话（<span lang=\"EN-US\">2016</span>年<span lang=\"EN-US\">7</span>月<span lang=\"EN-US\">20</span>日）<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●我们今天的努力是要使这些地区的群众实现&ldquo;两不愁三保障&rdquo;，使这些地区基本公共服务主要领域指标接近全国平均水平。在这个问题上，我们要实事求是，不要好高骛远，不要吊高各方面胃口。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;摘自习近平在深度贫困地区脱贫攻坚座谈会上的讲话（<span lang=\"EN-US\">2017</span>年<span lang=\"EN-US\">6</span>月<span lang=\"EN-US\">23</span>日）<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●从现在到<span lang=\"EN-US\">2020</span>年，是全面建成小康社会决胜期。要突出抓重点、补短板、强弱项，特别是要坚决打好防范化解重大风险、精准脱贫、污染防治的攻坚战，使全面建成小康社会得到人民认可、经得起历史检验。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;摘自党的十九大报告（<span lang=\"EN-US\">2017</span>年<span lang=\"EN-US\">10</span>月<span lang=\"EN-US\">18</span>日）<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●要实现精准扶贫，人才是关键，培养一支素质高、能力强、技术硬的&ldquo;精准&rdquo;人才队伍，是打赢扶贫攻坚战的基础。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;摘自党的十九大报告（<span lang=\"EN-US\">2017</span>年<span lang=\"EN-US\">10</span>月<span lang=\"EN-US\">18</span>日）<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●<span style=\"background:\n" +
                                    "white\">我国社会主要矛盾已经转化为人民日益增长的美好生活需要和不平衡不充分的发展之间的矛盾。</span><span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;摘自党的十九大报告（<span lang=\"EN-US\">2017</span>年<span lang=\"EN-US\">10</span>月<span lang=\"EN-US\">18</span>日）<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●全面建成小康社会，一个不能少；共同富裕路上，一个不能掉队。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">我们将举全党全国之力，坚决完成脱贫攻坚任务，确保兑现我们的承诺。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;摘自习近平在中共十九大的中外记者会上的讲话（<span lang=\"EN-US\">2017</span>年<span lang=\"EN-US\">10</span>月<span lang=\"EN-US\">25</span>日）<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●必须打好精准脱贫攻坚战，走中国特色减贫之路。坚持精准扶贫、精准脱贫，把提高脱贫质量放在首位，注重扶贫同扶志、扶智相结合，瞄准贫困人口精准帮扶，聚焦深度贫困地区集中发力，激发贫困人口内生动力，强化脱贫攻坚责任和监督，开展扶贫领域腐败和作风问题专项治理，采取更加有力的举措、更加集中的支持、更加精细的工作，坚决打好精准脱贫这场对全面建成小康社会具有决定意义的攻坚战。<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;摘自习近平在中央农村工作会议上的讲话（<span lang=\"EN-US\" style=\"background:white\">2017</span><span style=\"background:white\">年<span lang=\"EN-US\">12</span>月<span lang=\"EN-US\">28-29</span>日</span>）<span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●<span style=\"background:\n" +
                                    "white\">到<span lang=\"EN-US\">2020</span>年我国现行标准下农村贫困人口实现脱贫，是我们的庄严承诺。一诺千金。到<span lang=\"EN-US\">2020</span>年只有<span lang=\"EN-US\">3</span>年的时间，全社会要行动起来，尽锐出战，精准施策，不断夺取新胜利。<span lang=\"EN-US\">3</span>年后如期打赢脱贫攻坚战，这在中华民族几千年历史发展上将是首次整体消除绝对贫困现象，让我们一起来完成这项对中华民族、对整个人类都具有重大意义的伟业。<span lang=\"EN-US\"><o:p></o:p></span></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;摘自<span style=\"background:\n" +
                                    "white\">习近平二〇一八年新年贺词（<span lang=\"EN-US\">2017</span>年<span lang=\"EN-US\">12</span>月<span lang=\"EN-US\">31</span>日）<span lang=\"EN-US\"><o:p></o:p></span></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●在脱贫攻坚伟大实践中，我们积累了许多宝贵经验。<span style=\"background:white\">一是坚持党的领导、强化组织保证，落实脱贫攻坚一把手负责制，省市县乡村五级书记一起抓，为脱贫攻坚提供坚强政治保证。二是坚持精准方略、提高脱贫实效，解决好扶持谁、谁来扶、怎么扶、如何退问题，扶贫扶到点上扶到根上。三是坚持加大投入、强化资金支持，发挥政府投入主体和主导作用，吸引社会资金广泛参与脱贫攻坚。四是坚持社会动员、凝聚各方力量，充分发挥政府和社会两方面力量作用，形成全社会广泛参与脱贫攻坚格局。五是坚持从严要求、促进真抓实干，把全面从严治党要求贯穿脱贫攻坚工作全过程和各环节，确保帮扶工作扎实、脱贫结果真实，使脱贫攻坚成效经得起实践和历史检验。六是坚持群众主体、激发内生动力，充分调动贫困群众积极性、主动性、创造性，用人民群众的内生动力支撑脱贫攻坚。这些经验弥足珍贵，要长期坚持并不断完善和发展。<span lang=\"EN-US\"><o:p></o:p></span></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;<span style=\"background:\n" +
                                    "white\">习近平在成都市主持召开打好精准脱贫攻坚战座谈会上的讲话（<span lang=\"EN-US\">2018</span>年<span lang=\"EN-US\">2</span>月<span lang=\"EN-US\">12</span>日）<br />\n" +
                                    "&nbsp;&nbsp;<img alt=\"\" src=\"http://218.29.203.38:8094/hcfp-jeecg/userfiles/images/QQ%E5%9B%BE%E7%89%8720180505151734.png\" /><img alt=\"\" src=\"http://218.29.203.38:8094/hcfp-jeecg/userfiles/images/V8_%40N4%40A5BK~TTX%25TH1%24K%40K.jpg\" style=\"max-width: 100%; max-height: 100%;\" /><span lang=\"EN-US\"><o:p></o:p></span></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">●<span style=\"background:\n" +
                                    "white\">实行最严格的考核评估制度是打赢脱贫攻坚战的重要保障。离脱贫攻坚目标实现期限越近，任务越艰巨，越要实行严格的考核评估。要用好考核结果，对好的给予表扬奖励，对差的约谈整改，对违纪违规的严肃查处。要结合脱贫攻坚进展和考核情况，改进完善考核评估机制，通过较真碰硬的考核，促进真抓实干，确保脱贫工作务实，脱贫过程扎实，脱贫结果真实，让脱贫成效真正获得群众认可、经得起实践和历史检验。<span lang=\"EN-US\"><o:p></o:p></span></span></span></p>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\">&mdash;&mdash;<span style=\"background:\n" +
                                    "white\">习近平在听取<span lang=\"EN-US\">2017</span>年省级党委和政府脱贫攻坚工作成效考核情况汇报会上的讲话（<span lang=\"EN-US\">2018</span>年<span lang=\"EN-US\">3</span>月<span lang=\"EN-US\">30</span>日）</span></span></p>\n" +
                                    "\n" +
                                    "<table border=\"2\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;max-width:100%;\" max-height=100%\">\n" +
                                    "\t<colgroup>\n" +
                                    "\t\t<col style=\"width:54pt\" width=\"72\" />\n" +
                                    "\t\t<col style=\"mso-width-source:userset;mso-width-alt:1728;width:41pt\" width=\"54\" />\n" +
                                    "\t\t<col style=\"mso-width-source:userset;mso-width-alt:2304;width:54pt\" width=\"72\" />\n" +
                                    "\t\t<col style=\"mso-width-source:userset;mso-width-alt:3840;width:90pt\" width=\"120\" />\n" +
                                    "\t\t<col style=\"mso-width-source:userset;mso-width-alt:14016;width:329pt\" width=\"438\" />\n" +
                                    "\t\t<col style=\"mso-width-source:userset;mso-width-alt:4544;width:107pt\" width=\"142\" />\n" +
                                    "\t\t<col style=\"width:54pt\" width=\"72\" />\n" +
                                    "\t</colgroup>\n" +
                                    "\t<tbody>\n" +
                                    "\t\t<tr height=\"32\" style=\"height:24.0pt\">\n" +
                                    "\t\t\t<td align=\"right\" class=\"xl65\" height=\"32\" style=\"height:24.0pt;\n" +
                                    "  width:54pt\" width=\"72\">1</td>\n" +
                                    "\t\t\t<td class=\"xl69\" rowspan=\"5\" style=\"width:41pt\" width=\"54\">APP责任人端</td>\n" +
                                    "\t\t\t<td class=\"xl69\" rowspan=\"3\" style=\"width:54pt\" width=\"72\">我的问题</td>\n" +
                                    "\t\t\t<td class=\"xl63\" style=\"border-left:none;width:90pt\" width=\"120\">查看问题记录列表</td>\n" +
                                    "\t\t\t<td class=\"xl67\" style=\"border-left:none;width:329pt\" width=\"438\">列表字段添加回复状态：<br />\n" +
                                    "\t\t\t【已回复】【未回复】</td>\n" +
                                    "\t\t\t<td class=\"xl69\" style=\"border-left:none;width:107pt\" width=\"142\">个人中心</td>\n" +
                                    "\t\t\t<td align=\"left\" class=\"xl65\" style=\"border-left:none;width:54pt\" width=\"72\">已完成</td>\n" +
                                    "\t\t</tr>\n" +
                                    "\t\t<tr height=\"32\" style=\"height:24.0pt\">\n" +
                                    "\t\t\t<td align=\"right\" class=\"xl65\" height=\"32\" style=\"height:24.0pt;border-top:none\">2</td>\n" +
                                    "\t\t\t<td class=\"xl63\" style=\"border-top:none;border-left:none;width:90pt\" width=\"120\">查看问题记录列表</td>\n" +
                                    "\t\t\t<td class=\"xl67\" style=\"border-top:none;border-left:none;width:329pt\" width=\"438\">列表字段添加公开状态：<br />\n" +
                                    "\t\t\t【屏蔽】【公开】</td>\n" +
                                    "\t\t\t<td class=\"xl69\" style=\"border-top:none;border-left:none;width:107pt\" width=\"142\">个人中心</td>\n" +
                                    "\t\t\t<td align=\"left\" class=\"xl65\" style=\"border-top:none;border-left:none\">已完成</td>\n" +
                                    "\t\t</tr>\n" +
                                    "\t\t<tr height=\"32\" style=\"height:24.0pt\">\n" +
                                    "\t\t\t<td align=\"right\" class=\"xl65\" height=\"32\" style=\"height:24.0pt;border-top:none\">3</td>\n" +
                                    "\t\t\t<td class=\"xl63\" style=\"border-top:none;border-left:none;width:90pt\" width=\"120\">查看记录详情</td>\n" +
                                    "\t\t\t<td class=\"xl67\" style=\"border-top:none;border-left:none;width:329pt\" width=\"438\">详情添加回复记录查看，可能出现多次回复情况<br />\n" +
                                    "\t\t\t回复内容显示包括：【回复人】【回复时间】【回复内容】</td>\n" +
                                    "\t\t\t<td class=\"xl69\" style=\"border-top:none;border-left:none;width:107pt\" width=\"142\">个人中心</td>\n" +
                                    "\t\t\t<td align=\"left\" class=\"xl65\" style=\"border-top:none;border-left:none\">已完成</td>\n" +
                                    "\t\t</tr>\n" +
                                    "\t\t<tr height=\"32\" style=\"mso-height-source:userset;height:24.0pt\">\n" +
                                    "\t\t\t<td align=\"right\" class=\"xl65\" height=\"32\" style=\"height:24.0pt;border-top:none\">4</td>\n" +
                                    "\t\t\t<td class=\"xl67\" style=\"border-top:none;border-left:none;width:54pt\" width=\"72\">我的日志</td>\n" +
                                    "\t\t\t<td class=\"xl63\" style=\"border-top:none;border-left:none;width:90pt\" width=\"120\">我的日志</td>\n" +
                                    "\t\t\t<td class=\"xl67\" style=\"border-top:none;border-left:none;width:329pt\" width=\"438\">列表字段添加公开状态：<br />\n" +
                                    "\t\t\t【屏蔽】【公开】</td>\n" +
                                    "\t\t\t<td class=\"xl69\" style=\"border-top:none;border-left:none;width:107pt\" width=\"142\">个人中心</td>\n" +
                                    "\t\t\t<td align=\"left\" class=\"xl65\" style=\"border-top:none;border-left:none\">第三周</td>\n" +
                                    "\t\t</tr>\n" +
                                    "\t\t<tr height=\"48\" style=\"mso-height-source:userset;height:36.0pt\">\n" +
                                    "\t\t\t<td align=\"right\" class=\"xl65\" height=\"48\" style=\"height:36.0pt;border-top:none\">5</td>\n" +
                                    "\t\t\t<td class=\"xl67\" style=\"border-top:none;border-left:none;width:54pt\" width=\"72\">设置</td>\n" +
                                    "\t\t\t<td class=\"xl66\" style=\"border-top:none;border-left:none;width:90pt\" width=\"120\">密码修改</td>\n" +
                                    "\t\t\t<td class=\"xl68\" style=\"border-top:none;border-left:none;width:329pt\" width=\"438\">添加个人密码修改功能<br />\n" +
                                    "\t\t\t修改时要求输入原密码以及两次输入新密码</td>\n" +
                                    "\t\t\t<td class=\"xl69\" style=\"border-top:none;border-left:none;width:107pt\" width=\"142\">个人中心</td>\n" +
                                    "\t\t\t<td align=\"left\" class=\"xl65\" style=\"border-top:none;border-left:none\">已完成</td>\n" +
                                    "\t\t</tr>\n" +
                                    "\t</tbody>\n" +
                                    "</table>\n" +
                                    "\n" +
                                    "<p class=\"MsoNormal\" style=\"text-indent:32.0pt;mso-char-indent-count:2.0;\n" +
                                    "line-height:23.0pt;mso-line-height-rule:exactly\"><br />\n" +
                                    "<br />\n" +
                                    "<span style=\"font-size:16.0pt;\n" +
                                    "font-family:宋体;mso-bidi-font-family:宋体;color:black\"><span style=\"background:\n" +
                                    "white\"><span lang=\"EN-US\"><o:p></o:p></span></span></span></p>\n" +
                                    "</body>\n" +
                                    "</html>\n";
                            mEditor.setHtml(html);
                            //tip.setHint("");
                            break;
                        case "-1":
                        case "-2":
                            //tip.setHint("没有查询到相关信息");
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


    private void initWidget() {
        setAppTitle("详情");
    }

    public void setAppTitle(String title) {
        Log.d("reg", "title:" + title);
        apptitle.setText(title);
    }


    public void setPublic() {
        List<String> names = new ArrayList<>();
        names.add("公开");
        names.add("屏蔽");
        showDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long vid) {
                switch (position) {
                    case 0:
                        RetrofitHelper.getCommonServiceAPI()
                                .setupPublics(3, id, "公开")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(bean -> {
                                    String a = bean.string();
                                    ApiMsg apiMsg = JSON.parseObject(a, ApiMsg.class);
                                    String state = apiMsg.getState();
                                    switch (state) {
                                        case "0":
                                            publics.setText("公开");
                                            Drawable drawable = getResources().getDrawable(R.drawable.pub);
                                            publics.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

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
                                .setupPublics(3, id, "屏蔽")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(bean -> {
                                    String a = bean.string();
                                    ApiMsg apiMsg = JSON.parseObject(a, ApiMsg.class);
                                    String state = apiMsg.getState();
                                    switch (state) {
                                        case "0":
                                            publics.setText("屏蔽");
                                            Drawable drawable = getResources().getDrawable(R.drawable.pri);
                                            publics.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
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


    @SuppressLint("CheckResult")
    public void baocun(){
        dialog.show();
        String name=  PreferenceUtil.getStringPRIVATE("name",UserState.NA);
        RetrofitHelper.getAnuncementAPI()
                .push(title.getText().toString(),mEditor.getHtml(),name,"notice",code,level)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a = bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a, ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state) {
                        case "0":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            Intent intent = new Intent();
                            setResult(10086, intent);   //单选不需要裁剪，返回数据
                            finish();
                            //tip.setHint("");
                            break;
                        case "-1":
                        case "-2":
                            //tip.setHint("没有查询到相关信息");
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
    public void xiugai(String id){
        dialog.show();
        String name=  PreferenceUtil.getStringPRIVATE("name",UserState.NA);
        RetrofitHelper.getAnuncementAPI()
                .update(id,title.getText().toString(),mEditor.getHtml(),name,"notice",code)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a = bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a, ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state) {
                        case "0":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            //tip.setHint("");
                            break;
                        case "-1":
                        case "-2":
                            //tip.setHint("没有查询到相关信息");
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
    ArrayList<ImageItem> images = null;
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    Log.d("reg","images:"+images.get(0).path);
                    selImageList.clear();
                    selImageList.addAll(images);

                    for(int i=0;i<selImageList.size();i++){

                        Tiny.getInstance().source(selImageList.get(i).path).asFile().withOptions(options).compress(new FileCallback() {
                            @Override
                            public void callback(boolean isSuccess, String outfile) {
                                File file = new File(outfile);
                                files.add(file);
                                String pic_key="pictures";
                                uploadPic(ApiConstants.Base_URL+"uploadpicture",null,pic_key,files);
                            }
                        });

                    }

                }
            }
        }

        if (requestCode == REQUEST_CODE_EDIT_LINKED&&data!=null) {
            String html = data.getStringExtra("link");
            String txt = data.getStringExtra("txt");
            mEditor.insertLink(html, txt);
//            tvAddLinked.setText(linkContent.getTitle());
//            Log.e(TAG, "startActivityForResult: " + linkContent);
        }
    }

    public void imgpick(){
        List<String> names = new ArrayList<>();
        names.add("拍照");
        names.add("相册");
        showDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // 直接调起相机
                        /**
                         * 0.4.7 目前直接调起相机不支持裁剪，如果开启裁剪后不会返回图片，请注意，后续版本会解决
                         *
                         * 但是当前直接依赖的版本已经解决，考虑到版本改动很少，所以这次没有上传到远程仓库
                         *
                         * 如果实在有所需要，请直接下载源码引用。
                         */
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                        Intent intent = new Intent(PolicyinterpretationDetailActivity.this, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                        startActivityForResult(intent, REQUEST_CODE_SELECT);
                        break;
                    case 1:
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                        Intent intent1 = new Intent(PolicyinterpretationDetailActivity.this, ImageGridActivity.class);
                        /* 如果需要进入选择的时候显示已经选中的图片，
                         * 详情请查看ImagePickerActivity
                         * */
//                                intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
                        startActivityForResult(intent1, REQUEST_CODE_SELECT);
                        break;
                    default:
                        break;
                }

            }
        }, names);
    }
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    public void uploadPic(String reqUrl, Map<String, String> params, String pic_key, List<File> files){
        OkHttpClient okHttpClient = new OkHttpClient();
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);

        //遍历map中所有参数到builder
        if (params != null){
            for (String key : params.keySet()) {
                if(null!=params.get(key)){
                    multipartBodyBuilder.addFormDataPart(key, params.get(key));
                }else{
                    multipartBodyBuilder.addFormDataPart(key, "");
                }

            }
        }

        //遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
        if (files != null){
            for (File file : files) {
                multipartBodyBuilder.addFormDataPart(pic_key, file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
            }
        }
        dialog.show();
        //构建请求体
        RequestBody requestBody = multipartBodyBuilder.build();
        Request.Builder RequestBuilder = new Request.Builder();
        RequestBuilder.url(reqUrl);// 添加URL地址
        RequestBuilder.post(requestBody);
        Request request = RequestBuilder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("err", "result:" + e);
                call.cancel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.d("reg", "result:" + str);
                ApiMsg apiMsg = JSON.parseObject(str,ApiMsg.class);
                String state = apiMsg.getState();
                switch (state){
                    case "0":
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.ShortToast(apiMsg.getMessage());
                                mEditor.insertImage(apiMsg.getResult(), "photoUrl");
                                dialog.dismiss();
                            }
                        });
                        break;
                    case "-1":
                    case "-2":
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.ShortToast(apiMsg.getMessage());
                            }
                        });
                        break;


                }
                call.cancel();
            }
        });
    }
}
