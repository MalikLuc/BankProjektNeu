package verarbeitung;

import org.decimal4j.util.DoubleRounder;

public enum Waehrung {
    EUR(1), BGN(1.9558), DKK(61.62), MKD(7.4604);

    private double exchangeRate;

    Waehrung(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    /**
     * This method converts euros in the currency of the Konto
     * @param betrag the amount to be converted
     * @return der konvertierte Betrag in der Kontowährung
     */
    public double euroInWaehrungUmrechnen(double betrag){
        return DoubleRounder.round(betrag * this.exchangeRate,2);
    }

    /**
     * Diese Methode konvertiert die Kontowährung in Euro
     * @param betrag der zu konvertierende Betrag
     * @return der Betrag konvertiert von Euro in Kontowährung
     */
    public double waehrungInEuroUmrechnen(double betrag){
        return DoubleRounder.round(betrag / this.exchangeRate,2);
    }

    /**
     * Diese Methode konvertiert den Betrag von der Kontowährung in die gegebene Währung w
     * @param betrag der gegebene Betrag
     * @param w die gegebene Währung
     * @return den Betrag der Kontowährung konvertiert in die gegebene Währung
     */
    public double kontowaehrungInWaehrung(double betrag, Waehrung w){
        return DoubleRounder.round(w.euroInWaehrungUmrechnen(waehrungInEuroUmrechnen(betrag)),2);
    }

    /**
     * Diese Methode konvertiert den Betrag von der gegebenen Währung w in die Kontowährung
     * @param betrag der gegebene Betrag
     * @param w die gegebene Währung
     * @return den Betrag der gegebenen Währung konvertiert in die Kontowährung
     */
    public double waehrungInKontowaehrung(double betrag, Waehrung w) {
        return DoubleRounder.round(euroInWaehrungUmrechnen(w.waehrungInEuroUmrechnen(betrag)), 2);
    }

}

