package epistemique.formule;

import java.util.ArrayList;
import java.util.Collection;

import epistemique.modeles.Modele;
import epistemique.modeles.MondeEpistemique;

/**
 * Classe représentant une disjonction de formules.
 * Hérite de la classe abstraite Formule.
 * @author ludovicjeanbaptiste
 *
 */
public class Ou extends Formule {
	/**
	 * Sous-formule gauche.
	 */
	protected Collection<Formule> membres;
	
	/**
	 * Constructeur logique.
	 * @param membres les sous-formules de la disjonction (minimum 2).
	 * @throws IllegalArgumentException si il y a moins de deux sous-formules.
	 */
	public Ou(Formule... membres) throws IllegalArgumentException {
		if(membres.length < 2)
			throw new IllegalArgumentException("La disjonction ET doit prendre au minimum 2 sous-membres.");
		
		this.membres = new ArrayList<>();
		for(Formule m : membres)
			this.membres.add(m);
	}
	
	/**
	 * Constructeur logique.
	 * @param membres les sous-formules de la disjonction (minimum 2).
	 * @throws IllegalArgumentException si il y a moins de deux sous-formules.
	 */
	public Ou(Collection<Formule> membres) throws IllegalArgumentException {
		if(membres.size() < 2)
			throw new IllegalArgumentException("La disjonction ET doit prendre au minimum 2 sous-membres.");
		
		this.membres = new ArrayList<>(membres);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean estEpistemique() {
		//si une des sous-formules est épistémique alors la disjonction est épistémique.
		for(Formule m : this.membres) {
			if(m.estEpistemique())
				return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean estLitteral() {
		// Une disjonction est composée de sous-formules donc ce n'est pas un littéral.
		return false;
	}
	
	/**
	 * Accesseur.
	 * @return les sous-formules de la disjonction.
	 */
	public Collection<Formule> getMembres() {
		return this.membres;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean satisfaite(Modele<MondeEpistemique> modele, MondeEpistemique mondeRef) {
		for(Formule m : this.membres) {
			if(m.satisfaite(modele, mondeRef))
				return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String ch = "(";
		for(Formule m : this.membres)
			ch += m + " ∨ ";
		ch = ch.substring(0, ch.length()-3);
		return ch + ")";
	}
}
