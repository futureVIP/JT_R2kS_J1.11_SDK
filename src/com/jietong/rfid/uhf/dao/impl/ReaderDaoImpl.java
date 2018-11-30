package com.jietong.rfid.uhf.dao.impl;

import java.nio.ByteBuffer;
import com.jietong.rfid.uhf.dao.ReaderDao;
import com.jietong.rfid.uhf.entity.AntStruct;
import com.jietong.rfid.uhf.entity.Multichannel16_32Ant;
import com.jietong.rfid.uhf.entity.MultichannelAnt4;
import com.jietong.rfid.uhf.service.CallBack;
import com.jietong.rfid.uhf.service.CallBackStopReadCard;
import com.jietong.rfid.uhf.tool.RFIDException;

public class ReaderDaoImpl implements ReaderDao {
	@Override
	public Reader connect(String portName, int baudRate) {
		Reader reader = new Reader();
		reader.setHost(reader, portName, baudRate);
		if (!reader.connect(reader)) {
			reader.deviceConnected = false;
			reader = null;
		} else {
			reader.deviceConnected = true;
		}
		return reader;
	}
	
	@Override
	public Reader connect(String portName) {
		Reader reader = new Reader();
		reader.setHost(reader, portName);
		if (!reader.connect(reader)) {
			reader.deviceConnected = false;
			reader = null;
		} else {
			reader.deviceConnected = true;
		}
		return reader;
	}

	@Override
	public void deviceUnInit(Reader reader) {
		if (null == reader) {
			return;
		}
		if (!reader.isSerialPortConnect) {
			reader.threadStart = false;
		}
		reader.isSerialPortConnect = false;
		reader.serialPortNo = 0;
		reader.headCount = 0;
		reader.dataCount = 0;
	}

	@Override
	public boolean disconnect(Reader reader) {
		if (null == reader) {
			return false;
		}
		if (!reader.deviceConnected) {
			return false;
		} else {
			reader.disconnect(reader);
		}
		return true;
	}

	@Override
	public String version(Reader reader) {
		if (null == reader) {
			return null;
		}
		ByteBuffer buffer = ByteBuffer.allocate(16);
		boolean version = reader.version(reader, buffer);
		if (version) {
			String ver = new String(buffer.array()).trim();
			reader.setVersion(ver);
			return ver;
		}
		return null;
	}

	@Override
	public boolean invOnce(Reader reader,CallBack callBack) {
		if (null == reader) {
			return false;
		}
		return reader.invOnce(reader,callBack);
	}

	@Override
	public boolean beginInv(Reader reader,CallBack getReadData) {
		if (null == reader) {
			return false;
		}
		return reader.beginInv(reader,getReadData);
	}

	@Override
	public boolean stopInv(Reader reader,CallBackStopReadCard callBackStopReadCard) {
		if (null == reader) {
			return false;
		}
		return reader.stopInv(reader,callBackStopReadCard);
	}
	
	@Override
	public AntStruct getAnt(Reader reader) {
		if (null == reader) {
			return null;
		}
		if (!reader.deviceConnected) {
			return null;
		}
		AntStruct struct = new AntStruct(reader.getChannel());
		ByteBuffer total = ByteBuffer.allocate(36);
		reader.getAnt(reader, total);
		byte[] buffer = total.array();
		if (struct.state == 4 || struct.state == 6) {
			return new MultichannelAnt4().ant4(reader, buffer);
		} else {
			return new Multichannel16_32Ant().ant32(reader, buffer);
		}
	}

	@Override
	public boolean setAnt(Reader reader, AntStruct ant) {
		if (null == reader) {
			return false;
		}
		boolean result = reader.setAnt(reader, ant);
		return result;
	}

	@Override
	public boolean writeTagData(Reader reader, int bank, int begin, int size,String data, byte[] password) {
		if (null == reader) {
			return false;
		}
		if (bank < 0 || begin < 0 || size < 0) {
			try {
				throw new RFIDException("bank/begin/size Must be a positive integer£¡");
			} catch (RFIDException e) {
				e.printStackTrace();
			}
		}
		if (bank > 3) {
			try {
				throw new RFIDException("bank cannot be anything but 0-3");
			} catch (RFIDException e) {
				e.printStackTrace();
			}
		}
		if (bank == 1 && (begin + size > 8 || begin < 2)) {
			try {
				throw new RFIDException(
						"When writing the contents of the EPC region, begin must start at 2, and begin(write the address in the region)+size(the length to be written) has a value of no more than 8.Check the input parameter values");
			} catch (RFIDException e) {
				e.printStackTrace();
			}
		}
		if (bank == 0 && (begin + size > 4)) {
			try {
				throw new RFIDException("When writing the tenure contents, begin(the address in the write field)+size(the length to be written) does not exceed 4.Check the input parameter values");
			} catch (RFIDException e) {
				e.printStackTrace();
			}
		}
		if (data.length() != 4 * size) {
			try {
				throw new RFIDException("The length of data must be size*4");
			} catch (RFIDException e) {
				e.printStackTrace();
			}
		}
		if (password == null || "".equals(password)) {
			for (int i = 0; i < 4; ++i) {
				password[0] = (byte) 0;
			}
		}
		return reader.writeTagData(reader, bank, begin, size, data, password);
	}

	@Override
	public boolean lockTag(Reader reader, byte locktType, byte lockBank,
			byte[] password) {
		if (null == reader) {
			return false;
		}
		return reader.lockTag(reader, locktType, lockBank, password);
	}

	@Override
	public int getBuzzer(Reader reader) {
		int total = -1;
		if (null == reader) {
			return total;
		}
		ByteBuffer buffer = ByteBuffer.allocate(1);
		boolean result = reader.getBuzzer(reader, buffer);
		if (result) {
			total = buffer.array()[0];
		}
		return total;
	}

	@Override
	public boolean setBuzzer(Reader reader, byte state) {
		if (null == reader) {
			return false;
		}
		return reader.setBuzzer(reader, state);
	}

	@Override
	public boolean getDI(Reader reader, ByteBuffer state) {
		if (null == reader) {
			return false;
		}
		return reader.getDI(reader, state);
	}

	@Override
	public boolean setDO(Reader reader, int port, int state) {
		if (null == reader) {
			return false;
		}
		return reader.setDO(reader, port, state);
	}

	@Override
	public boolean setWorkMode(Reader reader, int mode) {
		if (null == reader) {
			return false;
		}
		return reader.setWorkMode(reader, mode);
	}

	@Override
	public boolean getWorkMode(Reader reader, ByteBuffer workMode) {
		if (null == reader) {
			return false;
		}
		return reader.getWorkMode(reader, workMode);
	}

	@Override
	public boolean setTrigModeDelayTime(Reader reader, byte trigTime) {
		if (null == reader) {
			return false;
		}
		return reader.setTrigModeDelayTime(reader, trigTime);
	}

	@Override
	public boolean getTrigModeDelayTime(Reader reader, ByteBuffer trigTime) {
		if (null == reader) {
			return false;
		}
		return reader.getTrigModeDelayTime(reader, trigTime);
	}

	@Override
	public boolean getNeighJudge(Reader reader, ByteBuffer enableNJ,ByteBuffer neighJudgeTime) {
		if (null == reader) {
			return false;
		}
		return reader.getNeighJudge(reader, enableNJ, neighJudgeTime);
	}

	@Override
	public boolean setNeighJudge(Reader reader, byte neighJudgeTime) {
		if (null == reader) {
			return false;
		}
		return reader.setNeighJudge(reader, neighJudgeTime);
	}

	@Override
	public boolean getDeviceNo(Reader reader, ByteBuffer deviceNo) {
		if (null == reader) {
			return false;
		}
		return reader.getDeviceNo(reader, deviceNo);
	}

	@Override
	public boolean setDeviceNo(Reader reader, byte deviceNo) {
		if (null == reader) {
			return false;
		}
		return reader.setDeviceNo(reader, deviceNo);
	}

	@Override
	public boolean getClock(Reader reader, ByteBuffer clock) {
		if (null == reader) {
			return false;
		}
		return reader.getClock(reader, clock);
	}

	@Override
	public boolean setClock(Reader reader, byte[] clock) {
		if (null == reader) {
			return false;
		}
		return reader.setClock(reader, clock);
	}

	@Override
	public boolean getReadZone(Reader reader, ByteBuffer zone) {
		if (null == reader) {
			return false;
		}
		return reader.getReadZone(reader, zone);
	}

	@Override
	public boolean getReadZonePara(Reader reader, ByteBuffer bank,
			ByteBuffer begin, ByteBuffer length) {
		if (null == reader) {
			return false;
		}
		return reader.getReadZonePara(reader, bank, begin, length);
	}

	@Override
	public boolean setReadZone(Reader reader, byte state) {
		if (null == reader) {
			return false;
		}
		return reader.setReadZone(reader, state);
	}

	@Override
	public boolean setReadZonePara(Reader reader, byte bank, byte begin,
			byte length) {
		if (null == reader) {
			return false;
		}
		return reader.setReadZonePara(reader, bank, begin, length);
	}

	@Override
	public boolean getOutputMode(Reader reader, ByteBuffer outputMode) {
		if (null == reader) {
			return false;
		}
		return reader.getOutputMode(reader, outputMode);
	}

	@Override
	public boolean setOutputMode(Reader reader, byte outputMode) {
		if (null == reader) {
			return false;
		}
		return reader.setOutputMode(reader, outputMode);
	}

	@Override
	public boolean readTagBuffer(Reader reader, CallBack getReadData,
			int readTime) {
		if (null == reader) {
			return false;
		}
		return reader.readTagBuffer(reader, getReadData, readTime);
	}

	@Override
	public boolean resetTagBuffer(Reader reader) {
		if (null == reader) {
			return false;
		}
		return reader.resetTagBuffer(reader);
	}

	@Override
	public boolean killTag(Reader reader, byte[] accessPwd, byte[] killPwd) {
		if (null == reader) {
			return false;
		}
		return reader.killTag(reader, accessPwd, killPwd);
	}

	@Override
	public boolean setAlive(Reader reader, byte interval) {
		if (null == reader) {
			return false;
		}
		return reader.setAlive(reader, interval);
	}

	@Override
	public boolean getRelayAutoState(Reader reader, ByteBuffer state) {
		if (null == reader) {
			return false;
		}
		return reader.getRelayAutoState(reader, state);
	}

	@Override
	public boolean setRelayAutoState(Reader reader, byte time) {
		if (null == reader) {
			return false;
		}
		return reader.setRelayAutoState(reader, time);
	}

	@Override
	public boolean getDeviceConfig(Reader reader, ByteBuffer para) {
		if (null == reader) {
			return false;
		}
		return reader.getDeviceConfig(reader, para);
	}

	@Override
	public boolean setDeviceConfig(Reader reader, byte[] para) {
		if (null == reader) {
			return false;
		}
		return reader.setDeviceConfig(reader, para);
	}

	@Override
	public boolean readTagData(Reader reader, byte bank, byte begin, byte size,ByteBuffer getBuffer, byte[] password) {
		if (null == reader) {
			return false;
		}
		return reader.readTagData(reader, bank, begin, size,getBuffer,password);
	}
}
