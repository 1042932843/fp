package com.dyhl.dusky.huangchuanfp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dyhl.dusky.huangchuanfp.Module.entity.GzdInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.MajorSecretary;
import com.dyhl.dusky.huangchuanfp.R;

import java.util.List;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/4/17
 * @DESCRIPTION:
 */
public class MajorSecretaryAdapter extends RecyclerView.Adapter<MajorSecretaryAdapter.ViewHolder>{

    private List<MajorSecretary> home;
    private Context context;

    public MajorSecretaryAdapter(List<MajorSecretary> home, Context context) {
        this.home = home;
        this.context=context;
    }

    public void updateData(List<MajorSecretary> home) {
        this.home.addAll(home);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.majorsecretary_item, parent, false);
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
            holder.name.setText(home.get(position).getName());
        }
        if(!TextUtils.isEmpty(home.get(position).getPhone())){
            holder.phone.setText(home.get(position).getPhone());
        }
        if(!TextUtils.isEmpty(home.get(position).getDepartment())) {
            holder.from.setText(home.get(position).getDepartment());
        }
        if(!TextUtils.isEmpty(home.get(position).getTown()+home.get(position).getVillage())) {
            holder.to.setText(home.get(position).getTown()+home.get(position).getVillage());
        }
        if(!TextUtils.isEmpty(home.get(position).getPosition())) {
            holder.zhiwu.setText(home.get(position).getPosition());
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

        TextView name,phone,from,zhiwu,to;
        RelativeLayout sexlayout;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            phone = (TextView) itemView.findViewById(R.id.phone);
            from = (TextView) itemView.findViewById(R.id.from);
            zhiwu = (TextView) itemView.findViewById(R.id.zhiwu);
            to=(TextView) itemView.findViewById(R.id.to);

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

