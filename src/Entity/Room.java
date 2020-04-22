package Entity;

public class Room {
	private String roomId;
	private String roomType;
	private String bedType;
	private String viewType;
	private String roomStatus;
	private String roomRate;
	private String roomFloor;
	private String roomNumber;
	private String wifiEnabled;
	private String smokingStatus;
	
	public Room() {
		
	}
	
	public Room(String roomId, String roomType, String bedType, String viewType, String roomStatus, String roomRate,
			String roomFloor, String roomNumber, String wifiEnabled, String smokingStatus) {
		super();
		this.roomId = roomId;
		this.roomType = roomType;
		this.bedType = bedType;
		this.viewType = viewType;
		this.roomStatus = roomStatus;
		this.roomRate = roomRate;
		this.roomFloor = roomFloor;
		this.roomNumber = roomNumber;
		this.wifiEnabled = wifiEnabled;
		this.smokingStatus = smokingStatus;
	}

	public String getSmokingStatus() {
		return smokingStatus;
	}

	public void setSmokingStatus(String smokingStatus) {
		this.smokingStatus = smokingStatus;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public String getBedType() {
		return bedType;
	}

	public void setBedType(String bedType) {
		this.bedType = bedType;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public String getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(String roomStatus) {
		this.roomStatus = roomStatus;
	}

	public String getRoomRate() {
		return roomRate;
	}

	public void setRoomRate(String roomRate) {
		this.roomRate = roomRate;
	}

	public String getRoomFloor() {
		return roomFloor;
	}

	public void setRoomFloor(String roomFloor) {
		this.roomFloor = roomFloor;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getWifiEnabled() {
		return wifiEnabled;
	}

	public void setWifiEnabled(String wifiEnabled) {
		this.wifiEnabled = wifiEnabled;
	}

	
}
