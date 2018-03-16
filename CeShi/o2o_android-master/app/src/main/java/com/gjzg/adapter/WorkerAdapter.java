package com.gjzg.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gjzg.R;
import com.gjzg.bean.Worker;
import com.gjzg.config.GlideCircleTransform;

import java.util.List;

import com.gjzg.bean.LonLatBean;

import com.gjzg.listener.IdPosClickHelp;
import com.gjzg.utils.DataUtils;
import com.gjzg.utils.UserUtils;
import com.gjzg.custom.CImageView;

public class WorkerAdapter extends BaseAdapter {

    private Context mContext;
    private List<Worker.DataBeanX.DataBean> mList;
    private IdPosClickHelp idPosClickHelp;

    public WorkerAdapter(Context mContext, List<Worker.DataBeanX.DataBean> mList, IdPosClickHelp idPosClickHelp) {
        this.mContext = mContext;
        this.mList = mList;
        this.idPosClickHelp = idPosClickHelp;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_worker, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Worker.DataBeanX.DataBean dataBean = mList.get(position);
        if (dataBean != null) {
            LonLatBean lonLatBean1 = UserUtils.getLonLat(mContext);
            LonLatBean lonLatBean2 = new LonLatBean();
            lonLatBean2.setLon(dataBean.getUcp_posit_x());
            lonLatBean2.setLat(dataBean.getUcp_posit_y());
            if (!TextUtils.isEmpty(dataBean.getU_img()))
                Glide.with(mContext).load(dataBean.getU_img()).placeholder(R.mipmap.person_face_default).transform(new GlideCircleTransform(mContext)).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.iconIv);
            holder.titleTv.setText(dataBean.getU_true_name());
            holder.infoTv.setText(dataBean.getUei_info());
            String status = dataBean.getU_task_status();
            if (!TextUtils.isEmpty(status)) {
                if (status.equals("0")) {
                    holder.statusIv.setImageResource(R.mipmap.worker_leisure);
                } else if (status.equals("1")) {
                    holder.statusIv.setImageResource(R.mipmap.worker_mid);
                }
            }
            String distance = DataUtils.getDistance(lonLatBean1, lonLatBean2);
            if (!TextUtils.isEmpty(distance)) {
                holder.distanceTv.setText(distance);
            } else {
                holder.distanceTv.setText("无法获取距离");
            }
            switch (dataBean.getIs_fav()) {
                case 0:
                    holder.collectIv.setImageResource(R.mipmap.collect_gray);
                    break;
                case 1:
                    holder.collectIv.setImageResource(R.mipmap.collect_yellow);
                    break;
            }
            final int p = position;
            final int llId = holder.ll.getId();
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idPosClickHelp.onClick(llId, p);
                }
            });
            final int collectId = holder.collectIv.getId();
            holder.collectIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idPosClickHelp.onClick(collectId, p);
                }
            });
        }
        return convertView;
    }

    private class ViewHolder {

        private LinearLayout ll;
        private CImageView iconIv;
        private ImageView statusIv, collectIv;
        private TextView titleTv, infoTv, distanceTv;

        public ViewHolder(View itemView) {
            ll = (LinearLayout) itemView.findViewById(R.id.ll_item_worker);
            iconIv = (CImageView) itemView.findViewById(R.id.iv_item_worker_icon);
            statusIv = (ImageView) itemView.findViewById(R.id.iv_item_worker_status);
            collectIv = (ImageView) itemView.findViewById(R.id.iv_item_worker_collect);
            titleTv = (TextView) itemView.findViewById(R.id.tv_item_worker_title);
            infoTv = (TextView) itemView.findViewById(R.id.tv_item_worker_info);
            distanceTv = (TextView) itemView.findViewById(R.id.tv_item_worker_distance);
        }
    }
}
