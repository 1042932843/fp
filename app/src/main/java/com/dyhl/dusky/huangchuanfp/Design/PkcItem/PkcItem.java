package com.dyhl.dusky.huangchuanfp.Design.PkcItem;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dyhl.dusky.huangchuanfp.Adapter.SignListAdapter;
import com.dyhl.dusky.huangchuanfp.R;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/5/24
 * @DESCRIPTION:
 */
public class PkcItem extends LinearLayout {
    TextView nameTv;
    String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    String code;

    public int getMytag() {
        return mytag;
    }

    public void setMytag(int mytag) {
        this.mytag = mytag;
    }

    int mytag;

    public PkcItem(Context context) {
        super(context);
        initView();
    }

    public PkcItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PkcItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView() {
        View view = View.inflate(getContext(), R.layout.pkc_item,this);
        nameTv = (TextView) view.findViewById(R.id.name);


    }


    public void setName(String name){
        this.name=name;
        nameTv.setText(name);
    }


    PkcItem.OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onClick(String name,String code);
    }
    public void setOnItemClickListener(PkcItem.OnItemClickListener onItemClickListener ){
        this.onItemClickListener=onItemClickListener;
        //单独对应类型的设置事件
        if( onItemClickListener!= null){
            PkcItem.this.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(name,code);
                }
            });
        }
    }
}
