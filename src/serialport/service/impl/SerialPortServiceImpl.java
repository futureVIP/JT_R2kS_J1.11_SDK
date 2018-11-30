package serialport.service.impl;

import java.util.List;

import serialport.dao.SerialPortDao;
import serialport.dao.impl.SerialPortDaoImpl;
import serialport.service.SerialPortService;
import gnu.io.SerialPort;

public class SerialPortServiceImpl implements SerialPortService {

	SerialPortDao dao = new SerialPortDaoImpl();
	
	@Override
	public byte[] read(SerialPort serialPort) {
		return dao.read(serialPort);
	}

	@Override
	public void close(SerialPort serialPort) {
		dao.close(serialPort);
	}

	@Override
	public boolean send(SerialPort serialPort, byte[] sendData) {
		return dao.send(serialPort, sendData);
	}

	@Override
	public SerialPort open(String host, int port) {
		return dao.open(host, port);
	}

	@Override
	public List<String> findSerialPorts() {
		return dao.findSerialPorts();
	}

}
