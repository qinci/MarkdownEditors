package ren.qinc.markdowneditors.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ren.qinc.markdowneditors.R;
import ren.qinc.markdowneditors.base.BaseApplication;
import ren.qinc.markdowneditors.entity.FileBean;
import ren.qinc.markdowneditors.utils.ColorUtils;
import ren.qinc.markdowneditors.utils.UnitsUtils;

/**
 * 文件管理器的Adapter
 * Created by 沈钦赐 on 16/1/26.
 */
public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileViewHolder> {
    private List<FileBean> mDatas;
    private LayoutInflater mInflater;
    private Context mContext;
    String folder;

    private int colorPrimary;
    private int alphaColorPrimary;
    private int colorPrimaryText;
    private int colorSecondaryText;
    private int colorDivider;
    PorterDuffColorFilter colorFilter;
    private OnItemClickLitener mOnItemClickLitener;

    private boolean isEditMode = false;

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    public FileListAdapter(Context context, List<FileBean> datas) {
        mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.mContext = context;
        this.folder = context.getString(R.string.create_folder);
        initColor();
    }

    public void addData(int position, FileBean fileBean) {
        mDatas.add(position, fileBean);
        notifyItemInserted(position);
    }

    public void removeData(FileBean file) {
        int position = mDatas.indexOf(file);
        mDatas.remove(position);
        notifyItemRemoved(position);
//        notifyDataSetChanged();
    }


    private void initColor() {
        this.colorPrimary = BaseApplication.color(R.color.colorPrimary);
        this.alphaColorPrimary = ColorUtils.getAlphaColor(colorPrimary, 130);
        this.colorPrimaryText = BaseApplication.color(R.color.colorPrimaryText);
        this.colorSecondaryText = BaseApplication.color(R.color.colorSecondaryText);
        this.colorDivider = BaseApplication.color(R.color.colorDivider);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_file_list, parent, false);
        FileViewHolder holder = new FileViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, int position) {
        FileBean bean = mDatas.get(position);

        holder.name.setText(bean.name);
        holder.fileTime.setText(UnitsUtils.friendlyTime(bean.lastTime));

        //图标
        if (bean.isDirectory) {
            holder.fileSize.setText("文件夹");
            holder.fileIcon.setImageResource(R.drawable.ic_folder);
        } else {
            holder.fileSize.setText(UnitsUtils.getFormatSize(bean.size));
            holder.fileIcon.setImageResource(R.drawable.ic_file);
        }

        //分割线
        if (position == 0 && holder.divider.getVisibility() == View.VISIBLE) {
            holder.divider.setVisibility(View.GONE);
        } else if (position != 0 && holder.divider.getVisibility() == View.GONE) {
            holder.divider.setVisibility(View.VISIBLE);
        }

        //选择模式 颜色设置
        if (bean.isSelect) {//选择了
            if (colorFilter == null)
                colorFilter = new PorterDuffColorFilter(colorPrimary, PorterDuff.Mode.SRC_IN);

            holder.itemView.setBackgroundColor(alphaColorPrimary);
            holder.name.setTextColor(0xffffffff);
            holder.fileSize.setTextColor(0xaaffffff);
            holder.fileTime.setTextColor(0xaaffffff);
            holder.divider.setBackgroundColor(0xffffffff);
            holder.fileIcon.setColorFilter(0xff727272, PorterDuff.Mode.DST_IN);
            holder.fileIcon.getDrawable().setColorFilter(colorFilter);
        } else {
            holder.itemView.setBackgroundColor(0xffffffff);
            holder.name.setTextColor(colorPrimaryText);
            holder.fileSize.setTextColor(colorSecondaryText);
            holder.fileTime.setTextColor(colorSecondaryText);
            holder.divider.setBackgroundColor(colorDivider);
            holder.fileIcon.getDrawable().setColorFilter(null);
        }


        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(v -> {
                int pos = holder.getAdapterPosition();
                mOnItemClickLitener.onItemClick(holder.name, pos);
            });

            holder.itemView.setOnLongClickListener(v -> {
                int pos = holder.getAdapterPosition();
                mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                return !isEditMode;
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class FileViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.file_name)
        TextView name;
        @Bind(R.id.file_size)
        TextView fileSize;
        @Bind(R.id.file_time)
        TextView fileTime;
        @Bind(R.id.file_icon)
        ImageView fileIcon;
        @Bind(R.id.divider)
        View divider;

        public FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
