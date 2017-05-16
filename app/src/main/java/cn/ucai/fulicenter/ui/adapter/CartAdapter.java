package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.bean.CartBean;
import cn.ucai.fulicenter.data.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.data.utils.ImageLoader;
import cn.ucai.fulicenter.ui.activity.GoodsDetailActivity;
import cn.ucai.fulicenter.ui.activity.MainActivity;

/**
 * Created by clawpo on 2017/5/16.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context mContext;
    List<CartBean> mList;

    public CartAdapter(Context context, List<CartBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CartViewHolder holder = new CartViewHolder(View.inflate(mContext, R.layout.item_cart, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        holder.bnid(position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cb_cart_selected)
        CheckBox mCbCartSelected;
        @BindView(R.id.iv_cart_thumb)
        ImageView mIvCartThumb;
        @BindView(R.id.tv_cart_good_name)
        TextView mTvCartGoodName;
        @BindView(R.id.iv_cart_add)
        ImageView mIvCartAdd;
        @BindView(R.id.tv_cart_count)
        TextView mTvCartCount;
        @BindView(R.id.iv_cart_del)
        ImageView mIvCartDel;
        @BindView(R.id.tv_cart_price)
        TextView mTvCartPrice;

        CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bnid(int position) {
            final CartBean bean = mList.get(position);
            if (bean!=null) {
                GoodsDetailsBean goods = bean.getGoods();
                if (goods!=null) {
                    ImageLoader.downloadImg(mContext, mIvCartThumb, goods.getGoodsThumb());
                    mTvCartPrice.setText(goods.getCurrencyPrice());
                    mTvCartGoodName.setText(goods.getGoodsName());
                }
                mTvCartCount.setText("("+bean.getCount()+")");
                mCbCartSelected.setChecked(bean.isChecked());
                mIvCartThumb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity)mContext).startActivityForResult(
                                new Intent(mContext, GoodsDetailActivity.class)
                                .putExtra(I.GoodsDetails.KEY_GOODS_ID,bean.getGoodsId()),0);
                    }
                });
            }
        }
    }
}
