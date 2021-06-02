package epistemique.modeles;

/**
 * Classe représentant un agent pour la logique épistémique.
 * @author Ludovic JEAN-BAPTISTE
 *
 */
public class Agent {
	/**
	 * Numéro de l'agent.
	 */
	protected String nom;
	
	/**
	 * Constructeur.
	 * @param nom nom de l'agent.
	 */
	public Agent(String nom) {
		this.nom = nom;
	}
	
	/**
	 * Accesseur.
	 * @return le nom de l'agent.
	 */
	public String getNom() {
		return this.nom;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.nom;
	}
}
