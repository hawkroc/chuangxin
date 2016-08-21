package com.fh.util;

public class LatLonUtil {

	private static final double PI = 3.14159265; // 圆周率
	private static final double EARTH_RADIUS = 6378137; // 地球半径
	private static final double RAD = Math.PI / 180.0; // 一百八十度角
	private static Double degree = (24901 * 1609) / 360.0;
	private static double rad(double d)
	{
	   return d * Math.PI / 180.0;
	}
	public static double GetDistance(double lat1, double lng1, double lat2, double lng2)
	{
	   double radLat1 = rad(lat1);
	   double radLat2 = rad(lat2);
	   double a = radLat1 - radLat2;
	   double b = rad(lng1) - rad(lng2);

	   double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
	    Math.cos((radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2))));
	   s = s * EARTH_RADIUS;
	   s = Math.round(s * 10000) / 10000;
	   return s;
	}

	/**
	 * @param raidus
	 * 单位米 return minLat 
	 * 最小经度 minLng 
	 * 最小纬度 maxLat 
	 * 最大经度 maxLng 
	 * 最大纬度 minLat
	 */
	public static double[] getAround(double lat, double lon, int raidus) {

		Double latitude = lat;// 传值给经度
		Double longitude = lon;// 传值给纬度

		//Double degree = (24901 * 1609) / 360.0; // 获取每度
		double raidusMile = raidus;

		Double dpmLat = 1 / degree;
		Double radiusLat = dpmLat * raidusMile;
		// 获取最小纬度
		Double minLat = latitude - radiusLat;
		// 获取最大纬度
		Double maxLat = latitude + radiusLat;

		Double mpdLng = degree * Math.cos(latitude * (PI / 180));
		Double dpmLng = 1 / mpdLng;
		Double radiusLng = dpmLng * raidusMile;
		//获取最小经度
		Double minLng = longitude - radiusLng;
		// 获取最大经度
		Double maxLng = longitude + radiusLng;
		
		System.out.println("jingdu" + minLat + "weidu" + minLng + "zuidajingdu"
				+ maxLat + "zuidaweidu" + maxLng);

		return new double[] { minLat, minLng, maxLat, maxLng };
	}
	
	public static double[] getDefaultAround(double lat, double lon) {
		return getAround(lat,lon,1000);
		
	}
	//测试方法
	public static void main(String [] src){
		getAround(36.68027, 117.12744, 1000);
	}

}