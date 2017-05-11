package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.data.bean.User;
import cn.ucai.fulicenter.data.utils.ImageLoader;
import cn.ucai.fulicenter.data.utils.SharePrefrenceUtils;

/**
 * Created by clawpo on 2017/5/11.
 */

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.tv_common_title)
    TextView mTvCommonTitle;
    @BindView(R.id.iv_user_profile_avatar)
    ImageView mIvUserProfileAvatar;
    @BindView(R.id.tv_user_profile_name)
    TextView mTvUserProfileName;
    @BindView(R.id.tv_user_profile_nick)
    TextView mTvUserProfileNick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        mTvCommonTitle.setText(R.string.settings);
        initData();
    }

    private void initData() {
        User user = FuLiCenterApplication.getInstance().getCurrentUser();
        if (user!=null){
            mTvUserProfileNick.setText(user.getMuserNick());
            mTvUserProfileName.setText(user.getMuserName());
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), SettingsActivity.this,
                    mIvUserProfileAvatar);
        }
    }

    @OnClick({R.id.backClickArea, R.id.btn_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backClickArea:
                finish();
                break;
            case R.id.btn_logout:
                logout();
                break;
        }
    }

    private void logout() {
        FuLiCenterApplication.getInstance().setCurrentUser(null);
        SharePrefrenceUtils.getInstance().removeUser();
        startActivity(new Intent(SettingsActivity.this,LoginActivity.class));
        finish();
    }
}
