package serialport.service;

import java.util.List;

import gnu.io.SerialPort;

public interface SerialPortService {

	byte[] read(SerialPort serialPort);

	void close(SerialPort serialPort);

	boolean send(SerialPort serialPort, byte[] sendData);

	SerialPort open(String host, int port);

	List<String> findSerialPorts();

}
