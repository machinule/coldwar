package com.berserkbentobox.coldwar.logic;

public class Status {
	private boolean ok;
	private int code;
	public Status(boolean ok, int code) {
		this.ok = ok;
		this.code = code;
	}
	public boolean ok() {
		return this.ok;
	}
	public int code() {
		return this.code;
	}
	public static Status OK = new Status(true, 0);
}
