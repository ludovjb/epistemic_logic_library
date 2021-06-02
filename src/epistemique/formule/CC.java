package epistemique.formule;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import epistemique.modeles.Agent;
import epistemique.modeles.Modele;
import epistemique.modeles.MondeEpistemique;

/**
 * Classe qui représente la connaissance commune d'une formule pour un ensemble d'agents donné.
 * @author Ludovic JEAN-BAPTISTE
 *
 */
public class CC extends Formule {

	/**
	 * Formule membre de cette formule épistémique.
	 */
	protected Formule membre;
	
	/**
	 * L'ensemble des agents concernés par la connaissance commune.
	 */
	protected HashSet<Agent> ensembleAgents;
	
	/**
	 * Constructeur.
	 * @param ensembleAgents l'ensemble des agents de la connaissance commune.
	 * @param membre la formule connue communément.
	 */
	public CC(Agent[] ensembleAgents, Formule membre) {
		this.ensembleAgents = new HashSet<Agent>();
		for(Agent a : ensembleAgents)
			this.ensembleAgents.add(a);
		this.membre = membre;
	}
	
	/**
	 * Constructeur.
	 * @param ensembleAgents l'ensemble des agents de la connaissance commune.
	 * @param membre formule la formule connue communément.
	 */
	public CC(Collection<Agent> ensembleAgents, Formule membre) {
		this.ensembleAgents = new HashSet<Agent>(ensembleAgents);
		this.membre = membre;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean estEpistemique() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean estLitteral() {
		return false;
	}
	
	/**
	 * Accesseur.
	 * @return l'ensemble d'agents concerné.
	 */
	public HashSet<Agent> getAgents() {
		return new HashSet<>(this.ensembleAgents);
	}
	
	/**
	 * Accesseur.
	 * @return le sous-membre de la formule.
	 */
	public Formule getMembre() {
		return this.membre;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean satisfaite(Modele<MondeEpistemique> modele, MondeEpistemique mondeRef) {
		 
		// pour tous les agents concernés par la connaissance commune.
		for(Agent agent : this.ensembleAgents) {
			LinkedList<MondeEpistemique> liste = modele.getSuccesseurs(agent, mondeRef);
			
			// on parcourt l'ensemble des mondes successeurs.
			for(MondeEpistemique monde : liste) {
				
				// si un monde successeur ne satisfait pas la formule, on retourne false.
				if(!this.membre.satisfaite(modele, monde))
					return false;
			}
		}
		
		// rien ne contredit la formule, il y a connaissance commune : on retourne true.
		return true;
	}

}
