package com.e.rssapplication.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Rss_Manager";


    //    Các bảng dữ liệu
    private static final String TABLE_NEWS = "News";
    private static final String TABLE_RSS_LINK = "RssLink";


    //    Các trường dữ liệu bảng news
    private static final String COLUMN_NEWS_ID = "News_Id";
    private static final String COLUMN_NEWS_TITLE = "News_Title";
    private static final String COLUMN_NEWS_DESCRIPTION = "News_Description";
    private static final String COLUMN_NEWS_LINK = "News_Link";
    private static final String COLUMN_NEWS_IMAGE = "News_Image";
    private static final String COLUMN_NEWS_PUBDATE = "News_Pubdate";
    private static final String COLUMN_NEWS_TYPE = "News_Type";
    private static final String COLUMN_NEWS_WEB = "News_Web";
    private static final String COLUMN_NEWS_SAVED = "News_Saved";
    private static final String COLUMN_NEWS_PATH = "News_Path";


    //    Các trường dữ liệu bảng rss link
    private static final String COLUMN_RSS_LINK_ID = "Rss_Link_Id";
    private static final String COLUMN_RSS_LINK_TYPE = "Rss_Link_Type";
    private static final String COLUMN_RSS_LINK_WEB = "Rss_Link_Web";
    private static final String COLUMN_RSS_LINK_LINK = "Rss_Link_Link";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

//        Khởi tạo các bảng
        String scriptAssembly = "CREATE TABLE " + TABLE_NEWS + "("
                + COLUMN_NEWS_ID + " TEXT PRIMARY KEY,"
                + COLUMN_NEWS_TITLE + " TEXT,"
                + COLUMN_NEWS_DESCRIPTION + " TEXT,"
                + COLUMN_NEWS_LINK + " TEXT,"
                + COLUMN_NEWS_IMAGE + " TEXT,"
                + COLUMN_NEWS_PUBDATE + " INTEGER,"
                + COLUMN_NEWS_TYPE + " TEXT,"
                + COLUMN_NEWS_WEB + " TEXT,"
                + COLUMN_NEWS_SAVED + " INTEGER,"
                + COLUMN_NEWS_PATH + " TEXT"
                + ")";

        String scriptHistory = "CREATE TABLE " + TABLE_RSS_LINK + "("
                + COLUMN_RSS_LINK_ID + " TEXT PRIMARY KEY,"
                + COLUMN_RSS_LINK_TYPE + " TEXT,"
                + COLUMN_RSS_LINK_WEB + " TEXT,"
                + COLUMN_RSS_LINK_LINK + " TEXT"
                + ")";
        // Chạy script
        sqLiteDatabase.execSQL(scriptAssembly);
        sqLiteDatabase.execSQL(scriptHistory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

//        Nếu cập nhật lại DB, xóa các bảng và tạo lại DB
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RSS_LINK);
        onCreate(sqLiteDatabase);
    }


    //    Thêm một bản ghi news mới
    public void addNews(News news) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        news.setTitle(formatString(news.getTitle()));

//        Map các thuộc tính vào các trường giá trị
        ContentValues values = new ContentValues();
        values.put(COLUMN_NEWS_ID, news.getId());
        values.put(COLUMN_NEWS_TITLE, news.getTitle());
        if (news.getDescription() != null)
            values.put(COLUMN_NEWS_DESCRIPTION, news.getDescription());
        values.put(COLUMN_NEWS_LINK, news.getLink());
        if (news.getImage() != null)
            values.put(COLUMN_NEWS_IMAGE, news.getImage());
        if (news.getPubdate() != null)
            values.put(COLUMN_NEWS_PUBDATE, news.getPubdate().getTime());
        values.put(COLUMN_NEWS_TYPE, news.getTypeNews().name());
        values.put(COLUMN_NEWS_WEB, news.getWebSite().name());
        values.put(COLUMN_NEWS_SAVED, news.isSaved() ? 1 : 0);
        if (news.isSaved() && news.getPath() != null)
            values.put(COLUMN_NEWS_PATH, news.getPath());


//        Câu lệnh insret
        sqLiteDatabase.insert(TABLE_NEWS, null, values);
        sqLiteDatabase.close();
    }

    //    Sửa một bản ghi news
    public int updateNews(News news) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        news.setTitle(formatString(news.getTitle()));

//        Map các thuộc tính vào các trường giá trị
//        Các if để check có giá trị mới đưa vào
        ContentValues values = new ContentValues();
        values.put(COLUMN_NEWS_TITLE, news.getTitle());
        if (news.getDescription() != null)
            values.put(COLUMN_NEWS_DESCRIPTION, news.getDescription());
        values.put(COLUMN_NEWS_LINK, news.getLink());
        if (news.getImage() != null)
            values.put(COLUMN_NEWS_IMAGE, news.getImage());
        if (news.getPubdate() != null)
            values.put(COLUMN_NEWS_PUBDATE, news.getPubdate().getTime());
        values.put(COLUMN_NEWS_TYPE, news.getTypeNews().name());
        values.put(COLUMN_NEWS_WEB, news.getWebSite().name());
        values.put(COLUMN_NEWS_SAVED, news.isSaved() ? 1 : 0);
        if (news.isSaved() && news.getPath() != null)
            values.put(COLUMN_NEWS_PATH, news.getPath());

        // updating row
        return sqLiteDatabase.update(TABLE_NEWS, values, COLUMN_NEWS_ID + " = ?",
                new String[]{String.valueOf(news.getId())});
    }

//    Xóa một bản ghi tin tức news
    public void deleteNews(News news) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NEWS, COLUMN_NEWS_ID + " = ?",
                new String[] { String.valueOf(news.getId()) });
        db.close();
    }

    //    Thêm một bản ghi news mới
    public News getNewsByTitleAndType(String title, EnumWebSite enumWebSite) {
        News news = null;

        title = formatString(title);
//        Câu lệnh query
        String selectQuery = "SELECT  * FROM " + TABLE_NEWS + " WHERE UPPER(" + COLUMN_NEWS_TITLE + ") = '" + title.toUpperCase() + "' AND " + COLUMN_NEWS_WEB + " = '" + enumWebSite.name() + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

//        Đọc từng bản ghi
        if (cursor.moveToFirst()) {
            do {
                news = new News();
                news.setId(cursor.getString(0));
                news.setTitle(cursor.getString(1));
                news.setDescription(cursor.getString(2));
                news.setLink(cursor.getString(3));
                news.setImage(cursor.getString(4));
                news.setPubdate(new Date(cursor.getLong(5)));
                news.setTypeNews(EnumTypeNews.valueOf(cursor.getString(6)));
                news.setWebSite(EnumWebSite.valueOf(cursor.getString(7)));
                news.setSaved(cursor.getInt(8) == 1);
                news.setPath(cursor.getString(9));

            } while (cursor.moveToNext());
        }

        db.close();
        //Trả về kết quả
        return news;
    }

    //    Lấy tất cả tin tức news đã lưu
    public List<News> getAllNews() {
        List<News> newsList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NEWS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                News news = new News();
                news.setId(cursor.getString(0));
                news.setTitle(cursor.getString(1));
                news.setDescription(cursor.getString(2));
                news.setLink(cursor.getString(3));
                news.setImage(cursor.getString(4));
                news.setPubdate(new Date(cursor.getLong(5)));
                news.setTypeNews(EnumTypeNews.valueOf(cursor.getString(6)));
                news.setWebSite(EnumWebSite.valueOf(cursor.getString(7)));
                news.setSaved(cursor.getInt(8) == 1);
                news.setPath(cursor.getString(9));

                newsList.add(news);
            } while (cursor.moveToNext());
        }

        db.close();
        //Trả về kết quả
        return newsList;
    }


    //    Lấy tin tức theo website
    public List<News> getNewsByWebsite(EnumWebSite webSite) {
        List<News> newsList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NEWS + " WHERE " + COLUMN_NEWS_WEB + " = '" + webSite.name() + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                News news = new News();
                news.setId(cursor.getString(0));
                news.setTitle(cursor.getString(1));
                news.setDescription(cursor.getString(2));
                news.setLink(cursor.getString(3));
                news.setImage(cursor.getString(4));
                news.setPubdate(new Date(cursor.getLong(5)));
                news.setTypeNews(EnumTypeNews.valueOf(cursor.getString(6)));
                news.setWebSite(EnumWebSite.valueOf(cursor.getString(7)));
                news.setSaved(cursor.getInt(8) == 1);
                news.setPath(cursor.getString(9));

                newsList.add(news);
            } while (cursor.moveToNext());
        }

        db.close();
        //Trả về kết quả
        return newsList;
    }

    //    Thêm bản ghi rss link
    public void addRssLink(RssLink rssLink) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //        Map các thuộc tính vào các trường giá trị

        ContentValues values = new ContentValues();
        values.put(COLUMN_RSS_LINK_ID, rssLink.getId());
        values.put(COLUMN_RSS_LINK_TYPE, rssLink.getEnumTypeNews().name());
        values.put(COLUMN_RSS_LINK_WEB, rssLink.getWebSite().name());
        values.put(COLUMN_RSS_LINK_LINK, rssLink.getLink());

//        Câu lệnh insert
        sqLiteDatabase.insert(TABLE_RSS_LINK, null, values);

        sqLiteDatabase.close();
    }


    //    Lấy link rss ra
    public RssLink getRssLink(EnumTypeNews enumTypeNews, EnumWebSite enumWebSite) {

        RssLink rssLink = null;

//        Câu lệnh query
        String selectQuery = "SELECT  * FROM " + TABLE_RSS_LINK + " WHERE " + COLUMN_RSS_LINK_TYPE + " = '" + enumTypeNews.name() + "' AND " + COLUMN_RSS_LINK_WEB + " = '" + enumWebSite.name() + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

//        Đọc từng bản ghi
        if (cursor.moveToFirst()) {
            do {
                rssLink = new RssLink();
                rssLink.setId(cursor.getString(0));
                rssLink.setEnumTypeNews(EnumTypeNews.valueOf(cursor.getString(1)));
                rssLink.setWebSite(EnumWebSite.valueOf(cursor.getString(2)));
                rssLink.setLink(cursor.getString(3));
            } while (cursor.moveToNext());
        }

        //Trả về kết quả
        return rssLink;
    }


    //    Khởi tạo cơ sở dữ liệu link rss của các trang báo
//    Chỉ được chạy lần đầu
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


//        Gọi thêm dữ liệu sau khi đã có dánh sách các link
        for (RssLink rssLink : rssLinkList) {
            addRssLink(rssLink);
        }
    }


//    Định dạng vài kí tự đặc biệt như ' - tránh làm sai câu truy vấn sql bằng cách thay hết bằng " hoặc xóa
    private String formatString(String text) {
        text = text.replace("'", "\"");
        text = text.replace("-", "");
        return text;
    }

}
