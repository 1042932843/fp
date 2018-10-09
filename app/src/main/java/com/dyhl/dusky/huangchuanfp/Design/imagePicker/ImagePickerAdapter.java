package com.dyhl.dusky.huangchuanfp.Design.imagePicker;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dyhl.dusky.huangchuanfp.Module.SignActivity;
import com.dyhl.dusky.huangchuanfp.R;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;

import java.util.ArrayList;
import java.util.List;


public class ImagePickerAdapter extends RecyclerView.Adapter<ImagePickerAdapter.SelectedPicViewHolder> {
    public void setMaxImgCount(int maxImgCount) {
        this.maxImgCount = maxImgCount;
    }

    private int maxImgCount;
    private Context mContext;
    private List<ImageItem> mData;
    private LayoutInflater mInflater;
    private OnRecyclerViewItemClickListener listener;


    private boolean isAdded;   //是否额外添加了最后一个图片
  private String path;
    private int status;
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public void setImages(List<ImageItem> data) {
        mData = new ArrayList<>(data);
        if (getItemCount() < maxImgCount) {
            mData.add(new ImageItem());
            isAdded = true;
        } else {
            isAdded = false;
        }
        notifyDataSetChanged();
    }

    public void setStatus(int  status) {
        Log.d("reg","status1:"+status);
        this.status =status;
    }


    public List<ImageItem> getImages() {
        //由于图片未选满时，最后一张显示添加图片，因此这个方法返回真正的已选图片
        if (isAdded) return new ArrayList<>(mData.subList(0, mData.size() - 1));
        else return mData;
    }

    public ImagePickerAdapter(Context mContext, List<ImageItem> data, int maxImgCount) {
        this.mContext = mContext;
        this.maxImgCount = maxImgCount;
        this.mInflater = LayoutInflater.from(mContext);
        setImages(data);
    }

    @Override
    public SelectedPicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedPicViewHolder(mInflater.inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(SelectedPicViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class SelectedPicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView iv_img;
        private int clickPosition;

        public SelectedPicViewHolder(View itemView) {
            super(itemView);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
        }

        public void bind(int position) {
            //设置条目的点击事件
            itemView.setOnClickListener(this);
            //根据条目位置设置图片
            ImageItem item = mData.get(position);
            if (isAdded && position == getItemCount() - 1) {
                iv_img.setImageResource(R.drawable.selector_image_add);
                clickPosition = SignActivity.IMAGE_ITEM_ADD;
            } else {
//                    ImagePicker.getInstance().getImageLoader().displayImage((Activity) mContext, "/storage/emulated/0/Pictures/Screenshots/crop_crop_Screenshot_2018-01-29-14-16-04.png", iv_img, 0, 0);
                if (item.path.indexOf("http") != -1) {
                    System.out.println("包含该字符串");
                    Glide.with(mContext).load(item.path).into(iv_img);
                } else {
                    ImagePicker.getInstance().getImageLoader().displayImage((Activity) mContext, item.path, iv_img, 0, 0);
                }
                clickPosition = position;
            }
        }

        @Override
        public void onClick(View v) {
            Log.d("reg","status1:"+status);
            Log.d("reg","clickPosition:"+clickPosition);
            if (clickPosition==0){
                if (listener != null) listener.onItemClick(v, clickPosition);
            }else  if (status<=2){
                if (listener != null) listener.onItemClick(v, clickPosition);
            }
        }
    }
}