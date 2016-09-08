package com.auais.note.util.mail;

/**
 * Mail属性实体
 * 
 */
public class MailDto implements Cloneable {

	public static final String ENCODEING = "UTF-8";
	
	private String host; // 服务器地址
	
	private String sender; // 发件人的邮箱
	
	private String receiver; // 收件人的邮箱
	
	private String name; // 发件人昵称
	
	private String userName; // 账号
	
	private String password; // 密码
	
	private String subject; // 主题
	
	private String message; // 信息(支持HTML)
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getSender() {
		return sender;
	}
	
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String getReceiver() {
		return receiver;
	}
	
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	@Override
	public String toString() {
		StringBuffer stringb = new StringBuffer();
		stringb.append("host:").append(this.getHost()).append(",")
			.append("sender:").append(this.getSender()).append(",")
			.append("receiver:").append(this.getReceiver()).append(",")
			.append("name:").append(this.getName()).append(",")
			.append("userName:").append(this.getUserName()).append(",")
			.append("password:").append(this.getPassword()).append(",")
			.append("subject:").append(this.getSubject()).append(",")
			.append("message:").append(this.getMessage()).append(",");
		return stringb.toString();
	}
}