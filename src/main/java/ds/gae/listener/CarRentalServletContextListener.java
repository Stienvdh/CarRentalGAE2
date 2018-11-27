package ds.gae.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ds.gae.CarRentalModel;
import ds.gae.EMF;
import ds.gae.entities.Agency;
import ds.gae.entities.Car;
import ds.gae.entities.CarRentalCompany;
import ds.gae.entities.CarType;

public class CarRentalServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// This will be invoked as part of a warming request, 
		// or the first user request if no warming request was invoked.
						
		// check if dummy data is available, and add if necessary
		if(!isDummyDataAvailable()) {
			addDummyData();
		}
	}
	
	private boolean isDummyDataAvailable() {
		// If the Hertz car rental company is in the datastore, we assume the dummy data is available
		return CarRentalModel.get().getAllRentalCompanyNames().contains("Hertz");

	}
	
	private void addDummyData() {
		Set<CarRentalCompany> companies = new HashSet<CarRentalCompany>();
		
		CarRentalCompany company1 = loadRental("Hertz","hertz.csv");
		CarRentalCompany company2 = loadRental("Dockx","dockx.csv");
		
		companies.add(company1);
		companies.add(company2);
        
        EntityManager manager = EMF.get().createEntityManager();
        try {
        	Agency agency = new Agency(companies);
        	manager.persist(agency);
        }
        finally {
        	manager.close();
        }
	}
	
	private CarRentalCompany loadRental(String name, String datafile) {
		Logger.getLogger(CarRentalServletContextListener.class.getName()).log(Level.INFO, "loading {0} from file {1}", new Object[]{name, datafile});
		CarRentalCompany company = null;
		
		//EntityManager manager = EMF.get().createEntityManager();
		
        try {
            Set<Car> cars = loadData(name, datafile);
            company = new CarRentalCompany(name, cars, carTypes);
            //manager.persist(company);
        } catch (NumberFormatException ex) {
            Logger.getLogger(CarRentalServletContextListener.class.getName()).log(Level.SEVERE, "bad file", ex);
        } catch (IOException ex) {
            Logger.getLogger(CarRentalServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally { } //manager.close();
        return company;
	}
	
	private static Set<CarType> carTypes;
	
	public static Set<Car> loadData(String name, String datafile) throws NumberFormatException, IOException {
		carTypes = new HashSet<CarType>();
		Set<Car> cars = new HashSet<Car>();
		int carId = 1;

		//open file from jar
		BufferedReader in = new BufferedReader(new InputStreamReader(CarRentalServletContextListener.class.getClassLoader().getResourceAsStream(datafile)));
		//while next line exists
		while (in.ready()) {
			//read line
			String line = in.readLine();
			//if comment: skip
			if (line.startsWith("#")) {
				continue;
			}
			//tokenize on ,
			StringTokenizer csvReader = new StringTokenizer(line, ",");
			//create new car type from first 5 fields
			CarType type = new CarType(csvReader.nextToken(),
					Integer.parseInt(csvReader.nextToken()),
					Float.parseFloat(csvReader.nextToken()),
					Double.parseDouble(csvReader.nextToken()),
					Boolean.parseBoolean(csvReader.nextToken()));
			carTypes.add(type);
			//create N new cars with given type, where N is the 5th field
			for (int i = Integer.parseInt(csvReader.nextToken()); i > 0; i--) {
				cars.add(new Car(carId++, type.getId()));
			}
		}

		return cars;
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// Please leave this method empty.
	}
}