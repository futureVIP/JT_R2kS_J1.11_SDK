package com.jietong.rfid.uhf.tool;

import com.jietong.rfid.uhf.dao.impl.Reader;

public class ReaderUtil {
	public static int connectCount = 0;
	/**
	 * 限制连接设备数量
	 */
	public final static int MAX_DEVICE_NUM = 50;
	
	public static Reader readers = null;
}
