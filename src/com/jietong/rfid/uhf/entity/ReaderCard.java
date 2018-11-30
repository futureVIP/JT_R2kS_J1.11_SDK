package com.jietong.rfid.uhf.entity;

import com.jietong.rfid.uhf.dao.impl.Reader;
import com.jietong.rfid.uhf.service.CallBack;

public class ReaderCard implements Runnable {
	Reader reader = null;

	CallBack callBack = null;

	public ReaderCard() {
	}

	public ReaderCard(Reader reader, CallBack callBack) {
		this.reader = reader;
		this.callBack = callBack;
	}

	@Override
	public void run() {
		reader.threadFunc(reader, callBack);
	}
}
