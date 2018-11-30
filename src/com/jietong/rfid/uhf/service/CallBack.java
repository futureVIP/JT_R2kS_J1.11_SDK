package com.jietong.rfid.uhf.service;

/**
 * 获取读取数据接口
 * 
 * @author zhuQixiang createDate 2017-10-25
 * 
 */
public interface CallBack {
	/**
	 * 循环读卡或者寻卡一次回调函数
	 * 
	 * @param data
	 *            电子标签数据
	 * @param antNo
	 *            天线编号
	 */
	void readData(String data,String rssi, String antNo);
}
