package com.predict.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileReader preResult_fr;
		BufferedReader preResult_br;
		String preResultPath = "8.txt";
		int count = 0;
		try {
			preResult_fr = new FileReader(preResultPath);
			preResult_br = new BufferedReader(preResult_fr);
			String strLine = "";
			try {
				while ((strLine = preResult_br.readLine()) != null) {
//					String[] t=strLine.trim().split(" ");
//					for(int i=0;i<t.length;i++){
//						System.out.println(t[i]+"*");
//					}
					System.out.println((strLine.trim().split("     ")[1]).trim().toString());
					count++;
				}
				System.out.println("共条数："+count);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
