package cn.ucai.fulicenter.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.data.bean.CartBean;
import cn.ucai.fulicenter.data.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.data.bean.MessageBean;
import cn.ucai.fulicenter.data.bean.User;
import cn.ucai.fulicenter.data.net.IUserModel;
import cn.ucai.fulicenter.data.net.OnCompleteListener;
import cn.ucai.fulicenter.data.net.UserModel;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.data.utils.ResultUtils;
import cn.ucai.fulicenter.ui.adapter.CartAdapter;
import cn.ucai.fulicenter.ui.view.SpaceItemDecoration;

/**
 * Created by clawpo on 2017/5/5.
 */

public class CartFragment extends Fragment {
    private static final String TAG = "CartFragment";
    @BindView(R.id.tv_refresh)
    TextView mTvRefresh;
    @BindView(R.id.rv_goods)
    RecyclerView mRvGoods;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;
    @BindView(R.id.tv_nomore)
    TextView mTvNomore;
    IUserModel model;
    Unbinder bind;
    ProgressDialog pd;
    CartAdapter adapter;
    LinearLayoutManager llm;
    ArrayList<CartBean> list = new ArrayList<>();
    @BindView(R.id.tv_cart_sum_price)
    TextView mTvCartSumPrice;
    @BindView(R.id.tv_cart_save_price)
    TextView mTvCartSavePrice;
    @BindView(R.id.tv_cart_buy)
    TextView mTvCartBuy;
    @BindView(R.id.layout_cart)
    RelativeLayout mLayoutCart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, null);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new UserModel();
        initDialog();
        initView();
        loadData();
        setListener();
    }

    private void loadData() {
        if (FuLiCenterApplication.getInstance().isLogined()) {
            User user = FuLiCenterApplication.getInstance().getCurrentUser();
            model.loadCart(getContext(), user.getMuserName(), new OnCompleteListener<CartBean[]>() {
                @Override
                public void onSuccess(CartBean[] result) {
                    pd.dismiss();
                    setLayoutVisibility(false);
                    setListVisibility(true, false);
                    list.clear();

                    if (result != null) {
                        list.addAll(ResultUtils.array2List(result));
                        updateUI();
                        if (list.size() == 0) {
                            setListVisibility(false, false);
                        }
                    } else {
                        setListVisibility(false, false);
                    }
                }

                @Override
                public void onError(String error) {
                    L.e("main", "error=" + error);
                    pd.dismiss();
                    setLayoutVisibility(false);
                    list.clear();
                    setListVisibility(false, true);
                }
            });
        }
    }


    private void initDialog() {
        pd = new ProgressDialog(getContext());
        pd.setMessage(getString(R.string.load_more));
        pd.show();
    }

    private void initView() {
        llm = new LinearLayoutManager(getContext());
        mRvGoods.setLayoutManager(llm);
        mSrl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_yellow)
        );
        mRvGoods.addItemDecoration(new SpaceItemDecoration(12));
    }

    private void setListener() {
        setPullDownListener();
    }

    private void setPullDownListener() {
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setLayoutVisibility(true);
                loadData();
            }
        });
    }

    void setLayoutVisibility(boolean visibility) {
        mSrl.setRefreshing(visibility);
        mTvRefresh.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    void setListVisibility(boolean visibility, boolean isError) {
        L.e(TAG, "setListVisibility,visibility=" + visibility + ",isError=" + isError);
        mTvNomore.setText(isError ? R.string.reload_data : R.string.cart_no_more);
        mTvNomore.setVisibility(visibility ? View.GONE : View.VISIBLE);
        mSrl.setVisibility(visibility ? View.VISIBLE : View.GONE);
        mLayoutCart.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.tv_nomore)
    public void reloadData() {
        pd.show();
        loadData();
    }

    private void updateUI() {
        if (adapter == null) {
            adapter = new CartAdapter(getContext(), list);
            adapter.setCbkListener(cbkListener);
            adapter.setClickListener(clickListener);
            mRvGoods.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bind != null) {
            bind.unbind();
        }
    }

    private void sumPrice(){
        int sumPrice = 0;
        int savePrice = 0;
        if (list.size()>0){
            for (CartBean bean : list) {
                if (bean.isChecked()){
                    GoodsDetailsBean goods = bean.getGoods();
                    if (goods!=null){
                        sumPrice+=getPrice(goods.getCurrencyPrice())*bean.getCount();
                        savePrice+=(getPrice(goods.getCurrencyPrice())-getPrice(goods.getRankPrice()))
                                *bean.getCount();
                    }
                }
            }
        }else{
            sumPrice = 0;
            savePrice = 0;
        }
        mTvCartSumPrice.setText("合计：￥ "+sumPrice);
        mTvCartSavePrice.setText("节省：￥ "+savePrice);
    }

    private int getPrice(String currencyPrice) {
        String price = currencyPrice.substring(currencyPrice.indexOf("￥")+1);
        return Integer.parseInt(price);
    }

    CompoundButton.OnCheckedChangeListener cbkListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = (int) buttonView.getTag();
            L.e(TAG,"onCheckedChanged,position="+position+",isChecked="+isChecked);
            list.get(position).setChecked(isChecked);
            sumPrice();
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            updateCart(position,1);
        }
    };

    private void updateCart(final int position, final int count) {
        final CartBean bean = list.get(position);
        model.updateCart(getContext(), bean.getId(), bean.getCount() + count, false,
                new OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result!=null && result.isSuccess()){
                            list.get(position).setCount(bean.getCount()+count);
                            adapter.notifyDataSetChanged();
                            sumPrice();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }
}
