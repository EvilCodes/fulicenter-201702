package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.data.bean.CategoryChildBean;
import cn.ucai.fulicenter.data.bean.CategoryGroupBean;
import cn.ucai.fulicenter.data.utils.ImageLoader;

/**
 * Created by clawpo on 2017/5/8.
 */

public class CategoryAdapter extends BaseExpandableListAdapter {
    List<CategoryGroupBean> groupList;
    List<List<CategoryChildBean>> childList;
    Context context;

    public CategoryAdapter(List<CategoryGroupBean> groupList, List<List<CategoryChildBean>> childList, Context context) {
        this.groupList = groupList;
        this.childList = childList;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return groupList == null ? 0 : groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList != null && childList.get(groupPosition) != null
                ? childList.get(groupPosition).size() : 0;
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        return groupList != null ? groupList.get(groupPosition) : null;
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        return childList != null && childList.get(groupPosition) != null ?
                childList.get(groupPosition).get(childPosition) : null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if (convertView==null) {
            convertView = View.inflate(context, R.layout.item_category_group, null);
            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.bind(groupPosition,isExpanded);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView==null){
            convertView = View.inflate(context, R.layout.item_category_child, null);
            holder = new ChildViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ChildViewHolder) convertView.getTag();
        }
        holder.bind(groupPosition,childPosition);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupViewHolder {
        @BindView(R.id.iv_group_thumb)
        ImageView mIvGroupThumb;
        @BindView(R.id.tv_group_name)
        TextView mTvGroupName;
        @BindView(R.id.iv_indicator)
        ImageView mIvIndicator;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(int groupPosition, boolean isExpanded) {
            CategoryGroupBean bean = groupList.get(groupPosition);
            if (bean!=null) {
                ImageLoader.downloadImg(context, mIvGroupThumb, bean.getImageUrl());
                mTvGroupName.setText(bean.getName());
                mIvIndicator.setImageResource(isExpanded ? R.mipmap.expand_off : R.mipmap.expand_on);
            }
        }
    }

    class ChildViewHolder {
        @BindView(R.id.iv_category_child_thumb)
        ImageView mIvCategoryChildThumb;
        @BindView(R.id.tv_category_child_name)
        TextView mTvCategoryChildName;
        @BindView(R.id.layout_category_child)
        RelativeLayout mLayoutCategoryChild;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(int groupPosition, int childPosition) {
            CategoryChildBean bean = getChild(groupPosition, childPosition);
            if (bean!=null){
                ImageLoader.downloadImg(context,mIvCategoryChildThumb,bean.getImageUrl());
                mTvCategoryChildName.setText(bean.getName());
            }
        }
    }
}
