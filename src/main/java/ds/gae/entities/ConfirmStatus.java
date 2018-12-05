package ds.gae.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.appengine.api.datastore.Key;

@Entity
public class ConfirmStatus implements Serializable {
	
	private Status status;
	@Id private String key;
	private String renter;
	
	public ConfirmStatus(String renter, List<Quote> quotes, Status status) {
		this.renter = renter;
		this.status = status;
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		this.key = timeStamp;
	}
	
	public Status getStatus() {
		return this.status;
	}
	
	public String getRenter() {
		return this.renter;
	}
	
	public String getKey() {
		return this.key;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public enum Status {
		Confirmed, Approved, Cancelled;
	}

}
