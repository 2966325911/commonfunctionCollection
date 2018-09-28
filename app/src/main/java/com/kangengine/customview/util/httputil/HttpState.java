package com.kangengine.customview.util.httputil;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.kangengine.customview.R;
import com.kangengine.customview.util.DPIUtils;

import java.util.concurrent.atomic.AtomicInteger;

import pl.droidsonroids.gif.GifImageView;

public class HttpState {

	private LayoutParams layoutParams;
	private LayoutParams layoutParamsIn;
	private LayoutParams layoutParamsContent;
	private ViewGroup modal;
	private Activity myActivity;
	private AtomicInteger cnt;
	//	private ImageView imageView;
	private GifImageView imageView;
	private ViewGroup rootFrameLayout;
	private ViewGroup contentView;

	private int statusBarHeight;

	private String TAG="HttpState";


	public HttpState(Activity myactivity) {
		cnt = new AtomicInteger(0);
		myActivity = myactivity;
		layoutParams = new android.widget.RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParamsIn = new android.widget.RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

		layoutParamsContent= new android.widget.RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		getStatusHeight();
	}

	@SuppressWarnings("deprecation")
	public ViewGroup getModal() {
		if (modal == null) {
			modal = new RelativeLayout(myActivity);
			modal.setOnTouchListener(new android.view.View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
//					if(myActivity!=null && myActivity instanceof MainActivity){
//						return false;
//					}
					return true;
				}
			});

		}
		return modal;
	}

	private ViewGroup getRootFrameLayout() {
		if (rootFrameLayout == null) {
			rootFrameLayout = (ViewGroup) myActivity.getWindow().peekDecorView();
			if (rootFrameLayout == null) {
				try {
					Thread.sleep(50L);
				} catch (InterruptedException interruptedexception) {
				}
				rootFrameLayout = getRootFrameLayout();
			}
		}
		return rootFrameLayout;
	}

	private void  setModelLayoutPadding(){
		//左边的距离是：(ScreenWidth-2*padding_layout-margin_blocks)/5.2;
		// 目的是：显示的progress显示的位置是MainActivity右边fragment的中间
		int padding_layout= (int) myActivity.getResources().getDimension(R.dimen.size_padding_small);
		double margin_blocks=myActivity.getResources().getDimension(R.dimen.size_padding_xsmall);
		int marginLeft= (int) (( DPIUtils.getWidth()-2*padding_layout-margin_blocks)/5.2 + padding_layout + margin_blocks);
		int width=DPIUtils.getWidth()-marginLeft-padding_layout;
		int height=DPIUtils.getHeight()-statusBarHeight-2*padding_layout;
		layoutParamsContent.width=width;
		layoutParamsContent.height=height;
		//相对布局中，marginBottom不起作用
		layoutParamsContent.setMargins(marginLeft,padding_layout + statusBarHeight,padding_layout,padding_layout);
	}

	private void getStatusHeight(){
		Rect frame = new Rect();
		myActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		statusBarHeight = frame.top;
		if(statusBarHeight==0){
			statusBarHeight=32;
		}
	}

	private void createNewProgressBar() {
		if (modal != null) {
			if (imageView != null) {
				modal.removeView(imageView);
			}

			imageView = new GifImageView(myActivity);
			imageView.setImageResource(R.mipmap.loading);
			ColorDrawable colordrawable = new ColorDrawable(Color.GRAY);
			colordrawable.setAlpha(100);

//			if(myActivity instanceof MainActivity){
//				modal.removeView(contentView);
//				contentView=new RelativeLayout(myActivity);
//				setModelLayoutPadding();
//				contentView.setBackgroundDrawable(colordrawable);
//				contentView.setLayoutParams(layoutParamsContent);
//				contentView.addView(imageView,layoutParams);
//				contentView.setOnTouchListener(new View.OnTouchListener() {
//					@Override
//					public boolean onTouch(View v, MotionEvent event) {
//						return true;
//					}
//				});
//
//				modal.addView(contentView, layoutParamsContent);
//			}else{
//				modal.removeView(imageView);
				modal.setBackgroundDrawable(colordrawable);
				modal.addView(imageView, layoutParams);
//			}

		}
	}


	private void addModal() {
		rootFrameLayout = getRootFrameLayout();
		modal = getModal();

		createNewProgressBar();
		rootFrameLayout.post(new Runnable() {

			@Override
			public void run() {
				rootFrameLayout.addView(modal, layoutParamsIn);
				rootFrameLayout.invalidate();
			}
		});
	}

	public boolean show() {
		cnt.incrementAndGet();
		if (cnt.intValue() > 1) {
			return false;
		} else {
			addModal();
			return true;
		}
	}

	public boolean remove() {
		cnt.decrementAndGet();
		if (cnt.intValue() <= 0) {
			removeModal();
			return true;
		} else {
			return false;
		}
	}

	private void removeModal() {
		if(rootFrameLayout != null && modal !=null) {
			rootFrameLayout.post(new Runnable() {
				@Override
				public void run() {
					rootFrameLayout.removeView(modal);
					rootFrameLayout.invalidate();
				}

			});
		}
	}
}
