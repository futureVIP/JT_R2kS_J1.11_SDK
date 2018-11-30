package networks.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import networks.dao.NetworksDao;

public class NetworksDaoImpl implements NetworksDao {

	@Override
	public byte[] read(Socket socket) {
		InputStream in = null;
		byte[] bytes = null;
		try {
			in = socket.getInputStream();
			int bufflenth = in.available(); // 获取buffer里的数据长度
			while (bufflenth != 0) {
				bytes = new byte[bufflenth]; // 初始化byte数组为buffer中数据的长度
				in.read(bytes);
				bufflenth = in.available();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return bytes;
	}

	@Override
	public void close(Socket socket) {
		if (socket != null) {
			try {
				socket.close();
				System.out.println("Close " + socket + " sucessfully !");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			}
			socket = null;
		}
	}

	@Override
	public boolean send(Socket socket, byte[] sendData) {
		boolean flag = false;
		OutputStream out = null;
		try {
			out = socket.getOutputStream();
			out.write(sendData);
			Thread.sleep(100);
			out.flush();
			flag = true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return flag;
	}

	@Override
	public Socket open(String host, int port) {
		// 通过端口名识别端口
		Socket socket = new Socket();
		try {
			InetAddress addr = InetAddress.getByName(host);
			try {
				socket.connect(new InetSocketAddress(addr, port), 300);
				// socket.setSendBufferSize(100);
				System.out.println("Open " + host + " sucessfully !");
				return socket;
			} catch (IOException e) {
				System.out.println("连接失败");
				// e.printStackTrace();
			}
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		return null;
	}
}
