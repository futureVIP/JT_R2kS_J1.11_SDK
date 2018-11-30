package serialport.dao.impl;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import serialport.dao.impl.serialportexception.NoSuchSerialPort;
import serialport.dao.impl.serialportexception.NotASerialPort;
import serialport.dao.impl.serialportexception.ReadDataFromSerialPortFailure;
import serialport.dao.impl.serialportexception.SendDataToSerialPortFailure;
import serialport.dao.impl.serialportexception.SerialPortInUse;
import serialport.dao.impl.serialportexception.SerialPortInputStreamCloseFailure;
import serialport.dao.impl.serialportexception.SerialPortOutputStreamCloseFailure;
import serialport.dao.impl.serialportexception.SerialPortParameterFailure;
import serialport.dao.impl.serialportexception.TooManyListeners;

/**
 * 串口服务类，提供打开、关闭串口，读取、发送串口数据等服务（采用单例设计模式）
 * 
 * @author 
 * 
 */
public class SerialPortTool{
	private static SerialPortTool serialTool = null;
	public Thread loopThread = null;

	// 私有化SerialTool类的构造方法，不允许其他类生成SerialTool对象
	public SerialPortTool() {
		// 在该类被ClassLoader加载时就初始化一个SerialTool对象
		// if (serialTool == null) {
		// serialTool = new SerialPortDao();
		// }
	}

	/**
	 * 获取提供服务的SerialTool对象
	 * 
	 * @return serialTool
	 */
	public static SerialPortTool getSerialTool() {
		if (serialTool == null) {
			serialTool = new SerialPortTool();
		}
		return serialTool;
	}

	/**
	 * 查找所有可用端口
	 * 获取串口号列表
	 * @return 可用端口名称列表
	 */
	@SuppressWarnings("unchecked")
	public static final ArrayList<String> findPort() {
		// 获得当前所有可用串口
		Enumeration<CommPortIdentifier> portList = CommPortIdentifier
				.getPortIdentifiers();
		ArrayList<String> portNameList = new ArrayList<String>();
		// 将可用串口名添加到List并返回该List
		while (portList.hasMoreElements()) {
			CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
			//String portName = portList.nextElement().getName();
			// 判断是端口就添加
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				// 打出所有port的名字
				portNameList.add(portId.getName());
				//portNameList.add(portName);
	     	}
		}
		return portNameList;
	}

	/**
	 * 打开串口
	 * 
	 * @param portName
	 *            端口名称
	 * @param baudrate
	 *            波特率
	 * @return 串口对象
	 * @throws SerialPortParameterFailure
	 *             设置串口参数失败
	 * @throws NotASerialPort
	 *             端口指向设备不是串口类型
	 * @throws NoSuchSerialPort
	 *             没有该端口对应的串口设备
	 * @throws SerialPortInUse
	 *             端口已被占用
	 */
	public static final SerialPort openPort(String port, int baudrate){
		//String port = "COM" + portName;
		try {
			// 通过端口名识别端口
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(port);
			// 打开端口，并给端口名字和一个timeout（打开操作的超时时间）
			CommPort commPort = portIdentifier.open(port, 2000);
			// 判断是不是串口
			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				try {
					// 设置一下串口的波特率等参数 baudrate
					serialPort.setSerialPortParams(baudrate,SerialPort.DATABITS_8, SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
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
				// 不是串口
				//throw new NotASerialPort();
			}
		} catch (NoSuchPortException e1) {
			//throw new NoSuchPort();
			return null;
		} catch (PortInUseException e2) {
			System.out.println("异常 "+ e2.getMessage());
			//throw new PortInUse();
			return null;
		}
		return null;
	}

	/**
	 * 关闭串口
	 * 
	 * @param serialport
	 *            待关闭的串口对象
	 */
	public static void comClose(SerialPort serialPort) {
		if (serialPort != null) {
			serialPort.notifyOnDataAvailable(false);
			serialPort.removeEventListener();
			serialPort.close();
			System.out.println("Close " + serialPort + " sucessfully !");
			//serialPort = null;
		}
	}

	/**
	 * 往串口发送数据
	 * 
	 * @param serialPort
	 *            串口对象
	 * @param data
	 *            待发送数据
	 * @throws InterruptedException
	 * @throws SendDataToSerialPortFailure
	 *             向串口发送数据失败
	 * @throws SerialPortOutputStreamCloseFailure
	 *             关闭串口对象的输出流出错
	 */
	public static boolean sendToPort(SerialPort serialPort, byte[] data) {
		//A0 03 A8 00 B5
		boolean flag = false;
		OutputStream out = null;
		try {
			out = serialPort.getOutputStream();
			out.write(data);
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

	/**
	 * 从串口读取数据
	 * 
	 * @param serialPort
	 *            当前已建立连接的SerialPort对象
	 * @return 读取到的数据
	 * @throws ReadDataFromSerialPortFailure
	 *             从串口读取数据时出错
	 * @throws SerialPortInputStreamCloseFailure
	 *             关闭串口对象输入流出错
	 * @throws InterruptedException
	 */
	public static byte[] readFromPort(SerialPort serialPort)
			throws ReadDataFromSerialPortFailure,
			SerialPortInputStreamCloseFailure, InterruptedException {
		InputStream in = null;
		byte[] bytes = null;
		try {
			in = serialPort.getInputStream();
			int bufflenth = in.available(); // 获取buffer里的数据长度
			while (bufflenth != 0) {
				bytes = new byte[bufflenth]; // 初始化byte数组为buffer中数据的长度
				in.read(bytes);
				bufflenth = in.available();
			}
		} catch (IOException e) {
			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//throw new ReadDataFromSerialPortFailure();
			return null;
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (IOException e) {
				//throw new SerialPortInputStreamCloseFailure();
				return null;
			}
		}
		return bytes;
	}
	/**
	 * 添加监听器
	 * 
	 * @param port
	 *            串口对象
	 * @param listener
	 *            串口监听器
	 * @throws TooManyListener
	 *             监听类对象过多
	 */
	public static void addListener(SerialPort port,SerialPortEventListener listener) throws TooManyListeners {
		try {
			// 给串口添加监听器
			port.addEventListener(listener);
			// 设置当有数据到达时唤醒监听接收线程
			port.notifyOnDataAvailable(true);
			// 设置当通信中断时唤醒中断线程
			port.notifyOnBreakInterrupt(true);
			//port.removeEventListener();
			//port.notifyOnDataAvailable(false);
			//port.close();
		} catch (TooManyListenersException e) {
			throw new TooManyListeners();
		}
	}
	
	/**
	 * 实现接口SerialPortEventListener中的方法 读取从串口中接收的数据
	 */
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:// 获取到串口返回信息
			//readComm();
			break;
		default:
			break;
		}
	}
}