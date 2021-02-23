package com.e.rssapplication.WebActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.e.rssapplication.DataBase.DatabaseHelper;
import com.e.rssapplication.DataBase.News;
import com.e.rssapplication.DataBase.Utilities;
import com.e.rssapplication.R;

import java.io.File;
import java.util.UUID;

public class WebActivity extends AppCompatActivity {

    WebView webView;
    boolean saveable = false, wannaSave = false, saveTask = true;
    News news, newsDB;
    ProgressDialog progressDialog;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Tin tức");

        databaseHelper = new DatabaseHelper(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đang lưu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Hủy lưu offline", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(WebActivity.this, "Tin đã được lưu để đọc online", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        AndroidWebClient client = new AndroidWebClient();
        webView.setWebViewClient(client);

        loadData();
    }

    private void loadData() {
        news = (News) getIntent().getSerializableExtra("ITEM");

        newsDB = databaseHelper.getNewsByTitleAndType(news.getTitle(), news.getWebSite());

        if (newsDB != null) {
            news = newsDB;
        }

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting()) {
            webView.loadUrl(news.getLink());
        } else {
            if (news.isSaved()) {
                webView.loadUrl(news.getPath());
                Toast.makeText(WebActivity.this, "Không có kết nối internet, hiển thị tin đã lưu!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(WebActivity.this, "Không có kết nối internet, thử lại sau!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void savePage() {
        new saveImage().execute(news.getImage(),news.getWebSite().name() + news.getId() + ".jpg");
        news.setImage(new File(Utilities.ROOT_DIR_STORAGE_PICTURE_CACHE, news.getWebSite().name() + news.getId() + ".jpg").getPath());
        saveTask = false;
        wannaSave = true;
        if (!progressDialog.isShowing())
            progressDialog.show();

        if (news.getPath() != null && news.isSaved()) {
            Toast.makeText(WebActivity.this, "Tin đã được lưu có thể đọc offline", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            saveTask = true;
        } else {
            if (saveable) {
                if (newsDB == null) {
                    news.setId(UUID.randomUUID().toString());
                    webView.saveWebArchive(Utilities.ROOT_DIR_STORAGE_HTML_CACHE + "/" + news.getWebSite().name() + news.getId() + ".mht");
                    news.setPath("file:///" + Utilities.ROOT_DIR_STORAGE_HTML_CACHE + "/" + news.getWebSite().name() + news.getId() + ".mht");
                    news.setSaved(true);
                    databaseHelper.addNews(news);
                } else {
                    if (news.getPath() == null && !news.isSaved()) {
                        webView.saveWebArchive(Utilities.ROOT_DIR_STORAGE_HTML_CACHE + "/" + news.getWebSite().name() + news.getId() + ".mht");
                        news.setPath("file:///" + Utilities.ROOT_DIR_STORAGE_HTML_CACHE + "/" + news.getWebSite().name() + news.getId() + ".mht");
                        news.setSaved(true);
                        databaseHelper.updateNews(news);
                    }
                }
                Toast.makeText(WebActivity.this, "Tin đã được lưu có thể đọc offline", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                saveTask = true;
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_save:
                savePage();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class AndroidWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url,
                                  android.graphics.Bitmap favicon) {
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            saveable = true;
            if (!isFinishing()) {
                if (wannaSave)
                    savePage();
            }
        }

        public void onLoadResource(WebView view, String url) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!saveTask) {
            News newsDB = databaseHelper.getNewsByTitleAndType(news.getTitle(), news.getWebSite());

            if (newsDB == null) {
                news.setId(UUID.randomUUID().toString());
                databaseHelper.addNews(news);
            }
        }
    }

    private class saveImage extends AsyncTask<String, String, String> {

        protected String doInBackground(String... strings) {
            return Utilities.saveImage(strings[0], strings[1]);
        }
    }

}