package TrungHuynh;

class InstanceObject {

	private String refund; // {yes, no}
	private String MaritalS; // {single, married, divorced}
	private int Income; // number
	private String Cheat; // {yes, no}

	public InstanceObject(String refund, String MaritalS, int Income, String Cheat) {
		this.refund = refund;
		this.MaritalS = MaritalS;
		this.Income = Income;
		this.Cheat = Cheat;

	}

	public String getRefund() {
		return refund;
	}

	public void setRefund(String refund) {
		this.refund = refund;
	}

	public String getMaritalS() {
		return MaritalS;
	}

	public void setMaritalS(String maritalS) {
		MaritalS = maritalS;
	}

	public int getIncome() {
		return Income;
	}

	public void setIncome(int income) {
		Income = income;
	}

	public String getCheat() {
		return Cheat;
	}

	public void setCheat(String cheat) {
		Cheat = cheat;
	}
	
	public String getString(){
		return refund+", "+MaritalS+", "+Income+", "+ Cheat;
	}
}
