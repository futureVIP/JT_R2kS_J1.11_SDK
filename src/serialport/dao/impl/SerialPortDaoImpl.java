package serialport.dao.impl;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import serialport.dao.SerialPortDao;
import serialport.dao.impl.serialportexception.SendDataToSerialPortFailure;
import serialport.dao.impl.serialportexception.SerialPortOutputStreamCloseFailure;
import serialport.dao.impl.serialportexception.SerialPortParameterFailure;

public class SerialPortDaoImpl implements SerialPortDao {

	@Override
	public byte[] read(SerialPort serialPort) {
		InputStream in = null;
		byte[] bytes = null;
		try {
			in = serialPort.getInputStream();
			int bufflenth = in.available(); // ��ȡbuffer������ݳ���
			while (bufflenth != 0) {
				bytes = new byte[bufflenth]; // ��ʼ��byte����Ϊbuffer�����ݵĳ���
				in.read(bytes);
				bufflenth = in.available();
			}
		} catch (IOException e) {
			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (IOException e) {
				return null;
			}
		}
		return bytes;
	}

	@Override
	public void close(SerialPort serialPort) {
		if (serialPort != null) {
			serialPort.notifyOnDataAvailable(false);
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	@Override
	public boolean send(SerialPort serialPort, byte[] sendData) {
		boolean flag = false;
		OutputStream out = null;
		try {
			out = serialPort.getOutputStream();
			out.write(sendData);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			out.flush();
			flag = true;
		} catch (IOException e) {
			try {
				throw new SendDataToSerialPortFailure();
			} catch (SendDataToSerialPortFailure e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				if (out != null) {
					out.close();
					out = null;
				}
			} catch (IOException e) {
				try {
					throw new SerialPortOutputStreamCloseFailure();
				} catch (SerialPortOutputStreamCloseFailure e1) {
					e1.printStackTrace();
				}
			}
		}
		return flag;
	}

	@Override
	public SerialPort open(String port, int baudrate) {
		// String port = "COM" + portName;
		try {
			// ͨ���˿���ʶ��˿�
			CommPortIdentifier portIdentifier = CommPortIdentifier
					.getPortIdentifier(port);
			// �򿪶˿ڣ������˿����ֺ�һ��timeout���򿪲����ĳ�ʱʱ�䣩
			CommPort commPort = portIdentifier.open(port, 2000);
			// �ж��ǲ��Ǵ���
			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				try {
					// ����һ�´��ڵĲ����ʵȲ��� baudrate
					serialPort.setSerialPortParams(baudrate,
							SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);
				} catch (UnsupportedCommOperationException e) {
					try {
						throw new SerialPortParameterFailure();
					} catch (SerialPortParameterFailure e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				System.out.println("Open " + port + " sucessfully !");
				return serialPort;
			} else {
				// ���Ǵ���
				// throw new NotASerialPort();
			}
		} catch (NoSuchPortException e1) {
			// throw new NoSuchPort();
			return null;
		} catch (PortInUseException e2) {
			System.out.println("�쳣 " + e2.getMessage());
			// throw new PortInUse();
			return null;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findSerialPorts() {
		Enumeration<CommPortIdentifier> portList = null;
		try {
			// ��õ�ǰ���п��ô���
			 portList = CommPortIdentifier.getPortIdentifiers();
			ArrayList<String> portNameList = new ArrayList<String>();
			// �����ô�������ӵ�List�����ظ�List
			while (portList.hasMoreElements()) {
				CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
				// String portName = portList.nextElement().getName();
				// �ж��Ƕ˿ھ����
				if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					// �������port������
					portNameList.add(portId.getName());
					// portNameList.add(portName);
				}
			}
			return portNameList;
		} catch (Exception e) {
			System.out.println(exceptionInfo());
		}
		return null;
	}
	
	private String exceptionInfo(){
		StringBuffer sb = new StringBuffer();
		sb.append("java����RXTXcomm�����쳣no rxtxSerial in java.library.path");
		sb.append("\n\n");
		sb.append("�����ע����32λ��64λ����:mfz-rxtx-2.2-20081207-win-x86x64");
		sb.append("\n\n");
		sb.append("1����RXTXcomm.jar������Ŀ������");
		sb.append("\n\n");
		sb.append("2��rxtxParallel.dll����%java_home%\\jre\\lib\\ext��");
		sb.append("\n\n");
		sb.append("3��rxtxSerial.dll����%java_home%\\jre\\bin��");
		return sb.toString();
	}
}
