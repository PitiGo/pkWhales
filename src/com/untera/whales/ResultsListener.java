package com.untera.whales;

public interface ResultsListener {
	/**
	* Activites that wish to be notified about results
	* in onPostExecute of an AsyncTask must implement
	* this interface.
	*
	* This is the basic Observer pattern.
	*/
	
	    public void onResultsSucceededPOST(String result);
	    public void onResultsSucceededGET(String result);
	    public void listViewChanged(int pos);
	

}
