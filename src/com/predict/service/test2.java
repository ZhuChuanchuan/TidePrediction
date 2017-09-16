package com.predict.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class test2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<Date,Integer> map=new TreeMap<Date,Integer>();
        Calendar now=Calendar.getInstance();
        
        Date dt = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		
		//NT 与大通相差2day
		now.set(2013,0,1,12,0,0);
        map.put(now.getTime(),0);
		now.set(2013,0,2,12,0,0);
        map.put(now.getTime(),24);
//        now.set(2017,4,31,24,0,0);
//        map.put(now.getTime(),28200);
//        now.set(2017,5,1,24,0,0);
//        map.put(now.getTime(),28300);
//        now.set(2017,5,2,24,0,0);
//        map.put(now.getTime(),28400);
//        now.set(2017,5,3,24,0,0);
//        map.put(now.getTime(),28500);
        
        Calendar instance=Calendar.getInstance();
        instance.set(2013,0,1,0,0,0); //自2010 1 1起
        
        Calendar instance2=Calendar.getInstance();
        instance2.set(2013,0,1,24,0,0);
        Date queryDate=instance2.getTime();
        
        System.out.println(queryDate+" "+qInterpolation(queryDate,instance.getTime(),map));
		
	}
	private static  double getHours(Date date1,Date date2){
        long time=date1.getTime()-date2.getTime();
        double hours=(time/1000)/3600.0;
        return hours;
    }
    public static  double qInterpolation(Date date,Date instance,Map<Date,Integer> qMap){
        double k=0;
        double b=0;
        double h=getHours(date,instance);
        Date dl=new Date();
        int ql=0;
        Date dr=new Date();
        int qr=0;
        long time=date.getTime();
        //遍历qMap
        for (Map.Entry<Date, Integer> entry : qMap.entrySet()) {
            //判斷date位于哪個區間内
            if (time<=entry.getKey().getTime()){
                dr=entry.getKey();
                qr=entry.getValue();
                break;
            }
        }
        dl=new Date(dr.getTime()-24*3600*1000);
        ql=qMap.get(dl);
        k=(qr-ql)/(getHours(dr,dl));
        b=qr-k*getHours(dr,instance);
        return k*h+b;
    }

}
