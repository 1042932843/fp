package com.dyhl.dusky.huangchuanfp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dyhl.dusky.huangchuanfp.Base.DuskyApp;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyInformation;
import com.dyhl.dusky.huangchuanfp.R;

import java.util.List;


/**
 * @AUTHOR: dsy
 * @TIME: 2018/4/17
 * @DESCRIPTION:
 */
public class ChoiceAdapter extends RecyclerView.Adapter<ChoiceAdapter.ViewHolder>{

    private List<PovertyInformation> home;
    private Context context;

    public ChoiceAdapter(List<PovertyInformation> home, Context context) {
        this.home = home;
        this.context=context;
    }

    public void updateData(List<PovertyInformation> home) {
        this.home.addAll(home);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_count_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // 绑定数据
        if(home!=null){
            holder.mTv.setText(home.get(position).getName());
        }

       /* if("".equals(home.get(position).getPortrait())||home.get(position).getPortrait()==null){
            if("男".equals(home.get(position).getSex())){
                Glide.with(context).load(R.drawable.def_women).apply(DuskyApp.optionsRoundedCircle).into(holder.img);
            }else{
                Glide.with(context).load(R.drawable.def_women).apply(DuskyApp.optionsRoundedCircle).into(holder.img);
            }

        }else{
            Glide.with(context).load(home.get(position).getPortrait()).apply(DuskyApp.optionsRoundedCircle).into(holder.img);
        }*/
        //单独对应类型的设置事件
        if( onItemClickListener!= null){
            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(position);
                }
            });
            holder.itemView.setOnLongClickListener( new View.OnLongClickListener() {
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

        //ImageView img;
        TextView mTv;

        public ViewHolder(View itemView) {
            super(itemView);
            //img=(ImageView)itemView.findViewById(R.id.head) ;
            mTv = (TextView) itemView.findViewById(R.id.text);
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
}

