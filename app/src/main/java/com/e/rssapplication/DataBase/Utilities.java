package com.e.rssapplication.DataBase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.e.rssapplication.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;

public class Utilities {

//    Chỗ lưu ảnh thumbnail của tin khi lưu đọc offline
    @SuppressLint("SdCardPath")
    public static final String ROOT_DIR_STORAGE_PICTURE_CACHE = "/data/data/com.e.rssapplication/cache/picture_cache";

//    Chỗ lưu nội dung tin tức khi lưu đọc offline
    @SuppressLint("SdCardPath")
    public static final String ROOT_DIR_STORAGE_HTML_CACHE = "/data/data/com.e.rssapplication/cache/html_cache";;

//    Hàm xuất logo với website tương ứng
    public static int logoWeb(EnumWebSite webSite){
        switch (webSite){
            case THANHNIEN: return R.drawable.logo_thanhnien;
            case TUOITRE: return R.drawable.logo_tuoitre;
            case CAND: return R.drawable.logo_cand;
            case HAITUH: return R.drawable.logo_24h;
            case VTC: return R.drawable.logo_vtc;
            default: return R.drawable.logo_vnexpress;
        }
    }

    public static String saveImage(String urlDownload,String nameFile) {

        try {
            File rootFile = new File(Utilities.ROOT_DIR_STORAGE_PICTURE_CACHE);
            if (!rootFile.exists())
                rootFile.mkdir();
            File pathFile = new File(Utilities.ROOT_DIR_STORAGE_PICTURE_CACHE, nameFile);
            URL url = new URL(urlDownload);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.connect();
            FileOutputStream f = new FileOutputStream(pathFile);
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();

            return pathFile.getPath();
        } catch (IOException e) {
            Log.d("Error....", e.toString());
        }
        return null;
    }
}
