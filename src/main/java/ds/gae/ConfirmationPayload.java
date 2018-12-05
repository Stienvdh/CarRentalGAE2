package ds.gae;

import java.io.Serializable;
import java.util.List;

import com.google.appengine.api.datastore.Key;

import ds.gae.entities.Quote;

public class ConfirmationPayload implements Serializable {
	
	private List<Quote> quotes;
	private String confirmationStatusKey;
	
	public ConfirmationPayload(List<Quote> quotes, String key) {
		this.quotes = quotes;
		this.confirmationStatusKey = key;
	}
	
	public List<Quote> getQuotes() {
		return this.quotes;
	}
	
	public String getConfirmationStatusKey() {
		return this.confirmationStatusKey;
	}
	
}
