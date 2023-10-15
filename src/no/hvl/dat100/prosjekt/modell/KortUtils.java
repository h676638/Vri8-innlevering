package no.hvl.dat100.prosjekt.modell;

import java.util.Random;

import no.hvl.dat100.prosjekt.TODO;

public class KortUtils {

	/**
	 * Sorterer en samling. Rekkefølgen er bestemt av compareTo() i Kort-klassen.
	 * 
	 * @see Kort
	 * 
	 * @param samling
	 * 			samling av kort som skal sorteres. 
	 */
	
	public static void sorter(KortSamling samling) {
		
		// TODO - START
		Kort[] tempSamling = new Kort[samling.getAntalKort()];
		boolean flagg = true;
		for (int i = 0;i<samling.getAntalKort();i++) {
			tempSamling[i] = samling.getAllekort()[i];
					
		}
		while (flagg) {
			flagg = false;
			for (int i = 1;i<samling.getAntalKort();i++) {
				if (tempSamling[i-1].compareTo(tempSamling[i]) > 0) {
					Kort x = tempSamling[i-1];
					Kort y = tempSamling[i];
					tempSamling[i-1] = y;
					tempSamling[i] = x;
					flagg = true;
				}
			}
		}
		samling.fjernAlle();
		for (int i = 0;i<tempSamling.length;i++) {
			samling.leggTil(tempSamling[i]);
		}
		// TODO - END
	}
	
	/**
	 * Stokkar en kortsamling. 
	 * 
	 * @param samling
	 * 			samling av kort som skal stokkes. 
	 */
	public static void stokk(KortSamling samling) {
		
		// TODO - START
		Random rand = new Random();
		for (int i = 0;i<samling.getSamling().length;i++) {
			int randInt = rand.nextInt(samling.getSamling().length);
			Kort x = samling.getSamling()[i];
			Kort y = samling.getSamling()[randInt];
			samling.getSamling()[i] = y;
			samling.getSamling()[randInt] = x;
		}
		// TODO - END
	}
	
}
