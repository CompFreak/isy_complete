package spieler.lasch;

import spieler.lasch.ZugLaSch;

public class ZugLaSch {
	    int spalte;
	    int zeile;
	    Integer bewertung = null;

	    ZugLaSch(int zeile, int spalte) {
	        this.zeile = zeile;
	        this.spalte = spalte;
	    }

	    public boolean equals(Object o) {
	        ZugLaSch z2 = (ZugLaSch)o;
	        if (this.zeile == z2.zeile && this.spalte == z2.spalte) {
	            return true;
	        }
	        return false;
	    }

	    public Integer getBewertung() {
	        return this.bewertung;
	    }

	    public void setBewertung(Integer bewertung) {
	        this.bewertung = bewertung;
	    }

	    public int getSpalte() {
	        return this.spalte;
	    }

	    public void setSpalte(int spalte) {
	        this.spalte = spalte;
	    }

	    public int getZeile() {
	        return this.zeile;
	    }

	    public void setZeile(int zeile) {
	        this.zeile = zeile;
	    }

	    public String toString() {
	        return "(" + this.zeile + "," + this.spalte + "): " + this.bewertung;
	    }
}
