package com.vastsoft.yingtai.module.sys.entity;

public class TPlace {
	private long id;
	private String name;
	private long up;
	private int secp;
	private String youbian;
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public long getUp() {
		return up;
	}
	public int getSecp() {
		return secp;
	}
	public String getYoubian() {
		return youbian;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setUp(long up) {
		this.up = up;
	}
	public void setSecp(int secp) {
		this.secp = secp;
	}
	public void setYoubian(String youbian) {
		this.youbian = youbian;
	}
}
