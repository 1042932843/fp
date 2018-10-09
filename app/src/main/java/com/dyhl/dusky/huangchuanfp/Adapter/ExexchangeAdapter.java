package com.dyhl.dusky.huangchuanfp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Design.imagePicker.ImagePickerAdapter;
import com.dyhl.dusky.huangchuanfp.Design.imagePicker.SelectDialog;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.Exp;
import com.dyhl.dusky.huangchuanfp.Module.entity.Liable;
import com.dyhl.dusky.huangchuanfp.Net.ApiConstants;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/4/17
 * @DESCRIPTION:
 */
public class ExexchangeAdapter extends RecyclerView.Adapter<ExexchangeAdapter.ViewHolder>{

    private List<Exp> home;
    private Context context;
    String code;

    public ExexchangeAdapter(List<Exp> home, Context context) {
        this.home = home;
        this.context=context;
         code= PreferenceUtil.getStringPRIVATE("permissions", UserState.NA);
    }

    public void updateData(List<Exp> home) {
        this.home.addAll(home);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exp, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(home.get(position)==null){
            return;
        }
        // 绑定数据
        holder.name.setText(home.get(position).getName());
        holder.time.setText(home.get(position).getInputtime());
        holder.title.setText(home.get(position).getTitle());

        holder.title.getPaint().setFakeBoldText(true);
        limitTextViewString(home.get(position).getValue(),76,holder.context);
        holder.publics.setText(home.get(position).getPublics());

        /*if(code.length()<=6){
            Drawable drawable = context.getResources().getDrawable(R.drawable.ic_action_down);
            holder.publics.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
            holder.publics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setPublic(home.get(position),position);
                }
            });
        }else{*/
            if("公开".equals(home.get(position).getPublics())){
                Drawable drawable2 = context.getResources().getDrawable(R.drawable.pub);
                holder.publics.setCompoundDrawablesWithIntrinsicBounds(drawable2,null,null,null);
            }else{
                Drawable drawable3 = context.getResources().getDrawable(R.drawable.pri);
                holder.publics.setCompoundDrawablesWithIntrinsicBounds(drawable3,null,null,null);
            }
       // }

        ImagePickerAdapter adapter;
        ArrayList<ImageItem> selImageList;
        selImageList=new ArrayList<>();
        adapter = new ImagePickerAdapter(context, selImageList, 0);
        holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setAdapter(adapter);

        String pics=home.get(position).getPicture();
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
            adapter.setOnItemClickListener(new ImagePickerAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //打开预览
                    if(context!=null){
                        Intent intentPreview = new Intent(context, ImagePreviewDelActivity.class);
                        intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                        intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                        intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                        intentPreview.putExtra(ImagePicker.EXTRA_CAN_DELETE, false);//设置不能点击删除，因为是作展示用的
                        context.startActivity(intentPreview);
                    }

                }
            });
        }

        //单独对应类型的设置事件
        if( onItemClickListener!= null){
            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(position);
                }
            });
            holder. itemView.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return home == null ? 0 : home.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView time;
        TextView title;
        TextView context,publics;
        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.publisher);
            time = (TextView) itemView.findViewById(R.id.time);
            title = (TextView) itemView.findViewById(R.id.title);
            context = (TextView) itemView.findViewById(R.id.context);
            publics=(TextView) itemView.findViewById(R.id.publics);
            recyclerView=(RecyclerView) itemView.findViewById(R.id.recyclerView);
        }
    }

    OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.onItemClickListener=onItemClickListener;
    }


    public void setPublic(Exp exp,int p){
        List<String> names = new ArrayList<>();
        names.add("公开");
        names.add("屏蔽");
        showDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        RetrofitHelper.getExperienceServiceAPI().setupExperience(exp.getExpid(),"公开").subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()).subscribe(bean -> {
                                    String a=bean.string();
                                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                                    String state = apiMsg.getState();
                                    switch (state){
                                        case "0":
                                            exp.setPublics("公开");
                                            notifyItemChanged(p);
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
                        RetrofitHelper.getExperienceServiceAPI()
                                .setupExperience(exp.getExpid(),"屏蔽")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(bean -> {
                            String a=bean.string();
                            ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                            String state = apiMsg.getState();
                            switch (state){
                                case "0":
                                    exp.setPublics("屏蔽");
                                    notifyItemChanged(p);
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
    Activity activity;
    SelectDialog dialog;
    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        try{
            activity=(Activity) context;
        }catch (Exception e){

        }
        if(activity!=null){
             dialog = new SelectDialog((Activity) context, R.style
                    .transparentFrameWindowStyle,
                    listener, names);

            if (!activity.isFinishing()) {
                dialog.show();
            }
        }

        return dialog;
    }

    /**
     * get the last char index for max limit row,if not exceed the limit,return -1
     * @param textView
     * @param content
     * @param width
     * @param maxLine
     * @return
     */
    private int getLastCharIndexForLimitTextView(TextView textView, String content, int width, int maxLine){
        TextPaint textPaint  = textView.getPaint();
        StaticLayout staticLayout = new StaticLayout(content, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        if(staticLayout.getLineCount()>maxLine) return staticLayout.getLineStart(maxLine) - 1;//exceed
        else return -1;//not exceed the max line
    }

    /**
     * 限制TextView显示字符字符，并且添加showMore和show more的点击事件
     * @param textString
     * @param textView
     */
    private void limitTextViewString(String textString, int maxFirstShowCharCount,final TextView textView) {
        //计算处理花费时间
        final long startTime = System.currentTimeMillis();
        if(textView==null)return;
        int width = textView.getWidth();//在recyclerView和ListView中，由于复用的原因，这个TextView可能以前就画好了，能获得宽度
        if(width==0) width = 1000;//获取textView的实际宽度，这里可以用各种方式（一般是dp转px写死）填入TextView的宽度
        int lastCharIndex = getLastCharIndexForLimitTextView(textView,textString,width,10);
//返回-1表示没有达到行数限制
        if(lastCharIndex<0 && textString.length() <= maxFirstShowCharCount) {
            //如果行数没超过限制
            textView.setText(textString);
            return;
        }
        //如果超出了行数限制
        //textView.setMovementMethod(LinkMovementMethod.getInstance());//this will deprive the recyclerView's focus
        if(lastCharIndex>maxFirstShowCharCount || lastCharIndex<0) {
            lastCharIndex=maxFirstShowCharCount;
        }
        //构造spannableString
        String explicitText = null;
        if(textString.charAt(lastCharIndex)=='\n'){//manual enter
            explicitText = textString.substring(0,lastCharIndex);
        }else if(lastCharIndex > 12){
            //如果最大行数限制的那一行到达12以后则直接显示 展开
            explicitText = textString.substring(0,lastCharIndex-12)+"...";
        }
        int sourceLength = explicitText.length();
        String showMore = "展开";
        explicitText = explicitText  + showMore;
        final SpannableString mSpan = new SpannableString(explicitText);


        mSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPrimary)), sourceLength, explicitText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置为“显示更多”状态下的TextVie
        textView.setText(mSpan);
        Log.i("info", "字符串处理耗时" + (System.currentTimeMillis() - startTime) + " ms");
    }
}

