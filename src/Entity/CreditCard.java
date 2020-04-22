package Entity;

public class CreditCard {

	private String creditCardNumber;
	private String creditCardType;
	private String creditCardExp;
	private String creditCardCvv;
	private String guestId;
	
	public String getCreditCardType() {
		return creditCardType;
	}
	
	public void setCreditCardType(String creditCardType) {
		this.creditCardType = creditCardType;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public String getCreditCardExp() {
		return creditCardExp;
	}

	public void setCreditCardExp(String creditCardExp) {
		this.creditCardExp = creditCardExp;
	}

	public String getCreditCardCvv() {
		return creditCardCvv;
	}

	public void setCreditCardCvv(String creditCardCvv) {
		this.creditCardCvv = creditCardCvv;
	}
	
	public String getGuestId() {
		return guestId;
	}

	public void setGuestId(String guestId) {
		this.guestId = guestId;
	}

	public CreditCard() {
		super();
	}

	public CreditCard(String creditCardNumber, String creditCardType, String creditCardExp, String creditCardCvv,
			String guestId) {
		super();
		this.creditCardNumber = creditCardNumber;
		this.creditCardType = creditCardType;
		this.creditCardExp = creditCardExp;
		this.creditCardCvv = creditCardCvv;
		this.guestId = guestId;
	}
	
	
	
	
	
}
