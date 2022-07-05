package server;

public enum Zaehlerart {
	STROM("Strom"),
	GAS("Gas"),
	HEIZUNG("Heizung"),
	WASSER("Wasser");
	
	private final String text;
	
	private Zaehlerart(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
}
