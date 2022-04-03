package com.wuda.bbs.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.utils.DateUtils;
import com.wuda.bbs.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @author：luck
 * @date：2016-7-27 23:02
 * @describe：GridImageAdapter
 */
public class GridAttachmentAdapter extends RecyclerView.Adapter<GridAttachmentAdapter.ViewHolder> {
    public static final String TAG = "PictureSelector";
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;
    private final LayoutInflater mInflater;
    private final ArrayList<LocalMedia> list = new ArrayList<>();
    private int selectMax = 9;

    /**
     * 删除
     */
    public void delete(int position) {
        try {

            if (position != RecyclerView.NO_POSITION && list.size() > position) {
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GridAttachmentAdapter(Context context, List<LocalMedia> result) {
        this.mInflater = LayoutInflater.from(context);
        this.list.addAll(result);
    }

    public void setSelectMax(int selectMax) {
        this.selectMax = selectMax;
    }

    public int getSelectMax() {
        return selectMax;
    }

    public ArrayList<LocalMedia> getData() {
        return list;
    }

    public void remove(int position) {
        if (position < list.size()) {
            list.remove(position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView content_iv;
        ImageView delete_iv;

        public ViewHolder(View view) {
            super(view);
            content_iv = view.findViewById(R.id.selectedAttachment_content_imageView);
            delete_iv = view.findViewById(R.id.selectedAttachment_delete_imageView);
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() < selectMax) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    /**
     * 创建ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.selected_attachment_item, viewGroup, false);
        return new ViewHolder(view);
    }

    private boolean isShowAddItem(int position) {
        int size = list.size();
        return position == size;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        //少于MaxSize张，显示继续添加的图标
        if (getItemViewType(position) == TYPE_CAMERA) {
            viewHolder.content_iv.setImageResource(R.drawable.ic_add_image);
            viewHolder.content_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null) {
                        mItemClickListener.openPicture();
                    }
                }
            });
            viewHolder.delete_iv.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.delete_iv.setVisibility(View.VISIBLE);
            viewHolder.delete_iv.setOnClickListener(view -> {
                int index = viewHolder.getAbsoluteAdapterPosition();
                if (index != RecyclerView.NO_POSITION && list.size() > index) {
                    list.remove(index);
                    notifyItemRemoved(index);
                    notifyItemRangeChanged(index, list.size());
                }
            });
            LocalMedia media = list.get(position);
            int chooseModel = media.getChooseModel();
            String path;
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                path = media.getCutPath();
            } else if (media.isCut() || media.isCompressed()) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                path = media.getCompressPath();
            } else {
                // 原图
                path = media.getPath();
            }

            Log.i(TAG, "原图地址::" + media.getPath());

            if (media.isCut()) {
                Log.i(TAG, "裁剪地址::" + media.getCutPath());
            }
            if (media.isCompressed()) {
                Log.i(TAG, "压缩地址::" + media.getCompressPath());
                Log.i(TAG, "压缩后文件大小::" + new File(media.getCompressPath()).length() / 1024 + "k");
            }
            if (media.isToSandboxPath()) {
                Log.i(TAG, "Android Q特有地址::" + media.getSandboxPath());
            }
            if (media.isOriginal()) {
                Log.i(TAG, "是否开启原图功能::" + true);
                Log.i(TAG, "开启原图功能后地址::" + media.getOriginalPath());
            }

//            long duration = media.getDuration();
//            viewHolder.tvDuration.setVisibility(PictureMimeType.isHasVideo(media.getMimeType())
//                    ? View.VISIBLE : View.GONE);
//            if (chooseModel == SelectMimeType.ofAudio()) {
//                viewHolder.tvDuration.setVisibility(View.VISIBLE);
//                viewHolder.tvDuration.setCompoundDrawablesRelativeWithIntrinsicBounds
//                        (R.drawable.ps_ic_audio, 0, 0, 0);
//
//            } else {
//                viewHolder.tvDuration.setCompoundDrawablesRelativeWithIntrinsicBounds
//                        (R.drawable.ps_ic_video, 0, 0, 0);
//            }
//            viewHolder.tvDuration.setText(DateUtils.formatDurationTime(duration));
            if (chooseModel == SelectMimeType.ofAudio()) {
                viewHolder.content_iv.setImageResource(R.drawable.ps_audio_placeholder);
            } else {
                Glide.with(viewHolder.itemView.getContext())
                        .load(PictureMimeType.isContent(path) && !media.isCut() && !media.isCompressed() ? Uri.parse(path)
                                : path)
                        .centerCrop()
                        .placeholder(R.color.DaLiShiHui)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(viewHolder.content_iv);
            }
            //itemView 的点击事件
            if (mItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(v -> {
                    int adapterPosition = viewHolder.getAbsoluteAdapterPosition();
                    mItemClickListener.onItemClick(v, adapterPosition);
                });
            }

        }
    }

    public void clear() {
        // board change
        notifyItemRangeRemoved(0, list.size());
        list.clear();
    }

    private OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mItemClickListener = l;
    }

    public interface OnItemClickListener {
        /**
         * Item click event
         *
         * @param v
         * @param position
         */
        void onItemClick(View v, int position);

        /**
         * Open PictureSelector
         */
        void openPicture();
    }

}
