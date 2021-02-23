package com.e.rssapplication.LocalStorageActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.e.rssapplication.DataBase.DatabaseHelper;
import com.e.rssapplication.DataBase.EnumWebSite;
import com.e.rssapplication.DataBase.News;
import com.e.rssapplication.R;
import com.e.rssapplication.WebActivity.WebActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Fragment chứa nội dung danh sách các tin từng tab
public class LocalNewsFragment extends Fragment {

//    Khai báo các công cụ cần thiết
    EnumWebSite enumWebSite = EnumWebSite.CAND; /*Website tin cần hiện*/
    ListView listView;
    TextView textViewEmpty;
    AdapterItemNewsLocal adapterItemNewsLocal;
    List<News> newsList;    /*Danh sách tin*/
    DatabaseHelper databaseHelper;  /*Biến thao tác cơ sở dữ liệu*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_local_news, container, false);
        if (getArguments() != null) {
            enumWebSite =(EnumWebSite) getArguments().getSerializable("WEBSITE");
        }

        Init(rootView);

        return rootView;
    }

//    Ánh xạ các thành phần
    private void Init(View rootView){
        listView = rootView.findViewById(R.id.listView);
        textViewEmpty = rootView.findViewById(R.id.emptyView);
        listView.setEmptyView(textViewEmpty);
        newsList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(getActivity());

//        Lấy giá trị danh sách các tin của website đã lưu
        newsList = databaseHelper.getNewsByWebsite(enumWebSite);

        adapterItemNewsLocal = new AdapterItemNewsLocal(getActivity(), newsList);
        listView.setAdapter(adapterItemNewsLocal);

//        Xử lý sự kiện ấn vào tin, mở web lên để đọc
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("ITEM",newsList.get(i));    /*Gửi thông tin tin sang web*/
                startActivity(intent);
            }
        });

//        Xử lý sự kiện khi nhấn giữ vào một dòng
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Bạn chắc chắn muốn xóa tin này?")
                        .setCancelable(false)
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(newsList.get(i).getPath()!=null){
                                    File file = new File(newsList.get(i).getPath());
                                    file.delete();
                                    if(file.exists()){
                                        try {
                                            file.getCanonicalFile().delete();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        if(file.exists()){
                                            getActivity().getApplicationContext().deleteFile(file.getName());
                                        }
                                    }
                                }

                                databaseHelper.deleteNews(newsList.get(i));
                                newsList.remove(i);
                                adapterItemNewsLocal.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Hủy bỏ", null)
                        .show();
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        newsList.clear();
        newsList.addAll(databaseHelper.getNewsByWebsite(enumWebSite));
        adapterItemNewsLocal.notifyDataSetChanged();
    }
}