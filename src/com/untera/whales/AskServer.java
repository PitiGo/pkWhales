package com.untera.whales;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class AskServer extends AsyncTask<String, Integer, String> {
	String resultado = "";

	static enum tipoPeticion {
		GET, POST, DELETE, PUT
	};

	ResultsListener listener;
	tipoPeticion kind;
	HttpClient cl;
	String url = "";

	public AskServer(ResultsListener rl, String url) {
		this.listener = rl;
		this.kind = AskServer.tipoPeticion.GET;
		this.url = url;
	}

	public void setKind(tipoPeticion p) {
		kind = p;
	}

	public void delete(String id) {
		this.setKind(AskServer.tipoPeticion.DELETE);
		this.url = this.url + '/' + id;
		this.execute();
	}

	@Override
	protected String doInBackground(String... params) {

		cl = new DefaultHttpClient();
		String resultado = "";
		
		
		switch (kind) {
		case GET:
			resultado = ejecutaGET();
			break;
		case POST:
			resultado = ejecutaPOST(params[0]);
			break;
		case DELETE:
			resultado = ejecutaDelete();
		default:
			break;

		}

		return resultado;

	}

	private String ejecutaDelete() {
		try {
			HttpDelete del = new HttpDelete(url);
			del.setHeader("Accept", "application/json");
			HttpResponse resp = cl.execute(del);
			int code = resp.getStatusLine().getStatusCode();
			resultado = "" + code;
			if (code == 204) {

			} else {
				
				resultado = "ERROR HTTP: " + code;
				Log.d("error:", resultado);
			}
		} catch (Exception ex) {
			
			
			resultado = "Error: " + ex.getMessage();
		}
		return resultado;
	}

	private String ejecutaGET() {
		try {

			HttpGet get = new HttpGet(url);// 10.0.2.2
			get.setHeader("Accept", "application/json");

			HttpResponse resp = cl.execute(get);

			int code = resp.getStatusLine().getStatusCode();

			if (code == 200) {

				String respStr = EntityUtils.toString(resp.getEntity());

				JSONObject o = new JSONObject(respStr);
				
				

				
				
				JSONArray allComments = o.getJSONObject("_embedded").getJSONArray("comment");
				
				int pagecount= o.getInt("page_count");
				int pagesize= o.getInt("page_size");
				int total_items= o.getInt("total_items");
				
				System.out.println("page count: "+pagecount);
				System.out.println("page size: "+pagesize);
				System.out.println("total items: "+total_items);
				resultado = allComments.toString();
   //
			} else {
				resultado = "ERROR HTTP: " + code;
			}

			return resultado;

		} catch (Exception ex) {
			Log.e("ServicioRest", "Error!" + ex.getMessage());
			resultado = resultado + ex.toString();

		}
		return resultado;
	}

	private String ejecutaPOST(String com) {

		try {
			HttpPost pst = new HttpPost(url);

			String cadenaComentario = "";

			JSONObject comentario = new JSONObject();

		/*	TimeZone tz = TimeZone.getTimeZone("UTC");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");*/
			/*
			 * "yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mmZ",
			 * "yyyy-MM-dd HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
			 */

		/*	df.setTimeZone(tz);
			String formattedDate = df.format(new Date());*/

			//comentario.accumulate("uri", "Mi url");
			comentario.accumulate("content", com);
			//comentario.accumulate("create_date", formattedDate);
			comentario.accumulate("user", "yo");

			cadenaComentario = comentario.toString();

			StringEntity se = new StringEntity(cadenaComentario);

			pst.setEntity(se);

			// 7. Set some headers to inform server about the type of the
			// content
			pst.setHeader("Accept", "application/json");
			pst.setHeader("Content-type", "application/json");

			/* Finalmente ejecutamos enviando la info al server */
			HttpResponse resp = cl.execute(pst);
			HttpEntity ent = resp.getEntity();/* y obtenemos una respuesta */

			resultado = EntityUtils.toString(ent);

		} catch (Exception e) {
			Log.e("ServicioRest", "Error!" + e.getMessage());
			resultado = resultado + e.toString();
		}

		return resultado;

	}

	@Override
	protected void onPostExecute(String result) {

		switch (kind) {

		case GET:
			listener.onResultsSucceededGET(result);
			break;
		case POST:
			listener.onResultsSucceededPOST(result);
			break;
		case DELETE:

		default:
			break;

		}

	}

}
