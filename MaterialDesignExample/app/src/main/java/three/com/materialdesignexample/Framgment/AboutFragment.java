package three.com.materialdesignexample.Framgment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import three.com.materialdesignexample.R;

/**
 * Created by Administrator on 2015/10/21.
 */
public class AboutFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.about_framgment, null);

        return view;
    }

}
