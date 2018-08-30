package com.cloudoc.share.yybpg.customview.bean;

/**
 * @author : Vic
 * time   : 2018/07/31
 * desc   :
 */
public class MenuItem {
    public int imageId;
    public String title;

    public MenuItem(){

    }
    public MenuItem(String title, int resId) {
        this.title = title;
        imageId = resId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
