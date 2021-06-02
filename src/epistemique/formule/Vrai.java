/**
 * 
 */
package epistemique.formule;

import epistemique.modeles.Modele;
import epistemique.modeles.MondeEpistemique;

/**
 * Classe représentant la valeur vrai d'une formule : ⊤.
 * @author ludovicjeanbaptiste
 *
 */
public class Vrai extends Formule {

	/**
	 * Constructeur.
	 */
	public Vrai() {
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
		return true; //toujours évalué à Vrai.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "⊤";
	}

}
