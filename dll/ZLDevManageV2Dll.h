/* 该文件定义了调用ZlDevManageDll的宏定义、类型和函数 */
#ifndef __INCLUDE_ZL_TYPES_H__

/* 类型定义 */
typedef unsigned char		zl_u8;
typedef unsigned short		zl_u16;
typedef unsigned int		zl_u32;
typedef char				zl_s8;
typedef short				zl_s16;
typedef int					zl_s32;

typedef unsigned int		zl_bool;
typedef unsigned int		zl_boolean;
typedef char				zl_char;
typedef void				zl_void;
typedef int					zl_int;

typedef zl_u32				IP_ADDR;

/* 宏定义 */
#define MAX_DEV_NAME_LEN	10
#define DNS_NAME_MAX_LEN	30

#define WORK_MODE_SERVER 	0
#define WORK_MODE_CLIENT 	1
#define WORK_MODE_UDP		2
#define WORK_MODE_UDP_GROUP		3

#define IP_MODE_STATIC		0
#define IP_MODE_DHCP		1

#define DEST_MODE_STATIC	0
#define DEST_MODE_DYNAMIC	1

#define FLOW_C_NONE			0
#define FLOW_C_CTS_RTS		1

#define PARAM_PARITY_NONE	0
#define PARAM_PARITY_ODD	1
#define PARAM_PARITY_EVEN	2
#define PARAM_PARITY_MARK	3
#define PARAM_PARITY_SPACE	4

#define DATA_BITS_8			0
#define DATA_BITS_7			1
#define DATA_BITS_6			2
#define DATA_BITS_5			3

#define BANDURATE_1200		0
#define BANDURATE_2400		1
#define BANDURATE_4800		2
#define BANDURATE_7200		3
#define BANDURATE_9600		4
#define BANDURATE_14400		5
#define BANDURATE_19200		6
#define BANDURATE_28800		7
#define BANDURATE_38400		8
#define BANDURATE_57600		9
#define BANDURATE_76800		10
#define BANDURATE_115200	11

#define APP_PRO_NONE			0
#define APP_PRO_MODBUS_RTU		1
#define MODBUS_TCP_SERVER_PORT	502
#define MODBUS_TCP_DATA_START	6

#define ZL_BASE_VERSION	383

#define FUNC_SEL_WEB_DOWNLOAD	0x01
#define FUNC_SEL_DNS			0x02
#define FUNC_SEL_REAL_COM		0x04
#define FUNC_SEL_MODBUS_TCP		0x08
#define FUNC_SEL_SPR			0x10
#define FUNC_SEL_DHCP			0x20
#define FUNC_SEL_UDP_FILTER		0x40
#define FUNC_SEL_UNICODE		0x80

#endif

#define DEV_STATUS_CLOSED		0
#define DEV_STATUS_OPENED		1

#define ID_LEN					6




#define ZLDM_HANDLER_ARRAY_MIN_SIZE		256

#define ZLDM_FAST_SEND					0x00
#define ZLDM_BULK_SEND					0x01

#define ZLDM_CALL_MESSAGE				0x00
#define ZLDM_CALL_BACK					0x02

#define ZLDM_VER						34

#define ZLDM_HANDLER_BROADCAST			0xffffffff

typedef zl_u32							ZLDevHandler;

typedef zl_s32 (__stdcall * tZLDM_RecvCallBack)(zl_u32 w, zl_s32 l);

/* 函数指针定义 */
typedef zl_u16	(__stdcall * tZLDM_StartSearchDev)();
typedef int		(__stdcall * tZLDM_GetVer)();
typedef zl_bool (__stdcall * tZLDM_SetParam)(zl_u8 nNum);

//V2.0 Add
typedef zl_s8 * (__stdcall * tZLDM_GetDevID)(zl_u8 nNum);
typedef zl_u8	(__stdcall * tZLDM_GetStatus)(zl_u8 nNum);
typedef zl_s8 * (__stdcall * tZLDM_GetDevName)(zl_u8 nNum);
typedef zl_u8	(__stdcall * tZLDM_GetIPMode)(zl_u8 nNum);
typedef zl_s8 *	(__stdcall * tZLDM_GetIP)(zl_u8 nNum);
typedef zl_u16	(__stdcall * tZLDM_GetPort)(zl_u8 nNum);
typedef zl_s8 *	(__stdcall * tZLDM_GetGateWay)(zl_u8 nNum);
typedef zl_u8	(__stdcall * tZLDM_GetWorkMode)(zl_u8 nNum);
typedef zl_s8 *	(__stdcall * tZLDM_GetNetMask)(zl_u8 nNum);
typedef zl_s8 *	(__stdcall * tZLDM_GetDestName)(zl_u8 nNum);
typedef zl_u16	(__stdcall * tZLDM_GetDestPort)(zl_u8 nNum);

//baud rate param
typedef zl_u8	(__stdcall * tZLDM_GetBaudrateIndex)(zl_u8 nNum);
typedef zl_u8	(__stdcall * tZLDM_GetParity)(zl_u8 nNum);
typedef zl_u8	(__stdcall * tZLDM_GetDataBits)(zl_u8 nNum);
typedef zl_u8	(__stdcall * tZLDM_GetFlowControl)(zl_u8 nNum);


//设置
typedef zl_bool	(__stdcall * tZLDM_SetDevName)(zl_u8 nNum, zl_s8 * DevName);
typedef zl_bool	(__stdcall * tZLDM_SetIPMode)(zl_u8 nNum, zl_u8 IPMode);
typedef zl_bool	(__stdcall * tZLDM_SetIP)(zl_u8 nNum, zl_s8 * IP);
typedef zl_bool	(__stdcall * tZLDM_SetPort)(zl_u8 nNum, zl_u16 Port);
typedef zl_bool	(__stdcall * tZLDM_SetGateWay)(zl_u8 nNum, zl_s8 * GateWay);
typedef zl_bool	(__stdcall * tZLDM_SetWorkMode)(zl_u8 nNum, zl_u8 WorkMode);
typedef zl_bool	(__stdcall * tZLDM_SetNetMask)(zl_u8 nNum, zl_s8 * NetMask);
typedef zl_bool	(__stdcall * tZLDM_SetDestName)(zl_u8 nNum, zl_s8 * DestName);
typedef zl_bool	(__stdcall * tZLDM_SetDestPort)(zl_u8 nNum, zl_u16 DestPort);

//baud rate param
typedef zl_bool	(__stdcall * tZLDM_SetBaudrateIndex)(zl_u8 nNum, zl_u8 Baudrate);
typedef zl_bool	(__stdcall * tZLDM_SetParity)(zl_u8 nNum, zl_u8 Parity);
typedef zl_bool	(__stdcall * tZLDM_SetDataBits)(zl_u8 nNum, zl_u8 DataBits);
typedef zl_bool	(__stdcall * tZLDM_SetFlowControl)(zl_u8 nNum, zl_u8 FlowControl);



