package epistemique.modeles;

import java.util.HashMap;
import java.util.Map.Entry;

import epistemique.formule.*;

/**
 * Classe représentant un monde pour un modèle épistémique de Kripke.
 * @author Ludovic JEAN-BAPTISTE
 */
public class MondeEpistemique extends Monde {
	
	/**
	 * Ensemble des valuations de ce monde.
	 */
	protected HashMap<Proposition, Boolean> valuations;
	
	/**
	 * Constructeur.
	 */
	public MondeEpistemique() {
		this.valuations = new HashMap<Proposition, Boolean>();
	}
	
	/**
	 * Constructeur.
	 * @param valuations l'ensemble des couples (proposition, valeur_de_verite).
	 */
	public MondeEpistemique(HashMap<Proposition, Boolean> valuations) {
		this.valuations = new HashMap<Proposition, Boolean>(valuations);
	}
	
	/**
	 * Constructeur.
	 * @param modele le modèle attaché à ce monde.
	 * @param valuations l'ensemble des couples (proposition, valeur_de_verite).
	 */
	public MondeEpistemique(Modele<MondeEpistemique> modele, HashMap<Proposition, Boolean> valuations) {
		this.valuations = new HashMap<Proposition, Boolean>(valuations);
		
		for(Proposition prop : modele.getPropositions()) 
			this.valuations.putIfAbsent(prop, false);
	}
	
	/**
	 * Constructeur.
	 * @param modele le modèle attaché à ce monde.
	 * @param propositionsVraies les propositions vraies parmi celles du modèle donné.
	 */
	public MondeEpistemique(Modele<MondeEpistemique> modele, Proposition... propositionsVraies) {
		this.valuations = new HashMap<Proposition, Boolean>();
		for(Proposition prop : propositionsVraies)
			this.valuations.put(prop, true);
		
		for(Proposition prop : modele.getPropositions()) 
			this.valuations.putIfAbsent(prop, false);
	}
	
	/**
	 * Ajout ou modifie une proposition
	 * @param proposition la proposition à ajouter/modifier.
	 * @param valeur la valeur associée (ou nouvelle valeur).
	 */
	public void ajouterProposition(Proposition proposition, boolean valeur) {
		this.valuations.put(proposition, valeur);
	}
	
	/**
	 * Met à jour et/ou ajoute des propositions.
	 * @param propositionsValeurs le dictionnaire proposition (clé) - nouvelle valeur (valeur)
	 */
	public void mettreAJour(HashMap<Proposition, Boolean> propositionsValeurs) {
		for(Entry<Proposition, Boolean> entree : propositionsValeurs.entrySet())
			this.valuations.put(entree.getKey(), entree.getValue());

	}
	
	/**
	 * Accesseur.
	 * @return l'ensemble des valuations de ce monde.
	 */
	public HashMap<Proposition, Boolean> getValuations() {
		return new HashMap<>(this.valuations);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String chaine = "[";
		for(Proposition p : this.valuations.keySet()) {
			if(this.valuations.get(p))
				chaine += p.toString()+", ";
		}
		if(chaine.length() > 2)
			chaine = chaine.substring(0, chaine.length()-2);
		return chaine+"]";
	}
}
