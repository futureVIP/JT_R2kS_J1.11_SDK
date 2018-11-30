package com.jietong.rfid.uhf.dao;

import java.nio.ByteBuffer;

import com.jietong.rfid.uhf.dao.impl.Reader;
import com.jietong.rfid.uhf.entity.AntStruct;
import com.jietong.rfid.uhf.service.CallBack;
import com.jietong.rfid.uhf.service.CallBackStopReadCard;

public interface ReaderDao {
	Reader connect(String portName, int baudRate);
	
	Reader connect(String portName);

	void deviceUnInit(Reader reader);

	boolean disconnect(Reader reader);

	String version(Reader reader);

	boolean invOnce(Reader reader,CallBack callBack);
	
	boolean beginInv(Reader reader,CallBack callBack);
	
	boolean stopInv(Reader reader,CallBackStopReadCard callBackStopReadCard);

	AntStruct getAnt(Reader reader);

	boolean setAnt(Reader reader, AntStruct ant);

	boolean writeTagData(Reader reader, int bank, int begin, int length,String data, byte[] password);
	
	boolean readTagData(Reader reader,byte bank, byte begin,byte size, ByteBuffer getBuffer, byte[] password);

	boolean lockTag(Reader reader, byte locktType, byte lockBank,byte[] password);

	int getBuzzer(Reader reader);

	boolean setBuzzer(Reader reader, byte state);

	boolean getDI(Reader reader, ByteBuffer buffer);

	boolean setDO(Reader reader, int port, int state);

	boolean setWorkMode(Reader reader, int mode);

	boolean getWorkMode(Reader reader, ByteBuffer workMode);

	boolean setTrigModeDelayTime(Reader reader, byte trigTime);

	boolean getTrigModeDelayTime(Reader reader, ByteBuffer trigTime);

	boolean getNeighJudge(Reader reader, ByteBuffer enableNJ,ByteBuffer neighJudgeTime);

	boolean setNeighJudge(Reader reader, byte neighJudgeTime);

	boolean getDeviceNo(Reader reader, ByteBuffer deviceNo);

	boolean setDeviceNo(Reader reader, byte deviceNo);

	boolean getClock(Reader reader, ByteBuffer clock);

	boolean setClock(Reader reader, byte[] clock);

	boolean getReadZone(Reader reader, ByteBuffer zone);

	boolean getReadZonePara(Reader reader, ByteBuffer bank, ByteBuffer begin,ByteBuffer length);

	boolean setReadZone(Reader reader, byte state);

	boolean setReadZonePara(Reader reader, byte bank, byte begin, byte length);

	boolean getOutputMode(Reader reader, ByteBuffer outputMode);

	boolean setOutputMode(Reader reader, byte outputMode);

	boolean readTagBuffer(Reader reader, CallBack getReadData, int readTime);

	boolean resetTagBuffer(Reader reader);

	boolean killTag(Reader reader, byte[] accessPwd, byte[] killPwd);

	boolean setAlive(Reader reader, byte interval);

	boolean getRelayAutoState(Reader reader, ByteBuffer state);

	boolean setRelayAutoState(Reader reader, byte time);

	boolean getDeviceConfig(Reader reader, ByteBuffer para);

	boolean setDeviceConfig(Reader reader, byte[] para);
}
