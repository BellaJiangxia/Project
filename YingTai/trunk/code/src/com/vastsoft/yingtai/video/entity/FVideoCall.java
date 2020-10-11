package com.vastsoft.yingtai.video.entity;

import java.util.Date;

public class FVideoCall {
	private String chatRoom;
	private long sendUserId;
	private long recvUserId;
	private Date create_time;
	private Date recv_time;
	
	public FVideoCall() {
		super();
		create_time=new Date();
	}
	public String getChatRoom() {
		return chatRoom;
	}
	public long getSendUserId() {
		return sendUserId;
	}
	public long getRecvUserId() {
		return recvUserId;
	}
	public void setChatRoom(String chatRoom) {
		this.chatRoom = chatRoom;
	}
	public void setSendUserId(long sendUserId) {
		this.sendUserId = sendUserId;
	}
	public void setRecvUserId(long recvUserId) {
		this.recvUserId = recvUserId;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getRecv_time() {
		return recv_time;
	}
	public void setRecv_time(Date recv_time) {
		this.recv_time = recv_time;
	}
	@Override
	public String toString() {
		return "FVideoCall [chatRoom=" + chatRoom + ", sendUserId=" + sendUserId + ", recvUserId=" + recvUserId
				+ ", create_time=" + create_time + ", recv_time=" + recv_time + "]";
	}
	
}
