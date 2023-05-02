package verwaltung;

import verarbeitung.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {

    private long highest = 0;


    private Map<Long, Konto> konten;
    private long bankleitzahl;

    public Bank(long bankleitzahl){
        this.bankleitzahl = bankleitzahl;
        this.konten = new HashMap();
    }

    public long getBankleitzahl(){
        return this.bankleitzahl;
    }

    /**
     * erstellt Girokonto für den Kunden
     * @param inhaber
     * @return
     */
    public long girokontoErstellen(Kunde inhaber){
        long kn = getHighest();
        konten.put(kn,new Girokonto(inhaber,kn,500));
        return kn;
    }


    /**
     *
     * @return
     */
    public long getHighest() {
        this.highest += 1;
        return highest;
    }

    /**
     *
     * @param inhaber
     * @return
     */
    public long sparbuchErstellen(Kunde inhaber){
        long kn = getHighest();
        konten.put(getHighest(),new Sparbuch(inhaber,kn));
        return kn;
    }

    public String getAlleKonten(){
        StringBuilder sb = new StringBuilder();
        for (Konto k : konten.values()){
            sb.append("Kontonummern: " + k.getKontonummerFormatiert() + " , Kontostand: " + k.getKontostandFormatiert() + "\n");
        }
        return sb.toString();
    }

    public List<Long> getAlleKontonummern(){
        return new ArrayList<>(this.konten.keySet());
    }

    public boolean geldAbheben(long von, double betrag)  {
        try {
            return this.konten.get(von).abheben(betrag);
        } catch (GesperrtException e){
            System.out.println("Konto ist gesperrt: " + e.getLocalizedMessage());
            return false;
        }
    }

    public void geldEinzahlen(long auf, double betrag){
        this.konten.get(auf).einzahlen(betrag);
    }

    public boolean kontoLoeschen(long nummer){
        
    }
}
