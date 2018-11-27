package ds.gae.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.appengine.api.datastore.Key;

@Entity
public class Agency implements Serializable {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Key key;
	@OneToMany(cascade=CascadeType.ALL) private Set<CarRentalCompany> companies = new HashSet<CarRentalCompany>();

	public Agency() {}
	
	public Agency(Set<CarRentalCompany> companies) {
		for(CarRentalCompany crc : companies)
			this.companies.add(crc);
	}
}
