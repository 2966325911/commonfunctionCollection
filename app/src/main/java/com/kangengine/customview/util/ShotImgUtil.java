package com.kangengine.customview.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Vic
 * time    : 2018-12-25 15:01
 * desc    :
 */
public class ShotImgUtil {
    /**
     * 获取当前Window 的 DrawingCache 的方式，即decorView的DrawingCache
     * @param activity
     * @return
     */
    public static Bitmap shotCurScreen(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(),0,0,view.getMeasuredWidth(),view.getMeasuredHeight());

        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();

        return bitmap;
    }


    public static Bitmap getViewBp(View v) {
        if(v == null) {
            return null;
        }

        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();

        v.setDrawingCacheEnabled(false);
        v.destroyDrawingCache();
        if(Build.VERSION.SDK_INT >= 11 ){
            v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(),View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(v.getHeight(),View.MeasureSpec.EXACTLY));
            v.layout((int)v.getX(),(int)v.getY(),(int)(v.getX()+ v.getMeasuredWidth()),(int)(v.getY()+v.getMeasuredHeight()));

        } else {
            v.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED));
            v.layout(0,0,v.getMeasuredWidth(),v.getMeasuredHeight());
        }
        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache(),0,0,v.getMeasuredWidth(),v.getMeasuredHeight());

        return bitmap;
    }

    /**
     * scrollView 截屏
     * @param scrollView
     * @return
     */
    public static Bitmap shotScrollView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        for(int i = 0; i < scrollView.getChildCount();i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
        }

        bitmap = Bitmap.createBitmap(scrollView.getWidth(),h,Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);

        return bitmap;
    }

    /**
     * 获取listview 截屏
     * @param listview
     * @return
     */
    public static Bitmap shotListView(ListView listview) {

        ListAdapter adapter = listview.getAdapter();
        int itemscount = adapter.getCount();
        int allitemsheight = 0;
        List<Bitmap> bmps = new ArrayList<Bitmap>();

        for (int i = 0; i < itemscount; i++) {

            View childView = adapter.getView(i, null, listview);
            childView.measure(
                    View.MeasureSpec.makeMeasureSpec(listview.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
            childView.setDrawingCacheEnabled(true);
            childView.buildDrawingCache();
            bmps.add(childView.getDrawingCache());
            allitemsheight += childView.getMeasuredHeight();
        }

        Bitmap bigbitmap =
                Bitmap.createBitmap(listview.getMeasuredWidth(), allitemsheight, Bitmap.Config.ARGB_8888);
        Canvas bigcanvas = new Canvas(bigbitmap);

        Paint paint = new Paint();
        int iHeight = 0;

        for (int i = 0; i < bmps.size(); i++) {
            Bitmap bmp = bmps.get(i);
            bigcanvas.drawBitmap(bmp, 0, iHeight, paint);
            iHeight += bmp.getHeight();

            bmp.recycle();
            bmp = null;
        }

        return bigbitmap;
    }

    /**
     * 获取RecyclerView截屏
     * @param view
     * @return
     */
    public static Bitmap shotRecyclerView(RecyclerView view) {
        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(
                        View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(),
                        holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }
                height += holder.itemView.getMeasuredHeight();
            }

            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            Drawable lBackground = view.getBackground();
            if (lBackground instanceof ColorDrawable) {
                ColorDrawable lColorDrawable = (ColorDrawable) lBackground;
                int lColor = lColorDrawable.getColor();
                bigCanvas.drawColor(lColor);
            }

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }
        }
        return bigBitmap;
    }


    public static int getStatusH(Activity ctx) {
        Rect s = new Rect();
        ctx.getWindow().getDecorView().getWindowVisibleDisplayFrame(s);
        return s.top;
    }

    /**
     * get the height of status *
     */
    public static int getStatusH(Context ctx) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = ctx.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * get the height of status *
     */
    public static int getStatusHeight(Activity activity) {
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return resourceId > 0 ? activity.getResources().getDimensionPixelSize(resourceId) : 0;
    }

    /**
     * get the height of title *
     */
    public static int getTitleH(Activity ctx) {
        int contentTop = ctx.getWindow()
                .findViewById(Window.ID_ANDROID_CONTENT).getTop();
        return contentTop - getStatusH(ctx);
    }

    /**
     * get the width of screen **
     */
    public static int getScreenW(Context ctx) {
        int w = 0;
        if (Build.VERSION.SDK_INT > 13) {
            Point p = new Point();
            ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getSize(p);
            w = p.x;
        } else {
            w = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getWidth();
        }
        return w;
    }

    /**
     * get the height of screen *
     */
    public static int getScreenH(Context ctx) {
        int h = 0;
        if (Build.VERSION.SDK_INT > 13) {
            Point p = new Point();
            ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getSize(p);
            h = p.y;
        } else {
            h = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getHeight();
        }
        return h;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }


    public static String saveScreenBitmap(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory() + "/img/");

        if(!file.exists()) {
            file.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File fName = new File(file,fileName);
        try {
            FileOutputStream fos = new FileOutputStream(fName);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fName.getAbsolutePath();
    }
}
