package three.com.materialdesignexample.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.volley.Request;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import three.com.materialdesignexample.CallBack;
import three.com.materialdesignexample.Framgment.AboutFragment;
import three.com.materialdesignexample.Framgment.CourseFramgment;
import three.com.materialdesignexample.Framgment.NewsFramgment;
import three.com.materialdesignexample.Framgment.ScoreFramgment;
import three.com.materialdesignexample.Models.News;
import three.com.materialdesignexample.R;
import three.com.materialdesignexample.Util.HandleResponseUtil;
import three.com.materialdesignexample.Util.HttpUtil;

/**
 * Created by Administrator on 2015/10/8.
 */
public class DrawerLayoutActivity extends AppCompatActivity {

    private Toolbar toolbar=null;
    private DrawerLayout drawerLayout=null;
    private ActionBarDrawerToggle drawerToggle=null;
    private NavigationView navigationView=null;
    public  ProgressDialog progressDialog=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle=new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);

        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);

        navigationView= (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerContent(navigationView);
        UmengUpdateAgent.update(this);

        switchToCourse();  //主界面选为课表


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    //设置NavigationView点击事件
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.school_news:
                                switchToNews();
                                break;
                            case R.id.school_course:
                                switchToCourse();
                                break;
                            case R.id.school_score:
                                switchToScore();
                                break;
                            case R.id.about:
                                switchToAbout();
                                break;

                        }
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    private void switchToScore() {

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ScoreFramgment()).commit();

        toolbar.setTitle(R.string.school_score);
    }

    private void switchToNews() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new NewsFramgment()).commit();
        if(HttpUtil.datamap.values().size()==0){
            showProgressDialog();
            HttpUtil.getHtmlUtil(this, News.NEWS_INDEX, new CallBack() {
                @Override
                public void onStart() {
                    showProgressDialog();
                }

                @Override
                public void onFinsh(String response) {
                    HandleResponseUtil.parseTitleData(response);
                    closeProgressDialog();
                }
            }, Request.Method.GET, null);
        }
        toolbar.setTitle(R.string.school_news);
    }

    private void switchToCourse() {

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new CourseFramgment()).commit();

        toolbar.setTitle(R.string.school_course);

    }


    private void switchToAbout() {

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new AboutFragment()).commit();
        toolbar.setTitle(R.string.about);
    }

    public  void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
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
