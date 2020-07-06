package com.kore.ai.widgetsdk.application;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class KaSecurePicassoRequestHandler extends RequestHandler {

    @Override public boolean canHandleRequest(Request data) {
        if(data.uri.toString().contains("http")){
            return false;
        }else if(!(new File(data.uri.getPath()).exists()) ){
            return false;
        }
        return true;
    }

    @Override
    public Result load(Request request, int networkPolicy) throws IOException {
        File file = new File(request.uri.getPath());
        FileInputStream fis = null;
        InputStream cis = null;
        if(file.exists()){
            fis = new FileInputStream(file);
            //cis = AppSandboxUtils.getCipheredInputStream(fis);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            fis.close();
            if (bitmap==null) {
                return null;
            }
            else {
                return new Result(bitmap, Picasso.LoadedFrom.DISK);
            }
        }
        return null;
    }
}
