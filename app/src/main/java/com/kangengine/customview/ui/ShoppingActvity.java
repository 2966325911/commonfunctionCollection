package com.kangengine.customview.ui;


import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudoc.share.yybpg.customview.R;
import com.kangengine.customview.adapter.BaseAdapter;
import com.kangengine.customview.adapter.BaseViewHolder;
import com.kangengine.customview.bean.ShopGoodsBean;
import com.kangengine.customview.bean.StoreGoodsBean;
import com.kangengine.customview.ui.dialog.ShoppingCartDialog;
import com.kangengine.customview.util.ToastUtil;
import com.kangengine.customview.util.Util;
import com.kangengine.customview.widget.AppBarStateChangeListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class ShoppingActvity extends BaseActivity implements BaseAdapter.BaseAdapterListener<ShopGoodsBean> {


    private static final long TIME = 300;   // 动画的执行时间
    @BindView(R.id.rv_goods)
    RecyclerView mRvGoods;
    int reduceLeft = 0;
    int addLeft = 0;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.iv_shopping_cart)
    ImageView mIvShoppingCart;
    @BindView(R.id.tv_shopping_cart_count)
    TextView mTvShoppingCartCount;
    @BindView(R.id.appBarLayout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout mRlHeader;


    String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private ArrayList<ShopGoodsBean> mGoodsList = new ArrayList<>();
    private float[] mCurrentPosition = new float[2];
    private int allCount;
    private BaseAdapter mAdapter;
    private int RC_CAMERA_AND_LOCATION = 0X1;
    private String SHOP_ID = "12";


    @Override
    public int getLayoutId() {
        return R.layout.activity_shoppping_actvity;
    }

    @Override
    public String setTitle() {
        return "ShoppingGoods";
    }

    @Override
    public void initView() {
//        EasyPermissions.requestPermissions(this, null, RC_CAMERA_AND_LOCATION, perms);
        initToolbar();
        initHeight();
    }

    @Override
    public void initData() {
        initDatas();
        initAdapter();
    }

    private void initDatas() {
        int id = 0x100;
        HashMap<String,Integer> hashMap = ShoppingCartHistoryManager.getInstance().get(SHOP_ID);
        this.allCount = ShoppingCartHistoryManager.getInstance().getAllGoodsCount(SHOP_ID);

        mTvShoppingCartCount.setVisibility(allCount == 0 ? View.GONE : View.VISIBLE);
        mTvShoppingCartCount.setText(String.valueOf(allCount));
        // TODO: 2018/6/5 0005 模拟请求到的数据
        for (int i = 0; i < 10; i++) {
            mGoodsList.add(new ShopGoodsBean(0, "小猪包套餐" + i, id++ + ""));
        }
        if (hashMap != null) {
            for (ShopGoodsBean bean : mGoodsList) {
                String goodsId = bean.getGoodsId();
                if (hashMap.containsKey(goodsId)) {
                    Integer count = hashMap.get(goodsId);
                    bean.setCount(count);
                }
            }
        }

    }

    private void initAdapter() {
        mAdapter = new BaseAdapter(mGoodsList, R.layout.item_shop_goods, this);
        mRvGoods.setLayoutManager(new LinearLayoutManager(this));
        mRvGoods.setAdapter(mAdapter);
        mRvGoods.setItemAnimator(null);
    }

    private void initToolbar() {
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {

                if (state == State.COLLAPSED) {
                    mTvTitle.setText("芭比馒头");
                    mRlHeader.setVisibility(View.GONE);
                } else {
                    mTvTitle.setText("");
                    mRlHeader.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void initHeight() {
        // ToolBar的top值
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mToolbar.getLayoutParams();
        lp.topMargin = (int) getStatusBarHeight(this);
        mToolbar.setLayoutParams(lp);
        // AppBarLayout的高度
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mAppBarLayout.getLayoutParams();
        layoutParams.height = DensityUtil.dp2px(200) + (int) getStatusBarHeight(this);
        mAppBarLayout.setLayoutParams(layoutParams);
    }

    @Override
    public void convert(final BaseViewHolder holder, ShopGoodsBean bean) {
        // 原价
        TextView tv_goods_original_price = holder.getView(R.id.tv_goods_original_price);
        // 中划线
        Util.drawStrikethrough(tv_goods_original_price);
        // 商品数
        final TextView tv_goods_count = holder.getView(R.id.tv_goods_count);
        // 减少
        final ImageView iv_goods_reduce = holder.getView(R.id.iv_goods_reduce);
        iv_goods_reduce.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 获取减少图标的位置
                reduceLeft = iv_goods_reduce.getLeft();
                iv_goods_reduce.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        iv_goods_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                ShopGoodsBean shopGoodsBean = mGoodsList.get(position);
                int count = shopGoodsBean.getCount();
                count--;
                // 防止过快点击出现多个关闭动画
                if (count == 0) {
                    animClose(iv_goods_reduce);
                    tv_goods_count.setText("");
                    // 考虑到用户点击过快
                    allCount--;
                } else if (count < 0) {
                    // 防止过快点击出现商品数为负数
                    count = 0;
                } else {
                    allCount--;
                    tv_goods_count.setText(String.valueOf(count));
                }
                // 商品的数量是否显示
                if (allCount <= 0) {
                    allCount = 0;
                    mTvShoppingCartCount.setVisibility(View.GONE);
                } else {
                    mTvShoppingCartCount.setText(String.valueOf(allCount));
                    mTvShoppingCartCount.setVisibility(View.VISIBLE);
                }
                shopGoodsBean.setCount(count);
            }
        });
        // 增加
        final ImageView iv_goods_add = holder.getView(R.id.iv_goods_add);
        iv_goods_add.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 获取增加图标的位置
                addLeft = iv_goods_add.getLeft();
                iv_goods_add.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        iv_goods_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                ShopGoodsBean shopGoodsBean = mGoodsList.get(position);
                int count = shopGoodsBean.getCount();
                count++;
                allCount++;
                if (allCount > 0) {
                    mTvShoppingCartCount.setVisibility(View.VISIBLE);
                }
                mTvShoppingCartCount.setText(String.valueOf(allCount));
                if (count == 1) {
                    iv_goods_reduce.setVisibility(View.VISIBLE);
                    animOpen(iv_goods_reduce);
                }
                addGoods2CartAnim(iv_goods_add);
                tv_goods_count.setText(String.valueOf(count));
                shopGoodsBean.setCount(count);
            }
        });

        int count = bean.getCount();
        tv_goods_count.setText(count == 0 ? "" : String.valueOf(count));
        iv_goods_reduce.setVisibility(count == 0 ? View.INVISIBLE : View.VISIBLE);
        // 标题
        holder.setTitle(R.id.tv_goods_title, bean.getGoods());
    }

    private void animOpen(ImageView imageView) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator translationAnim = ObjectAnimator.ofFloat(imageView,"translationX",addLeft - reduceLeft,0);
        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(imageView,"rotation",0,180);
        animatorSet.play(translationAnim).with(rotateAnim);
        animatorSet.setDuration(TIME).start();
    }

    private void animClose(final ImageView imageView) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator translationAnim = ObjectAnimator.ofFloat(imageView,"translationX",0,addLeft - reduceLeft);
        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(imageView,"rotation",0,180);
        animatorSet.play(translationAnim).with(rotateAnim);
        animatorSet.setDuration(TIME).start();

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ObjectAnimator oa = ObjectAnimator.ofFloat(imageView,"translationX",addLeft - reduceLeft,0);
                oa.setDuration(0);
                oa.start();
                imageView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 贝塞尔动画
     * @param goodsImageView
     */
    private void addGoods2CartAnim(final ImageView goodsImageView) {
        final ImageView goods=  new ImageView(this);
        goods.setImageResource(R.mipmap.icon_goods_add);

        int size = Util.dp2px(this,24);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(size,size);
        goods.setLayoutParams(lp);

        mCoordinatorLayout.addView(goods);

        //控制点的位置
        int[] recyclerLocation = new int[2];
        mCoordinatorLayout.getLocationInWindow(recyclerLocation);
         //购物车的位置起始点
        int[] startLocation = new int[2];
        goodsImageView.getLocationInWindow(startLocation);

        //购物车的终点位置
        int[] endLocation = new int[2];
        mIvShoppingCart.getLocationInWindow(endLocation);

        int startX = startLocation[0] - recyclerLocation[0];
        int startY = startLocation[1] - recyclerLocation[1];

        int endX = endLocation[0] - recyclerLocation[0];
        int endY = endLocation[1] - recyclerLocation[1];

        final Path path = new Path();
        path.moveTo(startX,startY);
        path.quadTo((startX + startY)/2,startY,endX,endY);

        final PathMeasure pathMeasure = new PathMeasure(path,false);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,pathMeasure.getLength());

        //为了计算贝塞尔执行的时间 根据具体计算
        int tempX = Math.abs(startX - endX);
        int tempY = Math.abs(startY - endY);
        int time = (int)(0.5 * Math.sqrt((tempX*tempX) + (tempY * tempY)));
        valueAnimator.setDuration(time);

        valueAnimator.start();
        valueAnimator.setInterpolator(new AccelerateInterpolator());

        valueAnimator.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                pathMeasure.getPosTan(value,mCurrentPosition,null);
                goods.setTranslationX(mCurrentPosition[0]);
                goods.setTranslationY(mCurrentPosition[1]);

            }
        });


        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCoordinatorLayout.removeView(goods);
                mTvShoppingCartCount.setText(String.valueOf(allCount));
            }
        });

    }

    private double getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    @OnClick({R.id.iv_back, R.id.layout_shopping_cart, R.id.tv_shopping_cart_pay, R.id.tv_goods_get})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 返回
            case R.id.iv_back:
                ToastUtil.showToast(this, "返回");
                break;
            // 购物车的弹窗
            case R.id.layout_shopping_cart:
                shoppingCartDialog();
                break;
            // 支付
            case R.id.tv_shopping_cart_pay:
                pay();
                break;
            // 领取
            case R.id.tv_goods_get:
//                showDiscountCouponDialog();
                break;
        }
    }

    public void shoppingCartDialog() {
        ShoppingCartDialog dialog = new ShoppingCartDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ShoppingCartDialog.CART_GOODS, mGoodsList);
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "cartGoods");
        dialog.setCartGoodsDialogListener(new ShoppingCartDialog.CartGoodsDialogListener() {
            @Override
            public void add(int allCount, ShopGoodsBean shopGoodsBean) {
                notifyItemChanged(allCount, shopGoodsBean);
            }

            @Override
            public void reduce(int allCount, ShopGoodsBean shopGoodsBean) {
                notifyItemChanged(allCount, shopGoodsBean);
            }

            @Override
            public void clear() {
                clearShoppingGoods();
            }
        });
    }

    public void notifyItemChanged(int allCount, ShopGoodsBean shopGoodsBean) {
        this.allCount = allCount;
        mTvShoppingCartCount.setVisibility(allCount == 0 ? View.GONE : View.VISIBLE);
        mTvShoppingCartCount.setText(String.valueOf(allCount));
        // 遍历，格局id查找对象
        for (int i = 0; i < mGoodsList.size(); i++) {
            ShopGoodsBean bean = mGoodsList.get(i);
            String goodsId = bean.getGoodsId();
            if (goodsId.equals(shopGoodsBean.getGoodsId())) {
                bean = shopGoodsBean;
                mAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    public void clearShoppingGoods() {
        allCount = 0;
        mTvShoppingCartCount.setVisibility(View.GONE);
        for (ShopGoodsBean bean : mGoodsList) {
            bean.setCount(0);
        }
        mAdapter.notifyDataSetChanged();
    }


    public void pay() {
        String remind = "购物车中空空如也";
        if (allCount > 0) {
            remind = "去支付";
        }
        ToastUtil.showToast(this, remind);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (allCount != 0) {
            HashMap<String, Integer> hashMap = new HashMap<>();
            StoreGoodsBean storeGoodsBean = new StoreGoodsBean(hashMap);
            for (ShopGoodsBean bean : mGoodsList) {
                int count = bean.getCount();
                String goodsId = bean.getGoodsId();
                if (count != 0) {
                    hashMap.put(goodsId, count);
                }
            }
            ShoppingCartHistoryManager.getInstance().add(SHOP_ID, storeGoodsBean);
        } else {
            ShoppingCartHistoryManager.getInstance().delete(SHOP_ID);
        }
    }

}
