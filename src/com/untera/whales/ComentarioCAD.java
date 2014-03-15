package com.untera.whales;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ComentarioCAD {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_COMMENT,MySQLiteHelper.COLUMN_ID_SERVIDOR };

	public ComentarioCAD(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Comentario createComment(Comentario c) {
		
		
		
		
		ContentValues values = new ContentValues();
		
		values.put(MySQLiteHelper.COLUMN_COMMENT, c.getComment());
		values.put(MySQLiteHelper.COLUMN_ID_SERVIDOR,c.getIdServer());
		
		long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS, null,
				values);
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		
		cursor.moveToFirst();
		
		Comentario newComment = cursorToComment(cursor);
		newComment.setServerId(c.getIdServer());
		cursor.close();
		return newComment;
	}

	public void deleteComment(Comentario comment) {
		
		long id = comment.getId();
		
		System.out.println("Comment deleted with id: " + id);
		
		database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public ArrayList<Comentario> getAllComments() {
		ArrayList<Comentario> comments = new ArrayList<Comentario>();
		
		
		return comments;

	/*	Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Comentario comment = cursorToComment(cursor);
			comments.add(comment);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();*/
	//	return comments;
	}

	private Comentario cursorToComment(Cursor cursor) {
		Comentario comment = new Comentario();
		comment.setId(cursor.getLong(0));
		comment.setComment(cursor.getString(1));
		return comment;
	}
}
