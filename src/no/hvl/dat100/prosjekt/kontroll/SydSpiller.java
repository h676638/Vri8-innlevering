package no.hvl.dat100.prosjekt.kontroll;

import java.util.Random;

import no.hvl.dat100.prosjekt.TODO;
import no.hvl.dat100.prosjekt.kontroll.dommer.Regler;
import no.hvl.dat100.prosjekt.kontroll.spill.Handling;
import no.hvl.dat100.prosjekt.kontroll.spill.HandlingsType;
import no.hvl.dat100.prosjekt.kontroll.spill.Spillere;
import no.hvl.dat100.prosjekt.modell.Kort;
import no.hvl.dat100.prosjekt.modell.KortSamling;

/**
 * Klasse som for å representere en vriåtter syd-spiller. Strategien er å lete
 * gjennom kortene man har på hand og spille det første som er lovlig.
 *
 */
public class SydSpiller extends Spiller {

	/**
	 * Konstruktør.
	 * 
	 * @param spiller
	 *            posisjon for spilleren (nord eller syd).
	 */
	public SydSpiller(Spillere spiller) {
		super(spiller);
	}

	/**
	 * Metode for å implementere strategi. Strategien er å spille det første
	 * kortet som er lovlig (også en åtter selv om man har andre kort som også
	 * kan spilles). Dersom man ikke har lovlige kort å spille, trekker man om
	 * man ikke allerede har trukket maks antall ganger. I så fall sier man
	 * forbi.
	 * 
	 * @param topp
	 *            kort som ligg øverst på til-bunken.
	 */

	// TODO - START
	/* first-fit strategi */
	int[] playedValues = new int[52];
	int[] playedSuits = new int[52];
	int[] uniqueIdentifier = new int[52];
	int uniqueIdentifierIterator = 0;
	int iteratorPlayedCards = 0;
	/**
	 * Metode for å implementere strategi. Strategien er å spille det første
	 * kortet som er lovlig (også en åtter selv om man har andre kort som også
	 * kan spilles). Dersom man ikke har lovlige kort å spille, trekker man om
	 * man ikke allerede har trukket maks antall ganger. I så fall sier man
	 * forbi.
	 * 
	 * @param topp
	 *            kort som ligg øverst på til-bunken.
	 */
	public boolean checkIfScanned(Kort kort) {
		for (int each:uniqueIdentifier) {
			if(checkUniqueIdentifier(kort) == each) {
				return false;
			}
		}
		return true;
	}

	public int checkUniqueIdentifier(Kort kort) {
		int suit = kort.getFarge().ordinal();
		int verdi = kort.getVerdi();
		int uniqueIdentifier = 13 * suit + verdi;
		return uniqueIdentifier;
	}

	public void hasSeen(Kort kort){
		playedValues[iteratorPlayedCards] = kort.getVerdi();
		playedSuits[iteratorPlayedCards] = kort.getFarge().ordinal();
		iteratorPlayedCards ++;
		uniqueIdentifier[uniqueIdentifierIterator] = checkUniqueIdentifier(kort);
		uniqueIdentifierIterator ++;
	}

	public void scanHand(Kort topp) {
		for (Kort kort:this.getHand().getAllekort()) {
			if(checkIfScanned(kort)) {
				hasSeen(kort);
			}
		}
		if(checkIfScanned(topp)) {
				hasSeen(topp);
			}
	}
	
	public int pointGiver(Kort kort) {
		int points = 0;
		int suit = kort.getFarge().ordinal();
		int verdi = kort.getVerdi();
		for (int x:playedSuits) {
			if (x == suit) {
				points += 1;
			}
		}
		for (int x:playedValues) {
			if (x == verdi) {
				points += 3;
			}
		}
		return points;
	}

	public Kort bestCard(Kort[] cards) {
		Kort bestCard = cards[0];
		for (Kort each:cards) {
			if (pointGiver(each)>pointGiver(bestCard)) {
				bestCard = each;
			}
		}
		return bestCard;
	}

	@Override
	public Handling nesteHandling(Kort topp) {
		// Add cards in hand to memory along with top card
		scanHand(topp);
		// TODO - START
		/* first-fit strategi */
		Kort[] hand = getHand().getAllekort();
		KortSamling lovlige = new KortSamling();
		KortSamling attere = new KortSamling();

		// Gå igjennom kort å finn ut hvilke som kan spilles
		for (Kort k : hand) {
			if (Regler.kanLeggeNed(k, topp)) {
				if (Regler.atter(k)) {
					attere.leggTil(k);
				} else {
					lovlige.leggTil(k);
				}
			}
		}

		Kort spill = null;
		Kort[] spillFra = null;

		if (!lovlige.erTom()) {
			spillFra = lovlige.getAllekort();
		} else if (!attere.erTom())  {
			spillFra = attere.getAllekort();
		}

		Handling handling = null;
		
		if (spillFra != null) {
			
			spill = bestCard(spillFra);
			handling = new Handling(HandlingsType.LEGGNED, spill);
			// setAntallTrekk(0);
			
		} else if (getAntallTrekk() < Regler.maksTrekk()) {
			handling = new Handling(HandlingsType.TREKK, null);
		} else {
			handling = new Handling(HandlingsType.FORBI, null);
			// setAntallTrekk(0);
		}

		return handling;
	
		// TODO - END
	}
}
