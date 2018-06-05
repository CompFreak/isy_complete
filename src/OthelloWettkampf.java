import java.util.ArrayList;
import java.util.List;

import spieler.OthelloSpieler;

public class OthelloWettkampf {
	public static void main(String[] args)
	{
		/* Es muss ein OthelloArena-Objekt erzeugt
	     * werden. Bei der Erzeugung werden die am Wettkampf
	     * teilnehmenden Spieler in Form einer Liste von
	     * Othello-Spielern uebergeben. Durch die Erzeugung des
	     * OthelloArena-Objekts wird der Wettkampf gestartet.
	     */
	    
	    //Spielerliste aufbauen
	    List<OthelloSpieler> spieler = 
	        new ArrayList<OthelloSpieler>();
	    //Die Spieler
	    //spieler.add(new spieler.Referenzspieler( )); //Suchtiefe Default
	    spieler.add(new spieler.Referenzspieler(9)); //Suchtiefe 9
	    spieler.add(new spieler.hiro.Spieler());
	    
	    
	    new rahmen.OthelloArena(150,  //Gesamtbedenkzeit in Sekunden 
	          spieler,                //Spielerliste 
	          true);                  //debug aus
	  }
}
