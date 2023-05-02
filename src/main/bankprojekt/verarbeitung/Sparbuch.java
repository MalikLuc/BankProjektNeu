package verarbeitung;

import java.time.LocalDate;

/**
 * ein Sparbuch, d.h. ein Konto, das nur recht eingeschränkt genutzt
 * werden kann. Insbesondere darf man monatlich nur höchstens 2000€
 * abheben, wobei der Kontostand nie unter 0,50€ fallen darf. 
 * @author Doro
 *
 */
public class Sparbuch extends Konto {
	/**
	 * Zinssatz, mit dem das Sparbuch verzinst wird. 0,03 entspricht 3%
	 */
	private double zinssatz;
	
	/**
	 * Monatlich erlaubter Gesamtbetrag für Abhebungen
	 */
	public static final double ABHEBESUMME = 2000;
	
	/**
	 * Betrag, der im aktuellen Monat bereits abgehoben wurde
	 */
	private double bereitsAbgehoben = 0;

	/**
	 * Monat und Jahr der letzten Abhebung
	 */
	private LocalDate zeitpunkt = LocalDate.now();
	
	/**
	* ein Standard-Sparbuch
	*/
	public Sparbuch() {
		zinssatz = 0.03;
	}

	/**
	* ein Standard-Sparbuch, das inhaber gehört und die angegebene Kontonummer hat
	* @param inhaber der Kontoinhaber
	* @param kontonummer die Wunsch-Kontonummer
	* @throws IllegalArgumentException wenn inhaber null ist
	*/
	public Sparbuch(Kunde inhaber, long kontonummer) {
		super(inhaber, kontonummer);
		zinssatz = 0.03;
	}
	
	@Override
	public String toString()
	{
    	String ausgabe = "-- SPARBUCH --" + System.lineSeparator() +
    	super.toString()
    	+ "Zinssatz: " + this.zinssatz * 100 +"%" + System.lineSeparator();
    	return ausgabe;
	}

	@Override
	public boolean abheben (double betrag) throws GesperrtException{
		if (betrag < 0 || Double.isNaN(betrag)|| Double.isInfinite(betrag)) {
			throw new IllegalArgumentException("Betrag ungültig");
		}
		if(this.isGesperrt())
		{
			GesperrtException e = new GesperrtException(this.getKontonummer());
			throw e;
		}
		LocalDate heute = LocalDate.now();
		if(heute.getMonth() != zeitpunkt.getMonth() || heute.getYear() != zeitpunkt.getYear())
		{
			this.bereitsAbgehoben = 0;
		}
		if (getKontostand() - betrag >= 0.50 && 
				 bereitsAbgehoben + betrag <= getWaehrung().euroInWaehrungUmrechnen(Sparbuch.ABHEBESUMME))
		{
			setKontostand(getKontostand() - betrag);
			bereitsAbgehoben += betrag;
			this.zeitpunkt = LocalDate.now();
			return true;
		}
		else
			return false;
	}

	@Override
	public boolean abheben(double betrag, Waehrung w) throws GesperrtException {
		return this.abheben(this.getAktuelleWaehrung().waehrungInKontowaehrung(betrag,w));
	}

	@Override
	public void einzahlen(double betrag, Waehrung w) {
		this.einzahlen(this.getAktuelleWaehrung().waehrungInKontowaehrung(betrag,w));
	}

	/**
	 * Führt einen Währungswechsel für das Sparbuch durch
	 * @param neu Waehrung in die gewechselt werden soll
	 */
	@Override
	public void waehrungswechsel(Waehrung neu) {
		// Überprüfen, ob die Währung gleich ist
		if(getAktuelleWaehrung() == neu){
			return;
		}
		setKontostand(this.getAktuelleWaehrung().kontowaehrungInWaehrung(getKontostand(),neu));
		this.bereitsAbgehoben = this.getAktuelleWaehrung().kontowaehrungInWaehrung(this.bereitsAbgehoben,neu);
		setWaehrung(neu);
	}

}
