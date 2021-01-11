package com.e.rssapplication.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Note_Manager";

    private static final String TABLE_NEWS = "News";
    private static final String TABLE_HTML_CONTENT = "HtmlContent";
    private static final String TABLE_RSS_LINK = "RssLink";

    private static final String COLUMN_NEWS_ID = "News_Id";
    private static final String COLUMN_NEWS_TITLE = "News_Title";
    private static final String COLUMN_NEWS_DESCRIPTION = "News_Description";
    private static final String COLUMN_NEWS_LINK = "News_Link";
    private static final String COLUMN_NEWS_IMAGE = "News_Image";
    private static final String COLUMN_NEWS_PUBDATE = "News_Pubdate";
    private static final String COLUMN_NEWS_TYPE = "News_Type";
    private static final String COLUMN_NEWS_WEB = "News_Web";

    private static final String COLUMN_RSS_LINK_ID = "Rss_Link_Id";
    private static final String COLUMN_RSS_LINK_TYPE = "Rss_Link_Type";
    private static final String COLUMN_RSS_LINK_WEB = "Rss_Link_Web";
    private static final String COLUMN_RSS_LINK_LINK = "Rss_Link_Link";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String scriptAssembly = "CREATE TABLE " + TABLE_NEWS + "("
                + COLUMN_NEWS_ID + " TEXT PRIMARY KEY,"
                + COLUMN_NEWS_TITLE + " TEXT,"
                + COLUMN_NEWS_DESCRIPTION + " TEXT,"
                + COLUMN_NEWS_LINK + " TEXT,"
                + COLUMN_NEWS_IMAGE + " TEXT,"
                + COLUMN_NEWS_PUBDATE + " INTEGER,"
                + COLUMN_NEWS_TYPE + " TEXT,"
                + COLUMN_NEWS_WEB + " TEXT"
                + ")";

        String scriptHistory = "CREATE TABLE " + TABLE_RSS_LINK + "("
                + COLUMN_RSS_LINK_ID + " TEXT PRIMARY KEY,"
                + COLUMN_RSS_LINK_TYPE + " TEXT,"
                + COLUMN_RSS_LINK_WEB + " TEXT,"
                + COLUMN_RSS_LINK_LINK + " TEXT"
                + ")";
        // Execute Script.
        sqLiteDatabase.execSQL(scriptAssembly);
        sqLiteDatabase.execSQL(scriptHistory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RSS_LINK);
        onCreate(sqLiteDatabase);
    }

    public void addNews(News news) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NEWS_ID, news.getId());
        values.put(COLUMN_NEWS_TITLE, news.getTitle());
        values.put(COLUMN_NEWS_DESCRIPTION, news.getDescription());
        values.put(COLUMN_NEWS_LINK, news.getLink());
        values.put(COLUMN_NEWS_IMAGE, news.getImage());
        if (news.getPubdate() != null)
            values.put(COLUMN_NEWS_PUBDATE, news.getPubdate().getTime());
        values.put(COLUMN_NEWS_TYPE, news.getTypeNews().name());
        values.put(COLUMN_NEWS_WEB, news.getWebSite().name());

        sqLiteDatabase.insert(TABLE_NEWS, null, values);

        sqLiteDatabase.close();
    }

    public void addRssLink(RssLink rssLink) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_RSS_LINK_ID, rssLink.getId());
        values.put(COLUMN_RSS_LINK_TYPE, rssLink.getEnumTypeNews().name());
        values.put(COLUMN_RSS_LINK_WEB, rssLink.getWebSite().name());
        values.put(COLUMN_RSS_LINK_LINK, rssLink.getLink());

        sqLiteDatabase.insert(TABLE_RSS_LINK, null, values);

        sqLiteDatabase.close();
    }

    public RssLink getRssLink(EnumTypeNews enumTypeNews, EnumWebSite enumWebSite) {

        RssLink rssLink = null;
        String selectQuery = "SELECT  * FROM " + TABLE_RSS_LINK + " WHERE " + COLUMN_RSS_LINK_TYPE + " = '" + enumTypeNews.name() + "' AND " + COLUMN_RSS_LINK_WEB + " = '" + enumWebSite.name() + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                rssLink = new RssLink();
                rssLink.setId(cursor.getString(0));
                rssLink.setEnumTypeNews(EnumTypeNews.valueOf(cursor.getString(1)));
                rssLink.setWebSite(EnumWebSite.valueOf(cursor.getString(2)));
                rssLink.setLink(cursor.getString(3));
            } while (cursor.moveToNext());
        }

        return rssLink;
    }

    public void initRssLink() {
        List<RssLink> rssLinkList = new ArrayList<>();

//        thanhnien.vn
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.HOMEPAGE, EnumWebSite.THANHNIEN, "https://thanhnien.vn/rss/home.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.NEWS, EnumWebSite.THANHNIEN, "https://thanhnien.vn/rss/thoi-su.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.WORLD, EnumWebSite.THANHNIEN, "https://thanhnien.vn/rss/the-gioi.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.ECONOMY, EnumWebSite.THANHNIEN, "https://thanhnien.vn/rss/tai-chinh-kinh-doanh.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.LAW, EnumWebSite.THANHNIEN, "https://thanhnien.vn/rss/thoi-su/phap-luat.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.CULTURAL, EnumWebSite.THANHNIEN, "https://thanhnien.vn/rss/van-hoa.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.EDUCATION, EnumWebSite.THANHNIEN, "https://thanhnien.vn/rss/giao-duc.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.SPORT, EnumWebSite.THANHNIEN, "https://thethao.thanhnien.vn/rss/home.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.SCIENCEANDTECHNOLOGY, EnumWebSite.THANHNIEN, "https://thanhnien.vn/rss/cong-nghe.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.HEALTH, EnumWebSite.THANHNIEN, "https://thanhnien.vn/rss/suc-khoe.rss"));

//        tuoitre.vn
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.HOMEPAGE, EnumWebSite.TUOITRE, "https://tuoitre.vn/rss/tin-moi-nhat.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.NEWS, EnumWebSite.TUOITRE, "https://tuoitre.vn/rss/thoi-su.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.WORLD, EnumWebSite.TUOITRE, "https://tuoitre.vn/rss/the-gioi.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.ECONOMY, EnumWebSite.TUOITRE, "https://tuoitre.vn/rss/kinh-doanh.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.LAW, EnumWebSite.TUOITRE, "https://tuoitre.vn/rss/phap-luat.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.CULTURAL, EnumWebSite.TUOITRE, "https://tuoitre.vn/rss/van-hoa.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.EDUCATION, EnumWebSite.TUOITRE, "https://tuoitre.vn/rss/giao-duc.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.SPORT, EnumWebSite.TUOITRE, "https://tuoitre.vn/rss/the-thao.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.SCIENCEANDTECHNOLOGY, EnumWebSite.TUOITRE, "https://tuoitre.vn/rss/khoa-hoc.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.HEALTH, EnumWebSite.TUOITRE, "https://tuoitre.vn/rss/suc-khoe.rss"));

        //        vnexpress.net
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.HOMEPAGE, EnumWebSite.VNEXPRESS, "https://vnexpress.net/rss/tin-moi-nhat.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.NEWS, EnumWebSite.VNEXPRESS, "https://vnexpress.net/rss/thoi-su.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.WORLD, EnumWebSite.VNEXPRESS, "https://vnexpress.net/rss/the-gioi.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.ECONOMY, EnumWebSite.VNEXPRESS, "https://vnexpress.net/rss/kinh-doanh.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.LAW, EnumWebSite.VNEXPRESS, "https://vnexpress.net/rss/phap-luat.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.CULTURAL, EnumWebSite.VNEXPRESS, "https://vnexpress.net/rss/gia-dinh.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.EDUCATION, EnumWebSite.VNEXPRESS, "https://vnexpress.net/rss/giao-duc.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.SPORT, EnumWebSite.VNEXPRESS, "https://vnexpress.net/rss/the-thao.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.SCIENCEANDTECHNOLOGY, EnumWebSite.VNEXPRESS, "https://vnexpress.net/rss/khoa-hoc.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.HEALTH, EnumWebSite.VNEXPRESS, "https://vnexpress.net/rss/suc-khoe.rss"));

        //        vtc.vn
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.HOMEPAGE, EnumWebSite.VTC, "https://vtc.vn/rss/feed.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.NEWS, EnumWebSite.VTC, "https://vtc.vn/rss/thoi-su.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.WORLD, EnumWebSite.VTC, "https://vtc.vn/rss/the-gioi.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.ECONOMY, EnumWebSite.VTC, "https://vtc.vn/rss/kinh-te.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.LAW, EnumWebSite.VTC, "https://vtc.vn/rss/phap-luat.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.CULTURAL, EnumWebSite.VTC, "https://vtc.vn/rss/gioi-tre.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.EDUCATION, EnumWebSite.VTC, "https://vtc.vn/rss/giao-duc.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.SPORT, EnumWebSite.VTC, "https://vtc.vn/rss/the-thao.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.SCIENCEANDTECHNOLOGY, EnumWebSite.VTC, "https://vtc.vn/rss/khoa-hoc-cong-nghe.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.HEALTH, EnumWebSite.VTC, "https://vtc.vn/rss/suc-khoe.rss"));

        //        cand.vn
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.HOMEPAGE, EnumWebSite.CAND, "http://www.cand.com.vn/rss/trang-chu/"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.NEWS, EnumWebSite.CAND, "http://www.cand.com.vn/rss/thoi-su/"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.WORLD, EnumWebSite.CAND, "http://cand.com.vn/rss/Xa-hoi/"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.ECONOMY, EnumWebSite.CAND, "http://www.cand.com.vn/rss/Kinh-te/"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.LAW, EnumWebSite.CAND, "http://www.cand.com.vn/rss/Phap-luat/"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.CULTURAL, EnumWebSite.CAND, "http://www.cand.com.vn/rss/van-hoa/"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.EDUCATION, EnumWebSite.CAND, "http://www.cand.com.vn/rss/giao-duc/"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.SPORT, EnumWebSite.CAND, "http://www.cand.com.vn/rss/The-thao/"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.SCIENCEANDTECHNOLOGY, EnumWebSite.CAND, "http://www.cand.com.vn/rss/Khoa-hoc-Quan-su/"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.HEALTH, EnumWebSite.CAND, "http://www.cand.com.vn/rss/y-te/"));

        //        24h.com.vn
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.HOMEPAGE, EnumWebSite.HAITUH, "https://cdn.24h.com.vn/upload/rss/trangchu24h.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.NEWS, EnumWebSite.HAITUH, "https://cdn.24h.com.vn/upload/rss/tintuctrongngay.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.WORLD, EnumWebSite.HAITUH, "https://cdn.24h.com.vn/upload/rss/tintuctrongngay.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.ECONOMY, EnumWebSite.HAITUH, "https://cdn.24h.com.vn/upload/rss/taichinhbatdongsan.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.LAW, EnumWebSite.HAITUH, "https://cdn.24h.com.vn/upload/rss/anninhhinhsu.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.CULTURAL, EnumWebSite.HAITUH, "https://cdn.24h.com.vn/upload/rss/bantrecuocsong.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.EDUCATION, EnumWebSite.HAITUH, "https://cdn.24h.com.vn/upload/rss/giaoducduhoc.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.SPORT, EnumWebSite.HAITUH, "https://cdn.24h.com.vn/upload/rss/thethao.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.SCIENCEANDTECHNOLOGY, EnumWebSite.HAITUH, "https://cdn.24h.com.vn/upload/rss/congnghethongtin.rss"));
        rssLinkList.add(new RssLink(UUID.randomUUID().toString(), EnumTypeNews.HEALTH, EnumWebSite.HAITUH, "https://cdn.24h.com.vn/upload/rss/suckhoedoisong.rss"));

        for (RssLink rssLink : rssLinkList) {
            addRssLink(rssLink);
        }
    }


}
