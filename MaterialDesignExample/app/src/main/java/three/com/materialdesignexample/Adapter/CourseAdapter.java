package three.com.materialdesignexample.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import three.com.materialdesignexample.Models.Course;
import three.com.materialdesignexample.R;

/**
 * Created by Administrator on 2015/10/15.
 */
public class CourseAdapter extends BaseAdapter {
    private Context context;
    private List<Course> data;

    public CourseAdapter(Context context, List<Course> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Course getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.course_item_layout, null);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.teacherTv = (TextView) convertView.findViewById(R.id.teacher_tv);
            holder.classroomTv = (TextView) convertView.findViewById(R.id.classroom_tv);
            holder.weekTv = (TextView) convertView.findViewById(R.id.week_tv);
            holder.categoryTv= (TextView) convertView.findViewById(R.id.category_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Course course = getItem(position);
        if(course!=null){
            String time =course.getTime();
            int circleShape = 0;
            int color = 0;
            if (time.equals("1-2")) {
                circleShape = R.drawable.circle_cyan;
                color = context.getResources().getColor(R.color.cyan_500);
            } else if (time.equals("3-4")) {
                circleShape = R.drawable.circle_amber;
                color = context.getResources().getColor(R.color.amber_500);
            } else if (time.equals("5-6")) {
                circleShape = R.drawable.circle_deep_orange;
                color = context.getResources().getColor(R.color.deep_orange_500);
            } else if (time.equals("7-8")) {
                circleShape = R.drawable.circle_blue;
                color = context.getResources().getColor(R.color.blue_500);
            } else if (time.equals("9-10")) {
                circleShape = R.drawable.circle_indigo;
                color = context.getResources().getColor(R.color.indigo_500);
            }
            else {
                circleShape = R.drawable.circle_indigo;
                color = context.getResources().getColor(R.color.indigo_500);
            }

            //设置课程名
            holder.nameTv.setText(course.getCourseName());
            //设置课程时间
            holder.timeTv.setText(time);
            holder.timeTv.setBackgroundResource(circleShape);

            holder.teacherTv.setText(course.getTeacher());

            holder.weekTv.setText(course.getWeek());
            //设置课程类型
            holder.categoryTv.setText(course.getCategory() );
            //设置教室
            holder.classroomTv.setText(course.getClassroom());
        }
        return convertView;

    }
    private static class ViewHolder {
        TextView timeTv;
        TextView nameTv;
        TextView classroomTv;
        TextView weekTv;
        TextView teacherTv;
        TextView categoryTv;
    }
}