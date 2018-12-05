package ds.gae;

import java.io.*;
import java.util.*;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ds.gae.entities.*;
import ds.gae.entities.ConfirmStatus.Status;

public class Worker extends HttpServlet {
	private static final long serialVersionUID = -7058685883212377590L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);		
		
		ConfirmStatus status = null;
		
		EntityManager manager = EMF.get().createEntityManager();
		try {
			// hier nog confirmation status ophalen en updaten
			ObjectInputStream stream = new ObjectInputStream(req.getInputStream());
			ConfirmationPayload payload = (ConfirmationPayload) stream.readObject();
			System.out.println("payload: "+payload.getConfirmationStatusKey());
			status = manager.find(ConfirmStatus.class, payload.getConfirmationStatusKey());
			
			// confirm request
			confirmRequest(payload.getQuotes());
			status.setStatus(Status.Approved);
			
		} catch (ReservationException e) {
			status.setStatus(Status.Cancelled);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			manager.close();
		}
		
	}
	
	private void confirmRequest(List<Quote> quotes) throws ReservationException {
		List<Reservation> reservations = new ArrayList<Reservation>();
		EntityManager manager = EMF.get().createEntityManager();
		try {
			for (Quote quote : quotes) {
				CarRentalCompany crc = (CarRentalCompany) manager.createNamedQuery("findCrc").setParameter("name", quote.getRentalCompany()).getResultList().get(0);
				reservations.add(crc.confirmQuote(quote));
			}
		} catch (ReservationException e) {
			for (Reservation res : reservations) {
				CarRentalCompany crc = (CarRentalCompany) manager.createNamedQuery("findCrc").setParameter("name", res.getRentalCompany()).getResultList().get(0);
				crc.cancelReservation(res);
			}
			throw new ReservationException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close();
		}
	}

}
