package com.e.rssapplication.MainActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.e.rssapplication.DataBase.DatabaseHelper;
import com.e.rssapplication.DataBase.EnumTypeNews;
import com.e.rssapplication.DataBase.EnumWebSite;
import com.e.rssapplication.DataBase.MySharedPreferences;
import com.e.rssapplication.DataBase.News;
import com.e.rssapplication.R;
import com.e.rssapplication.WebActivity.WebActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawer;
    List<News> newsList;
    FloatingActionButton fab;
    ListView listView;
    AdapterItemNews adapterItemNews;
    DatabaseHelper databaseHelper;
    EnumWebSite enumWebSiteDefault;
    EnumTypeNews enumTypeNews;
    ImageView imageViewHeaderDrawer;
    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cultural);
        setTitle("Trang chủ");

        init();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadRss().execute(databaseHelper.getRssLink(enumTypeNews,enumWebSiteDefault).getLink());
            }
        });

        setClick();
    }

    void init() {
        databaseHelper = new DatabaseHelper(this);
        enumWebSiteDefault = EnumWebSite.valueOf(MySharedPreferences.getPrefDefaultWebsite(this));
        enumTypeNews = EnumTypeNews.HOMEPAGE;

        if (MySharedPreferences.getPrefFirstOpen(this)) {
            databaseHelper.initRssLink();
            MySharedPreferences.setPrefFirstOpen(this, false);
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            webView = findViewById(R.id.webview);
        }

        newsList = new ArrayList<>();

        fab = findViewById(R.id.fab);
        listView = findViewById(R.id.listView);

        adapterItemNews = new AdapterItemNews(this, newsList);
        listView.setAdapter(adapterItemNews);

        drawer = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(drawerToggle);

        NavigationView navigationView = findViewById(R.id.nav_view);
        imageViewHeaderDrawer = navigationView.getHeaderView(0).findViewById(R.id.imageView);
        setImageViewHeaderDrawer();
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawer.closeDrawers();

                        switch (menuItem.getItemId()){
                            case R.id.nav_home: {
                                enumTypeNews = EnumTypeNews.HOMEPAGE;
                                setTitle("Trang chủ");
                                break;
                            }
                            case R.id.nav_news: {
                                enumTypeNews = EnumTypeNews.NEWS;
                                setTitle("Thời sự");
                                break;
                            }
                            case R.id.nav_world: {
                                enumTypeNews = EnumTypeNews.WORLD;
                                setTitle("Thế giới");
                                break;
                            }
                            case R.id.nav_sport: {
                                enumTypeNews = EnumTypeNews.SPORT;
                                setTitle("Thể thao");
                                break;
                            }
                            case R.id.nav_science_and_technology: {
                                enumTypeNews = EnumTypeNews.SCIENCEANDTECHNOLOGY;
                                setTitle("Khoa học - Công nghệ");
                                break;
                            }
                            case R.id.nav_health: {
                                enumTypeNews = EnumTypeNews.HEALTH;
                                setTitle("Sức khỏe");
                                break;
                            }
                            case R.id.nav_economy: {
                                enumTypeNews = EnumTypeNews.ECONOMY;
                                setTitle("Kinh tế");
                                break;
                            }
                            case R.id.nav_law: {
                                enumTypeNews = EnumTypeNews.LAW;
                                setTitle("Luật pháp");
                                break;
                            }
                            case R.id.nav_cultural: {
                                enumTypeNews = EnumTypeNews.CULTURAL;
                                setTitle("Văn hóa");
                                break;
                            }
                            case R.id.nav_education: {
                                enumTypeNews = EnumTypeNews.EDUCATION;
                                setTitle("Giáo dục");
                                break;
                            }
                        }

                        new ReadRss().execute(databaseHelper.getRssLink(enumTypeNews,enumWebSiteDefault).getLink());

                        return true;
                    }
                });
    }

    void setClick(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    webView.getSettings().setJavaScriptEnabled(true);
                    // Load local HTML from url
                    webView.loadUrl(newsList.get(i).getLink());
                }else {
                    Intent intent = new Intent(MainActivity.this, WebActivity.class);
                    intent.putExtra("URL",newsList.get(i).getLink());
                    startActivity(intent);
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDialogSelectWeb();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void displayDialogSelectWeb() {
        final View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_select_website, null);
        CardView cardViewVnExpress = dialogLayout.findViewById(R.id.vnexpress);
        CardView cardViewThanhNien = dialogLayout.findViewById(R.id.thanhnien);
        CardView cardViewTuoiTre = dialogLayout.findViewById(R.id.tuoitre);
        CardView cardViewVtc = dialogLayout.findViewById(R.id.vtc);
        CardView cardViewCand = dialogLayout.findViewById(R.id.cand);
        CardView cardView24h = dialogLayout.findViewById(R.id.haitu);

        AlertDialog.Builder editDialog = new AlertDialog.Builder(this);
        editDialog.setView(dialogLayout);
        AlertDialog dialog = editDialog.create();

        switch (enumWebSiteDefault){
            case VNEXPRESS: cardViewVnExpress.setCardBackgroundColor(0xFF94D5E1); break;
            case THANHNIEN: cardViewThanhNien.setCardBackgroundColor(0xFF94D5E1); break;
            case TUOITRE: cardViewTuoiTre.setCardBackgroundColor(0xFF94D5E1); break;
            case HAITUH: cardView24h.setCardBackgroundColor(0xFF94D5E1); break;
            case VTC: cardViewVtc.setCardBackgroundColor(0xFF94D5E1); break;
            case CAND: cardViewCand.setCardBackgroundColor(0xFF94D5E1); break;
        }

        cardViewVnExpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Picasso.get().load(R.drawable.logo_vnexpress).into(imageViewHeaderDrawer);
                enumWebSiteDefault = EnumWebSite.VNEXPRESS;
                MySharedPreferences.setPrefDefaultWebsite(MainActivity.this,enumWebSiteDefault.name());
                new ReadRss().execute(databaseHelper.getRssLink(enumTypeNews,enumWebSiteDefault).getLink());
            }
        });

        cardViewThanhNien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Picasso.get().load(R.drawable.logo_thanhnien).into(imageViewHeaderDrawer);
                enumWebSiteDefault = EnumWebSite.THANHNIEN;
                MySharedPreferences.setPrefDefaultWebsite(MainActivity.this,enumWebSiteDefault.name());
                new ReadRss().execute(databaseHelper.getRssLink(enumTypeNews,enumWebSiteDefault).getLink());
            }
        });

        cardViewTuoiTre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Picasso.get().load(R.drawable.logo_tuoitre).into(imageViewHeaderDrawer);
                enumWebSiteDefault = EnumWebSite.TUOITRE;
                MySharedPreferences.setPrefDefaultWebsite(MainActivity.this,enumWebSiteDefault.name());
                new ReadRss().execute(databaseHelper.getRssLink(enumTypeNews,enumWebSiteDefault).getLink());
            }
        });

        cardViewVtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Picasso.get().load(R.drawable.logo_vtc).into(imageViewHeaderDrawer);
                enumWebSiteDefault = EnumWebSite.VTC;
                MySharedPreferences.setPrefDefaultWebsite(MainActivity.this,enumWebSiteDefault.name());
                new ReadRss().execute(databaseHelper.getRssLink(enumTypeNews,enumWebSiteDefault).getLink());
            }
        });

        cardViewCand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Picasso.get().load(R.drawable.logo_cand).into(imageViewHeaderDrawer);
                enumWebSiteDefault = EnumWebSite.CAND;
                MySharedPreferences.setPrefDefaultWebsite(MainActivity.this,enumWebSiteDefault.name());
                new ReadRss().execute(databaseHelper.getRssLink(enumTypeNews,enumWebSiteDefault).getLink());
            }
        });

        cardView24h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Picasso.get().load(R.drawable.logo_24h).into(imageViewHeaderDrawer);
                enumWebSiteDefault = EnumWebSite.HAITUH;
                MySharedPreferences.setPrefDefaultWebsite(MainActivity.this,enumWebSiteDefault.name());
                new ReadRss().execute(databaseHelper.getRssLink(enumTypeNews,enumWebSiteDefault).getLink());
            }
        });

        dialog.show();
    }

    void setImageViewHeaderDrawer(){
        switch (enumWebSiteDefault){
            case VNEXPRESS: Picasso.get().load(R.drawable.logo_vnexpress).into(imageViewHeaderDrawer); break;
            case THANHNIEN: Picasso.get().load(R.drawable.logo_thanhnien).into(imageViewHeaderDrawer); break;
            case TUOITRE: Picasso.get().load(R.drawable.logo_tuoitre).into(imageViewHeaderDrawer); break;
            case HAITUH: Picasso.get().load(R.drawable.logo_24h).into(imageViewHeaderDrawer); break;
            case VTC: Picasso.get().load(R.drawable.logo_vtc).into(imageViewHeaderDrawer); break;
            case CAND: Picasso.get().load(R.drawable.logo_cand).into(imageViewHeaderDrawer); break;
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class ReadRss extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();

                newsList.clear();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        @SuppressLint("SetJavaScriptEnabled")
        @Override
        protected void onPostExecute(String s) {
            DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
            Document doc = Jsoup.parse(s, "", Parser.xmlParser());

            Elements itemElements = doc.getElementsByTag("item");
            for (int i = 0; i < itemElements.size(); i++) {
                Element item = itemElements.get(i);
                String title = removeCdata(item.getElementsByTag("title").first().text());
                String pubDate = removeCdata(item.getElementsByTag("pubDate").first().text()).trim();
                pubDate = pubDate.substring(0, pubDate.lastIndexOf(" "));
                String link = removeCdata(item.getElementsByTag("link").first().text());
                Document descriptionDoc;
                if(enumWebSiteDefault.equals(EnumWebSite.VTC))
                    descriptionDoc = Jsoup.parse(removeCdata(item.getElementsByTag("description").first().text()));
                else
                    descriptionDoc = Jsoup.parse(removeCdata(item.getElementsByTag("description").first().toString().trim()));
                String imageLink = descriptionDoc.getElementsByTag("img").first()!=null?descriptionDoc.getElementsByTag("img").first().attr("src"):null;
                String description = descriptionDoc.text();

                News news = new News();
                news.setTitle(title);
                news.setDescription(description);
                news.setLink(link);
                news.setImage(imageLink);
                news.setTypeNews(enumTypeNews);
                news.setWebSite(enumWebSiteDefault);
                try {
                    news.setPubdate(formatter.parse(pubDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                newsList.add(news);
            }

            adapterItemNews.notifyDataSetChanged();
            listView.smoothScrollToPosition(0);

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setLoadWithOverviewMode(true);
                webView.getSettings().setUseWideViewPort(true);
                webView.setWebViewClient(new WebViewClient(){

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);

                        return true;
                    }
                    @Override
                    public void onPageFinished(WebView view, final String url) {
                    }
                });

                webView.loadUrl(newsList.get(0).getLink());
            }

            super.onPostExecute(s);
        }
    }

    String removeCdata(String data) {
        data = data.replace("<![CDATA[", "");
        data = data.replace("]]>", "");
        return data.trim();
    }
}