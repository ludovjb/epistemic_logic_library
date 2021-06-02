package epistemique.formule;

import epistemique.modeles.Modele;
import epistemique.modeles.MondeEpistemique;

/**
 * Classe représentant une négation de formule.
 * Hérite de la classe abstraite Formule.
 * @author ludovicjeanbaptiste
 *
 */
public class Non extends Formule {
	/**
	 * Formule dont on applique la négation.
	 */
	protected Formule membre;
	
	/**
	 * Constructeur logique.
	 * @param membre la formule dont on applique la négation.
	 */
	public Non(Formule membre) {
		this.membre = membre;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean estEpistemique() {
		// si le membre est épistémique, sa négation est par définition épistémique.
		return this.membre.estEpistemique();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean estLitteral() {
		// si le membre est atomique, sa négation est par définition atomique.
		return this.membre.estLitteral();
	}
	
	/**
	 * Accesseur.
	 * @return la sous-formule de la négation.
	 */
	public Formule getMembre() {
		return this.membre;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean satisfaite(Modele<MondeEpistemique> modele, MondeEpistemique mondeRef) {
		return !this.membre.satisfaite(modele, mondeRef);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "¬" + this.membre;
	}
}
