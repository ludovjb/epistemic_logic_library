/**
 * 
 */
package epistemique.formule;

import epistemique.modeles.Modele;
import epistemique.modeles.MondeEpistemique;

/**
 * Classe représentant la valeur faux d'une formule : ⊥.
 * @author ludovicjeanbaptiste
 *
 */
public class Faux extends Formule {

	/**
	 * Constructeur.
	 */
	public Faux() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean estEpistemique() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean estLitteral() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean satisfaite(Modele<MondeEpistemique> modele, MondeEpistemique mondeRef) {
		return false; //toujours évalué à Faux.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "⊥";
	}

}
