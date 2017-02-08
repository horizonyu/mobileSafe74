package com.itheima.mobilesafe74.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima.mobilesafe74.R;
import com.itheima.mobilesafe74.dao.BlackListDBDao;
import com.itheima.mobilesafe74.dao.BlackListInfoDao;

import java.util.List;

/**
 * Created by horizon on 2/7/2017.
 */
public class BlackListActivity extends Activity{

    private ListView listView;
    private TextView tv_phone;
    private TextView tv_mode;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            MyBlackListAdapter myBlackListAdapter = new MyBlackListAdapter();

            listView.setAdapter(myBlackListAdapter);
        }
    };

    private List<BlackListInfoDao> mBlackNumberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_blacklist);

        initUI();
        initInfo();
    }

    private void initUI() {
        listView = (ListView) findViewById(R.id.lv_blacklist);
    }

    /**
     * 从数据库中初始化数据
     */
    private void initInfo() {
         new Thread(){
             @Override
             public void run() {
                 super.run();
                 //获取处理黑名单数据库操作的实例（单例模式）
                 BlackListDBDao blackListDBDao = BlackListDBDao.getInstance(getApplicationContext());
                 //获取数据库中存储的黑名单数据
                 mBlackNumberList = blackListDBDao.queryAll();

                 mHandler.sendEmptyMessage(0);
             }
         }.start();

    }

    private class MyBlackListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mBlackNumberList.size();
        }

        @Override
        public Object getItem(int position) {
            return mBlackNumberList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(),R.layout.blacklist_item,null);
            tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            tv_mode = (TextView) view.findViewById(R.id.tv_mode);

            tv_phone.setText(mBlackNumberList.get(position).phoneNumebr);
            int mode = Integer.parseInt(mBlackNumberList.get(position).mode);
                    switch (mode){
                        case 1:
                            tv_mode.setText("电话");
                            break;
                        case 2:
                            tv_mode.setText("短信");
                            break;
                        case 3:
                            tv_mode.setText("所有");
                            break;
                    }
            return view;
        }
    }

}
