package com.dyhl.dusky.huangchuanfp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dyhl.dusky.huangchuanfp.Module.entity.BaseInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyInformation;
import com.dyhl.dusky.huangchuanfp.R;

import java.util.List;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/4/17
 * @DESCRIPTION:
 */
public class PovertyFamilyInfoAdapter extends RecyclerView.Adapter<PovertyFamilyInfoAdapter.ViewHolder>{

    private List<PovertyInformation> home;
    private Context context;

    public PovertyFamilyInfoAdapter(List<PovertyInformation> home, Context context) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.povertyfamily_item, parent, false);
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
        holder.xingming_value.setText(home.get(position).getName());
        holder.guanxi_value.setText(home.get(position).getRelationship());
        holder.wenhuachengdu_value.setText(home.get(position).getEducation());
        holder.jiankang_value.setText(home.get(position).getHealth());
        holder.skill.setText(home.get(position).getSkill());
        if(!TextUtils.isEmpty(home.get(position).getWork())){
            holder.work.setText(home.get(position).getWork());
        }
        if(!TextUtils.isEmpty(home.get(position).getWorktime())){
            holder.worktime.setText(home.get(position).getWorktime());
        }
        holder.student.setText(home.get(position).getStudent());
        //holder.value.setText(home.get(position).getValue());
        holder.age_value.setText(home.get(position).getAge());
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

        TextView xingming_value,guanxi_value,wenhuachengdu_value,jiankang_value,skill,work,worktime,student,age_value;


        public ViewHolder(View itemView) {
            super(itemView);
            xingming_value = (TextView) itemView.findViewById(R.id.xingming_value);
            guanxi_value = (TextView) itemView.findViewById(R.id.guanxi_value);
            wenhuachengdu_value = (TextView) itemView.findViewById(R.id.wenhuachengdu_value);
            jiankang_value = (TextView) itemView.findViewById(R.id.jiankang_value);
            skill=(TextView) itemView.findViewById(R.id.skill_value);
            work=(TextView) itemView.findViewById(R.id.work_value);
            worktime=(TextView) itemView.findViewById(R.id.worktime_value);
            student=(TextView) itemView.findViewById(R.id.student_value);
            age_value=(TextView) itemView.findViewById(R.id.age_value);
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

