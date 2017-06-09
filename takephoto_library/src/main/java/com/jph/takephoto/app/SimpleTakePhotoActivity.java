package com.jph.takephoto.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoModel;
import com.jph.takephoto.model.TakePhotoOptions;
import com.jph.takephoto.uitl.TConstant;

import java.io.File;
import java.util.ArrayList;

import top.zibin.luban.Luban;


/**
 * @author wangkuan
 *         通过继承的方式实现拍照功能。
 */
public class SimpleTakePhotoActivity extends TakePhotoActivity {

    private TakePhoto takePhoto;

    private Uri imageUri = null;
    private TakePhotoModel takePhotoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        takePhoto = getTakePhoto();

        takePhotoModel = (TakePhotoModel) getIntent().getSerializableExtra(TConstant._entity);
        if (null == takePhotoModel) {
            takePhotoModel = new TakePhotoModel();
            takePhotoModel.setTakePhoto(true);
        }

        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        imageUri = Uri.fromFile(file);

        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);
        //
        if (takePhotoModel.isTakePhoto()) {
            takePhoto.onPickFromCapture(imageUri);
        } else {
            takePhoto.onPickMultiple(takePhotoModel.getLimit());

        }
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        ProgressDialog dialog = takePhoto.getWailLoadDialog();
        if (null != dialog) {
            dialog.dismiss();
        }
        TImage tImage = result.getImage();
        TakePhotoModel takePhotoModel = new TakePhotoModel();
        takePhotoModel.setCompressPath(tImage.getCompressPath());
        takePhotoModel.setOriginalPath(tImage.getOriginalPath());

        ArrayList<TImage> images = result.getImages();
        for (TImage image : images) {
            //只保存压缩后的图片
            takePhotoModel.addImages(image.getCompressPath());
        }

        Intent intent = new Intent();
        intent.putExtra(TConstant._entity, takePhotoModel);
        setResult(TConstant.takePhotoSuccessResult, intent);
        finish();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        finish();
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
        finish();
    }

    /**
     * 拍照option
     *
     * @param takePhoto
     */
    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(true);
        takePhoto.setTakePhotoOptions(builder.create());
    }

    /**
     * 设置压缩图片
     *
     * @param takePhoto
     */
    private void configCompress(TakePhoto takePhoto) {
        LubanOptions option = new LubanOptions.Builder()
                .setGear(Luban.THIRD_GEAR)
                .create();
        CompressConfig config = CompressConfig.ofLuban(option);
        config.enableReserveRaw(true);
        takePhoto.onEnableCompress(config, true);
    }
}
