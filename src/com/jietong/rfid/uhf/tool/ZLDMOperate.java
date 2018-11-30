package com.jietong.rfid.uhf.tool;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class ZLDMOperate {
	// 操作失败返回数据
	public static int FAILRESULT = -1;

	public static ZLDMDllLib lib = null;

	static short m_DevCnt;

	/**
	 * load dll file
	 * 
	 * @param dll
	 * @return 是否加载成功标记
	 */
	public static boolean init(String dll) {
//		try {
//			lib = (ZLDMDllLib) Native.loadLibrary(dll, ZLDMDllLib.class);
//			if (lib != null)
//				return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return false;
	}
}

interface ZLDMDllLib extends Library {

	short ZLDM_StartSearchDev();

	int ZLDM_GetVer();

	boolean ZLDM_SetParam(byte nNum);

	int ZLDM_GetDevID(byte nNum);

	String ZLDM_GetDevName(byte nNum);

	byte ZLDM_GetIPMode(byte nNum);

	String ZLDM_GetIP(byte nNum);

	short ZLDM_GetPort(byte nNum);

	String ZLDM_GetGateWay(byte nNum);

	byte ZLDM_GetWorkMode(byte nNum);

	String ZLDM_GetNetMask(byte nNum);

	String ZLDM_GetDestName(byte nNum);

	short ZLDM_GetDestPort(byte nNum);

	byte ZLDM_GetBaudrateIndex(byte nNum);

	byte ZLDM_GetParity(byte nNum);

	byte ZLDM_GetDataBits(byte nNum);

	boolean ZLDM_SetDevName(byte nNum, char DevName);

	boolean ZLDM_SetIP(byte nNum, byte[] IP);

	boolean ZLDM_SetGateWay(byte nNum, byte[] GateWay);

	boolean ZLDM_SetNetMask(byte nNum, byte[] NetMask);

	boolean ZLDM_SetDestName(byte nNum, byte[] DestName);

	boolean ZLDM_SetIPMode(byte nNum, byte IPMode);

	boolean ZLDM_SetPort(byte nNum, short Port);

	boolean ZLDM_SetWorkMode(byte nNum, byte WorkMode);

	boolean ZLDM_SetDestPort(byte nNum, short DestPort);

	boolean ZLDM_SetBaudrateIndex(byte nNum, byte BaudrateIndex);

	boolean ZLDM_SetParity(byte nNum, byte Parity);

	boolean ZLDM_SetDataBits(byte nNum, byte DataBits);
}
