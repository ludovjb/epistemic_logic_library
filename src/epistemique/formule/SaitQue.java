package epistemique.formule;

import java.util.LinkedList;

import epistemique.modeles.Agent;
import epistemique.modeles.Modele;
import epistemique.modeles.MondeEpistemique;

/**
 * Classe représentant le modal "Ki F" (autrement dit "l'agent i sait que F).
 * Il s'agit d'une formule épistémique.
 * @author ludovicjeanbaptiste
 *
 */
public class SaitQue extends Formule {
	
	/**
	 * Formule membre de cette formule épistémique.
	 */
	protected Formule membre;
	
	/**
	 * L'agent étiquetté par le modal "sait que".
	 */
	protected Agent agent;
	
	/** 
	 * Constructeur logique.
	 * @param agent l'agent qui sait que...
	 * @param membre la formule logique en question.
	 */
	public SaitQue(Agent agent, Formule membre) {
		this.agent = agent;
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
	 * @return l'agent concerné.
	 */
	public Agent getAgent() {
		return this.agent;
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
		LinkedList<MondeEpistemique> liste = modele.getSuccesseurs(this.agent, mondeRef);
		if(liste == null)
			return true;
		
		for(MondeEpistemique monde : liste) {
			if(!this.membre.satisfaite(modele, monde))
				return false;
		}
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "K"+this.agent+"{" + this.membre + "}";
	}
}
