package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.data.bean.NewGoodsBean;
import cn.ucai.fulicenter.data.utils.ImageLoader;

/**
 * Created by clawpo on 2017/5/4.
 */

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.GoodsViewHolder> {
    List<NewGoodsBean> list;
    Context context;

    public GoodsAdapter(List<NewGoodsBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public GoodsAdapter.GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsViewHolder(View.inflate(context, R.layout.item_goods, null));
    }

    @Override
    public void onBindViewHolder(GoodsAdapter.GoodsViewHolder holder, int position) {
        NewGoodsBean bean = list.get(position);
        holder.mTvGoodsName.setText(bean.getGoodsName());
        holder.mTvGoodsPrice.setText(bean.getCurrencyPrice());
        ImageLoader.downloadImg(context,holder.mIvGoodsThumb,bean.getGoodsThumb());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivGoodsThumb)
        ImageView mIvGoodsThumb;
        @BindView(R.id.tvGoodsName)
        TextView mTvGoodsName;
        @BindView(R.id.tvGoodsPrice)
        TextView mTvGoodsPrice;
        @BindView(R.id.layout_goods)
        LinearLayout mLayoutGoods;

        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
