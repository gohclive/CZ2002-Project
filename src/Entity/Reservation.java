package Entity;

import java.util.Date;

public class Reservation {
	private String reservationNum;
	private String guestId;
	private String roomId;
	private String status;
	private Date checkInDate;
	private Date checkOutDate;
	private int numOfAdults;
	private int numOfChildren;
	
	
	public Reservation(String reservationNum, String guestId, String roomId, String status, Date checkInDate,
			Date checkOutDate, int numOfAdults, int numOfChildren) {
		super();
		this.reservationNum = reservationNum;
		this.guestId = guestId;
		this.roomId = roomId;
		this.status = status;
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
		this.numOfAdults = numOfAdults;
		this.numOfChildren = numOfChildren;
	}
	
	public Reservation() {
		super();
	}
	public String getReservationNum() {
		return reservationNum;
	}
	public void setReservationNum(String reservationNum) {
		this.reservationNum = reservationNum;
	}
	public String getGuestId() {
		return guestId;
	}
	public void setGuestId(String guestId) {
		this.guestId = guestId;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCheckInDate() {
		return checkInDate;
	}
	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}
	public Date getCheckOutDate() {
		return checkOutDate;
	}
	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}
	public int getNumOfAdults() {
		return numOfAdults;
	}
	public void setNumOfAdults(int numOfAdults) {
		this.numOfAdults = numOfAdults;
	}
	public int getNumOfChildren() {
		return numOfChildren;
	}
	public void setNumOfChildren(int numOfChildren) {
		this.numOfChildren = numOfChildren;
	}
	
	
}
