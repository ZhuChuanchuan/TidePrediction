package com.predict.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class QueryTideUnit {

	/**
	 * @param args
	 * time：2017-6-20 14:39:56
	 * function：查询预测潮位和实测潮位数据
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection con=null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@127.0.0.1:1521:ORCL";
			String user = "Orcl_Z";
			String password = "orcl";
			con = DriverManager.getConnection(url, user, password);

			Calendar start = Calendar.getInstance();  
			start.set(2017, 6, 14, 0, 0, 0);
			Calendar end = Calendar.getInstance();  
			end.set(2017, 6, 16, 0, 0, 0);
			
			QueryTideService queryTideService=new QueryTideService();
			if(queryTideService.doExecute(con, start, end, "南通")){
				System.out.println("成功");
			}else{
				System.out.println("失败");
			}
			
			
//			String s="";
//			try {
//				s = ps.QueryPredictTide("NT", "2017/02/13", "2017/02/14");
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			//System.out.println(s);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
