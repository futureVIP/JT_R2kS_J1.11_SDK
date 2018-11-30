package networks.dao;

import java.net.Socket;

public interface NetworksDao {
	byte[] read(Socket socket);

	void close(Socket socket);

	boolean send(Socket socket, byte[] sendData);

	Socket open(String host, int port);
}
