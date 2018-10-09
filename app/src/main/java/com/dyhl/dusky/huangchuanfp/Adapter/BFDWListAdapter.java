package com.dyhl.dusky.huangchuanfp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dyhl.dusky.huangchuanfp.Module.entity.BFDWInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.PkcInfo;
import com.dyhl.dusky.huangchuanfp.R;

import java.util.List;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/4/17
 * @DESCRIPTION:
 */
public class BFDWListAdapter extends RecyclerView.Adapter<BFDWListAdapter.ViewHolder>{

    private List<BFDWInfo> home;
    private Context context;

    public BFDWListAdapter(List<BFDWInfo> home, Context context) {
        this.home = home;
        this.context=context;
    }

    public void updateData(List<BFDWInfo> home) {
        this.home.addAll(home);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bfdwlist_item, parent, false);
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
        holder.name.setText(home.get(position).getDepartment());
        holder.fzr.setText(home.get(position).getIncharge());
        holder.lxr.setText(home.get(position).getContact());
        holder.phone.setText(home.get(position).getPhone());

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
        TextView lxr;
        TextView fzr;
        TextView phone;

        public ViewHolder(View itemView) {
            super(itemView);
            name=(TextView) itemView.findViewById(R.id.name);
            lxr = (TextView) itemView.findViewById(R.id.department);
            fzr = (TextView) itemView.findViewById(R.id.bfhs);
            phone= (TextView)itemView.findViewById(R.id.position);
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

