package com.jietong.rfid.uhf.service;

import com.jietong.rfid.uhf.dao.impl.Reader;
import com.jietong.rfid.uhf.entity.AntStruct;

public interface ReaderService {
	/**
	 * ���ڻ���������
	 * @param portName
	 * @param baudRate
	 * @return Reader|null
	 */
	Reader connect(String portName, int baudRate);
	/**
	 * ���ڻ���������
	 * @param portName
	 * @param baudRate
	 * @return Reader|null
	 */
	Reader connect(String portName);
	/**
	 * 2.�Ͽ�����
	 * @param reader
	 * @return true|false
	 */
	boolean disconnect(Reader reader);

	/**
	 * 3.��ȡ�汾��
	 * @param reader
	 * @return value|null
	 */
	String version(Reader reader);
	/**
	 * 4.����Ѱ��
	 * @param r2k
	 * @return true|false
	 */
	boolean invOnce(Reader reader,CallBack callBack);
	/**
	 * 5.����Ѱ��
	 * @param reader
	 * @return true|false
	 */
	boolean beginInv(Reader reader,CallBack callBack);
	/**
	 * 6.ֹͣѰ��
	 * @param reader
	 * @return true|false
	 */
	boolean stopInv(Reader reader,CallBackStopReadCard callBackStopReadCard);
	/**
	 * 7.��ȡ����
	 * @param reader
	 * @return AntStruct|null
	 */
	AntStruct getAnt(Reader reader);
	/**
	 * 8.��������
	 * @param reader
	 * @param ant ���ߺ�
	 * @return true|false
	 */
	boolean setAnt(Reader reader, AntStruct ant);
	/**
	 * 9.������д������
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
	 * 10.ָ�������ȡ����
	 * @param reader
	 * @param bank ����
	 * @param begin ��ʼ��ַ
	 * @param size ����
	 * @param password
	 * @return value|null
	 */
	String readTagData(Reader reader,byte bank, byte begin,byte size,byte[] password);
	/**
	 * 11.����ǩ
	 * @param reader
	 * @param lockType 
	 * @param lockBank
	 * @param password
	 * @return ture|false
	 */
	boolean lockTag(Reader reader, byte lockType, byte lockBank,byte[] password);
	/**
	 * 12.��ȡ������״̬(0.�ر�|1.��)
	 * @param reader
	 * @return
	 */
	int getBuzzer(Reader reader);
	/**
	 * 13.���÷�����״̬(0.�ر�|1.��)
	 * @param reader
	 * @param state
	 * @return ture|false
	 */
	boolean setBuzzer(Reader reader, byte state);
	/**
	 * 14.���ù���ģʽ
	 * @param reader
	 * @param mode (01����ģʽ��02��ʱģʽ��03����ģʽ)
	 * @return true|false
	 */
	boolean setWorkMode(Reader reader, int mode);
	/**
	 * 15.��ȡ����ģʽ
	 * @param reader
	 * @return value(01����ģʽ��02��ʱģʽ��03����ģʽ)|-1
	 */
	int getWorkMode(Reader reader);
	/**
	 * 16.�趨������ʱ
	 * @param reader
	 * @param trigTime 
	 * @return true|false
	 */
	boolean setTrigModeDelayTime(Reader reader, byte trigTime);
	/**
	 * 17.��ȡ������ʱ
	 * @param reader
	 * @return value|-1
	 */
	int getTrigModeDelayTime(Reader reader);
	/**
	 * 18.��ȡ�豸��
	 * @param reader 
	 * @return value|null
	 */
	String getDeviceNo(Reader reader);
	/**
	 * 19.�����豸��
	 * @param reader
	 * @param deviceNo
	 * @return true|false
	 */
	boolean setDeviceNo(Reader reader, byte deviceNo);
	/**
	 * 20.���ٱ�ǩ
	 * @param reader
	 * @param accessPwd  ��������
	 * @param killPwd	  ��������
	 * @return 	true|false
	 */
	boolean killTag(Reader reader, byte[] accessPwd, byte[] killPwd);
	/**
	 * 21.���������ģʽ
	 * @param reader
	 * @param outputMode
	 * @return true|false
	 */
	boolean setOutputMode(Reader reader, byte outputMode);
	/**
	 * 22.��ȡ�����ģʽ
	 * @param reader
	 * @return value|-1
	 */
	int getOutputMode(Reader reader);
	/**
	 * 23.��ȡ�����б�
	 * @param reader
	 * @param trigTime
	 * @return 
	 */
	int getNeighJudge(Reader reader);
	/**
	 * 24.���������б�
	 * @param reader
	 * @param neighJudgeTime
	 * @return true|false
	 */
	boolean setNeighJudge(Reader reader, byte neighJudgeTime);
	/**
	 * 25.��ȡ������ʱ
	 * @param reader
	 * @param state
	 */
	int getRelayAutoState(Reader reader);
	/**
	 * 26.���ô�����ʱ
	 * @param reader
	 * @param time
	 * @return
	 */
	boolean setRelayAutoState(Reader reader, byte time);
}
