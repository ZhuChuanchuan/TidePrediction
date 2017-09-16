package com.predict.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;

public class Main {

	/**
	 * 最新修改：2017-6-2 10:03:31
	 * 插值大通流量，2013调和常数
	 */
	/**
	 * @param args
	 */
	//修改时间：2016-10-19
	//修改内容：加入潮位校正，根据前5天的数据对以后的潮位进行预测，选择参数为"今后几天"
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection con=null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@127.0.0.1:1521:ORCL";
			String user = "Orcl_Z";
			String password = "orcl";
			con = DriverManager.getConnection(url, user, password);
			PredictionService ps=new PredictionService(con);

			if(ps.DoPredict(3))
				System.out.println("chenggong");
			else
				System.out.println("shibai");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block			
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
