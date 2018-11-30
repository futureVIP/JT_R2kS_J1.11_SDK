package com.jietong.rfid.util;

import javax.swing.JOptionPane;

/**
 * 弹框信息提示
 * @author zhuqixiang createDate 2017-05-12
 * 
 */
public class Message {
	/**
	 * 
	 * @param content 对话框中显示内容
	 * @param title 对话框标题
	 */
	public static void Show(String contaier, String title) {
		JOptionPane.showMessageDialog(null, contaier, title,
				JOptionPane.INFORMATION_MESSAGE);
	}
}
