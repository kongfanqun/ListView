package com.shy.yimanage.ShyControls.Shylist.Recy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shy.yimanage.R;

import org.json.JSONArray;

/**
 * @User: shy
 * @Date: 18/2/2
 * @Time: 上午10:38
 * @Describe: 自定义 高效率例子
 */
public class ShyRecyclerListDemo extends Activity {
    ShyRecyclerListFragment shyRecyclerListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shy_recycler_list_demo);


        shyRecyclerListFragment = (ShyRecyclerListFragment) getFragmentManager().findFragmentById(R.id.shyRecyclerListFragment);
        shyRecyclerListFragment.setItemView(R.layout.shy_recycler_list_item,
                new ShyRecyclerListFragment.OnShyRecyclerListFragment() {
                    @Override
                    public void onBindViewHolder(View view, int position, JSONArray array) {
                        TextView title = view.findViewById(R.id.title);
                        title.setText(array.optString(position));
                    }
                },
                new ShyRecyclerListFragment.OnLoading() {
                    @Override
                    public void Loading(int pages) {

                    }
                },
                new ShyRecyclerListFragment.OnRefresh() {
                    @Override
                    public void Refresh() {

                    }
                });
        JSONArray array = new JSONArray() {
            {
                put("kongfanqun1");
                put("kongfanqun2");
                put("kongfanqun3");
                put("kongfanqun4");
                put("kongfanqun5");
                put("kongfanqun6");
                put("kongfanqun7");
                put("kongfanqun8");
                put("kongfanqun9");
                put("kongfanqun0");
                put("kongfanqun11");
                put("kongfanqun12");
                put("kongfanqun13");
                put("kongfanqun14");
                put("kongfanqun15");
                put("kongfanqun16");
                put("kongfanqun17");
                put("kongfanqun3");
                put("kongfanqun4");
                put("kongfanqun5");
                put("kongfanqun1");
                put("kongfanqun2");
                put("kongfanqun3");
                put("kongfanqun4");
                put("kongfanqun5");
                put("kongfanqun1");
                put("kongfanqun2");
                put("kongfanqun3");
                put("kongfanqun4");
                put("kongfanqun5");
                put("kongfanqun1");
                put("kongfanqun2");
                put("kongfanqun3");
                put("kongfanqun4");
                put("kongfanqun5");
                put("kongfanqun1");
                put("kongfanqun2");
                put("kongfanqun3");
                put("kongfanqun4");
                put("kongfanqun5");
            }
        };
        shyRecyclerListFragment.getShyAdapte().setDataArray(array);

    }
}
