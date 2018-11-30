package serialport.dao;

import gnu.io.SerialPort;

import java.util.List;

public interface SerialPortDao {
	byte[] read(SerialPort serialPort);

	void close(SerialPort serialPort);

	boolean send(SerialPort serialPort, byte[] sendData);

	SerialPort open(String host, int port);

	List<String> findSerialPorts();
}
