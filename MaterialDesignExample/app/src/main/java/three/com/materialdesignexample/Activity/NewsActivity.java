package three.com.materialdesignexample.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.umeng.analytics.MobclickAgent;

import three.com.materialdesignexample.CallBack;
import three.com.materialdesignexample.Db.Db;
import three.com.materialdesignexample.Models.News;
import three.com.materialdesignexample.R;
import three.com.materialdesignexample.Util.HandleResponseUtil;
import three.com.materialdesignexample.Util.HttpUtil;

/**
 * Created by Administrator on 2015/10/9.
 */
public class NewsActivity extends AppCompatActivity {

    public  TextView newsTv;
    private WebView newsWebView;
    private ScrollView scrollView;
    private static News mNews;
    private final static String EXTRA_NEWS = "extra_news";
    public ProgressDialog progressDialog=null;

    public static void startNewsActivity(Context context, News news) {
        Intent intent = new Intent(context, NewsActivity.class);
        mNews=news;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        initView();
        HttpUtil.getHtmlUtil(this, mNews.getPath(), new CallBack() {
            @Override
            public void onStart() {
                showProgressDialog();
            }

            @Override
            public void onFinsh(String response) {
                HandleResponseUtil.parseContentData(response);
                closeProgressDialog();
                newsTv.setText(mNews.getContent());
                HandleResponseUtil.handleNewsHtmlStr(response, new CallBack() {
                    @Override
                    public void onStart() {
                        HandleResponseUtil.db= Db.getInstance(NewsActivity.this);
                    }

                    @Override
                    public void onFinsh(String response) {

                        loadWebView(newsWebView, response);
                    }
                });
            }
        }, Request.Method.GET,null);
        newsTv.setText(mNews.getContent());
    }

    private void loadWebView(final WebView newsWebView, String htmlStr) {
        newsWebView.loadData(htmlStr, "text/html; charset=utf-8", "utf-8");
    }

    private void initView() {
        newsTv = (TextView) findViewById(R.id.news_tv);
        newsWebView = (WebView) findViewById(R.id.news_webView);
        scrollView = (ScrollView) findViewById(R.id.news_scrollView);

        //设置webView可缩放
        WebSettings webSettings = newsWebView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        newsWebView.setInitialScale(10);

        //设置actionBar的标题
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3b8dff")));
        actionBar.setTitle("新闻");
        //actionBar.setSubtitle(data.getDate());

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news, menu);
        if (newsTv.getVisibility() == View.VISIBLE) {
            menu.add(1, 1, 1, "网页视图");
        } else {
            menu.add(1, 1, 1, "内容视图");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (newsTv.getVisibility() == View.VISIBLE) {
            newsTv.setVisibility(View.GONE);
            newsWebView.setVisibility(View.VISIBLE);
            invalidateOptionsMenu();
        } else {
            newsTv.setVisibility(View.VISIBLE);
            newsWebView.setVisibility(View.GONE);
            invalidateOptionsMenu();
        }

        return super.onOptionsItemSelected(item);
    }

    public  void showProgressDialog() {
        progressDialog.show();
    }

    private  void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
