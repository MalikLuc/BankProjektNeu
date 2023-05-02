package verarbeitung;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class AppTest
{

    /**
     * Teste den Währungstausch Euro → BGN
     */
    @Test
    public void eurInBgnTest() {
        double betrag = 100;
        double ergebnis = Waehrung.BGN.euroInWaehrungUmrechnen(betrag);
        assertEquals(195.58, ergebnis);
    }

    /**
     * Teste den Währungstausch BGN → Euro
     */
    @Test
    public void bgnInEurTest() {
        double betrag = 100;
        double ergebnis = Waehrung.BGN.waehrungInEuroUmrechnen(betrag);
        assertEquals(51.13, ergebnis);
    }

    /**
     * Teste den Währungstausch DKK → BGN
     */
    @Test
    public void dkkInBgnTest() {
        double betrag = 100;
        double ergebnis = Waehrung.DKK.kontowaehrungInWaehrung(betrag, Waehrung.BGN);
        assertEquals(3.17, ergebnis);
    }

    /**
     * Teste den Währungstausch BGN -> BGN
     */
    @Test
    public void bgnInBgnTest() {
        double betrag = 100;
        double ergebnis = Waehrung.BGN.kontowaehrungInWaehrung(betrag, Waehrung.BGN);
        assertEquals(100.0, ergebnis);
    }

    /**
     * Teste abheben von einem Girokonto in Euro
     */
    @Test
    public void girokontoAbhebenTest() throws GesperrtException {
        Konto konto = new Girokonto();
        konto.einzahlen(100);
        assertEquals(100.0, konto.getKontostand());
        konto.abheben(50);
    }

    /**
     * Teste Währungswechsel abheben von einem Girokonto in einer Fremdwährung
     */
    @Test
    public void girokontoAbhebenInFremdwaehrungWechselTest() throws GesperrtException {
        Konto konto = new Girokonto();
        konto.einzahlen(100, Waehrung.BGN);
        assertEquals(51.13, konto.getKontostand());
        assertTrue(konto.abheben(100, Waehrung.BGN));
        assertEquals(0.0, konto.getKontostand());
    }

    /**
     * Teste abheben von einem Girokonto über den Dispo hinweg
     */
    @Test
    public void girokontoAbhebenDispoTest() throws GesperrtException {
        Konto konto = new Girokonto();
        assertTrue(konto.abheben(450));
        assertEquals(-450.0, konto.getKontostand());
        assertFalse(konto.abheben(100));
        assertEquals(-450.0, konto.getKontostand());
    }

    /**
     * Teste abheben von einem Girokonto über den Dispo hinweg in einer Fremdwährung
     */
    @Test
    public void girokontoAbhebenDispoInFremdwaehrungTest() throws GesperrtException {
        Konto konto = new Girokonto();
        assertFalse(konto.abheben(1050, Waehrung.BGN));
    }

    //Tests für die methode waehrungswechsel
    /**
     * Teste Währungswechsel von einem Girokonto in einer Fremdwährung
     */
    @Test
    public void girokontoWaehrungswechselTest() throws GesperrtException {
        Konto konto = new Girokonto();
        konto.einzahlen(100, Waehrung.BGN);
        assertEquals(51.13, konto.getKontostand());
        konto.waehrungswechsel(Waehrung.BGN);
        assertEquals(100.0, konto.getKontostand());
    }

    /**
     * Teste Währungswechsel von einem Girokonto in der kontowährung
     */
    @Test
    public void girokontoWaehrungswechselInKontowaehrungTest() throws GesperrtException {
        Konto konto = new Girokonto();
        konto.einzahlen(100);
        assertEquals(100.0, konto.getKontostand());
        konto.waehrungswechsel(Waehrung.EUR);
        assertEquals(100.0, konto.getKontostand());
    }

    /**
     * Teste Währungswechsel von einem Girokonto von BGN → DKK
     */
    @Test
    public void girokontoWaehrungswechselDKKInBGNTest() throws GesperrtException {
        Konto konto = new Girokonto();
        konto.einzahlen(100, Waehrung.BGN);
        assertEquals(51.13, konto.getKontostand());
        konto.waehrungswechsel(Waehrung.BGN);
        assertEquals(100.0, konto.getKontostand());
        konto.waehrungswechsel(Waehrung.DKK);
        assertEquals(3150.63, konto.getKontostand());
    }

    /**
     * Teste GesperrtException
     */
    @Test
    public void gesperrtExceptionTest() throws GesperrtException {
        Konto konto = new Girokonto();
        konto.sperren();
        try {
            konto.abheben(100);
            fail("GesperrtException wurde nicht geworfen");
        } catch (GesperrtException e) {
            assertEquals("Zugriff auf gesperrtes Konto mit Kontonummer 99887766", e.getMessage());
        }
    }

    /**
     * Teste, ob die umrechnung des Kontostandes richtig funktioniert
     */
    @Test
    public void kontostandUmrechnenTest() throws GesperrtException {
        Konto konto = new Girokonto();
        konto.einzahlen(100, Waehrung.BGN);
        assertEquals(51.13, konto.getKontostand());
        konto.waehrungswechsel(Waehrung.BGN);
        assertEquals(100.0, konto.getKontostand());
        konto.waehrungswechsel(Waehrung.DKK);
        assertEquals(3150.63, konto.getKontostand());
        konto.waehrungswechsel(Waehrung.MKD);
        assertEquals(381.45, konto.getKontostand());
    }

    /**
     * Teste, ob die umrechnung des Dispos richtig funktioniert
     */
    @Test
    public void dispoUmrechnenTest() throws GesperrtException {
        Konto konto = new Girokonto();
        konto.abheben(100, Waehrung.BGN);
        assertEquals(-51.13, konto.getKontostand());
        konto.waehrungswechsel(Waehrung.BGN);
        assertEquals(-100.0, konto.getKontostand());
        konto.waehrungswechsel(Waehrung.DKK);
        assertEquals(-3150.63, konto.getKontostand());
        konto.waehrungswechsel(Waehrung.MKD);
        assertEquals(-381.45, konto.getKontostand());
    }

    /**
     * Teste, ob die umrechnung des der ABHEBESUMME richtig funktioniert und damit die bisher einzige
     */
    @Test
    public void abhebesummeUmrechnenTest() throws GesperrtException {
        Konto konto = new Sparbuch();
        konto.einzahlen(4000, Waehrung.BGN);
        konto.waehrungswechsel(Waehrung.BGN);
        assertTrue(konto.abheben(3000, Waehrung.BGN));
    }

    /**
     * Teste, ob bei einem ungültigem betrag beim abheben eine IllegalArgumentException geworfen wird
     */
    @Test
    public void abhebenUngueltigerBetragTest() throws GesperrtException {
        Konto konto = new Sparbuch();
        try {
            konto.abheben(-100, Waehrung.BGN);
            fail("IllegalArgumentException wurde nicht geworfen");
        } catch (IllegalArgumentException e) {
            assertEquals("Betrag ungültig", e.getMessage());
        }
    }

}
