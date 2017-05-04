package cn.ucai.fulicenter.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.data.bean.NewGoodsBean;
import cn.ucai.fulicenter.data.net.GoodsModel;
import cn.ucai.fulicenter.data.net.IGoodsModel;
import cn.ucai.fulicenter.data.net.OnCompleteListener;
import cn.ucai.fulicenter.data.utils.L;
import cn.ucai.fulicenter.data.utils.ResultUtils;
import cn.ucai.fulicenter.ui.adapter.GoodsAdapter;

/**
 * Created by clawpo on 2017/5/4.
 */

public class NewGoodsFragment extends Fragment {
    private static final String TAG = "NewGoodsFragment";
    @BindView(R.id.tv_refresh)
    TextView mTvRefresh;
    @BindView(R.id.rv_goods)
    RecyclerView mRvGoods;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;
    @BindView(R.id.tv_nomore)
    TextView mTvNomore;
    IGoodsModel model;
    GoodsAdapter adapter;
    GridLayoutManager gm;
    int catId = I.CAT_ID;
    int pageId = 1;
    int pageSize = I.PAGE_SIZE_DEFAULT;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newgoods, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new GoodsModel();
        gm = new GridLayoutManager(getContext(), I.COLUM_NUM);
        mRvGoods.setLayoutManager(gm);
        initView();
        loadData();
        setListener();
    }

    private void initView() {
        mSrl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_yellow)
        );
    }

    private void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setPullUpListener() {
        mRvGoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItemPosition = gm.findLastVisibleItemPosition();
                if (adapter.getItemCount()-1==lastVisibleItemPosition
                        && newState == RecyclerView.SCROLL_STATE_IDLE
                        && adapter.isMore()){
                    pageId++;
                    loadData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void setPullDownListener() {
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSrl.setRefreshing(true);
                mTvRefresh.setVisibility(View.VISIBLE);
                pageId=1;
                loadData();
            }
        });
    }

    public void loadData(){
        L.e(TAG,"onActivityCreated....");
        model.loadNewGoodsData(getContext(), catId, pageId, pageSize,
                new OnCompleteListener<NewGoodsBean[]>() {
                    @Override
                    public void onSuccess(NewGoodsBean[] result) {

                        mSrl.setRefreshing(false);
                        mTvRefresh.setVisibility(View.GONE);

                        L.e("main","result="+result);
                        if (result!=null){
                            ArrayList<NewGoodsBean> list = ResultUtils.array2List(result);
                            updateUI(list);
                        }

                        if (result!=null && result.length==pageSize){
                            adapter.setMore(true);
                        }else{
                            adapter.setMore(false);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        L.e("main","error="+error);
                        mSrl.setRefreshing(false);
                        mTvRefresh.setVisibility(View.GONE);
                    }
                });
    }

    private void updateUI(ArrayList<NewGoodsBean> list) {
        L.e(TAG,"updateUI....list="+list);
        L.e(TAG,"updateUI....adapter="+adapter);
        if (adapter==null){
            adapter = new GoodsAdapter(list,getContext());
            mRvGoods.setAdapter(adapter);
        }else{
            adapter.addData(list);
        }
    }
}
