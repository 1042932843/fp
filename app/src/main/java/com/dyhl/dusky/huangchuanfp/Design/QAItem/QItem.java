package com.dyhl.dusky.huangchuanfp.Design.QAItem;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dyhl.dusky.huangchuanfp.Module.entity.Answer;
import com.dyhl.dusky.huangchuanfp.Module.entity.Question;
import com.dyhl.dusky.huangchuanfp.R;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/6/29
 * @DESCRIPTION:
 */
public class QItem extends LinearLayout {
    TextView content;
    TextView time;

    public QItem(Context context) {
        super(context);
        initView();
    }

    public QItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public QItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView() {
        View view = View.inflate(getContext(), R.layout.r1_question_item, this);
        content = (TextView) view.findViewById(R.id.content);
        time = (TextView) view.findViewById(R.id.time);

    }

    public void setData(Question question) {
        content.setText(question.getValue());
        time.setText(question.getTime());
    }

}

