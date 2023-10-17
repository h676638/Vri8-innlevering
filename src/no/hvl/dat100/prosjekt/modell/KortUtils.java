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
		boolean flagg = true;
		while(flagg) {
			flagg = false;
			Kort[] sam = samling.getSamling();
			for (int i = 1;i<sam.length;i++) {
				if (sam[i-1] != null && sam[i] != null) {
					if(sam[i-1].compareTo(sam[i]) > 0) {
						Kort tempCard = sam[i-1];
						sam[i-1] = sam[i];
						sam[i] = tempCard;
						flagg = true;
					}
				}
			}
			if (flagg == false) {
				break;
			}
		}
	}
	
	/**
	 * Stokkar en kortsamling. 
	 * 
	 * @param samling
	 * 			samling av kort som skal stokkes. 
	 */
	public static void stokk(KortSamling samling) {
		Random rand = new Random();
		for (int i = 0;i<samling.getSamling().length;i++) {
			int randInt = rand.nextInt(samling.getSamling().length);
			Kort x = samling.getSamling()[i];
			Kort y = samling.getSamling()[randInt];
			samling.getSamling()[i] = y;
			samling.getSamling()[randInt] = x;
		}
	}
	
}
