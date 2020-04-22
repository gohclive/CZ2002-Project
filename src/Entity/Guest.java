package Entity;

public class Guest {
	private String guestId;
	private String Name;
	private String Gender;
	private String Nationality;
	private String Country;
	private String phoneNumber;
	private String Address;
	private String creditCardNumber;


	public Guest() {
		
	}
	
	public Guest(String guestId,String Name, String Gender, String Nationality, String Country,
			String phoneNumber, String Address, String creditCardNumber) {
		this.guestId = guestId;
		this.Name = Name;
		this.Gender = Gender;
		this.Nationality = Nationality;
		this.Country = Country;
		this.phoneNumber = phoneNumber;
		this.Address = Address;
		this.creditCardNumber = creditCardNumber;

	}

	public String getGuestId() {
		return guestId;
	}

	public void setGuestId(String guestId) {
		this.guestId = guestId;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		this.Name = name;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		this.Gender = gender;
	}

	public String getNationality() {
		return Nationality;
	}

	public void setNationality(String nationality) {
		this.Nationality = nationality;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		this.Country = country;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		this.Address = address;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

} 

