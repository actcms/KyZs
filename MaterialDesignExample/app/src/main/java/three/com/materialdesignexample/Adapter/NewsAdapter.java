package three.com.materialdesignexample.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import three.com.materialdesignexample.Activity.NewsActivity;
import three.com.materialdesignexample.Models.News;
import three.com.materialdesignexample.R;

/**
 * Created by Administrator on 2015/10/8.
 */
public class NewsAdapter extends RecyclerView.Adapter {

    private ArrayList<News> dataset;
    public static Context context;

    public NewsAdapter(ArrayList<News> dataset, Context context) {
        this.dataset=dataset;
        this.context=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item_layout,viewGroup,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder mvh = (ViewHolder) viewHolder;
        mvh.textView.setText(dataset.get(i).getTitle());
        viewHolder.itemView.setTag(dataset.get(i));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textView=null;
        public ViewHolder(View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            NewsActivity.startNewsActivity(context, (News) v.getTag());
        }
    }


}
