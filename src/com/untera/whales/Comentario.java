package com.untera.whales;

public class Comentario {
	private long id;
	private String comment;
	private String user;
	private long id_server;
	private String dateTime;
	
	
	
	
	public void Comentario(){
		
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getUser(){
		return user;
	}
	
	public void setUser(String usr){
		user = usr;
	}

	

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public long getIdServer(){
	 return id_server;
	}
	
	public void setServerId(long id){
		id_server = id;
	}
	
	public void setDateTime(String d){
		this.dateTime = d;
	}
	
	
	public String getDateTime(){
		return dateTime;
	}
	
	

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return user+comment;
	}
}