/**
 * 
 */
package epistemique.formule;

import epistemique.modeles.Modele;
import epistemique.modeles.MondeEpistemique;

/**
 * Classe qui représente l'implication entre deux formule A et B.
 * A implique B.
 * @author Ludovic JEAN-BAPTISTE
 *
 */
public class Implique extends Formule {
	
	/**
	 * Sous-formule gauche.
	 */
	protected Formule membreGauche;
	
	/**
	 * Sous-formule droite.
	 */
	protected Formule membreDroit;
	
	/**
	 * Constructeur logique.
	 * @param membreDroit la sous-formule droite de la conjonction.
	 * @param membreGauche la sous-formule gauche de la conjonction.
	 */
	public Implique(Formule membreGauche, Formule membreDroit) {
		this.membreGauche = membreGauche;
		this.membreDroit = membreDroit;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean estEpistemique() {
		//si une des sous-formules est épistémique alors la conjonction est épistémique.
		return this.membreGauche.estEpistemique() || this.membreDroit.estEpistemique();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean estLitteral() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean satisfaite(Modele<MondeEpistemique> modele, MondeEpistemique mondeRef) {
		return !this.membreGauche.satisfaite(modele, mondeRef) || this.membreDroit.satisfaite(modele, mondeRef);
	}

}
