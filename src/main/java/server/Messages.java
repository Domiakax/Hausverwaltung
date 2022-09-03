package server;

public enum Messages {
	
	BAD_REQUEST("Abfrage fehlerhaft aufgebaut"),
	KUNDE_MODIFIED("Kundeninformationen erfolgreich geändert"),
	KUNDE_NOT_FOUND("Kunde wurde nicht gefunden"),
	KUNDE_NOT_DELETED("Kunde konnte nicht gelöscht werden, da er nicht gefunden werden konnte"),
	KUNDE_DELETED("Kunde erfolgreich gelöscht"),
	ABLESUNG_CREATED("Ablesung wurde erfolgreich gespeichert"),
	ABLESUNG_NOT_CREATED("Ablesung konnte nicht gespeichert werden, da der entsprechende Kunde nicht existiert"),
	ABLESUNG_MODIFIED("Ablesung erfolgreich geändert"),
	ABLESUNG_NOT_MODIFIED("Ablesung konnte nicht geändert werden, da Ablesung-Id oder Kunden-Id nicht existiert"),
	ABLESUNG_NOT_FOUND("Ablesung wurde nicht gefunden"),
	ABLESUNG_DELETED("Ablesung wurde erfolgreich gelöscht");

	private final String message;
	
	private Messages(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return message;
	}
}
