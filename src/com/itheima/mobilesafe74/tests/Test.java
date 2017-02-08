package com.itheima.mobilesafe74.tests;

import android.test.AndroidTestCase;

import com.itheima.mobilesafe74.dao.BlackListDBDao;

/**
 * Created by horizon on 2/8/2017.
 */

public class Test extends AndroidTestCase {
    public void tests(){
        final int expected = 1;
        final int reality = 1;
       // assertEquals(expected,reality);

        BlackListDBDao instance = BlackListDBDao.getInstance(getContext());
        instance.insert("114","1");

     //   instance.update("110","2");



       // instance.delete("110");

        //
      //  assertEquals( instance.insert("110","1"),1);
    }
}
