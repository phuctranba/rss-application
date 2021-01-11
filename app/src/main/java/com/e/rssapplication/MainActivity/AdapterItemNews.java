package com.e.rssapplication.MainActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.e.rssapplication.DataBase.EnumWebSite;
import com.e.rssapplication.DataBase.News;
import com.e.rssapplication.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterItemNews extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<News> data;

    public AdapterItemNews(Context context, List<News> data) {
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
        public TextView textViewDescription;
        public TextView textViewTitle;
        public TextView textViewDate;
        public ImageView imageViewThumbnail;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_news, null);
            viewHolder = new ViewHolder();
            viewHolder.textViewDescription = (TextView) view.findViewById(R.id.description);
            viewHolder.textViewTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.textViewDate = (TextView) view.findViewById(R.id.date);
            viewHolder.imageViewThumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        News news = data.get(i);


        viewHolder.textViewTitle.setText(news.getTitle());
        viewHolder.textViewDescription.setText(news.getPubdate()!=null?news.getDescription():"");
        viewHolder.textViewDate.setText(news.getPubdate()!=null?convertDateToString(news.getPubdate()):"");

        if (news.getImage() != null)
            Picasso.get().load(news.getImage()).into(viewHolder.imageViewThumbnail);
        else
            Picasso.get().load(logoWeb(news.getWebSite())).into(viewHolder.imageViewThumbnail);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_list);
        view.startAnimation(animation);

        return view;
    }

    public String convertDateToString(Date date) {
        String pattern = "HH:mm dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    private int logoWeb(EnumWebSite webSite){
        switch (webSite){
            case THANHNIEN: return R.drawable.logo_thanhnien;
            case TUOITRE: return R.drawable.logo_tuoitre;
            case CAND: return R.drawable.logo_cand;
            case HAITUH: return R.drawable.logo_24h;
            case VTC: return R.drawable.logo_vtc;
            default: return R.drawable.logo_vnexpress;
        }
    }
}
