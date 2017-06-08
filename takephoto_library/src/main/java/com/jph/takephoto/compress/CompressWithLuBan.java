package com.jph.takephoto.compress;

import android.content.Context;

import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


/**
 * 压缩照片,采用luban
 * Author: crazycodeboy
 * Date: 2016/11/5 0007 20:10
 * Version:4.0.0
 * 技术博文：http://www.devio.org/
 * GitHub:https://github.com/crazycodeboy
 * Eamil:crazycodeboy@gmail.com
 */
public class CompressWithLuBan implements CompressImage {
    private ArrayList<TImage> images;
    private CompressListener listener;
    private Context context;
    private LubanOptions options;
    private ArrayList<File> files = new ArrayList<>();

    public CompressWithLuBan(Context context, CompressConfig config, ArrayList<TImage> images, CompressListener listener) {
        options = config.getLubanOptions();
        this.images = images;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public void compress() {
        if (images == null || images.isEmpty()) {
            listener.onCompressFailed(images, " images is null");
            return;
        }
        for (TImage image : images) {
            if (image == null) {
                listener.onCompressFailed(images, " There are pictures of compress  is null.");
                return;
            }
            files.add(new File(image.getOriginalPath()));
        }
        if (images.size() == 1) {
            compressOne();
        } else {
            compressMulti();
        }
    }

    private void compressOne() {
        Luban.get(context).load(files.get(0)).putGear(options.getGear())
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        TImage image = images.get(0);
                        image.setCompressPath(file.getPath());
                        image.setCompressed(true);
                        listener.onCompressSuccess(images);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onCompressFailed(images, e.getMessage() + " is compress failures");
                    }
                }).launch();
    }

    private void compressMulti() {
        final List<File> fileList = new ArrayList<>();
        for (File file : files) {
            Luban.get(context).load(file).putGear(options.getGear())
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(File file1) {
                            fileList.add(file1);
                            if (fileList.size() == files.size())
                                handleCompressCallBack(fileList);
                        }

                        @Override
                        public void onError(Throwable e) {
                            listener.onCompressFailed(images, e.getMessage() + " is compress failures");
                        }
                    });
        }

    }

    private void handleCompressCallBack(List<File> files) {
        for (int i = 0, j = images.size(); i < j; i++) {
            TImage image = images.get(i);
            image.setCompressed(true);
            image.setCompressPath(files.get(i).getPath());
        }
        listener.onCompressSuccess(images);
    }
}
