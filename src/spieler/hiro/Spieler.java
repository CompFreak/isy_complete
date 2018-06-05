package spieler.hiro;

import java.util.Vector;

import spieler.Farbe;
import spieler.OthelloSpieler;
import spieler.Zug;
import spieler.ZugException;

public class Spieler implements OthelloSpieler {
    public static final int PASSEN = -1;
    private static int[][] brettBewertung = new int[8][8];
    private static int ALPHA = -100000;
    private static int BETA = 100000;
    private int[][] brett;
    private int spieler;
    private int anzahlPassen = 0;
    private int tiefe = 9;
    
    /**
    * fuellt die Bewertungsmatrix aus, diese ist in 4 Abschnitte eingeteilt
    *  _________
    * |    |    |
    * |  1 |  2 |
    * |----|----|
    * | 3  |  4 |
    * |____|____|
    */
    private static void fuelleBewertungsmatrix() {
        int spalte;
        Spieler.brettBewertung[0][0] = 50;
        Spieler.brettBewertung[0][1] = -1;
        Spieler.brettBewertung[0][2] = 5;
        Spieler.brettBewertung[0][3] = 2;
        Spieler.brettBewertung[1][0] = -1;
        Spieler.brettBewertung[1][1] = -10;
        Spieler.brettBewertung[1][2] = 1;
        Spieler.brettBewertung[1][3] = 1;
        Spieler.brettBewertung[2][0] = 5;
        Spieler.brettBewertung[2][1] = 1;
        Spieler.brettBewertung[2][2] = 1;
        Spieler.brettBewertung[2][3] = 1;
        Spieler.brettBewertung[3][0] = 2;
        Spieler.brettBewertung[3][1] = 1;
        Spieler.brettBewertung[3][2] = 1;
        Spieler.brettBewertung[3][3] = 0;
        int zeile = 0;
        while (zeile < 4) {
            spalte = 4;
            while (spalte < 8) {
                Spieler.brettBewertung[zeile][spalte] = brettBewertung[zeile][7 - spalte];
                ++spalte;
            }
            ++zeile;
        }
        zeile = 4;
        while (zeile < 8) {
            spalte = 0;
            while (spalte < 8) {
                Spieler.brettBewertung[zeile][spalte] = brettBewertung[7 - zeile][spalte];
                ++spalte;
            }
            ++zeile;
        }
    }

    /**
    * Konstruktor
    * fuellt die Bewertungsmatrix mit aus und initialisiert das Spielfeld
    */
    public Spieler() {
        Spieler.fuelleBewertungsmatrix();
        this.brett = new int[8][8];
    }

    /**
    * Konstruktor mit Suchtiefe
    * @param suchtiefe int gibt die Suchtiefe an
    */
    public Spieler(int suchtiefe) {
        this();
        if (suchtiefe > 0 && suchtiefe < 60) {
            tiefe = suchtiefe;
        }
    }

    private Spieler(int farbeComputer, int suchtiefe) {
        this(suchtiefe);
        spieler = farbeComputer;
    }

    /*
     * Klont den Spieler mit dem aktuellen Brettstand
     * @return kopie Spieler Spielerkopie mit allen Werten und Brett
     */
    public Spieler clone() {
        Spieler kopie = new Spieler(spieler, tiefe);
        for (int zeile = 0; zeile < brett.length; zeile++) {
            for (int spalte = 0; spalte < brett[0].length; spalte++) {
                kopie.brett[zeile][spalte] = brett[zeile][spalte];
            }
        }
        return kopie;
    }

    /*
     * berechnet alle möglichen Züge und speichert diese in einem Vector ab
     * @param spielerAmZug int gibt an, für welchen Spieler die möglichen Züge berechnet werden sollen
     * @return moeglicheZuege Vector<ZugHiRo> gibt alle möglichen Züge zurück 
     */
    private Vector<ZugHiRo> berechneMoeglicheZuege(int spielerAmZug) {
    	// Variable für die möglichen Züge erzeugen
        Vector<ZugHiRo> moeglicheZuege = new Vector<ZugHiRo>();
        // zuerst die Ecken überprüfen, da diese eine besondere Stellung im Spiel haben
        if (brett[0][0] == 0 && (isLegal(0, 0, 0, 1, spielerAmZug) || isLegal(0, 0, 1, 0, spielerAmZug) || isLegal(0, 0, 1, 1, spielerAmZug))) {
            moeglicheZuege.add(new ZugHiRo(0, 0));
        }
        if (brett[0][7] == 0 && (isLegal(0, 7, 0, -1, spielerAmZug) || isLegal(0, 7, 1, 0, spielerAmZug) || isLegal(0, 7, 1, -1, spielerAmZug))) {
            moeglicheZuege.add(new ZugHiRo(0, 7));
        }
        if (brett[7][0] == 0 && (isLegal(7, 0, 0, 1, spielerAmZug) || isLegal(7, 0, -1, 0, spielerAmZug) || isLegal(7, 0, -1, 1, spielerAmZug))) {
            moeglicheZuege.add(new ZugHiRo(7, 0));
        }
        if (brett[7][7] == 0 && (isLegal(7, 7, 0, -1, spielerAmZug) || isLegal(7, 7, -1, 0, spielerAmZug) || isLegal(7, 7, -1, -1, spielerAmZug))) {
            moeglicheZuege.add(new ZugHiRo(7, 7));
        }
        
        // geht erst alle Spalten durch und prüft die möglichen Züge
        for (int spalte = 1; spalte < 7; spalte++) {
            if (brett[0][spalte] == 0 && (isLegal(0, spalte, 0, 1, spielerAmZug) || isLegal(0, spalte, 1, 1, spielerAmZug) || isLegal(0, spalte, 1, 0, spielerAmZug) || isLegal(0, spalte, 1, -1, spielerAmZug) || isLegal(0, spalte, 0, -1, spielerAmZug))) {
                moeglicheZuege.add(new ZugHiRo(0, spalte));
            }
            if (brett[7][spalte] == 0 && (isLegal(7, spalte, 0, 1, spielerAmZug) || isLegal(7, spalte, 0, -1, spielerAmZug) || isLegal(7, spalte, -1, -1, spielerAmZug) || isLegal(7, spalte, -1, 0, spielerAmZug) || isLegal(7, spalte, -1, 1, spielerAmZug))) {
                moeglicheZuege.add(new ZugHiRo(7, spalte));
            }
        }

        // prüft alle Zeilen auf mögliche Züge
        for (int zeile = 1; zeile < 7; zeile++) {
            if (brett[zeile][0] == 0 && (isLegal(zeile, 0, -1, 0, spielerAmZug) || isLegal(zeile, 0, -1, 1, spielerAmZug) || isLegal(zeile, 0, 0, 1, spielerAmZug) || isLegal(zeile, 0, 1, 1, spielerAmZug) || isLegal(zeile, 0, 1, 0, spielerAmZug))) {
                moeglicheZuege.add(new ZugHiRo(zeile, 0));
            }
            if (brett[zeile][7] == 0 && (isLegal(zeile, 7, -1, 0, spielerAmZug) || isLegal(zeile, 7, 1, 0, spielerAmZug) || isLegal(zeile, 7, 1, -1, spielerAmZug) || isLegal(zeile, 7, 0, -1, spielerAmZug) || isLegal(zeile, 7, -1, -1, spielerAmZug))) {
                moeglicheZuege.add(new ZugHiRo(zeile, 7));
            }
        }
        
        for (int zeile = 1; zeile < 7; zeile++) {
            for (int spalte = 1; spalte < 7; spalte++) {
                if (brett[zeile][spalte] == 0 && (isLegal(zeile, spalte, -1, -1, spielerAmZug) || isLegal(zeile, spalte, -1, 0, spielerAmZug) || isLegal(zeile, spalte, -1, 1, spielerAmZug) || isLegal(zeile, spalte, 0, -1, spielerAmZug) || isLegal(zeile, spalte, 0, 1, spielerAmZug) || isLegal(zeile, spalte, 1, -1, spielerAmZug) || isLegal(zeile, spalte, 1, 0, spielerAmZug) || isLegal(zeile, spalte, 1, 1, spielerAmZug))) {
                    moeglicheZuege.add(new ZugHiRo(zeile, spalte));
                }
            }
        }
        return moeglicheZuege;
    }

    /*
     * bewertet die aktuelle Position und gibt diese Bewertung zurück
     * @return bewertung int die Bewertung für die aktuelle Position
     */
    private int bewertePosition() {
        int bewertung = 0;
        // geht durch jedes Feld des Brettes und multipliziert es mit dem Wert aus der Bewertungmatrix (brettBewertung)
        for (int zeile = 0; zeile < 8; zeile++) {
            for (int spalte = 0; spalte < 8; spalte++) {
                bewertung += brett[zeile][spalte] * spieler * brettBewertung[zeile][spalte];
            }
        }
        return bewertung;
    }

    /*
     * findet den besten Zug
     * hier wird der Alpha-Beta-Algorithmus verwendet
     * @param tiefe int gibt die Tiefe an, bis zu der gegangen werden soll
     * @param iSpieler int  gibt den Spieler an
     * @paran alpha int Wert für ALPHA
     * @param beta int Wert für BETA
     * @return besterZug ZugHiRo gibt den Besten Zug zurück
     */
    private ZugHiRo findeBestenZug(int tiefe, int iSpieler, int alpha, int beta) {
        Vector<ZugHiRo> moeglicheZuege = berechneMoeglicheZuege(iSpieler);
        int anzahlMoeglicheZuege = moeglicheZuege.size();
        
        if (anzahlMoeglicheZuege > 0) {
            int maxpos = -1;
            int minpos = -1;
            for (int i = 0; i < anzahlMoeglicheZuege; i++) {
                Spieler brettKopie = this.clone();
                brettKopie.ziehe(iSpieler, moeglicheZuege.get(i));
                if (tiefe == 1) {
                    moeglicheZuege.get((int)i).bewertung = brettKopie.bewertePosition();
                } else {
                	System.out.println("suchtiefe:" + tiefe + " /ispieler:" + iSpieler + " /alpha:" + alpha + " /beta:" + beta);
                    ZugHiRo besterZug = brettKopie.findeBestenZug(tiefe - 1, iSpieler * -1, alpha, beta);
                    moeglicheZuege.get((int)i).bewertung = besterZug.bewertung;
                    if (iSpieler == spieler && besterZug.bewertung > alpha) {
                        alpha = besterZug.bewertung;
                    }
                    if (iSpieler != spieler && besterZug.bewertung < beta) {
                        beta = besterZug.bewertung;
                    }
                }
                if (i == 0) {
                    maxpos = 0;
                    minpos = 0;
                } else {
                    int bewertung = moeglicheZuege.get((int)i).bewertung;
                    if (bewertung > moeglicheZuege.get((int)maxpos).bewertung) {
                        maxpos = i;
                    } else if (bewertung < moeglicheZuege.get((int)minpos).bewertung) {
                        minpos = i;
                    }
                }
                if (alpha >= beta) 
                	break;
            }
            if (iSpieler == spieler) {
                return moeglicheZuege.get(maxpos);
            }
            return moeglicheZuege.get(minpos);
        }
        ZugHiRo besterZug = new ZugHiRo(-1, -1);
        
        if(tiefe == 1)
        	besterZug.bewertung = Integer.valueOf(bewertePosition());
        else
        	besterZug.bewertung = findeBestenZug((int)(tiefe - 1), (int)(spieler * -1), (int)alpha, (int)beta).bewertung;
        
        return besterZug;
    }

    private boolean isLegal(int zeile, int spalte, int dz, int ds, int spieler) {
        if (berechneDrehsteine(zeile, spalte, dz, ds, spieler) != null) {
            return true;
        }
        return false;
    }
    
    private Vector<Integer> ziehe(int spieler, ZugHiRo zug) {
        Vector<Integer> drehsteine = new Vector<Integer>();
        int dz = -1;
        while (dz < 2) {
            int ds = -1;
            while (ds < 2) {
                Vector<Integer> neueSteine;
                if ((dz != 0 || ds != 0) && (neueSteine = berechneDrehsteine(zug.zeile, zug.spalte, dz, ds, spieler)) != null) {
                    drehsteine.addAll(neueSteine);
                }
                ++ds;
            }
            ++dz;
        }
        if (!drehsteine.isEmpty()) {
            brett[zug.zeile][zug.spalte] = spieler;
            for (int i= 0; i < drehsteine.size(); i++) {
                int drehpos = drehsteine.get(i);
                brett[drehpos / 8][drehpos % 8] = spieler;
            }
        }
        return drehsteine;
    }

    private Vector<Integer> berechneDrehsteine(int zeile, int spalte, int dz, int ds, int spieler) {
        Vector<Integer> drehsteine = null;
        int gegenspieler = spieler * -1;
        int neueZeile = zeile + dz;
        int neueSpalte = spalte + ds;
        if (neueZeile >= 0 && neueZeile < 8 && neueSpalte >= 0 && neueSpalte < 8) {
            if (brett[neueZeile][neueSpalte] != gegenspieler) {
                return null;
            }
            drehsteine = new Vector<Integer>();
            drehsteine.add(new Integer(neueZeile * 8 + neueSpalte));
            while (neueZeile < 8 && (neueZeile) >= 0 && neueSpalte < 8 && (neueSpalte) >= 0) {
                if (brett[neueZeile][neueSpalte] == spieler) {
                    return drehsteine;
                }
                if (brett[neueZeile][neueSpalte] == 0) {
                    return null;
                }
                drehsteine.add(new Integer(neueZeile * 8 + neueSpalte));
                neueZeile += dz;
                neueSpalte += ds;
            }
        }
        return null;
    }

    private ZugHiRo eigenerZug(int suchtiefe) {
        ZugHiRo meinZug = findeBestenZug(suchtiefe, spieler, ALPHA, BETA);
        if (meinZug == null) {
            return null;
        }
        if (meinZug.zeile == -1) {
            anzahlPassen++;
            return meinZug;
        }
        anzahlPassen = 0;
        ziehe(spieler, meinZug);
        return meinZug;
    }
    private boolean gegnerZug(int zeile, int spalte) {
        ZugHiRo gZug;
       
        Vector<ZugHiRo> moeglicheZuege = berechneMoeglicheZuege(-1 * spieler);
        if (moeglicheZuege.contains(gZug = new ZugHiRo(zeile, spalte))) {
        	if(gZug.zeile == -1)
        		anzahlPassen++;
        	else
        		anzahlPassen = 0;
            ziehe(- spieler, gZug);
            return true;
        }
        return false;
    }

    @Override
    public Zug berechneZug(Zug vorherigerZug, long zeitWeiss, long zeitSchwarz) throws ZugException {
        if (vorherigerZug != null) {
            gegnerZug(vorherigerZug.getZeile(), vorherigerZug.getSpalte());
        }
        ZugHiRo meinZug = eigenerZug(tiefe);
        Zug ergebnis = new Zug(meinZug.zeile, meinZug.spalte);
        return ergebnis;
    }

    @Override
    public void neuesSpiel(Farbe meineFarbe, int bedenkzeitInSekunden) {
        spieler = -1;
        if (meineFarbe == Farbe.WEISS) {
            spieler = 1;
        }

        for (int zeile = 0; zeile < brett.length; zeile++) {
            for (int spalte = 0; spalte < brett[0].length; spalte++) {
                brett[zeile][spalte] = 0;
            }
        }
        brett[3][3] = 1;
        brett[3][4] = -1;
        brett[4][3] = -1;
        brett[4][4] = 1;
    }

    @Override
    public String meinName() {
        return "LaSch";
    }
}

