package com.untera.whales;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class AdapterMensajes extends BaseAdapter {

	protected ResultsListener activity;
	protected ArrayList<Comentario> items;

	public AdapterMensajes(ResultsListener activity,
			ArrayList<Comentario> items) {
		this.activity = activity;
		this.items = items;

	}

	

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Comentario getItem(int arg0) {
		return items.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).getId();

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		// Generamos una convertView por motivos de eficiencia
		View v = convertView;

		if (convertView == null) {
			LayoutInflater inf = (LayoutInflater) ((Activity) activity)
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inf.inflate(R.layout.filapersonalizada, null);
		}

		Comentario dir = items.get(position);

		TextView nombre = (TextView) v.findViewById(R.id.texto);
		nombre.setText(dir.getComment());

		ImageButton btn = (ImageButton) v.findViewById(R.id.imageButtonDelete);

		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.listViewChanged(position);
			}
		});
	
		return v;
	}

	public void Insert(Comentario c, int i) {
		items.add(i, c);
	}

	public void remove(Comentario c) {

		items.remove(c);
	}

	public void removeByPosition(int position) {
		items.remove(position);

	}
	
	public Boolean existsComment(Comentario c){
		
		int size = items.size();
		
		for(int i=0;i<size;i++){
			if(items.get(i).getIdServer()==c.getIdServer()){
				return true;
			}
		}
		
		return false;
		
	}

}
