package three.com.materialdesignexample.Util;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import three.com.materialdesignexample.Adapter.NewsAdapter;
import three.com.materialdesignexample.CallBack;
import three.com.materialdesignexample.Db.Db;
import three.com.materialdesignexample.Framgment.NewsFramgment;
import three.com.materialdesignexample.Models.Course;
import three.com.materialdesignexample.Models.News;
import three.com.materialdesignexample.Models.Score;

/**
 * Created by Administrator on 2015/10/20.
 */
public class HandleResponseUtil {

    public static Db db=null;

    //解析新闻标题
    public static void parseTitleData(String response){

        if (response!=null&&!"".equals(response)) {
            Document doc = Jsoup.parse(response);
            Elements linksElements = doc.select("div[class=article_list mtop10]>ul>li>a");
            // class为“article_list mtop10”的div里面ul里面li里面a标签
            for (Element ele:linksElements) {
                String href = ele.attr("href");
                String title = ele.text();
                News news =new News();
                news.setPath(href);
                news.setTitle(title);
                HttpUtil.datamap.put(news.getPath(), news);
            }
            NewsFramgment.adapter=new NewsAdapter(new ArrayList<News>(HttpUtil.datamap.values()), NewsAdapter.context);
            NewsFramgment.recyclerView.setAdapter(NewsFramgment.adapter);
            NewsFramgment.adapter.notifyDataSetChanged();
        }
    }

    //解析新闻内容
    public static void parseContentData(String response){

        if (response!=null&&!"".equals(response)) {
            Document document = Jsoup.parse(response);
            Elements pElements  = document.select("p");

            StringBuilder sb = new StringBuilder();
            for (Element e : pElements) {
                String str = e.text();
                sb.append(str + "\n");
            }
            HttpUtil.datamap.get(HttpUtil.path).setContent(sb.toString());
        }
    }

    //解析webView使用的Html内容
    public static void handleNewsHtmlStr(String htmlStr, final CallBack callback) {

        new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {

                if(db==null)
                    callback.onStart();

                String result = "";
                try {
                    Document document = Jsoup.parse(params[0]);
                    Elements pElements = document.select("div[class=article_content]");

                    Elements pngs = document.select("img[src]");
                    for (Element element : pngs) {
                        String imgUrl = element.attr("src");
                        if (imgUrl.trim().startsWith("/")) {
                            imgUrl = News.INDEX + imgUrl;
                            element.attr("src", imgUrl);
                        }
                    }

                    Elements iElements=null;
                    StringBuilder sb = new StringBuilder();
                    for (Element e : pElements) {
                        String str = e.html();
                        sb.append(str + "\n");
                    }
                    result = sb.toString();

                } catch (Exception e) {
                    result = "";
                    Log.d("winson", "解析错误： " + e);
                }

                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                callback.onFinsh(s);
            }
        }.execute(htmlStr);
    }

    public static ArrayList<List<Course>> courseData=new ArrayList<List<Course>>();
    public static List<Course> list1 = new ArrayList<Course>();
    public static List<Course> list2 = new ArrayList<Course>();
    public static List<Course> list3 = new ArrayList<Course>();
    public static List<Course> list4 = new ArrayList<Course>();
    public static List<Course> list5 = new ArrayList<Course>();
    public static List<Course> list6 = new ArrayList<Course>();
    public static List<Course> list7 = new ArrayList<Course>();

    public static void handleCourseHtmlStr(String htmlStr, final CallBack callback) {

        new AsyncTask<String, Integer, Boolean>() {

            @Override
            protected Boolean doInBackground(String... params) {

                Boolean result=false;

                int flag=0;
                int flagcount=0;
                String[] res = null;
                try {
                    Document document = Jsoup.parse(params[0]);
                    Elements pElements = document.select("td[rowSpan=2] > font");
                    for(int i=0;i<pElements.size();i++){
                        Element e=pElements.get(i);
                        int count=(i+1)%7;
                        int timeCount=(i)/7;
                        String response=  e.text();

                        //若有单双周在挤在一起则分割正则
                        if(response.length()>120){
                            if(flag==0){
                                res= response.split("上课周次：111111111111111111");
                                flag=res.length;
                            }
                            response=res[flagcount];
                            flagcount++;
                            if(flagcount<flag)
                                i--;
                            else{
                                flagcount=0;
                                flag=0;
                            }
                        }

                        Course course =new Course();
                        if(!TextUtils.isEmpty(response)){

                            course.setCount(count);

                            handleCourseTime(timeCount, course);   //获得上课时间
                            handleCourseResponse(response, course);  //正则课程内容

                            db.saveCourse(course);
                        }

                        if(course.getClassroom()!=null){
                            addToList(count, course);
                        }
                    }

                    addToCourseData();

                    result=true;

                } catch (Exception e) {
                    result=false;
                    Log.d("winson", "解析错误： " + e);
                }

                return result;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if(result)
                    callback.onFinsh("");
                else
                    Log.d("TAG","handle course failed");
            }
        }.execute(htmlStr);
    }

    public static void addToCourseData() {
        if(courseData.size()==0){
            courseData.add(list1);
            courseData.add(list2);
            courseData.add(list3);
            courseData.add(list4);
            courseData.add(list5);
            courseData.add(list6);
            courseData.add(list7);
        }

    }

    public static void addToList(int count, Course course) {
        switch (count){
            case 1:
                list1.add(course);
                break;
            case 2:
                list2.add(course);
                break;
            case 3:
                list3.add(course);
                break;
            case 4:
                list4.add(course);
                break;
            case 5:
                list5.add(course);
                break;
            case 6:
                list6.add(course);
                break;
            case 0:
                list7.add(course);
                break;
        }
    }

    //正则上课内容
    private static void handleCourseResponse(String response, Course course) {
        Pattern pattern = Pattern.compile("\\[+单?双?周?\\]+.*:");
        Matcher matcher = pattern.matcher(response);

        String find = null;
        if (matcher.find()) {
            find = matcher.group();
            Log.d("TAG", matcher.group());
        }
        String week = find.substring(0, 4);
        String name = find.substring(4, find.length() - 1);
        Log.d("TAG", name);
        course.setWeek(week);
        course.setCourseName(name);

        Pattern patternNum = Pattern.compile(":\\w*");
        Matcher matcherNum = patternNum.matcher(response);
        String findNum = null;
        if (matcherNum.find()) {
            findNum = matcherNum.group();
        }
        String number = findNum.substring(1);
        Log.d("TAG", number);
        course.setNumber(number);

        Pattern patternTea = Pattern.compile("：\\[\\D*\\] 课");
        Matcher matcherTea = patternTea.matcher(response);
        String findTea = null;
        if (matcherTea.find()) {
            findTea = matcherTea.group();
        }
        String teacher = findTea.substring(2,findTea.length()-3);
        Log.d("TAG", teacher);
        course.setTeacher(teacher);

        Pattern patternRoom = Pattern.compile("室：\\[.*\\]");
        Matcher matcherRoom = patternRoom.matcher(response);
        String findRoom = null;
        if (matcherRoom.find()) {
            findRoom = matcherRoom.group();
        }
        String room = findRoom.substring(3,findRoom.length()-1);
        Log.d("TAG", room);
        course.setClassroom(room);

        Pattern patternCate = Pattern.compile("别：\\[.*\\] 教");
        Matcher matcherCate = patternCate.matcher(response);
        String findCate = null;
        if (matcherCate.find()) {
            findCate = matcherCate.group();
        }
        String Cate = findCate.substring(3,findCate.length()-4);
        Log.d("TAG", Cate);
        course.setCategory(Cate);
    }

    //获得上课时间
    private static void handleCourseTime(int timeCount, Course course){
        switch (timeCount){
            case 0:
                course.setTime("1-2");
                break;
            case 1:
                course.setTime("3-4");
                break;
            case 2:
                course.setTime("5-6");
                break;
            case 3:
                course.setTime("7-8");
                break;
            case 4:
                course.setTime("9-10");
                break;
            case 5:
                course.setTime("11-12");
                break;
            default:
                break;
        }
    }
    public static ArrayList<Score> scores=new ArrayList<Score>();
    public static String allScore;
    public static void handleScoreHtmlStr(String htmlStr, final CallBack callBack) {

        new AsyncTask<String, Integer, Boolean>() {

            @Override
            protected Boolean doInBackground(String... params) {
                scores.clear();
                Boolean result=false;

                try {
                    int flag=0;
                    Document document = Jsoup.parse(params[0]);
                    Elements pElements = document.select("row");
                    for(Element e:pElements){
                        if(flag==0){
                            allScore=e.select("JDF").text();
                            flag=1;
                        }
                        else {
                            Score score =new Score();
                            score.setScoreName(e.select("coursename").text());
                            score.setCredit(e.select("credits").text());
                            score.setType(e.select("value").text());
                            score.setExamScore(e.select("examscore").text());
                            score.setPoint(e.select("point").text());
                            score.setTestScore(e.select("testscore").text());
                            db.saveScore(score);
                            scores.add(score);
                        }
                    }
                    result=true;

                } catch (Exception e) {
                    result=false;
                    Log.d("winson", "解析错误： " + e);
                }
                return result;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if(result)
                    callBack.onFinsh("");
                else
                    Log.d("TAG","handle Score failed");
            }
        }.execute(htmlStr);

    }

}
