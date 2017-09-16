package com.predict.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class QueryTideService {
	// 查询预测流量
	public boolean doExecute(Connection con,Calendar start,Calendar end,String stationName){
		HashMap<String,Float> predictMap=DoPredict(con,start,end,stationName);
		HashMap<String,Float> shiceMap=DoShice(con,start,end,stationName);
		
			String compareFilePath ="compare.txt";
			File compareFile = new File(compareFilePath);
			if (compareFile.exists()) {
				compareFile.delete();
			}
			FileWriter tq_fw = null;

			try {
				compareFile.createNewFile();
				tq_fw = new FileWriter(compareFile, true);
				
				while(start.compareTo(end)<0){
					String time=(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(start.getTime());
					tq_fw.write(time+" ");//时间
					tq_fw.write(predictMap.get(time)+" ");//预测
					tq_fw.write(String.valueOf(shiceMap.get(time)));//实测
					tq_fw.write("\n");
				
					start.add(Calendar.MINUTE, 1);//每分钟一条
				}
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}finally{
				try {
					tq_fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		return true;
	}
	private HashMap<String, Float> DoShice(Connection con,Calendar start,Calendar end,String stationName) {
		// TODO Auto-generated method stub
		HashMap<String, Float> map = new HashMap<String, Float>();

		PreparedStatement pre = null;
		ResultSet result = null;
		
		String sql2 = "select to_char(CLSJ,'yyyy/MM/dd HH24:mi:ss'),CLSW from T_SZHD_SWDTSLXX t where SWZID=(select ID from T_SZHD_SWJBXX where SWZMC=?) "
				+ "and CLSJ>to_date(? ,'yyyy-MM-dd') "
				+ "and CLSJ<to_date(?,'yyyy-MM-dd') order by CLSJ";

		try {
			pre = con.prepareStatement(sql2);
			String sStart = (new SimpleDateFormat("yyyy/MM/dd")).format(start
					.getTime());
			String sEnd = (new SimpleDateFormat("yyyy/MM/dd")).format(end
					.getTime());
			pre.setString(1, stationName);
			pre.setString(2, sStart);
			pre.setString(3, sEnd);
			result = pre.executeQuery();

			while (result.next()) {
				System.out.println(result.getString(1)+" "+ result.getFloat(2));
				map.put(result.getString(1), result.getFloat(2));
			}
			System.out.println("实测数据查询完成");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	private HashMap<String, Float> DoPredict(Connection con,Calendar start,Calendar end,String stationName) {
		// TODO Auto-generated method stub
		
		HashMap<String, Float> map = new HashMap<String, Float>();
		PreparedStatement pre = null;
		ResultSet result = null;
		try {
			String sql = "select to_char(pretime,'yyyy/MM/dd HH24:mi:ss'),to_char(t.PreTide,'fm99999999999999999990.00') from TB_PREDICTIONDATA t where STATIONNAME=? and PRETIME>to_date(?,'yyyy/mm/dd') and PRETIME<to_date(?,'yyyy/mm/dd') order by PRETIME";
			pre = con.prepareStatement(sql);
			String sStart = (new SimpleDateFormat("yyyy/MM/dd"))
					.format(start.getTime());
			String sEnd = (new SimpleDateFormat("yyyy/MM/dd"))
					.format(end.getTime());
			pre.setString(1, stationName);
			pre.setString(2, sStart);
			pre.setString(3, sEnd);
			result = pre.executeQuery();

			while (result.next()) {
				System.out.println(result.getString(1)+" "+ result.getFloat(2));
				map.put(result.getString(1), result.getFloat(2));
			}
			System.out.println("预测数据查询完成");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}
	public String QueryPredictTide(Connection conn,String station, String startDate,
			String endDate) throws ParseException {
		String stationName = "";
		if (station.equals("NJ")) {
			stationName = "南京";
		} else if (station.equals("JY")) {
			stationName = "江阴";
		} else if (station.equals("TSG")) {
			stationName = "天生港";
		} else if (station.equals("XLJ")) {
			stationName = "徐六泾";
		}else if(station.equals("NT")){
			stationName="南通";
		}
		
		
		JSONObject jsonObj = null;
		
		PreparedStatement pre = null;
		ResultSet result = null;
		try {
			String sql = "select to_char(PreTime,'yyyy/mm/dd HH:mm:ss'),to_char(t.PreTide,'fm99999999999999999990.00') from TB_PREDICTIONDATA t where STATIONNAME=? and PRETIME>to_date(?,'yyyy/mm/dd') and PRETIME<to_date(?,'yyyy/mm/dd') order by PRETIME";
			pre = conn.prepareStatement(sql);
			pre.setString(1, stationName);
			pre.setString(2, startDate);
			pre.setString(3, endDate);
			result = pre.executeQuery();
			
			Map<String,Object> resMap=new HashMap<String,Object>();
			ArrayList categories = new ArrayList();
			ArrayList values = new ArrayList();			
            while (result.next()) {
            	 categories.add(result.getString(1));
				 values.add(result.getString(2));
            }
            resMap.put("time", categories);
			resMap.put("tide", values);
            jsonObj=JSONObject.fromObject(resMap);
            
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				result.close();
				//pre.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObj.toString();
	}
}
