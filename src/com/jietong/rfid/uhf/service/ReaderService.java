package com.jietong.rfid.uhf.service;

import com.jietong.rfid.uhf.dao.impl.Reader;
import com.jietong.rfid.uhf.entity.AntStruct;

public interface ReaderService {
	/**
	 * 串口或网口连接
	 * @param portName
	 * @param baudRate
	 * @return Reader|null
	 */
	Reader connect(String portName, int baudRate);
	/**
	 * 串口或网口连接
	 * @param portName
	 * @param baudRate
	 * @return Reader|null
	 */
	Reader connect(String portName);
	/**
	 * 2.断开连接
	 * @param reader
	 * @return true|false
	 */
	boolean disconnect(Reader reader);

	/**
	 * 3.获取版本号
	 * @param reader
	 * @return value|null
	 */
	String version(Reader reader);
	/**
	 * 4.单次寻卡
	 * @param r2k
	 * @return true|false
	 */
	boolean invOnce(Reader reader,CallBack callBack);
	/**
	 * 5.连续寻卡
	 * @param reader
	 * @return true|false
	 */
	boolean beginInv(Reader reader,CallBack callBack);
	/**
	 * 6.停止寻卡
	 * @param reader
	 * @return true|false
	 */
	boolean stopInv(Reader reader,CallBackStopReadCard callBackStopReadCard);
	/**
	 * 7.获取天线
	 * @param reader
	 * @return AntStruct|null
	 */
	AntStruct getAnt(Reader reader);
	/**
	 * 8.设置天线
	 * @param reader
	 * @param ant 天线号
	 * @return true|false
	 */
	boolean setAnt(Reader reader, AntStruct ant);
	/**
	 * 9.定区域写入数据
	 * @param reader
	 * @param bank
	 * @param begin
	 * @param length
	 * @param data
	 * @param password
	 * @return true|false
	 */
	boolean writeTagData(Reader reader, int bank, int begin, int length,String data, byte[] password);
	/**
	 * 10.指定区域读取数据
	 * @param reader
	 * @param bank 区域
	 * @param begin 起始地址
	 * @param size 长度
	 * @param password
	 * @return value|null
	 */
	String readTagData(Reader reader,byte bank, byte begin,byte size,byte[] password);
	/**
	 * 11.锁标签
	 * @param reader
	 * @param lockType 
	 * @param lockBank
	 * @param password
	 * @return ture|false
	 */
	boolean lockTag(Reader reader, byte lockType, byte lockBank,byte[] password);
	/**
	 * 12.获取蜂鸣器状态(0.关闭|1.打开)
	 * @param reader
	 * @return
	 */
	int getBuzzer(Reader reader);
	/**
	 * 13.设置蜂鸣器状态(0.关闭|1.打开)
	 * @param reader
	 * @param state
	 * @return ture|false
	 */
	boolean setBuzzer(Reader reader, byte state);
	/**
	 * 14.设置工作模式
	 * @param reader
	 * @param mode (01主从模式；02定时模式；03触发模式)
	 * @return true|false
	 */
	boolean setWorkMode(Reader reader, int mode);
	/**
	 * 15.读取工作模式
	 * @param reader
	 * @return value(01主从模式；02定时模式；03触发模式)|-1
	 */
	int getWorkMode(Reader reader);
	/**
	 * 16.设定触发延时
	 * @param reader
	 * @param trigTime 
	 * @return true|false
	 */
	boolean setTrigModeDelayTime(Reader reader, byte trigTime);
	/**
	 * 17.读取触发延时
	 * @param reader
	 * @return value|-1
	 */
	int getTrigModeDelayTime(Reader reader);
	/**
	 * 18.读取设备号
	 * @param reader 
	 * @return value|null
	 */
	String getDeviceNo(Reader reader);
	/**
	 * 19.设置设备号
	 * @param reader
	 * @param deviceNo
	 * @return true|false
	 */
	boolean setDeviceNo(Reader reader, byte deviceNo);
	/**
	 * 20.销毁标签
	 * @param reader
	 * @param accessPwd  访问密码
	 * @param killPwd	  销毁密码
	 * @return 	true|false
	 */
	boolean killTag(Reader reader, byte[] accessPwd, byte[] killPwd);
	/**
	 * 21.设置输出口模式
	 * @param reader
	 * @param outputMode
	 * @return true|false
	 */
	boolean setOutputMode(Reader reader, byte outputMode);
	/**
	 * 22.获取输出口模式
	 * @param reader
	 * @return value|-1
	 */
	int getOutputMode(Reader reader);
	/**
	 * 23.获取相邻判别
	 * @param reader
	 * @param trigTime
	 * @return 
	 */
	int getNeighJudge(Reader reader);
	/**
	 * 24.设置相邻判别
	 * @param reader
	 * @param neighJudgeTime
	 * @return true|false
	 */
	boolean setNeighJudge(Reader reader, byte neighJudgeTime);
	/**
	 * 25.获取触发延时
	 * @param reader
	 * @param state
	 */
	int getRelayAutoState(Reader reader);
	/**
	 * 26.设置触发延时
	 * @param reader
	 * @param time
	 * @return
	 */
	boolean setRelayAutoState(Reader reader, byte time);
}
