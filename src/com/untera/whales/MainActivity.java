package com.untera.whales;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/*	Toast toast =Toast.makeText(this, "Enviado",Toast.LENGTH_SHORT);
 toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
 toast.show();*/

public class MainActivity extends Activity implements ResultsListener {

	ListView listView;
	private ComentarioCAD datos;
	// private CommentEN enData;
	private EditText et;
	ArrayList<Comentario> values;
	AdapterMensajes adapt;
	Context context = this;
	private final String URL_SERVER = "http://pkwhales.untera.com/comment?page2";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Get ListView object from xml
		listView = (ListView) findViewById(R.id.listComment);

	

		et = (EditText) findViewById(R.id.editText1);

		// Crea base de datos
		try {

			// enData = new CommentEN(this);

			datos = new ComentarioCAD(this);
			datos.open();
			values = datos.getAllComments();
			
			// Creo el adapter personalizado
			adapt = new AdapterMensajes(this, values);

			// Assign adapter to ListView
			listView.setAdapter(adapt);

			System.out.println("base de datos correcta");

		} catch (Exception ex) {
			System.out.println("ERROR:" + ex.getMessage());

			Toast toast = Toast.makeText(this,
					"ERROR AL CARGAR BASE DE DATOS:", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
			toast.show();

		}

	}

	public void doClickEnviar(View botonEnviar) {
		String textComment = et.getText().toString();
		AskServer u = new AskServer(MainActivity.this, URL_SERVER);
		u.setKind(AskServer.tipoPeticion.POST);
		u.execute(textComment);

		Comentario co = new Comentario();
		co.setComment(textComment);
		co.setServerId(-1);

	//	adapt.Insert(co, 0);
	//	adapt.notifyDataSetChanged();
		et.setText("");
	}

	public void doClickBorrar(View botonBorrar) {

		listViewChanged(adapt.getCount() - 1);
		// MainActivity.this.finish();

	}

	public void doClickActualiza(View botonActualizar) {

		AskServer t = new AskServer(MainActivity.this, URL_SERVER);
		t.execute();
	}

	public void clickRow(View row) {

		row.setSelected(true);
		// row.setSelected()

	}

	@Override
	public void onResultsSucceededPOST(String result) {

		JSONObject json;
		try {
			json = new JSONObject(result);
			String server_id = json.getString("id");
			String texto = json.getString("content");
			Comentario cc = new Comentario();
			cc.setComment(texto);
			cc.setServerId(Long.valueOf(server_id));
			//

			Comentario c = datos.createComment(cc);
			// de datos
			adapt.Insert(c, 0);
			adapt.notifyDataSetChanged();

		} catch (JSONException e) {
			// TODO Auto-generated catch block//
			e.printStackTrace();
		}

	}

	@Override
	public void onResultsSucceededGET(String result) {

		JSONObject json;
		try {
			JSONArray allComments = new JSONArray(result);
			System.out.println("Total: "+allComments.length());
			for (int i = 0; i < allComments.length(); i++) {
				json = allComments.getJSONObject(i);
				Comentario c = new Comentario();
				c.setComment(json.getString("content"));
				c.setServerId(Long.parseLong(json.getString("id")));
				c.setDateTime(json.getString("create_date"));

				if (!adapt.existsComment(c)) {
					adapt.Insert(c, 0);
					adapt.notifyDataSetChanged();

				}

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void listViewChanged(int pos) {
		Comentario c = new Comentario();

		try {

			if (!adapt.isEmpty()) {
				c = adapt.getItem(pos);
				// Comprobar que puedo borrarlo.
				AskServer u = new AskServer(MainActivity.this, URL_SERVER);
				String idServidor = Long.toString(c.getIdServer());

				u.delete(idServidor);

				datos.deleteComment(c);
				adapt.remove(c);
				adapt.notifyDataSetChanged();
			} else {
				Toast tu = Toast.makeText(this, "Message not found",
						Toast.LENGTH_LONG);
				tu.show();
			}
		} catch (Exception ex) {

			Toast t = Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG);
			t.show();

		}

	}

}
