package com.dyhl.dusky.huangchuanfp.Module;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dyhl.dusky.huangchuanfp.Adapter.SignListAdapter;
import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;
import com.dyhl.dusky.huangchuanfp.Design.PkcItem.PkcItem;
import com.dyhl.dusky.huangchuanfp.Module.entity.PkcLocation;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import jxl.Sheet;
import jxl.Workbook;


public class PkcActivity extends BaseActivity {

    ArrayList<PkcLocation> locations;

    @OnClick(R.id.img_back)
    public void back(){
        finish();
    }

    @BindView(R.id.txt_title)
    TextView apptitle;

    @BindView(R.id.container)
    FrameLayout container;


    @Override
    public int getLayoutId() {
        return R.layout.activity_pkc;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initViews(Bundle savedInstanceState) {
        initWidget();

    }
    int height;
    int width;
    boolean isFirst=true;
    @SuppressLint("CheckResult")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        height= container.getHeight();
        width= container.getWidth();

        if(height!=0&&width!=0&&isFirst){
            isFirst=false;
            locations=getXlsData("location.xls",0);
            int size=locations.size();
            for(int i=0;i<size;i++){
                initItem(locations.get(i),container);
            }

        }

    }

    public void initItem(PkcLocation pkcLocation,ViewGroup container){
        if(pkcLocation!=null&&container!=null){
            PkcItem pkcItem=new PkcItem(this);
            pkcItem.setName(pkcLocation.getName());
            pkcItem.setCode(pkcLocation.getCode());
            pkcItem.setOnItemClickListener(new PkcItem.OnItemClickListener() {
                @Override
                public void onClick(String name,String code) {
                   // ToastUtil.showShort(PkcActivity.this,"详情功能开发中，请留意后续版本更新");
                    Intent it=new Intent(PkcActivity.this, CommonListActivity.class);
                    it.putExtra("TownCode",pkcLocation.getCode());
                    it.putExtra("TownName",pkcLocation.getName());
                    it.putExtra("position",R.id.main2_pkc);
                    startActivity(it);
                }

            });
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            pkcItem.measure(w, h);
            int pkcItemheight = pkcItem.getMeasuredHeight();
            int pkcItemwidth = pkcItem.getMeasuredWidth();
            lp.topMargin=(int)(height*pkcLocation.getY()-pkcItemheight/2);
            lp.leftMargin=(int)(width*pkcLocation.getX()-pkcItemwidth/2);

            container.addView(pkcItem,lp);
        }

    }





    @Override
    public void initToolBar() {

    }
    @Override
    public void loadData() {

    }


    private void initWidget() {
        setAppTitle("贫困村");
    }

    public void setAppTitle(String title) {
        Log.d("reg", "title:" + title);
        apptitle.setText(title);
    }


    /**
     * 获取 excel 表格中的数据,不能在主线程中调用
     *
     * @param xlsName excel 表格的名称
     * @param index   第几张表格中的数据
     */
    private ArrayList<PkcLocation> getXlsData(String xlsName, int index) {
        ArrayList<PkcLocation> locations = new ArrayList<PkcLocation>();
        AssetManager assetManager = getAssets();

        try {
            Workbook workbook = Workbook.getWorkbook(assetManager.open(xlsName));
            Sheet sheet = workbook.getSheet(index);

            int sheetNum = workbook.getNumberOfSheets();
            int sheetRows = sheet.getRows();
            int sheetColumns = sheet.getColumns();

            Log.d(TAG, "the num of sheets is " + sheetNum);
            Log.d(TAG, "the name of sheet is  " + sheet.getName());
            Log.d(TAG, "total rows is 行=" + sheetRows);
            Log.d(TAG, "total cols is 列=" + sheetColumns);

            for (int i = 2; i < sheetRows; i++) {
                PkcLocation pkcLocation = new PkcLocation();
                pkcLocation.setName(sheet.getCell(0, i).getContents());
                pkcLocation.setX(Double.parseDouble(sheet.getCell(4, i).getContents()));
                pkcLocation.setY(Double.parseDouble(sheet.getCell(5, i).getContents()));
                pkcLocation.setCode(sheet.getCell(6, i).getContents());
                locations.add(pkcLocation);
            }

            workbook.close();

        } catch (Exception e) {
            Log.e(TAG, "read error=" + e, e);
        }

        return locations;
    }


}
