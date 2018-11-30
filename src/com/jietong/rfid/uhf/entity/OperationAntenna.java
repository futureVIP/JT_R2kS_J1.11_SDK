package com.jietong.rfid.uhf.entity;

public class OperationAntenna {
	/**
	 * 天线信号读取的速度选项列表
	 * 
	 * @param combo
	 *            下拉框控件
	 * @return 下拉列表
	 */
	public static String[] workTime() {
		String[] workTimes = new String[30];
		int count = 0;
		for (int i = 100; i <= 3000; i = i + 100) {
			workTimes[count++] = String.valueOf(i);
		}
		return workTimes;
	}

	/**
	 * 天线信号读取的选项列表，DBM毫瓦
	 * 
	 * @param combo
	 *            下拉框控件
	 * @return 下拉列表
	 */
	public static String[] power() {
		String[] powers = new String[11];
		int count = 0;
		for (int i = 20; i <= 30; i++) {
			powers[count++] = String.valueOf(i);
		}
		return powers;
	}
	
	public static int positionWorkTime(int workTime) {
		int position = 0;
		int count = 0;
		for (int i = 100; i <= 3000; i = i + 100) {
			if(workTime == i){
				position = count;
				return position;
			}
			count++;
		}
		return position;
	}
	
	public static int positionPower(int power) {
		int position = 0;
		int count = 0;
		for (int i = 20; i <= 30; i++) {
			if(power == i){
				position = count;
				return position;
			}
			count++;
		}
		return position;
	}
}
