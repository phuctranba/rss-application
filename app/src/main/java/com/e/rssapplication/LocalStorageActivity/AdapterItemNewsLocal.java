package com.e.rssapplication.LocalStorageActivity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.e.rssapplication.DataBase.News;
import com.e.rssapplication.DataBase.Utilities;
import com.e.rssapplication.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//Adapter cài đặt giao diện từng dòng tin
public class AdapterItemNewsLocal extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
//    List danh sách tin đầu vào
    private List<News> data;

    public AdapterItemNewsLocal(Context context, List<News> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private static class ViewHolder {
        private TextView textViewDescription; /*Chữ mô tả*/
        private TextView textViewTitle; /*Tiêu đề tin*/
        private TextView textViewDate; /* Ngày đăng tin*/
        private ImageView imageViewThumbnail;   /*Ảnh thumnbail, thu nhỏ*/
        private TextView textViewTypeNews;  /*Loại tin*/
        private TextView textViewTypeStorage;  /* Loại lưu trữ, có thể đọc online hay offline*/
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_news_local, null);
            viewHolder = new ViewHolder();
            viewHolder.textViewDescription = (TextView) view.findViewById(R.id.description);
            viewHolder.textViewTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.textViewDate = (TextView) view.findViewById(R.id.date);
            viewHolder.textViewTypeNews = (TextView) view.findViewById(R.id.typeNews);
            viewHolder.textViewTypeStorage = (TextView) view.findViewById(R.id.typeStorage);
            viewHolder.imageViewThumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        News news = data.get(i);

//        Cài giá trị của tin vào giao diện

//        Set tiêu đề
        viewHolder.textViewTitle.setText(news.getTitle());
//        Set mô tả
        viewHolder.textViewDescription.setText(news.getDescription()!=null?news.getDescription():"");
//        Set ngày tạo
        viewHolder.textViewDate.setText(news.getPubdate()!=null?convertDateToString(news.getPubdate()):"");
//        Set loại tin
        viewHolder.textViewTypeNews.setText(news.getTypeNews().toString());
//        Set loại lưu trữ
        viewHolder.textViewTypeStorage.setText(news.isSaved()?"Offline":"Online");
        viewHolder.textViewTypeStorage.setTextColor(news.isSaved()? Color.RED:Color.GREEN);

//        Set hình ảnh
        if (news.getImage() != null) {
//            Nếu có ảnh thì hiện
//            Picasso.get().load(news.getImage()).into(viewHolder.imageViewThumbnail);
            Picasso.get().load(new File(news.getImage()))
                    .into(viewHolder.imageViewThumbnail);
        }
        else {
//            Nếu tin không có ảnh thì load ảnh của báo
            Picasso.get().load(Utilities.logoWeb(news.getWebSite())).into(viewHolder.imageViewThumbnail);
        }

//        Cài đặt hiệu ứng fade (hiện dần) cho item list
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_list);
        view.startAnimation(animation);

        return view;
    }

//    Format ngày để hiển thị
    public String convertDateToString(Date date) {
        String pattern = "HH:mm dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }
}
