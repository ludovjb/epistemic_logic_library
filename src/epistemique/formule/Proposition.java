package epistemique.formule;

import epistemique.modeles.Modele;
import epistemique.modeles.MondeEpistemique;

/**
 * Classe représantant une proposition de la logique propositionnelle.
 * @author ludovicjeanbaptiste
 *
 */
public class Proposition extends Formule {
	
	/**
	 * Le nom de la proposition.
	 */
	protected String nom;
	
	/**
	 * Constructeur logique.
	 * @param nom le nom de la proposition
	 */
	public Proposition(String nom) {
		this.nom = nom;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj)
	{
		try {
			Proposition prop = (Proposition) obj;
			return this.nom.equals(prop.nom);
		}
		catch(ClassCastException e) {
			return false;
		}
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
	public int hashCode() {
		return this.nom.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean satisfaite(Modele<MondeEpistemique> modele, MondeEpistemique mondeRef) {
		return mondeRef.getValuations().get(this) == true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.nom;
	}
}
