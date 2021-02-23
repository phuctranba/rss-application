package com.e.rssapplication.LocalStorageActivity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.e.rssapplication.R;
import com.google.android.material.tabs.TabLayout;

public class LocalStorageActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_storage);

        Init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(R.string.title_localstorage);

    }

    private void Init() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(new LocalNewsFragment(), getString(R.string.web_name_cand));
        tabAdapter.addFragment(new LocalNewsFragment(), getString(R.string.web_name_haituh));
        tabAdapter.addFragment(new LocalNewsFragment(), getString(R.string.web_name_vnexpress));
        tabAdapter.addFragment(new LocalNewsFragment(), getString(R.string.web_name_vtc));
        tabAdapter.addFragment(new LocalNewsFragment(), getString(R.string.web_name_thanhnien));
        tabAdapter.addFragment(new LocalNewsFragment(), getString(R.string.web_name_tuoitre));

        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorPrimary));
        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.colorAccent), ContextCompat.getColor(this, R.color.colorPrimary));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setInlineLabel(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}