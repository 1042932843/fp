package com.dyhl.dusky.huangchuanfp.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dyhl.dusky.huangchuanfp.Module.entity.SignInList;
import com.dyhl.dusky.huangchuanfp.R;

import java.util.List;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/4/17
 * @DESCRIPTION:
 */
public class SignListInMain2Adapter extends RecyclerView.Adapter<SignListInMain2Adapter.ViewHolder>{

    private List<SignInList> home;
    private Context context;

    public SignListInMain2Adapter(List<SignInList> home, Context context) {
        this.home = home;
        this.context=context;
    }

    public void updateData(List<SignInList> home) {
        this.home.addAll(home);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_journal, parent, false);
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
        holder.mTv.setText(home.get(position).getAdress());
        holder.mTvtime.setText(home.get(position).getTime());
        holder.name.setText(home.get(position).getResponsible());
        holder.pkh_name.setText("贫困户 "+home.get(position).getName());
        if(!TextUtils.isEmpty(home.get(position).getPublics())){
            holder.publics.setText(" "+home.get(position).getPublics());
            /*if(code.length()<=6){
                Drawable drawable = context.getResources().getDrawable(R.drawable.ic_action_down);
                holder.publics.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
                holder.publics.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setPublic(home.get(position),position);
                    }
                });
            }else{*/
            if("公开".equals(home.get(position).getPublics())){
                Drawable drawable = context.getResources().getDrawable(R.drawable.pub);
                holder.publics.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
            }else{
                Drawable drawable = context.getResources().getDrawable(R.drawable.pri);
                holder.publics.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
            }
            // }
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

        TextView mTv,name,pkh_name;
        TextView mTvtime,publics;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvtime = (TextView) itemView.findViewById(R.id.time);
            mTv = (TextView) itemView.findViewById(R.id.adress);
            name = (TextView) itemView.findViewById(R.id.name);
            pkh_name= (TextView) itemView.findViewById(R.id.pkh_name);
            publics = (TextView) itemView.findViewById(R.id.publics);
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

