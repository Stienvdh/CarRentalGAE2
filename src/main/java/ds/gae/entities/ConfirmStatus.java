package ds.gae.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

public class ConfirmStatus implements Serializable {
	
	private List<Quote> quotes;
	private Status status;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Key key;
	private String renter;
	
	public ConfirmStatus(String renter, List<Quote> quotes, Status status) {
		this.renter = renter;
		this.quotes = quotes;
		this.status = status;		
	}
	
	public List<Quote> getQuotes() {
		return this.quotes;
	}
	
	public Status getStatus() {
		return this.status;
	}
	
	public String getRenter() {
		return this.renter;
	}
	
	public Key getKey() {
		return this.key;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	
	
	public enum Status {
		Confirmed, Approved, Cancelled;
	}

}
