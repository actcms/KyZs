package three.com.materialdesignexample.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;

import three.com.materialdesignexample.CallBack;
import three.com.materialdesignexample.R;
import three.com.materialdesignexample.Util.HttpUtil;
import three.com.materialdesignexample.Util.SafeCodeUtil;

/**
 * Created by Administrator on 2015/10/14.
 */
public class LoginActivity extends Activity{
    private EditText loginuser=null;
    private EditText loginpass=null;
    private EditText passported=null;
    private Button loginbtn=null;
    private Button safecodebtn=null;
    private ImageView codeimg=null;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        isCookieGet() ;

        loginuser= (EditText) findViewById(R.id.loginid_et);
        loginpass= (EditText) findViewById(R.id.loginpswd_et);
        passported=(EditText) findViewById(R.id.safecode_et);
        loginbtn= (Button) findViewById(R.id.login_ok_btn);
        safecodebtn=(Button) findViewById(R.id.safecode_btn);
        codeimg= (ImageView) findViewById(R.id.codeimg);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user=loginuser.getText().toString();
                pass=loginpass.getText().toString();
                passport=passported.getText().toString();
                saveUserAndPass();
            }
        });

        safecodebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user=loginuser.getText().toString();
                if(TextUtils.isEmpty(user)){
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("善意的提醒")
                            .setPositiveButton("确定", null)
                            .setMessage("请先填写学号")
                            .show();
                }
                else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            showProgressDialog();
                                        }
                                    }
                            );

                            SafeCodeUtil.getLoginCookie(user, new CallBack() {
                                @Override
                                public void onStart() {
                                    final Bitmap codemap = SafeCodeUtil.getSafeCodePic();
                                    // 通过runOnUiThread()方法回到主线程处理逻辑
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            codeimg.setImageBitmap(codemap);
                                            Log.d("TAG", "image over");
                                        }
                                    });
                                    closeProgressDialog();
                                }

                                @Override
                                public void onFinsh(String response) {
                                    // 通过runOnUiThread()方法回到主线程处理逻辑
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new AlertDialog.Builder(LoginActivity.this)
                                                    .setTitle("善意的提醒")
                                                    .setPositiveButton("确定", null)
                                                    .setMessage("请确保在校园网环境使用")
                                                    .show();
                                        }
                                    });
                                }
                            });


                        }
                    }).start();
                }
            }
        });
    }

    private void isCookieGet() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("cookie_OK", false)) {
            HttpUtil.cookie=prefs.getString("cookie",null);
            HttpUtil.userName=prefs.getString("username",null);
            Intent intent = new Intent(this, DrawerLayoutActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private String user="";
    private String pass="";
    private String passport="";

    private void saveUserAndPass() {


        if(TextUtils.isEmpty(user)||TextUtils.isEmpty(pass)||TextUtils.isEmpty(passport)){
            new AlertDialog.Builder(this)
                    .setTitle("善意的提醒")
                    .setPositiveButton("确定", null)
                    .setMessage("请填写完整的学号,密码和验证码")
                    .show();
        }
        else{

            HttpUtil.userName=user;
            HttpUtil.password=pass;
            HttpUtil.passport=passport;
            HttpUtil.login(new CallBack() {
                @Override
                public void onStart() {
                    saveCookie();
                    Log.d("TAG","获取cookie成功");
                    Intent intent = new Intent(LoginActivity.this, DrawerLayoutActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFinsh(String response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("善意的提醒")
                                    .setPositiveButton("确定", null)
                                    .setMessage("登陆失败,请确保账号密码输入正确,和在校园网下登陆")
                                    .show();
                        }
                    });
                }
            });
        }
    }
    private void saveCookie(){

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("cookie_OK", true);
        editor.putString("cookie", HttpUtil.cookie);
        editor.putString("username",HttpUtil.userName);
        editor.commit();
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
}
