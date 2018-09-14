package com.kangengine.customview.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.LruCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.UUID;

/**
 * @author : Vic
 * time   : 2018/09/12
 * desc   :
 */
public class ImageCache {
    private LruCache<String,Bitmap> mMemoryCache;

    int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);

    int cacheSize = maxMemory/8;
    public void init(){
        mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount()/1024;
            }
        };

    }

    public void putBitmap(String key,Bitmap bitmap) {
        if(mMemoryCache != null) {
            mMemoryCache.put(key,bitmap);
        }
    }

    public void getBitmap(String key) {
        if(mMemoryCache != null) {
            mMemoryCache.get(key);
        }
    }

    public static int calculateSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;
        if(width > reqWidth || height > reqHeight) {
            if(width > height) {
                inSampleSize = Math.round(height/reqHeight);
            }  else {
                inSampleSize = Math.round(width/reqHeight);
            }
        }
        return inSampleSize;
    }

    /**
     * 缩略图
     * @param path
     * @param maxWidth
     * @param maxHeight
     * @param autoRate
     * @return
     */
    public static Bitmap thumbnail(String path,int maxWidth,int maxHeight,boolean autoRate) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        options.inJustDecodeBounds = false;
        int sampleSize = calculateSampleSize(options,maxWidth,maxHeight);
        options.inSampleSize = sampleSize;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        if(bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        bitmap = BitmapFactory.decodeFile(path,options);

        return bitmap;
    }


    /**
     * 保存Bitmap
     * @param bitmap
     * @param format
     * @param quality
     * @param desFile
     * @return
     */
    public static String save(Bitmap bitmap, Bitmap.CompressFormat format,int quality,File desFile) {
        FileOutputStream outputStream = null;
        try {
          outputStream  = new FileOutputStream(desFile);
          if(bitmap.compress(format,quality,outputStream)){
              outputStream.flush();
              outputStream.close();
          }

          if(bitmap != null && !bitmap.isRecycled()) {
              bitmap.recycle();
          }

          return desFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String save(Bitmap bitmap, Bitmap.CompressFormat format, int quality, Context context) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        File dir = new File(Environment.getExternalStorageDirectory() +
        "/" + context.getPackageName());

        if(!dir.exists()){
            dir.mkdirs();
        }

        File descFile = new File(dir, UUID.randomUUID().toString());
        return save(bitmap,format,quality,descFile);
    }

    private Bitmap createScaleBitmap(Bitmap bitmap,int dstWidth,int dstHeight) {
        Bitmap bm = Bitmap.createScaledBitmap(bitmap,dstWidth,dstHeight,true);
        return bm;
    }


}
