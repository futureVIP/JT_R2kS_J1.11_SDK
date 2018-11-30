package com.jietong.rfid.uhf.tool;

/**
 * Dll 卓岚核心操作类
 * 
 * @author zhuQixiang createData 2017-05-15
 * 
 */
public class ZLDMHandler {
	// 搜索到的设备数量
	public int devCount = 0;

	public static byte selectedDevNo = 0;

	/**
	 * 加载Dll文件
	 * 
	 * @param dllName
	 *            dll 库文件名称,如果文件位置在项目根目录下，dllName直接是文件名即可，如果文件名称不在项目根目录下，
	 *            dllName需加上文件路径
	 * @return 加载是否成功标记：成功 true,失败 false
	 */
	public boolean dllInit(String dllName) {
		return ZLDMOperate.init(dllName);
	}

	/**
	 * 启动查找设备
	 * 
	 * @return 设备号
	 */
	public short startSearchDev() {
		return ZLDMOperate.lib.ZLDM_StartSearchDev();
	}

	/**
	 * 获取版本号
	 * 
	 * @return 版本号
	 */
	public int getVer() {
		return ZLDMOperate.lib.ZLDM_GetVer();
	}

	/**
	 * 设置参数
	 * 
	 * @param nNum
	 *            设备号
	 * @return 设置成功，返回true，否则false
	 */
	public boolean setParam(byte nNum) {
		return ZLDMOperate.lib.ZLDM_SetParam(nNum);
	}

	/**
	 * 获取设备编号
	 * 
	 * @param nNum
	 *            设备号
	 * @return 设备编号
	 */
	public int getDevID(byte nNum) {
		return ZLDMOperate.lib.ZLDM_GetDevID(nNum);
	}

	/**
	 * 获取设备名称
	 * 
	 * @param nNum
	 *            设备号
	 * @return 设备名称
	 */
	public String getDevName(byte nNum) {
		return ZLDMOperate.lib.ZLDM_GetDevName(nNum);
	}

	/**
	 * 获取IP模式
	 * 
	 * @param nNum
	 *            设备号
	 * @return IP模式
	 */
	public byte getIPMode(byte nNum) {
		return ZLDMOperate.lib.ZLDM_GetIPMode(nNum);
	}

	/**
	 * 获取IP
	 * 
	 * @param nNum
	 *            设备号
	 * @return IP
	 */
	public String getIP(byte nNum) {
		return ZLDMOperate.lib.ZLDM_GetIP(nNum);
	}

	/**
	 * 获取端口
	 * 
	 * @param nNum
	 *            设备号
	 * @return 端口
	 */
	public short getPort(byte nNum) {
		return ZLDMOperate.lib.ZLDM_GetPort(nNum);
	}

	/**
	 * 获取网关
	 * 
	 * @param nNum
	 *            设备号
	 * @return 网关
	 */
	public String getGateWay(byte nNum) {
		return ZLDMOperate.lib.ZLDM_GetGateWay(nNum);
	}

	/**
	 * 获取工作模式
	 * 
	 * @param nNum
	 *            设备号
	 * @return 工作模式
	 */
	public byte getWorkMode(byte nNum) {
		return ZLDMOperate.lib.ZLDM_GetWorkMode(nNum);
	}

	/**
	 * 获取 网络掩码
	 * 
	 * @param nNum
	 *            设备号
	 * @return 网络掩码
	 */
	public String getNetMask(byte nNum) {
		return ZLDMOperate.lib.ZLDM_GetNetMask(nNum);
	}

	/**
	 * 获取目的名称
	 * 
	 * @param nNum
	 *            设备号
	 * @return 目的名称
	 */
	public String getDestName(byte nNum) {
		return ZLDMOperate.lib.ZLDM_GetDestName(nNum);
	}

	/**
	 * 获取目的端口
	 * 
	 * @param nNum
	 *            设备号
	 * @return 目的端口
	 */
	public short getDestPort(byte nNum) {
		return ZLDMOperate.lib.ZLDM_GetDestPort(nNum);
	}

	/**
	 * 获取波特率指数
	 * 
	 * @param nNum
	 *            设备号
	 * @return 波特率指数
	 */

	public byte getBaudrateIndex(byte nNum) {
		return ZLDMOperate.lib.ZLDM_GetBaudrateIndex(nNum);
	}

	/**
	 * 设置校验位
	 * 
	 * @param nNum
	 *            设备号
	 * @return 校验位
	 */
	public byte getParity(byte nNum) {
		return ZLDMOperate.lib.ZLDM_GetParity(nNum);
	}

	/**
	 * 获取数据位
	 * 
	 * @param nNum
	 *            设备号
	 * @return 数据位
	 */
	public byte getDataBits(byte nNum) {
		return ZLDMOperate.lib.ZLDM_GetDataBits(nNum);
	}

	/**
	 * 设置网络设备名称
	 * 
	 * @param nNum
	 *            设备号
	 * @param DevName
	 *            设备名称
	 * @return 设置成功，返回true，否则false
	 */
	public boolean setDevName(byte nNum, char DevName) {
		return ZLDMOperate.lib.ZLDM_SetDevName(nNum, DevName);
	}

	/**
	 * 设置IP
	 * 
	 * @param nNum
	 *            设备号
	 * @param IP
	 *            IP
	 * @return 设置成功，返回true，否则false
	 */
	public boolean setIP(byte nNum, byte[] IP) {
		return ZLDMOperate.lib.ZLDM_SetIP(nNum, IP);
	}

	/**
	 * 设置网关
	 * 
	 * @param nNum
	 *            设备号
	 * @param GateWay
	 *            网关
	 * @return 设置成功，返回true，否则false
	 */
	public boolean setGateWay(byte nNum, byte[] GateWay) {
		return ZLDMOperate.lib.ZLDM_SetGateWay(nNum, GateWay);
	}

	/**
	 * 设置网络掩码
	 * 
	 * @param nNum
	 *            设备号
	 * @param NetMask
	 *            网络掩码
	 * @return 设置成功，返回true，否则false
	 */
	public boolean setNetMask(byte nNum, byte[] NetMask) {
		return ZLDMOperate.lib.ZLDM_SetNetMask(nNum, NetMask);
	}

	/**
	 * 设置设备名称
	 * 
	 * @param nNum
	 *            设备号
	 * @param DestName
	 *            设备名称
	 * @return 设置成功，返回true，否则false
	 */
	public boolean setDestName(byte nNum, byte[] DestName) {
		return ZLDMOperate.lib.ZLDM_SetDestName(nNum, DestName);
	}

	/**
	 * 设置IP模式
	 * 
	 * @param nNum
	 *            设备号
	 * @param IPMode
	 *            IP模式
	 * @return 设置成功，返回true，否则false
	 */
	public boolean setIPMode(byte nNum, byte IPMode) {
		return ZLDMOperate.lib.ZLDM_SetIPMode(nNum, IPMode);
	}

	/**
	 * 设置端口
	 * 
	 * @param nNum
	 *            设备号
	 * @param Port
	 *            端口
	 * @return 设置成功，返回true，否则false
	 */
	public boolean setPort(byte nNum, short Port) {
		return ZLDMOperate.lib.ZLDM_SetPort(nNum, Port);
	}

	/**
	 * 设置工作模式
	 * 
	 * @param nNum
	 *            设备号
	 * @param WorkMode
	 *            工作模式
	 * @return 设置成功，返回true，否则false
	 */
	public boolean setWorkMode(byte nNum, byte WorkMode) {
		return ZLDMOperate.lib.ZLDM_SetWorkMode(nNum, WorkMode);
	}

	/**
	 * 设置目的端口
	 * 
	 * @param nNum
	 *            设备号
	 * @param DestPort
	 *            目的端口
	 * @return 设置成功，返回true，否则false
	 */
	public boolean setDestPort(byte nNum, short DestPort) {
		return ZLDMOperate.lib.ZLDM_SetDestPort(nNum, DestPort);
	}

	/**
	 * 设置波特率
	 * 
	 * @param nNum
	 *            设备号
	 * @param BaudrateIndex
	 *            波特率指数
	 * @return 设置成功，返回true，否则false
	 */
	public boolean setBaudrateIndex(byte nNum, byte BaudrateIndex) {
		return ZLDMOperate.lib.ZLDM_SetBaudrateIndex(nNum, BaudrateIndex);
	}

	/**
	 * 设置校验位
	 * 
	 * @param nNum
	 *            设备号
	 * @param Parity
	 *            校验位
	 * @return 设置成功，返回true，否则false
	 */
	public boolean setParity(byte nNum, byte Parity) {
		return ZLDMOperate.lib.ZLDM_SetParity(nNum, Parity);
	}

	/**
	 * 设置数据位
	 * 
	 * @param nNum
	 *            设备号
	 * @param DataBits
	 *            数据位
	 * @return 设置成功，返回true，否则false
	 */
	public boolean setDataBits(byte nNum, byte DataBits) {
		return ZLDMOperate.lib.ZLDM_SetDataBits(nNum, DataBits);
	}
}
