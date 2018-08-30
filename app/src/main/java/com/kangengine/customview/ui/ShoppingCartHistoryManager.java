package com.kangengine.customview.ui;

import android.os.Environment;
import android.support.annotation.NonNull;

import com.kangengine.customview.bean.StoreGoodsBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Vic
 * time   : 2018/06/19
 * desc   :
 */
public class ShoppingCartHistoryManager {

    private static final String PATH = Environment.getExternalStorageDirectory().getPath()
            + File.separator + "goods";
    private static final String FILE_FORMAT = ".txt";
    private static ShoppingCartHistoryManager instance;

    private ShoppingCartHistoryManager(){

    }

    public static ShoppingCartHistoryManager getInstance() {
        if(instance == null) {
            synchronized (ShoppingCartHistoryManager.class) {
                if(instance == null) {
                    instance = new ShoppingCartHistoryManager();
                }
            }
        }
        return instance;
    }


    public ShoppingCartHistoryManager add(String storeId, StoreGoodsBean storeGoodsBean) {
        File file = new File(PATH);
        if(!file.exists()) {
            file.mkdirs();
        }

        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file.getAbsolutePath() + File.separator + storeId + FILE_FORMAT);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(storeGoodsBean);
            objectOutputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(objectOutputStream != null){
                    objectOutputStream.close();
                }
                if(fileOutputStream != null) {
                    fileOutputStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return this;
    }


    public HashMap<String,Integer> get(String storeId) {
        File file = new File(PATH);
        if(!file.exists()) {
            file.mkdirs();
        }

        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        StoreGoodsBean storeGoodsBean = null;

        try {
            fileInputStream = new FileInputStream(file.getAbsolutePath() + File.separator + storeId + FILE_FORMAT);
            objectInputStream = new ObjectInputStream(fileInputStream);
            storeGoodsBean = (StoreGoodsBean) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return storeGoodsBean.getHashMap();

    }

    /**
     * 获取商铺选择的总个数
     *
     * @param storeId 商铺id
     * @return
     */
    public int getAllGoodsCount(String storeId) {
        HashMap<String, Integer> hashMap = get(storeId);
        int allCount = 0;
        if (hashMap != null) {
            for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
                Integer value = entry.getValue();
                if (value != 0) {
                    allCount += value;
                }
            }
        }
        return allCount;
    }

    /**
     * 删除商铺缓存,如果数量为0
     *
     * @param storeId 商铺的id
     */
    public ShoppingCartHistoryManager delete(@NonNull String storeId) {
        File file = new File(PATH, storeId + FILE_FORMAT);
        if (file.exists()) {
            file.delete();
        }
        return this;
    }

}
