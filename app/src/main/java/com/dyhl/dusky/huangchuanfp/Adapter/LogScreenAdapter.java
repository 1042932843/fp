package com.dyhl.dusky.huangchuanfp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dyhl.dusky.huangchuanfp.Module.entity.BaseInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.logScreenData;
import com.dyhl.dusky.huangchuanfp.R;

import java.util.List;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/4/17
 * @DESCRIPTION:
 */
public class LogScreenAdapter extends RecyclerView.Adapter<LogScreenAdapter.ViewHolder>{

    private List<logScreenData> home;
    private Context context;
    private String type;

    public LogScreenAdapter(List<logScreenData> home,String type, Context context) {
        this.home = home;
        this.context=context;
        this.type=type;
    }

    public void updateData(List<logScreenData> home) {
        this.home.addAll(home);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.logscreen_item, parent, false);
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
        switch (type){
            case "1":
                holder.name.setText(home.get(position).getTimes());
                holder.num.setText(home.get(position).getNumber());
                break;
            case "2":
                holder.name.setText(home.get(position).getTimes());
                holder.num.setText(home.get(position).getNumber());
                break;
            case "3":
                holder.name.setText(home.get(position).getResponsiblename());
                holder.num.setText(home.get(position).getNumber());
                break;
            case "4":
                holder.name.setText(home.get(position).getPosition());
                holder.num.setText(home.get(position).getNumber());
                break;
            case "5":
                break;

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
        TextView num;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            num = (TextView) itemView.findViewById(R.id.num);
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

