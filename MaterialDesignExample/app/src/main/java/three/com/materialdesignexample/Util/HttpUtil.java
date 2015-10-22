package three.com.materialdesignexample.Util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import three.com.materialdesignexample.CallBack;
import three.com.materialdesignexample.JsHelper.HexMd5;
import three.com.materialdesignexample.Models.News;

/**
 * Created by Administrator on 2015/10/8.
 */
public class HttpUtil  {

    public static LinkedHashMap<String,News> datamap=new LinkedHashMap<String,News>();

    public static String path;

    private static boolean isGBK=false;

    public static String cookie="";

    private static Context mcontext;

    public static void getHtmlUtil( Context context,String url, final CallBack callBack,final int method,
                                    final Map<String, String> headers
                                    ){

        mcontext=context;

        StringRequest stringRequest = null;

        if(url.substring(0,1).equals("/")==true){
            path=url;
            url=News.INDEX+path;
        }

        RequestQueue mQueue = Volley.newRequestQueue(context);
        if(method== Request.Method.GET){
            stringRequest = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("TAG", response);

                            callBack.onStart();
                            callBack.onFinsh(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", error.getMessage(), error);
                }
            })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    if (headers != null) {
                        return headers;
                    } else {
                        return super.getHeaders();
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {

                    String result = "";
                    try {
                        if(isGBK){
                            result = new String(response.data, "GB2312");
                            isGBK=false;
                        }
                        else
                            result = new String(response.data, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
        }
        //设置20秒的超时
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
        mQueue.start();

    }


    public static String userName="";
    public static String password="";
    public static String passport="";
    public static String LoginId="";

    public static void login(final CallBack callBack){


        String Digest="";
        String plainText="";

        plainText=userName+LoginId+password+passport;
        Digest= HexMd5.getMD5(plainText);
        Log.d("TAG", "next url");

        final String finalDigest = Digest;

        new Thread(new Runnable() {
            @Override
            public void run() {
                getLoginCookie(finalDigest,callBack);
            }
        }).start();

    }

    @SuppressWarnings("deprecation")
    public static void getCourseHtml(final Context context, final CallBack callBack)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                callBack.onStart();
                String url = "http://10.22.151.40/scripts/wzw_jspk.asp?WHO=" + userName + "&year=2015&term=1&STUDENTNAME=" + yourName;
//                try {
//                    url = "http://10.22.151.40/scripts/wzw_jspk.asp?WHO=144173551&year=2015&term=1&STUDENTNAME=" + URLEncoder.encode("张坚", "UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
                Map<String, String> headers = new HashMap<String, String>();
                //设置referer
                headers.put("Referer", "http://10.22.151.40/scripts/login.exe/login?");
                headers.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/8.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729)");
                headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                Log.d("TAG", cookie);
                headers.put("Cookie", cookie);
                isGBK = true;
                getHtmlUtil(context, url, new CallBack() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinsh(String response) {
                        HandleResponseUtil.handleCourseHtmlStr(response, callBack);
                    }
                }, Request.Method.GET, headers);
            }
        }).start();

    }

    @SuppressWarnings("deprecation")
    public static void getScoreHtml(final Context context, final CallBack callBack)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                callBack.onStart();
                String url = "http://10.22.151.40/scripts/general.exe/query?YEAR=2015&TERM=1&STUDENTNO="+userName+"&page=1&pagename=l_cj_STUDENTALLSCORE.htm";
//                try {
//                    url = "http://10.22.151.40/scripts/wzw_jspk.asp?WHO=144173551&year=2015&term=1&STUDENTNAME=" + URLEncoder.encode("张坚", "UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
                Map<String, String> headers = new HashMap<String, String>();
                //设置referer
                headers.put("Referer", "http://10.22.151.40/scripts/login.exe/login?");
                headers.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/8.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729)");
                headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                Log.d("TAG", cookie);
                headers.put("Cookie", cookie);
                isGBK = true;
                getHtmlUtil(context, url, new CallBack() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinsh(String response) {
                        HandleResponseUtil.handleScoreHtmlStr(response, callBack);
                    }
                }, Request.Method.GET, headers);
            }
        }).start();

    }

    @SuppressWarnings("deprecation")
    public static void getLoginCookie(String Digest,CallBack callBack){

        final String url = "http://10.22.151.40/scripts/login.exe/login?";
        final DefaultHttpClient httpClient = new DefaultHttpClient();
        StringBuilder ckSb=new StringBuilder(cookie);

        try {
            String content =null;
            HttpPost post = new HttpPost(url);
            post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
            post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            post.setHeader("Cookie",cookie );
            List params = new ArrayList();

            params.add(new BasicNameValuePair("UserName",userName));
            params.add(new BasicNameValuePair("Digest",Digest));

            post.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
            HttpResponse response = httpClient.execute(post);
            if(response.getStatusLine().getStatusCode() ==200){
                content = EntityUtils.toString(response.getEntity(), "GBK");

                getYourName(content);//获取姓名
                // Log.d("TAG",content);
                // get cookieStore
                CookieStore cookieStore = httpClient.getCookieStore();
                // get Cookies
                List<Cookie> cookies = cookieStore.getCookies();
                if(cookies!=null){
                    ckSb.append(";SessionID=");
                    ckSb.append(cookies.get(1).getValue());
                    ckSb.append(";UserGroup=");
                    ckSb.append(cookies.get(2).getValue());
                    ckSb.append(";UserRoles=");
                    ckSb.append(cookies.get(3).getValue());
                    cookie=ckSb.toString();
                    callBack.onStart();
                }
                else {
                    callBack.onFinsh(null);
                    Log.d("TAG","获取Cookie失败");
                }
            }
        } catch (Exception e) {
            callBack.onFinsh(null);
            e.printStackTrace();
        }

    }
    private static String yourName;

    private static void getYourName(String content) {

        if (!TextUtils.isEmpty(content)) {
            Document document = Jsoup.parse(content);
            Elements pElements  = document.select("lable[id=NAME]");
            String str=null;
            for (Element e : pElements) {
                str= e.text();
            }
            yourName=str;
        }

    }


}
