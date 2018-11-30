package com.jietong.rfid.uhf.entity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文本文件操作
 * 
 * @author zhuQixiang
 */
public class FileOperation {

	/**
	 * 保存读卡记录
	 * 
	 * @param content
	 *            内容
	 * @throws Exception
	 */
	public static void writeFile(String content) throws Exception {
		// 在当前目录中保存文件名
		File fileName = new File("tagRecord.txt");
		if (FileOperation.createFile(fileName)) {
			writeTxtFile(content + "\t\t" + getDate(), fileName);
		} else {
			contentToTxt(fileName.getPath(), content);
		}
	}

	/**
	 * 创建文本文件
	 * 
	 * @param fileName
	 *            文件名称
	 * @return true or false
	 */
	public static boolean createFile(File fileName) throws Exception {
		boolean flag = false;
		try {
			if (!fileName.exists()) {
				fileName.createNewFile();
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 写入文本文件
	 * 
	 * @param content
	 *            内容
	 * @param fileName
	 *            文件名称
	 * @return true or false
	 * @throws Exception
	 */
	public static boolean writeTxtFile(String content, File fileName)
			throws Exception {
		RandomAccessFile mm = null;
		boolean flag = false;
		FileOutputStream o = null;
		try {
			o = new FileOutputStream(fileName);
			o.write(content.getBytes("UTF-8"));
			o.close();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mm != null) {
				mm.close();
			}
		}
		return flag;
	}

	/**
	 * 如果文本文件存在，则在文件中累加内容
	 * 
	 * @param filePath
	 *            文件路径
	 * @param content
	 *            内容
	 */
	public static void contentToTxt(String filePath, String content) {
		// 原来的.txt内容
		String str = new String();
		// 更新后的.txt内容
		String s1 = new String();

		try {
			File f = new File(filePath);
			if (!f.exists()) {
				// 当它不存在时创建
				f.createNewFile();
			}
			BufferedReader input = new BufferedReader(new FileReader(f));
			while ((str = input.readLine()) != null) {
				s1 += str + "\r\n";
			}
			// System.out.println(s1);
			input.close();
			s1 += content + "\t\t" + getDate();
			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			output.write(s1);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String date = sdf.format(new Date());
		return date;
	}
}
