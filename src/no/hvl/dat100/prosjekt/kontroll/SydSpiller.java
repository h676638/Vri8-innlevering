package no.hvl.dat100.prosjekt.kontroll;

import no.hvl.dat100.prosjekt.TODO;
import no.hvl.dat100.prosjekt.kontroll.dommer.Regler;
import no.hvl.dat100.prosjekt.kontroll.spill.Handling;
import no.hvl.dat100.prosjekt.kontroll.spill.HandlingsType;
import no.hvl.dat100.prosjekt.kontroll.spill.Spillere;
import no.hvl.dat100.prosjekt.modell.KortSamling;
import no.hvl.dat100.prosjekt.modell.Kort;
import no.hvl.dat100.prosjekt.modell.Kortfarge;

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
	int[] playedValues = new int[52];
	int[] playedSuits = new int[52];
	int[] uniqueIdentifier = new int[52];
	int uniqueIdentifierIterator = 0;
	int iteratorPlayedCards = 0;

	// Sjekker hvilken unik verdi kortet har
	public int checkUniqueIdentifier(Kort kort) {
		int suit = kort.getFarge().ordinal();
		int verdi = kort.getVerdi();
		int uniqueIdentifier = 13 * suit + verdi;
		return uniqueIdentifier;
	}

	// Sjekker hvert av kortene om de har blitt sett før av SydSpiiller
	public boolean checkIfScanned(Kort kort) {
		for (int each:uniqueIdentifier) {
			if(checkUniqueIdentifier(kort) == each) {
				return false;
			}
		}
		return true;
	}

	// Registrer at kortet har blitt sett og legger til fargen og verdien i tabellene
	public void hasSeen(Kort kort){
		playedValues[iteratorPlayedCards] = kort.getVerdi();
		playedSuits[iteratorPlayedCards] = kort.getFarge().ordinal();
		iteratorPlayedCards ++;
		uniqueIdentifier[uniqueIdentifierIterator] = checkUniqueIdentifier(kort);
		uniqueIdentifierIterator ++;
	}

	// Bruker hasSeen og checkIfScanned for å scanne alle kortene i hånden til SydSpiller
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
	
	// Gir poeng til et kort basert på hvor mange kort av samme farge/verdi SydSpiller har sett før
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

	// Angir pointGiver på alle kort i en tabell og velger kortet med høyest verdi
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
		// Scanner alle kort i hånden på begynnelsen av tur
		scanHand(topp);

		Kort[] hand = getHand().getAllekort();
		KortSamling lovlige = new KortSamling();
		KortSamling attere = new KortSamling();

		// Gå igjennom kort å finn ut hvilke som kan spilles
		for (Kort k : hand) {
			if (Regler.kanLeggeNed(k, topp)) {
				if (Regler.atter(k)) {
					attere.leggTil(k);
				} 
				else {
					lovlige.leggTil(k);
				}
			}
		}

		Kort spill = null;
		Kort[] spillFra = null;

		if (!lovlige.erTom()) {
			spillFra = lovlige.getAllekort();
		} 
		else if (!attere.erTom())  {
			spillFra = attere.getAllekort();
		}

		Handling handling = null;
		
		if (spillFra != null) {
			spill = bestCard(spillFra);
			handling = new Handling(HandlingsType.LEGGNED, spill);	
		} 
		else if (getAntallTrekk() < Regler.maksTrekk()) {
			handling = new Handling(HandlingsType.TREKK, null);
		} 
		else {
			handling = new Handling(HandlingsType.FORBI, null);
		}

		return handling;
	}
}
