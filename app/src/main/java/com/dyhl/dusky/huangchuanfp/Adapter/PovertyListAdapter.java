package com.dyhl.dusky.huangchuanfp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dyhl.dusky.huangchuanfp.Module.entity.Annuncement;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyInformation;
import com.dyhl.dusky.huangchuanfp.R;

import java.util.List;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/4/17
 * @DESCRIPTION:
 */
public class PovertyListAdapter extends RecyclerView.Adapter<PovertyListAdapter.ViewHolder>{

    private List<PovertyInformation> home;
    private Context context;

    public PovertyListAdapter(List<PovertyInformation> home, Context context) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.povertylist_item, parent, false);
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
        holder.name.setText(home.get(position).getName());
        String stu=home.get(position).getOvercomeattribute();
        holder.st.setText(stu);
        if(stu.contains("未脱")){
            holder.st.setBackground(context.getResources().getDrawable(R.drawable.btn_tip_red_solid_bg));
        }else{
            holder.st.setBackground(context.getResources().getDrawable(R.drawable.btn_tip_green_solid_bg));
        }
        holder.type.setText(home.get(position).getPovertyattribute());
        holder.res.setText(home.get(position).getReson());
        String add=home.get(position).getCounty()+home.get(position).getTown()+home.get(position).getVillage();
        holder.address.setText(add);

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
        TextView st;
        TextView type;
        TextView res;
        TextView address;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            st = (TextView) itemView.findViewById(R.id.pkst);
            type = (TextView) itemView.findViewById(R.id.pktype);
            res= (TextView)itemView.findViewById(R.id.pkres);
            address= (TextView)itemView.findViewById(R.id.address);
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

