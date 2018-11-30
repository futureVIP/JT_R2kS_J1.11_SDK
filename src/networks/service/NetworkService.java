package networks.service;

import java.net.Socket;

public interface NetworkService {

	byte[] read(Socket socket);

	void close(Socket socket);

	boolean send(Socket socket, byte[] sendData);

	Socket open(String host, int port);
}
