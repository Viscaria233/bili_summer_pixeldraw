package com.edplan.bilipixeldraw;
import android.graphics.*;
import android.util.*;
import java.io.IOException;
import org.json.JSONException;

public class BiliBitmapParser
{
	public static final int[] colors=new int[]{
		0xFF000000,
		0xFFFFFFFF,
		0xFFAAAAAA,
		0xFF555555,
		0xFFFED3C7,
		0xFFFFC4CE,
		
		0xFFFAAC8E,
		0xFFFF8B83,
		0xFFF44336,
		0xFFE91E63,
		0xFFE2669E,
		0xFF9C27B0,
		
		0xFF673AB7,
		0xFF3F51B5,
		0xFF004670,
		0xFF057197,
		0xFF2196F3,
		0xFF00BCD4,
		
		0xFF3BE5DB,
		0xFF97FDDC,
		0xFF167300,
		0xFF37A93C,
		0xFF89E642,
		0xFFD7FF07,
		
		0xFFFFF6D1,
		0xFFF8CB8C,
		0xFFFFEB3B,
		0xFFFFC107,
		0xFFFF9800,
		0xFFFF5722,
		
		0xFFB83F27,
		0xFF795548
	};
	
	
	public static final int[] colors2=new int[]{
		0xFF000000,
		0xFFFFFFFF,
		0xFFFCDE6B,
		0xFFFFF6D1,
		
		0xFF7D9591,
		0xFF71BED6,
		0xFF3BE5DB,
		0xFFFED3C7,
		
		0xFFB83F27,
		0xFFFAAC8E,
		0xFF004670,
		0xFF057197,
		
		0xFF44C95F,
		0xFF7754FF,
		0xFFFF0000,
		0xFFFF9800,
		
		0xFF97FDDC,
		0xFFF8CB8C,
		0xFF2E8FAF
	};
	
	
	private boolean isLoading=false;
	private Thread loadingThread=null;
	
	public char[] chars;
	
	//1280*720 p
	public Bitmap bitmap;
	
	public BiliBitmapParser(){
		chars=new char[1280*720];
		bitmap=Bitmap.createBitmap(1280,720,Bitmap.Config.ARGB_8888);
	}
	
	public void update(){
		if(isLoading){
			StaticM.makeSnakeBar("正在更新中，不要重复点击",null);
		}else{
			Thread loadThread=new Thread(){
				@Override
				public void run(){
					try
					{
						String s=BiliJsonGetter.getJsonString();
						String j=BiliJsonGetter.parseJson(s);
						if(j==null){
							StaticM.makeSnakeBar("更新画板失败 "+"null point",null);
							return;
						}
						parse(j);
						StaticM.makeSnakeBar("更新画板完成",null);
					}
					catch (IOException e)
					{
						LogUtil.logErr(e);
						StaticM.makeSnakeBar("更新画板失败 "+e.getMessage(),null);
					}
					catch(JSONException e)
					{
						LogUtil.logErr(e);
						StaticM.makeSnakeBar("更新画板失败 "+e.getMessage(),null);
					}
					isLoading=false;
				}
			};
			loadThread.start();
			StaticM.makeSnakeBar("开始更新(ง •̀_•́)ง",null);
			isLoading=true;
			loadingThread=loadThread;
		}
	}
	
	
	public void parse(String res){
		int count=0;
		char c;
		for(int i=0;i<res.length();i++){
			if((c=res.charAt(i))!=chars[i]){
				if(c!='0')count++;
				chars[i]=c;
				bitmap.setPixel(getX(i),getY(i),parseColor(c));
			}
		}
		
		Log.v("recode","count : "+count);
		//StaticM.toast("已经有"+count+"个像素点了(⑉°з°)-♡");
	}
	
	public int getX(int i){
		return i%1280;
	}
	
	public int getY(int i){
		return i/1280;
	}
	
	public int toIndex(int x,int y){
		return x+1280*y;
	}
	
	public static int getIndex(int color){
		for(int i=0;i<colors.length;i++){
			if(colors[i]==color){
				return i;
			}
		}
		return 0;
	}
	
	public static char getChar(int color){
		
		int i=getIndex(color);
		
		if(i==-1){
			return '0';
		}else if(i<10){
			return (char)(48+i);
		}else{
			return (char)(55+i);
		}
		
		/*
		switch(getIndex(color)){
			case 0:
				return '0';
			case 1:
				return '1';
			case 2:
				return '2';
			case 3:
				return '3';
			case 0xFF7D9591:
				return '4';
			case 0xFF71BED6:
				return '5';
			case 0xFF3BE5DB:
				return '6';
			case 0xFFFED3C7:
				//osu 粉
				//return colors[0];
				return '7';
			case 0xFFB83F27:
				return '8';
			case 0xFFFAAC8E:
				return '9';
			case 0xFF004670:
				return 'A';
			case 0xFF057197:
				return 'B';
			case 0xFF44C95F:
				return 'C';
			case 0xFF7754FF:
				return 'D';
			case 0xFFFF0000:
				return 'E';
			case 0xFFFF9800:
				return 'F';
			case 0xFF97FDDC:
				return 'G';
			case 0xFFF8CB8C:
				return 'H';
			case 0xFF2E8FAF:
				return 'I';
			default:
				return '0';
		}*/
	}
	
	public static int parseColor(char c){
		
		byte b=(byte)c;
		if(b>=48&&b<=57){
			return colors[b-48];
		}else if(b>=65&&b<=90){
			return colors[b-55];
		}else{
			return colors[0];
		}
		
		/*
		switch(c){
			case '0':
				return colors[0];
			case '1':
				return colors[1];
			case '2':
				return colors[2];
			case '3':
				return colors[3];
			case '4':
				return colors[4];
			case '5':
				return colors[5];
			case '6':
				return colors[6];
			case '7':
				//osu 粉
				//return colors[0];
				return colors[7];
			case '8':
				return colors[8];
			case '9':
				return colors[9];
			case 'A':
				return colors[10];
			case 'B':
				return colors[11];
			case 'C':
				return colors[12];
			case 'D':
				return colors[13];
			case 'E':
				return colors[14];
			case 'F':
				return colors[15];
			case 'G':
				return colors[16];
			case 'H':
				return colors[17];
			case 'I':
				return colors[18];
			default:
				return colors[0];
		}*/
	}
	
}
