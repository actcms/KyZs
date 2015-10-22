package three.com.materialdesignexample.Framgment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import three.com.materialdesignexample.ItemDivider;
import three.com.materialdesignexample.Models.News;
import three.com.materialdesignexample.Adapter.NewsAdapter;
import three.com.materialdesignexample.R;
import three.com.materialdesignexample.Util.HttpUtil;

/**
 * Created by Administrator on 2015/10/8.
 */
public class NewsFramgment extends Fragment {

    public static RecyclerView recyclerView=null;
    private RecyclerView.LayoutManager layoutManager=null;
    public static NewsAdapter adapter=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_title_list, null);

        recyclerView= (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new NewsAdapter(new ArrayList<News>(HttpUtil.datamap.values()),getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ItemDivider());


        return view;
    }
}
