package com.untera.whales;

import java.util.ArrayList;

import android.content.Context;
import android.util.Patterns;

public class CommentEN {
	private Comentario c;
	private ComentarioCAD datos;
	private String url;
	
	ArrayList<Comentario> values;

	public CommentEN(Context activity) {
		
		c = new Comentario();
	

		datos = new ComentarioCAD(activity);

	}

	public Comentario getComment() {
		return c;
	}

	public void setComment(Comentario c) {
		this.c = c;
	}

	public String getUrl() {
		return url;
	}

	public Boolean setUrl(String url) {
		
		if(Patterns.WEB_URL.matcher(url).matches()){
			this.url = url;
			return true;
		}
			
		return false;
		

	}

	public void delComment() {
		datos.deleteComment(c);
	}

	public void insertComment() {
		datos.createComment(c);
	}
	
	public ArrayList<Comentario> getAllComments(){
		
		return datos.getAllComments();
	}

}
