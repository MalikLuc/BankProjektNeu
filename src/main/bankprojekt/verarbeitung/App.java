package verarbeitung;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws GesperrtException {
        Girokonto girokonto = new Girokonto();
        girokonto.einzahlen(1000);
        System.out.println(girokonto.getKontostandFormatiert());
        girokonto.abheben(100, Waehrung.BGN);
        System.out.println(girokonto.getKontostandFormatiert());

    }
}
