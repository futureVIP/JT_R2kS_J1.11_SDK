package com.jietong.rfid.uhf.entity;

import com.jietong.rfid.uhf.dao.impl.Reader;
import com.jietong.rfid.uhf.service.CallBackStopReadCard;

public class StopReaderCard implements Runnable {
	Reader reader = null;

	CallBackStopReadCard callBack = null;

	public StopReaderCard() {

	}

	public StopReaderCard(Reader reader, CallBackStopReadCard callBack) {
		this.reader = reader;
		this.callBack = callBack;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		callBack.stopReadCard(reader.stopRead);
	}
}
