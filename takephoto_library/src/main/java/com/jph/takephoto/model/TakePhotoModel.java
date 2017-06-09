package com.jph.takephoto.model;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wangkuan on 2017/1/5.
 * 拍照的model
 */
public class TakePhotoModel implements Serializable {
    private boolean takePhoto;//是否是拍照
    private int limit;//多选照片数量
    private String originalPath;//照片原始路径
    private String compressPath;//照片压缩后的路径

    private ArrayList<String> images;

    public boolean isTakePhoto() {
        return takePhoto;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getCompressPath() {
        return compressPath;
    }

    public void setCompressPath(String compressPath) {
        this.compressPath = compressPath;
    }

    public void setTakePhoto(boolean takePhoto) {
        this.takePhoto = takePhoto;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public void addImages(String image) {
        if (images == null) {
            images = new ArrayList<>();
        }
        this.images.add(image);
    }
}
