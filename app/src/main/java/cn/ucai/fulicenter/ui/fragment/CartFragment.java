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
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.data.bean.CartBean;
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
                    setListVisibility(true,false);
                    list.clear();

                    if (result!=null){
                        list.addAll(ResultUtils.array2List(result));
                        updateUI();
                        if (list.size()==0){
                            setListVisibility(false,false);
                        }
                    }else{
                        setListVisibility(false,false);
                    }
                }

                @Override
                public void onError(String error) {
                    L.e("main","error="+error);
                    pd.dismiss();
                    setLayoutVisibility(false);
                    list.clear();
                    setListVisibility(false,true);
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

    void setLayoutVisibility(boolean visibility){
        mSrl.setRefreshing(visibility);
        mTvRefresh.setVisibility(visibility?View.VISIBLE:View.GONE);
    }

    void setListVisibility(boolean visibility,boolean isError){
        L.e(TAG,"setListVisibility,visibility="+visibility+",isError="+isError);
        mTvNomore.setText(isError?R.string.reload_data:R.string.cart_no_more);
        mTvNomore.setVisibility(visibility?View.GONE:View.VISIBLE);
        mSrl.setVisibility(visibility?View.VISIBLE:View.GONE);
    }

    @OnClick(R.id.tv_nomore)
    public void reloadData(){
        pd.show();
        loadData();
    }

    private void updateUI() {
        if (adapter==null){
            adapter = new CartAdapter(getContext(),list);
            mRvGoods.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bind!=null){
            bind.unbind();
        }
    }
}
