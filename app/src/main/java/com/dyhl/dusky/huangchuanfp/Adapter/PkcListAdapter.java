package com.dyhl.dusky.huangchuanfp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dyhl.dusky.huangchuanfp.Module.entity.PkcInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyInformation;
import com.dyhl.dusky.huangchuanfp.R;

import java.util.List;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/4/17
 * @DESCRIPTION:
 */
public class PkcListAdapter extends RecyclerView.Adapter<PkcListAdapter.ViewHolder>{

    private List<PkcInfo> home;
    private Context context;

    public PkcListAdapter(List<PkcInfo> home, Context context) {
        this.home = home;
        this.context=context;
    }

    public void updateData(List<PkcInfo> home) {
        this.home.addAll(home);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pkclist_item, parent, false);
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
        holder.name.setText(home.get(position).getVillage());
        String stu=home.get(position).getOutpoverty();

        if(stu.contains("是")){
            holder.st.setText("已脱贫");
            holder.st.setBackground(context.getResources().getDrawable(R.drawable.btn_tip_green_solid_bg));
        }else{
            holder.st.setText("未脱贫");
            holder.st.setBackground(context.getResources().getDrawable(R.drawable.btn_tip_red_solid_bg));
        }
        holder.pkcfzr.setText(home.get(position).getIncharge());
        holder.pkcphone.setText(home.get(position).getPhone());

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
        TextView pkcfzr;
        TextView pkcphone;

        public ViewHolder(View itemView) {
            super(itemView);
            name=(TextView) itemView.findViewById(R.id.name);
            st = (TextView) itemView.findViewById(R.id.pkst);
            pkcfzr = (TextView) itemView.findViewById(R.id.pkcfzr);
            pkcphone= (TextView)itemView.findViewById(R.id.pkcphone);
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

