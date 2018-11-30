package com.jietong.rfid.uhf.entity;

public class EPC {
	private int id;
	private String epc;
	private int count;
	private String ant;
	
    public EPC() {
    	
    }

    public EPC(int id, String epc, int count, String ant) {
        this.id = id;
        this.epc = epc;
        this.count = count;
        this.ant = ant;
    }

	public String getAnt() {
		return ant;
	}

	public void setAnt(String ant) {
		this.ant = ant;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the epc
	 */
	public String getEpc() {
		return epc;
	}

	/**
	 * @param epc
	 *            the epc to set
	 */
	public void setEpc(String epc) {
		this.epc = epc;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EPC [id=" + id + ", epc=" + epc + ", count=" + count + "]";
	}
}
