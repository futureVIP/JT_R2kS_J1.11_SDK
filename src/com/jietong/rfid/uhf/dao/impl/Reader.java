package com.jietong.rfid.uhf.dao.impl;

import gnu.io.SerialPort;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import serialport.service.SerialPortService;
import serialport.service.impl.SerialPortServiceImpl;
import com.jietong.rfid.uhf.entity.AntStruct;
import com.jietong.rfid.uhf.entity.CMD;
import com.jietong.rfid.uhf.entity.Multichannel16_32Ant;
import com.jietong.rfid.uhf.entity.PACKAGE;
import com.jietong.rfid.uhf.entity.ReaderCard;
import com.jietong.rfid.uhf.entity.StopReaderCard;
import com.jietong.rfid.uhf.service.CallBack;
import com.jietong.rfid.uhf.service.CallBackStopReadCard;
import com.jietong.rfid.uhf.tool.BCC;
import com.jietong.rfid.uhf.tool.ERROR;
import com.jietong.rfid.util.DataConvert;

public class Reader extends PACKAGE {
	
	 void setHost(Reader reader, String host, int baudRate) {
		if (null == reader) {
			return;
		}
		String comm = host.substring(0, 1);
		reader.isSerialPortConnect = comm.equals("C");
		reader.host = host;
		reader.port = baudRate;
	}
	 
	void setHost(Reader reader, String host) {
		String comm = host.substring(0, 1);
		reader.isSerialPortConnect = (comm.equals("C") || comm.equals("c"));
		reader.host = host;
		if (reader.isSerialPortConnect) {
			reader.port = 115200;// 卓岚设置为230400
		} else {
			reader.port = 20058;
		}
	}

	 boolean connect(Reader reader) {
		if (null == reader) {
			return false;
		}
		boolean flag = false;
		if (reader.isSerialPortConnect) {
			try {
				SerialPortService serviceCom = new SerialPortServiceImpl();
				reader.serialPorts = serviceCom.open(reader.host, reader.port);
				if (null != reader.serialPorts) {
					flag = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		reader.headCount = 0;
		reader.dataCount = 0;
		return flag;
	}

	 void disconnect(Reader reader) {
		if (null == reader) {
			return;
		}
		if (reader.deviceConnected) {
			if (reader.isSerialPortConnect) {
				SerialPortService serviceCom = new SerialPortServiceImpl();
				serviceCom.close(reader.serialPorts);
				reader.serialPorts = null;
			}
			reader.deviceConnected = false;
		}
	}

	 boolean comSendData(SerialPort serialPort, byte[] sendData) {
		 SerialPortService serviceCom = new SerialPortServiceImpl();
		return serviceCom.send(serialPort, sendData);
	}

	void sendInfoShow(byte[] receiveData) {
		System.out.println("\n");
		System.out.print("发送的命令: ");
		for (int i = 0; i < receiveData.length; i++) {
			String data = DataConvert.bytesToHexString(receiveData[i]);
			System.out.print(data + " ");
		}
		System.out.println();
	}

	 boolean sendData(Reader reader, byte cmd, byte[] sendBuf,
			int bufsize) {
		if (null == reader) {
			return false;
		}
		reader.startcode[0] = CMD.NET_START_CODE1;
		reader.startcode[1] = CMD.NET_START_CODE2;
		reader.cmd = cmd;
		reader.seq = 0;
		reader.len[0] = (byte) bufsize;
		reader.len[1] = (byte) (bufsize / 256);
		reader.bcc = 0;
		if (bufsize > 0) {
			reader.data = Arrays.copyOf(sendBuf, bufsize + 1);
			reader.bcc = BCC.checkSum(sendBuf, bufsize);
		} else {
			reader.data = Arrays.copyOf(reader.data, 1);
		}
		reader.data[bufsize] = reader.bcc;
		byte[] receiveData = getSendCMD(bufsize);
		sendInfoShow(receiveData);
		boolean size = false;
		if (reader.isSerialPortConnect) {
			size = comSendData(reader.serialPorts, receiveData);
		} else {

		}
		return size;
	}

	 byte[] comReceiveData(Reader reader) {
		if (null == reader) {
			return null;
		}
		SerialPortService serviceCom = new SerialPortServiceImpl();
		byte [] result = serviceCom.read(reader.serialPorts);
		System.out.println("原始数据: " + DataConvert.bytesToHexString(result));
		return result;
	}

	 byte[] socketRecv(Reader reader) {
		if (null == reader) {
			return null;
		}
		return null;
	}

	boolean trandPackage(Reader reader, byte data,ByteBuffer buffer, ByteBuffer returnLength) {
		if (null == reader) {
			return false;
		}
		if (reader.headCount < CMD.HEAD_LENGTH) {
			switch (reader.headCount) {
			case 0: 
				if (data == CMD.NET_START_CODE1) {
					reader.headCount++;
				}
				break;
			case 1: 
				if (data == CMD.NET_START_CODE2) {
					reader.headCount++;
				}
				break;
			case 2: 
				reader.cmd = data;
				reader.headCount++;
				break;
			case 3: 
				reader.seq = data;
				reader.headCount++;
				break;
			case 4:
				reader.receiveLength = DataConvert.byteToInt(data);
				reader.len[0] = data;
				buffer.clear();
				returnLength.clear();
				returnLength.put(data);
				reader.headCount++;
				break;
			case 5:
				reader.len[1] = data;
				reader.headCount++;
				break;
			}
		} else if (reader.dataCount < reader.receiveLength) {
			buffer.put(data);
			reader.dataCount++;
		} else {
			byte[] bufData = Arrays.copyOf(buffer.array(), reader.receiveLength);
			reader.bcc = BCC.checkSum(bufData, returnLength.array()[0]);
			if (reader.bcc == data) {
				reader.headCount = 0;
				reader.dataCount = 0;
				return true;
			} else {
				reader.headCount = 0;
				reader.dataCount = 0;
				return false;
			}
		}
		return false;
	}

	boolean readData(Reader reader, byte cmd, ByteBuffer buffer,
			int length) {
		if (null == reader) {
			return false;
		}
		boolean flag = false;
		long begin = System.currentTimeMillis();
		long timeout = 1000;
		boolean once = false;
		byte[] receiveVal = null;
		while (reader.deviceConnected) {
			long end = System.currentTimeMillis();
			if (end - begin > timeout) {
				// return flag;
			}
			if (once) {
				return flag;
			}
			once = true;
			if (reader.isSerialPortConnect) {
				receiveVal = comReceiveData(reader);
			} else {
				receiveVal = socketRecv(reader);
			}
			if (null != receiveVal) {
				ByteBuffer receiveBuf = ByteBuffer.allocate(50);
				ByteBuffer receiveLength = ByteBuffer.allocate(1);
				for (int i = 0; i < receiveVal.length; i++) {
					if (trandPackage(reader, receiveVal[i], receiveBuf,receiveLength)) {
						if (reader.cmd == cmd) {
							int _length = DataConvert.byteToInt(receiveLength.array()[0]);
							byte[] _readData = Arrays.copyOf(receiveBuf.array(), _length);
							if (null != buffer && buffer.limit() > 0) {
								buffer.put(_readData);// 去掉附加的数据长度
							}
							flag = true;
						}
					}
				}
			}
		}
		return flag;
	}

	boolean compareStartCode(Reader reader) {
		if (null == reader) {
			return false;
		}
		if (reader.startcode[0] == CMD.NET_START_CODE1
				&& reader.startcode[1] == CMD.NET_START_CODE2) {
			return true;
		}
		return false;
	}

	 boolean version(Reader reader, ByteBuffer buffer) {
		if (reader == null) {
			return false;
		}
		if (sendData(reader, CMD.UHF_GET_VERSION, null, 0)) {
			if (readData(reader, CMD.UHF_GET_VERSION, buffer,
					CMD.VERSION_LENGTH)) {
				if (compareStartCode(reader)) {
					return true;
				}
			}
		}
		return false;
	}

	 boolean stopInv(Reader reader,
			CallBackStopReadCard callBackStopReadCard) {
		if (reader == null) {
			return false;
		}
		if (!reader.threadStart) {
			return true;
		}
		reader.threadStart = false; // 设置线程结束标志
		// ByteBuffer buffer = ByteBuffer.allocate(100);
		if (sendData(reader, CMD.UHF_INV_MULTIPLY_END, null, 0)) {
			StopReaderCard stopReaderCard = new StopReaderCard(reader,callBackStopReadCard);
			Thread thread = new Thread(stopReaderCard);
			thread.start();
			// if (readData(reader, CMD.UHF_INV_MULTIPLY_END, buffer, 1)) {
			// if (reader.data[0] != ERROR.HOST_ERROR) {
			// return true;
			// }
			// }
		}
		return false;
	}

	 boolean getAnt(Reader reader, ByteBuffer buffer) {
		if (null == reader) {
			return false;
		}
		if (sendData(reader, CMD.UHF_GET_ANT_CONFIG, null, 0)) {
			if (readData(reader, CMD.UHF_GET_ANT_CONFIG, buffer,
					CMD.ANT_CFG_LENGTH)) {
				if (compareStartCode(reader)) {
					return true;
				}
			}
		}
		return false;
	}

	 boolean invOnce(Reader reader, CallBack callBack) {
		if (null == reader) {
			return false;
		}
		if (!reader.deviceConnected) {
			return false;
		}
		// 一次寻卡时，读取天线工作状态, 用于控制线程何时结束
		ByteBuffer buffer = ByteBuffer.allocate(100);
		boolean getAnt = getAnt(reader, buffer);
		if (!getAnt) {
			return false;
		}
		// 工作天线状态设为0,需等待结束包代表完成寻卡
		reader.threadStart = true;
		reader.headCount = 0;
		reader.dataCount = 0;
		if (sendData(reader, CMD.UHF_INV_ONCE, null, 0)) {
			ReaderCard readerCard = new ReaderCard(reader, callBack);
			Thread thread = new Thread(readerCard);
			thread.start();
			return true;
		}
		return false;
	}

	 boolean beginInv(Reader reader, CallBack callBack) {
		if (null == reader) {
			return false;
		}
		reader.threadStart = false;
		reader.stopRead = false;
		boolean ret = false;
		if (!reader.deviceConnected) {
			return false;
		}
		reader.headCount = 0;
		reader.dataCount = 0;
		if (sendData(reader, CMD.UHF_INV_MULTIPLY_BEGIN, null, 0)) {
			reader.threadStart = true;
			ReaderCard readThread = new ReaderCard(reader, callBack);
			Thread loopThread = new Thread(readThread);
			loopThread.start();
			ret = true;
		} else {
			ret = false;
		}
		return ret;
	}

	 byte[] deviceReadBuffer(Reader reader) {
		if (null == reader) {
			return null;
		}
		byte[] buffer = null;
		if (null != reader) {
			if (reader.isSerialPortConnect) {
				buffer = comReceiveData(reader);
			} else {
				// size = reader.socketRecv(reader);// 设置1秒超时
			}
		}
		return buffer;
	}
	
	public List<String> filterEpcAndAnt(byte[] readData){
		List<String> epcAndAnt = null;
		if (readData.length == 16) {
			epcAndAnt = new ArrayList<String>();
			String total = DataConvert.bytesToHexString(readData);
			String antten = total.substring(total.length() - 2,	total.length());
			int ant = Integer.parseInt(antten, 16);
			String EPC = DataConvert.bytesToHexString(Arrays.copyOf(readData, 12));
			epcAndAnt.add(EPC);
			epcAndAnt.add(String.valueOf(ant + 1));
		}
		return epcAndAnt;
	}

	 void deviceTransBuffer(Reader reader, byte[] buffer,CallBack callBack) {
		if (null == reader) {
			return;
		}
		ByteBuffer receiveBuf = ByteBuffer.allocate(50);
		ByteBuffer receiveLength = ByteBuffer.allocate(1);
		List<String> epcAndAnt = null;
		for (int i = 0; i < buffer.length; i++) {
			if (trandPackage(reader, buffer[i], receiveBuf,receiveLength)) {
				int length = DataConvert.byteToInt(receiveLength.array()[0]);
				byte[] readData = Arrays.copyOf(receiveBuf.array(), length);
				epcAndAnt = filterEpcAndAnt(readData);
				switch (reader.cmd) {
				case 0x25:// 寻卡一次
					if (length == 16) {
						callBack.readData(epcAndAnt.get(0),null, epcAndAnt.get(1));
					}
					// 检查是不是结束包
					if (2 == length) {// 某天线寻卡结束数据包
						String data = DataConvert.bytesToHexString(readData[1]);
						if(data.equals("F0")){
							reader.threadStart = false; // 设置线程结束标志
						}
					}
					break;
				case 0x2A:// 连续寻卡模式，返回数据
					if (length == 16) {
						callBack.readData(epcAndAnt.get(0),null, epcAndAnt.get(1));
					}
					break;
				case 0x2B:// 连续寻卡模式，返回数据
					if (readData[0] != ERROR.HOST_ERROR) {
						reader.stopRead = true;
					}
					break;
				default:
					break;
				}
			}
		}
	}

	byte[] setAnt(AntStruct antStruct) {
		if (antStruct.state == 6 || antStruct.state == 4) {
			return setAnt4(antStruct);
		} else if (antStruct.state == 32) {
			return new Multichannel16_32Ant().setAnt32(antStruct);
		} else if (antStruct.state == 16) {
			return new Multichannel16_32Ant().setAnt16(antStruct);
		}
		return null;
	}

	byte[] setAnt4(AntStruct antStruct) {
		ByteBuffer sendAnt = ByteBuffer.allocate(36);
		byte[] antenner = new byte[4];
		for (int i = 0; i < antenner.length; i++) {
			antenner[i] = antStruct.enable[i];
		}
		sendAnt.put(antenner);
		byte[] time = new byte[4];
		for (int i = 0; i < time.length; i++) {
			time = DataConvert.intToByteArray(antStruct.dwellTime[i]);
			sendAnt.put(time);
		}
		byte[] power = new byte[4];
		for (int i = 0; i < power.length; i++) {
			power = DataConvert.intToByteArray(antStruct.power[i]);
			sendAnt.put(power);
		}
		return sendAnt.array();
	}

	 boolean setAnt(Reader reader, AntStruct ant) {
		if (null == reader) {
			return false;
		}
		ByteBuffer buffer = ByteBuffer.allocate(100);
		byte[] sendAntData = setAnt(ant);
		if (null == sendAntData) {
			return false;
		}
		if (sendData(reader, CMD.UHF_SET_ANT_CONFIG, sendAntData,CMD.ANT_CFG_LENGTH)) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (readData(reader, CMD.UHF_SET_ANT_CONFIG, buffer, 1)) {
				if (compareStartCode(reader) && reader.data[0] == 0) {
					return true;
				}
			}
		}
		return false;
	}

	 public void threadFunc(final Reader reader, final CallBack callBack) {
		if (null == reader) {
			return;
		}
		boolean exit = true;
		do {
			final byte[] buffer = reader.deviceReadBuffer(reader);
			if (null != buffer) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						reader.deviceTransBuffer(reader, buffer, callBack);
					}
				}).start();
			}
			if (!reader.threadStart) {
				if (null == buffer) {
					exit = reader.threadStart;
				}
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (exit);
		// 清理线程资源
		reader.receiveLength = 0;
		reader.headCount = 0;
		reader.dataCount = 0;
	}

	 boolean writeTagData(Reader reader, int bank, int begin,
			int length, String data, byte[] password) {
		if (null == reader) {
			return false;
		}
		byte buf[] = new byte[256];
		int bufsize = 3 + length * 2 + 4;// length是字
		buf[0] = (byte) bank;
		buf[1] = (byte) begin;
		buf[2] = (byte) length;
		System.arraycopy(password, 0, buf, 3, 4);
		byte[] inData = new byte[data.length() / 2];
		int count = 0;
		for (int i = 0; i < inData.length; i++) {
			int result = Integer.parseInt(data.substring(count, count + 2), 16);
			count += 2;
			inData[i] = (byte) result;
		}
		System.arraycopy(inData, 0, buf, 3 + 4, length * 2);// 要写入的数据
		ByteBuffer buffer = ByteBuffer.allocate(20);
		if (sendData(reader, CMD.UHF_WRITE_TAG_DATA, buf, bufsize)) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (readData(reader, CMD.UHF_WRITE_TAG_DATA, buffer, 1)) {
				if (reader.data[0] != ERROR.HOST_ERROR) {
					return true;
				}
			}
		}
		return false;
	}

	 boolean lockTag(Reader reader, byte locktType, byte lockBank,
			byte[] password) {
		if (null == reader) {
			return false;
		}
		if (!reader.deviceConnected) {
			return false;
		}
		if (password.length < 1) {
			return false;
		}
		if (locktType < 0 || locktType > 3) {
			return false;
		}
		if (lockBank < 0 || lockBank > 4) {
			return false;
		}
		byte buf[] = new byte[12];
		buf[0] = locktType;
		buf[1] = lockBank;
		System.arraycopy(password, 0, buf, 2, password.length);
		ByteBuffer buffer = ByteBuffer.allocate(100);
		if (sendData(reader, CMD.UHF_LOCK_TAG, buf, 6)) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (readData(reader, CMD.UHF_LOCK_TAG, buffer, 1)) {
				if (reader.data[0] != ERROR.HOST_ERROR) {
					return true;
				}
			}
		}
		return false;
	}

	 boolean getBuzzer(Reader reader, ByteBuffer buffer) {
		if (null == reader) {
			return false;
		}
		if (sendData(reader, CMD.UHF_GET_BUZZ, null, 0)) {
			if (readData(reader, CMD.UHF_GET_BUZZ, buffer, 0)) {
				if (reader.data[0] != ERROR.HOST_ERROR) {
					return true;
				}
			}
		}
		return false;
	}

	 boolean setBuzzer(Reader reader, byte buzz) {
		if (null == reader) {
			return false;
		}
		byte[] buf = new byte[2];
		buf[0] = buzz;
		ByteBuffer buffer = ByteBuffer.allocate(10);
		if (sendData(reader, CMD.UHF_SET_BUZZ, buf, 1)) {
			if (readData(reader, CMD.UHF_SET_BUZZ, buffer, 0)) {
				if (reader.data[0] != ERROR.HOST_ERROR) {
					return true;
				}
			}
		}
		return false;
	}

	 boolean getDI(Reader reader, ByteBuffer buffer) {
		if (null == reader) {
			return false;
		}
		if (sendData(reader, CMD.UHF_GET_DI_STATE, null, 0)) {
			if (readData(reader, CMD.UHF_GET_DI_STATE, buffer, 2)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 设置Digital Output状态
	 */
	 boolean setDO(Reader reader, int port, int state) {
		if (null == reader) {
			return false;
		}
		byte buf[] = new byte[32];
		if (port > 2 || port == 0) {
			return false;
		}
		buf[0] = (byte) port;
		buf[1] = (byte) state;
		ByteBuffer buffer = ByteBuffer.allocate(10);
		if (sendData(reader, CMD.UHF_SET_DO_STATE, buf, 2)) {
			if (readData(reader, CMD.UHF_SET_DO_STATE, buffer, 1)) {
				if (reader.data[0] != ERROR.HOST_ERROR) {
					return true;
				}
			}
		}
		return false;
	}

	 boolean setWorkMode(Reader reader, int mode) {
		if (null == reader) {
			return false;
		}
		byte buf[] = new byte[8];
		int bufsize = 1;
		buf[0] = (byte) mode;
		ByteBuffer buffer = ByteBuffer.allocate(100);
		if (sendData(reader, CMD.UHF_SET_MODE, buf, bufsize)) {
			if (readData(reader, CMD.UHF_SET_MODE, buffer, 1)) {
				if (reader.data[0] != ERROR.HOST_ERROR) {
					return true;
				}
			}
		}
		return false;
	}

	 boolean getWorkMode(Reader reader, ByteBuffer workMode) {
		if (null == reader) {
			return false;
		}
		ByteBuffer buffer = ByteBuffer.allocate(1);
		if (sendData(reader, CMD.UHF_GET_MODE, null, 0)) {
			if (readData(reader, CMD.UHF_GET_MODE, buffer, 1)) {
				workMode.put(buffer.array()[0]);
				return true;
			}
		}
		return false;
	}

	 boolean setTrigModeDelayTime(Reader reader, byte trigTime) {
		if (null == reader) {
			return false;
		}
		byte buf[] = new byte[16];
		buf[0] = trigTime;
		ByteBuffer buffer = ByteBuffer.allocate(100);
		if (sendData(reader, CMD.UHF_SET_TRIGGER_TIME, buf, 1)) {
			if (readData(reader, CMD.UHF_SET_TRIGGER_TIME, buffer, 1)) {
				return true;
			}
		}
		return false;
	}

	 boolean getTrigModeDelayTime(Reader reader, ByteBuffer trigTime) {
		if (null == reader) {
			return false;
		}
		ByteBuffer buffer = ByteBuffer.allocate(100);
		if (sendData(reader, CMD.UHF_GET_TRIGGER_TIME, null, 0)) {
			if (readData(reader, CMD.UHF_GET_TRIGGER_TIME, buffer, 1)) {
				trigTime.put(buffer.array()[0]);
				return true;
			}
		}
		return false;
	}

	 boolean getNeighJudge(Reader reader, ByteBuffer enableNJ,
			ByteBuffer neighJudgeTime) {
		if (null == reader) {
			return false;
		}
		ByteBuffer buffer = ByteBuffer.allocate(2);
		if (sendData(reader, CMD.UHF_GET_TAG_FILTER, null, 0)) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (readData(reader, CMD.UHF_GET_TAG_FILTER, buffer, 2)) {
				enableNJ.put(buffer.array()[0]);
				neighJudgeTime.put(buffer.array()[1]);
				return true;
			}
		}
		return false;
	}

	 boolean setNeighJudge(Reader reader, byte neighJudgeTime) {
		if (null == reader) {
			return false;
		}
		int bufsize = 2;
		byte[] buf = new byte[16];
		buf[0] = (byte) (neighJudgeTime > 0 ? 1 : 0); // time为0,
														// 取消相邻判定，非0，设置相邻判定
		buf[1] = neighJudgeTime;
		ByteBuffer buffer = ByteBuffer.allocate(100);
		if (sendData(reader, CMD.UHF_SET_TAG_FILTER, buf, bufsize)) {
			if (readData(reader, CMD.UHF_SET_TAG_FILTER, buffer, 1)) {
				if (reader.data[0] != ERROR.HOST_ERROR) {
					return true;
				}
			}
		}
		return false;
	}

	 boolean getDeviceNo(Reader reader, ByteBuffer deviceNo) {
		if (null == reader) {
			return false;
		}
		if (sendData(reader, CMD.UHF_GET_DEVICE_NO, null, 0)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (readData(reader, CMD.UHF_GET_DEVICE_NO, deviceNo, 1)) {
				if (reader.data[0] != ERROR.HOST_ERROR) {
					return true;
				}
			}
		}
		return false;
	}

	 boolean setDeviceNo(Reader reader, byte deviceNo) {
		if (null == reader) {
			return false;
		}
		byte buf[] = new byte[16];
		int bufsize = 1;
		buf[0] = deviceNo;
		ByteBuffer buffer = ByteBuffer.allocate(100);
		if (sendData(reader, CMD.UHF_SET_DEVICE_NO, buf, bufsize)) {
			if (readData(reader, CMD.UHF_SET_DEVICE_NO, buffer, 1)) {
				if (reader.data[0] != ERROR.HOST_ERROR) {
					return true;
				}
			}
		}
		return false;
	}

	 boolean getClock(Reader reader, ByteBuffer clock) {
		if (null == reader) {
			return false;
		}
		ByteBuffer buffer = ByteBuffer.allocate(10);
		if (sendData(reader, CMD.UHF_GET_CLOCK, null, 0)) {
			if (readData(reader, CMD.UHF_GET_CLOCK, buffer, 6)) {
				if (reader.data[0] != ERROR.HOST_ERROR) {
					clock.put(Arrays.copyOf(buffer.array(), 6));
					return true;
				}
			}
		}
		return false;
	}

	 boolean setClock(Reader reader, byte[] clock) {
		if (null == reader) {
			return false;
		}
		byte buf[] = new byte[16];
		System.arraycopy(clock, 0, buf, 0, 6);
		ByteBuffer buffer = ByteBuffer.allocate(10);
		if (sendData(reader, CMD.UHF_SET_CLOCK, buf, 6)) {
			if (readData(reader, CMD.UHF_SET_CLOCK, buffer, 1)) {
				if (reader.data[0] != ERROR.HOST_ERROR) {
					return true;
				}
			}
		}
		return false;
	}

	 boolean getReadZone(Reader reader, ByteBuffer zone) {
		if (null == reader) {
			return false;
		}
		ByteBuffer buffer = ByteBuffer.allocate(16);
		if (sendData(reader, CMD.UHF_GET_READ_ZONE, null, 0)) {
			if (readData(reader, CMD.UHF_GET_READ_ZONE, buffer, 1)) {
				if (reader.data[0] != ERROR.HOST_ERROR) {
					zone.put(buffer.array()[0]);
					return true;
				}
			}
		}
		return false;
	}

	 boolean getReadZonePara(Reader reader, ByteBuffer bank,
			ByteBuffer begin, ByteBuffer length) {
		if (null == reader) {
			return false;
		}
		ByteBuffer buffer = ByteBuffer.allocate(16);
		if (sendData(reader, CMD.UHF_GET_READZONE_PARA, null, 0)) {
			if (readData(reader, CMD.UHF_GET_READZONE_PARA, buffer, 3)) {
				if (reader.data[0] != ERROR.HOST_ERROR) {
					bank.put(buffer.array()[0]);
					begin.put(buffer.array()[1]);
					length.put(buffer.array()[2]);
					return true;
				}
			}
		}
		return false;
	}

	 boolean setReadZone(Reader reader, byte state) {
		if (null == reader) {
			return false;
		}
		byte buf[] = new byte[16];
		ByteBuffer buffer = ByteBuffer.allocate(16);
		if (0 == state) {
			buf[0] = 0;
		} else {
			buf[0] = 1;
		}
		if (sendData(reader, CMD.UHF_SET_READ_ZONE, buf, 1)) {
			if (readData(reader, CMD.UHF_SET_READ_ZONE, buffer, 1)) {
				return true;
			}
		}
		return false;
	}

	 boolean setReadZonePara(Reader reader, byte bank, byte begin,
			byte length) {
		if (null == reader) {
			return false;
		}
		byte buf[] = new byte[16];
		buf[0] = bank;
		buf[1] = begin;
		buf[2] = length;
		ByteBuffer buffer = ByteBuffer.allocate(16);
		if (sendData(reader, CMD.UHF_SET_READZONE_PARA, buf, 3)) {
			if (readData(reader, CMD.UHF_SET_READZONE_PARA, buffer, 1)) {
				return true;
			}
		}
		return false;
	}

	 boolean getOutputMode(Reader reader, ByteBuffer outputMode) {
		if (null == reader) {
			return false;
		}
		ByteBuffer buffer = ByteBuffer.allocate(16);
		if (sendData(reader, CMD.UHF_GET_OUTPUT, null, 0)) {
			if (readData(reader, CMD.UHF_GET_OUTPUT, buffer, 1)) {
				outputMode.put(buffer.array()[0]);
				return true;
			}
		}
		return false;
	}

	 boolean setOutputMode(Reader reader, byte outputMode) {
		if (null == reader) {
			return false;
		}
		byte buf[] = new byte[16];
		buf[0] = outputMode;
		ByteBuffer buffer = ByteBuffer.allocate(16);
		if (sendData(reader, CMD.UHF_SET_OUTPUT, buf, 1)) {
			if (readData(reader, CMD.UHF_SET_OUTPUT, buffer, 1)) {
				return true;
			}
		}
		return false;
	}

	 boolean readTagBuffer(Reader reader, CallBack getReadData,
			int readTime) {
		if (null == reader) {
			return false;
		}
		if (sendData(reader, CMD.UHF_GET_TAG_BUFFER, null, 0)) {
			// 暂未做
			return true;
		}
		return false;
	}

	 boolean resetTagBuffer(Reader reader) {
		if (null == reader) {
			return false;
		}
		ByteBuffer buffer = ByteBuffer.allocate(16);
		if (sendData(reader, CMD.UHF_RESET_TAG_BUFFER, null, 0)) {
			if (readData(reader, CMD.UHF_RESET_TAG_BUFFER, buffer, 1)) {
				return true;
			}
		}
		return false;
	}

	 boolean killTag(Reader reader, byte[] accessPwd, byte[] killPwd) {
		if (null == reader) {
			return false;
		}
		byte buf[] = new byte[16];
		System.arraycopy(killPwd, 0, buf, 0, 4);
		System.arraycopy(accessPwd, 0, buf, 4, 4);
		ByteBuffer buffer = ByteBuffer.allocate(100);
		if (sendData(reader, CMD.UHF_KILL_TAG, buf, 8)) {
			if (readData(reader, CMD.UHF_KILL_TAG, buffer, 1)) {
				if (reader.data[0] != ERROR.HOST_ERROR) {
					return true;
				}
			}
		}
		return false;
	}

	 boolean setAlive(Reader reader, byte interval) {
		if (reader == null) {
			return false;
		}
		byte buf[] = new byte[16];
		buf[0] = interval;
		ByteBuffer buffer = ByteBuffer.allocate(100);
		if (sendData(reader, CMD.UHF_ALIVE, buf, 1)) {
			if (readData(reader, CMD.UHF_ALIVE, buffer, 1)) {
				return true;
			}
		}
		return false;
	}

	 boolean getRelayAutoState(Reader reader, ByteBuffer state) {
		if (reader == null) {
			return false;
		}
		ByteBuffer buffer = ByteBuffer.allocate(100);
		if (sendData(reader, CMD.UHF_GET_TRIGGER_TIME, null, 0)) {
			if (readData(reader, CMD.UHF_GET_TRIGGER_TIME, buffer, 1)) {
				state.put(buffer.array()[0]);
				return true;
			}
		}
		return false;
	}

	 boolean setRelayAutoState(Reader reader, byte time) {
		if (reader == null) {
			return false;
		}
		byte buf[] = new byte[16];
		buf[0] = time;
		ByteBuffer buffer = ByteBuffer.allocate(100);
		if (sendData(reader, CMD.UHF_SET_TRIGGER_TIME, buf, 1)) {
			if (readData(reader, CMD.UHF_SET_TRIGGER_TIME, buffer, 1)) {
				return true;
			}
		}
		return false;
	}

	 boolean getDeviceConfig(Reader reader, ByteBuffer para) {
		if (reader == null) {
			return false;
		}
		ByteBuffer buffer = ByteBuffer.allocate(100);
		if (sendData(reader, CMD.UHF_GET_CONFIGURE, null, 0)) {
			if (readData(reader, CMD.UHF_GET_CONFIGURE, buffer, 20)) {
				return true;
			}
		}
		return false;
	}

	 boolean setDeviceConfig(Reader reader, byte[] para) {
		if (reader == null) {
			return false;
		}
		byte buf[] = new byte[128];
		byte bufSize = 20;
		System.arraycopy(para, 0, buf, 0, bufSize);
		ByteBuffer buffer = ByteBuffer.allocate(100);
		if (sendData(reader, CMD.UHF_SET_CONFIGURE, buf, bufSize)) {
			if (readData(reader, CMD.UHF_SET_CONFIGURE, buffer, 1)) {
				return true;
			}
		}
		return false;
	}

	boolean readTagData(Reader reader, byte bank, byte begin, byte size, ByteBuffer getBuffer, byte[] password) {
		if (null == reader) {
			return false;
		}
		if (getBuffer.limit() < 1) {
			return false;
		}
		if (bank == 0) {// 保留区
			if (begin + size > 4) {
				return false;
			}
		} else if (bank == 1) { // EPC区
			if (begin + size > 8) {
				return false;
			}
		} else if (bank == 2) { // TID区
			if (begin + size > 6) {
				return false;
			}
		} else if (bank == 3) { // 用户区
			if (begin + size > 32) {
				return false;
			}
		} else { // 无效的bank值
			return false;
		}
		byte sendBuf[] = new byte[256];
		int bufsize = 7;
		sendBuf[0] = (byte) bank;
		sendBuf[1] = (byte) begin;
		sendBuf[2] = (byte) size;
		System.arraycopy(password, 0, sendBuf, 3, 4);
		if (sendData(reader, CMD.UHF_READ_TAG_DATA, sendBuf, bufsize)) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 用来接收数据存放的buffer
			ByteBuffer buffer = ByteBuffer.allocate(20);
			if (readData(reader, CMD.UHF_READ_TAG_DATA, buffer, size * 2)) {
				if (reader.data[0] != ERROR.HOST_ERROR) {
					byte[] data = reader.data;
					for (int i = 0; i < data.length; i++) {
						getBuffer.put(data[i]);
					}
					return true;
				}
				return false;
			}
		}
		return false;
	}
}


