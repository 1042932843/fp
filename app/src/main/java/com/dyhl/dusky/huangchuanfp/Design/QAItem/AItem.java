package com.dyhl.dusky.huangchuanfp.Design.QAItem;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dyhl.dusky.huangchuanfp.Module.entity.Answer;
import com.dyhl.dusky.huangchuanfp.R;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/6/29
 * @DESCRIPTION:
 */
public class AItem extends LinearLayout{
    TextView name;
    TextView content;
    TextView time;
        public AItem(Context context) {
            super(context);
            initView();
        }

        public AItem(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public AItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public void initView() {
            View view = View.inflate(getContext(), R.layout.r2_answer_item,this);
            name=(TextView)view.findViewById(R.id.name);
            content=(TextView)view.findViewById(R.id.content);
            time=(TextView)view.findViewById(R.id.time);

        }

        public void setData(Answer answer){
            name.setText(answer.getName());
            content.setText(answer.getValue());
            time.setText(answer.getTime());
        }


}
