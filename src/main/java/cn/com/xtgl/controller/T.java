package cn.com.xtgl.controller;

public class T {

	public int cc(){
		int i = 0;
		try{
			i = 10 / 0;
		}catch(Exception e){
			e.printStackTrace();
		}
		return i;
	}
}
