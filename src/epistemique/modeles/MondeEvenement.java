package epistemique.modeles;

import java.util.HashMap;

import epistemique.formule.*;

/**
 * Classe représentant un monde pour un modèle d'évènement de Kripke.
 * @author Ludovic JEAN-BAPTISTE
 */
public class MondeEvenement extends Monde {
	
	/**
	 * Formule logique associée à ce monde.
	 */
	protected Formule preCondition;
	
	/**
	 * Dictionnaire pour post-condition
	 */
	protected HashMap<Proposition, Boolean> postCondition;
	
	/**
	 * Constructeur.
	 * Par défaut, la pre-condition est à Vrai et la post-condition est vide.
	 */
	public MondeEvenement() {
		this(new Vrai(), new HashMap<>());
	}
	
	/**
	 * Constructeur.
	 * Par défaut, la post-condition est vide.
	 * @param preCondition la formule qui décrit l'annonce ou l'évènement.
	 */
	public MondeEvenement(Formule preCondition) {
		this(preCondition, new HashMap<>());
	}
	
	/**
	 * Constructeur.
	 * @param preCondition la formule qui décrit l'annonce ou l'évènement.
	 * @param postCondition l'ensemble des propositions dont la valeur doit changer
	 *                      dictionnaire clé = proposition, valeur = nouvelle valeur pour cette proposition
	 *                      si pas de clé pour une proposition, sa valeur ne changera pas.
	 */
	public MondeEvenement(Formule preCondition, HashMap<Proposition, Boolean> postCondition) {
		this.preCondition = preCondition;
		this.postCondition = new HashMap<>(postCondition);
	}
	
	/**
	 * Constructeur.
	 * Par défaut, la pre-condition est à Vrai.
	 * @param postCondition l'ensemble des propositions dont la valeur doit changer
	 *                      dictionnaire clé = proposition, valeur = nouvelle valeur pour cette proposition
	 *                      si pas de clé pour une proposition, sa valeur ne changera pas.
	 */
	public MondeEvenement(HashMap<Proposition, Boolean> postCondition) {
		this(new Vrai(), postCondition);
	}
	
	/**
	 * Accesseur.
	 * @return le dictionnaire pour la post-condition
	 */
	public HashMap<Proposition, Boolean> getPostCondition() {
		return new HashMap<>(this.postCondition);
	}
	
	/**
	 * Accesseur.
	 * @return la formule associée à ce monde.
	 */
	public Formule getPreCondition() {
		return this.preCondition;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "["+ this.preCondition.toString() + "]";
	}
}
