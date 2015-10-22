package three.com.materialdesignexample.Db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/10/20.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    private Context mcontext=null;

    public static final String CREATE_NEWS ="create table News ("
            + "id integer primary key autoincrement,"
            + "path text,"
            + "title text,"
            + "content text)";

    public static final String CREATE_COURSE ="create table Course ("
            + "id integer primary key autoincrement,"
            + "Count integer,"
            + "CourseName text,"
            + "Number text,"
            + "Week text,"
            + "Teacher text,"
            + "Classroom text,"
            + "Category text,"
            + "Time text)";

    public static final String CREATE_SCORE ="create table Score ("
            + "id integer primary key autoincrement,"
            + "ScoreName text,"
            + "point text,"
            + "testScore text,"
            + "type text,"
            + "examScore text,"
            + "credit text)";

    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mcontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NEWS);
        db.execSQL(CREATE_COURSE);
        db.execSQL(CREATE_SCORE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
