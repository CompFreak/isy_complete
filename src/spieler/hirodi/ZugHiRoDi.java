package spieler.hirodi;

import spieler.hirodi.ZugHiRoDi;

/**
 * Klasse für den Zug
 * Hier sind die getter und setter Funktionen für den Zug aufgeführt.
 */
public class ZugHiRoDi {
	    int spalte;
	    int zeile;
	    Integer bewertung = null;
	    
	    /*
	     * Konstruktor
	     * @param zeile int Nummer der Zeile
	     * @param spalte int Nummer der Spalte
	     */
	    ZugHiRoDi(int zeile, int spalte) {
	        this.zeile = zeile;
	        this.spalte = spalte;
	    }

	    /*
	     * prüft, ob die Zeile und Spalte eines Zuges gleich sind
	     */
	    public boolean equals(Object o) {
	        ZugHiRoDi z2 = (ZugHiRoDi)o;
	        if (this.zeile == z2.zeile && this.spalte == z2.spalte) {
	            return true;
	        }
	        return false;
	    }

	    /*
	     * getter für die Bewertung
	     * @return bewertung Integer Bewertung des Spielzuges
	     */
	    public Integer getBewertung() {
	        return this.bewertung;
	    }

	    /*
	     * setter für die Bewertung
	     * @param bewertung Integer Bewertung des Spielzuges wird gesetzt
	     */
	    public void setBewertung(Integer bewertung) {
	        this.bewertung = bewertung;
	    }

	    /*
	     * getter für die Spaltenzahl
	     * @return spalte int gibt die aktuelle Spalte zurück
	     */
	    public int getSpalte() {
	        return this.spalte;
	    }

	    /*
	     * setter für die Spalte
	     * @param spalte int setzt die Spalte auf den angegebenen Wert
	     */
	    public void setSpalte(int spalte) {
	        this.spalte = spalte;
	    }

	    /*
	     * getter für die Zeile
	     * @return zeile int gibt die aktuelle Zeile zurück
	     */
	    public int getZeile() {
	        return this.zeile;
	    }

	    /*
	     * setter für die Zeile
	     * @param zeile int setzt die Zeile auf den angegebenen Wert 
	     */
	    public void setZeile(int zeile) {
	        this.zeile = zeile;
	    }

	    /*
	     * gibt die aktuelle Zeile, Spalte und Bewertung zurück
	     */
	    public String toString() {
	        return "(" + this.zeile + "," + this.spalte + "): " + this.bewertung;
	    }
}
