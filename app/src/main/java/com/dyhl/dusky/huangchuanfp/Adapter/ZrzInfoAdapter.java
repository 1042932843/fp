package com.dyhl.dusky.huangchuanfp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dyhl.dusky.huangchuanfp.Module.entity.GzdInfo;
import com.dyhl.dusky.huangchuanfp.R;

import java.util.List;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/4/17
 * @DESCRIPTION:
 */
public class ZrzInfoAdapter extends RecyclerView.Adapter<ZrzInfoAdapter.ViewHolder>{

    private List<GzdInfo> home;
    private Context context;

    public ZrzInfoAdapter(List<GzdInfo> home, Context context) {
        this.home = home;
        this.context=context;
    }

    public void updateData(List<GzdInfo> home) {
        this.home.addAll(home);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.zrz_item, parent, false);
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
        if(!TextUtils.isEmpty(home.get(position).getName())) {
            holder.xingming_v.setText(home.get(position).getName());
        }else{

        }
        if(!TextUtils.isEmpty(home.get(position).getSex())){
            holder.xinbie_v.setText(home.get(position).getSex());
        }
        if(!TextUtils.isEmpty(home.get(position).getRole())) {
            holder.fengong_v.setText(home.get(position).getRole());
        }
        if(!TextUtils.isEmpty(home.get(position).getPhone())) {
            holder.dianhua_v.setText(home.get(position).getPhone());
        }
        if(!TextUtils.isEmpty(home.get(position).getPosition())) {
            holder.zhiwu_v.setText(home.get(position).getPosition());
        }
        if(!TextUtils.isEmpty(home.get(position).getComment())) {
            holder.shuji_v.setText(home.get(position).getComment());
        }

        //holder.value.setText(home.get(position).getValue());

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

        TextView xingming_v,xinbie_v,fengong_v,danwei_v,zhiwu_v,dianhua_v,shuji_v;


        public ViewHolder(View itemView) {
            super(itemView);
            xingming_v = (TextView) itemView.findViewById(R.id.xingming_v);
            xinbie_v = (TextView) itemView.findViewById(R.id.xinbie_v);
            fengong_v = (TextView) itemView.findViewById(R.id.fengong_v);
            danwei_v = (TextView) itemView.findViewById(R.id.danwei_v);

            zhiwu_v = (TextView) itemView.findViewById(R.id.zhiwu_v);
            dianhua_v = (TextView) itemView.findViewById(R.id.dianhua_v);
            shuji_v = (TextView) itemView.findViewById(R.id.shuji_v);

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

