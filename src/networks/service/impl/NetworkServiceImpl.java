package networks.service.impl;

import java.net.Socket;

import serialport.dao.SerialPortDao;
import serialport.dao.impl.SerialPortDaoImpl;

import networks.entity.NetWorks;
import networks.service.NetworkService;

public class NetworkServiceImpl implements NetworkService {

	private NetworkService dao = new NetworkServiceImpl();
	
	@Override
	public byte[] read(Socket socket) {
		return dao.read(socket);
	}

	@Override
	public void close(Socket socket) {
		dao.close(socket);
	}

	@Override
	public boolean send(Socket socket, byte[] sendData) {
		return dao.send(socket, sendData);
	}

	@Override
	public Socket open(String host, int port) {
		return dao.open(host, port);
	}
}
