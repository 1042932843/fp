package com.dyhl.dusky.huangchuanfp.Module.Fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.SystemBarHelper;

import butterknife.OnTouch;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/5/5
 * @DESCRIPTION:
 */
public class XBottomSheetDialogFragment extends BottomSheetDialogFragment implements View.OnTouchListener{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.layout_bottom_sheet, container);
        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // 对话框背景色 原有边框会自动消失
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setDimAmount(0.0f);//背景黑暗度
        return inflate;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1 通过样式定义
        //setStyle(DialogFragment.STYLE_NORMAL,R.style.Mdialog);

    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }
}
