package epistemique.modeles;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;

import epistemique.formule.Proposition;

import java.util.HashMap;

/**
 * Classe représentant un modèle de Kripke avec ses mondes et ses relations d'équivalence.
 * Il est caractérisé par un type de Monde (MondeEpistemique ou MondeEvenement)
 * @author Ludovic JEAN-BAPTISTE
 *
 */
public class Modele<T extends Monde> {
	
	/**
	 * Ensemble des propositions du modèle.
	 * Toutes les proposition correspondant au modèle 
	 * doivent être transmises au modèle au préalable.
	 */
	protected HashSet<Proposition> propositions;
	
	/**
	 * Ensemble des mondes du modèle.
	 */
	protected HashSet<T> mondes;
	
	/**
	 * Liste de successeurs pour les relations.
	 */
	protected HashMap<Agent, HashMap<T, LinkedList<T>>> relationsSucc;
	
	/**
	 * Ensemble des agents de ce modèle.
	 */
	protected HashSet<Agent> agents;
	
	/**
	 * Monde pointé si il y en a un (sinon null)
	 */
	protected T mondePointe;
	
	/**
	 * Constructeur par défaut.
	 */
	public Modele() {
		this.propositions = new HashSet<Proposition>();
		this.mondes = new HashSet<T>();
		this.relationsSucc = new HashMap<Agent, HashMap<T, LinkedList<T>>>();
		this.agents = new HashSet<Agent>();
		this.mondePointe = null;
	}
	
	/**
	 * Constructeur.
	 * @param agents une collection des agents du modèle.
	 * @param propositions une collection des propositions du modèle.
	 */
	public Modele(Collection<Agent> agents, Collection<Proposition> propositions) {
		this();
		for(Agent a : agents)
			this.relationsSucc.put(a, new HashMap<T, LinkedList<T>>());
			
		this.agents.addAll(agents);
		this.propositions.addAll(propositions);
		
	}
	
	/**
	 * Ajoute un agent au modèle.
	 * @param agent l'agent à ajouter.
	 * @return true si l'agent l'agent a été ajouté (si il n'existait pas avant), false sinon.
	 */
	public boolean ajouterAgent(Agent agent) {
		this.relationsSucc.putIfAbsent(agent, new HashMap<T, LinkedList<T>>());
		return this.agents.add(agent);
	}
	
	/**
	 * Ajoute un monde préalablement créé.
	 * @param estPointe true si le monde ajouté est le monde pointé du modèle.
	 * @param monde le monde à ajouter.
	 * @return le monde ajouté.
	 */
	public T ajouterMonde(boolean estPointe, T monde) {
		if(this.mondes.add(monde)) {
			if(estPointe)
				this.mondePointe = monde;
			return monde;
		}
		return null;
	}
	
	/**
	 * Ajoute une proposition au modèle
	 * @param proposition la proposition à ajouter au modèle.
	 * @return la proposition nouvelle ajoutée ou null si elle était déjà ajoutée.
	 */
	public Proposition ajouterProposition(Proposition proposition) {
		if(this.propositions.add(proposition))
				return proposition;
		return null;
	}
	
	/**
	 * Ajoute une relation définie par l'agent étiqueté et les deux mondes (départ et arrivée) en argument.
	 * @param agent l'agent étiqueté par la relation.
	 * @param mondeA le monde d'origine.
	 * @param mondeB le monde d'arrivée.
	 * @return true si la relation a bien été ajoutée (si n'existait pas déjà), false sinon.
	 */
	public boolean ajouterRelation(Agent agent, T mondeA, T mondeB) {
		this.ajouterAgent(agent); //l'ajoutera si pas déjà ajouté au préalable.
		
		//si la relation existe, on ne la recréée pas.
		if(this.relationExiste(agent, mondeA, mondeB))
			return false;
	
		//ajoute dans la liste de successeurs
		if(!this.relationsSucc.containsKey(agent))
			this.relationsSucc.put(agent, new HashMap<T, LinkedList<T>>());
		HashMap<T, LinkedList<T>> mapAgent = this.relationsSucc.get(agent);
		
		//si le monde de départ n'existe pas dans le dictionnaire
		//on créé l'entrée clé-valeur (avec en clé le mondeA).
		if(!mapAgent.containsKey(mondeA)) {
			LinkedList<T> list = new LinkedList<T>();
			list.add(mondeB);
			mapAgent.put(mondeA, list);
		}
		//sinon, on ajoute la relation à la fin de la liste
		else
			mapAgent.get(mondeA).add(mondeB);
		
		//et on finit par retourner true : ajout effectué.
		return true;
	}
	
	/**
	 * Ajoute l'ensemble des relations deux à deux et de façon symétrique à chaque monde dans l'ensemble.
	 * @param agent l'agent concerné.
	 * @param mondes la collection des mondes
	 */
	public void ajouterRelations(Agent agent, Collection<T> mondes) {
		for(T m1 : mondes) {
			for(T m2 : mondes)
				this.ajouterRelationSym(agent, m1, m2);
		}
	}
	
	/**
	 * Ajoute l'ensemble des relations deux à deux et de façon symétrique pour chaque monde de l'ensemble.
	 * @param agent l'agent concerné.
	 * @param mondes la collection des mondes
	 */
	@SuppressWarnings("unchecked")
	public void ajouterRelations(Agent agent, T... mondes) {
		for(T m1 : mondes) {
			for(T m2 : mondes)
				this.ajouterRelationSym(agent, m1, m2);
		}
	}
	
	/**
	 * Ajoute une double relation (symétrique) définie par l'agent étiqueté et les deux mondes en argument.
	 * Elle appelle {@link #ajouterRelation(Agent, Monde, Monde)} pour (monde1 vers monde2) et (monde2 vers monde1).
	 * @param agent l'agent étiqueté par la relation.
	 * @param monde1 le premier monde.
	 * @param monde2 le deuxième monde.
	 * @return true si au moins un relation dans un sens a bien été ajoutée (si n'existait pas déjà), false sinon.
	 */
	public boolean ajouterRelationSym(Agent agent, T monde1, T monde2) {
		boolean b1 = this.ajouterRelation(agent, monde1, monde2);
		boolean b2 = this.ajouterRelation(agent, monde2, monde1);
		return b1 || b2;
	}
	
	/**
	 * Accesseur.
	 * @return l'ensemble des agents.
	 */
	public HashSet<Agent> getAgents() {
		return new HashSet<>(this.agents);
	}
	
	/**
	 * Accesseur.
	 * @return le monde pointé si il existe, sinon null.
	 */
	public T getMondePointe() {
		return this.mondePointe;
	}
		
	/**
	 * Accesseur.
	 * @return l'ensemble des mondes.
	 */
	public HashSet<T> getMondes() {
		return new HashSet<>(this.mondes);
	}
	
	/**
	 * Accesseur.
	 * @return l'ensemble des propositions.
	 */
	public HashSet<Proposition> getPropositions() {
		return new HashSet<>(this.propositions);
	}
	
	/**
	 * Récupère l'ensemble des relations pour un agent donné.
	 * @param agent l'agent concerné.
	 * @return un dictionnaire monde d'origine (clé) - liste de successeurs pour ce monde (valeur)
	 */
	public HashMap<T, LinkedList<T>> getRelations(Agent agent) {
		return new HashMap<>(this.relationsSucc.get(agent));
	}
	
	/**
	 * Retourne la liste des successeurs pour un agent et un monde donné.
	 * pour un agent et un monde d'origine donné.
	 * @param agent l'agent concerné par la relation recherchée.
	 * @param monde le monde d'origine.
	 * @return la liste de successeurs (une copie).
	 */
	public LinkedList<T> getSuccesseurs(Agent agent, T monde) {
		HashMap<T, LinkedList<T>> mapAgent = this.relationsSucc.get(agent);
		if(mapAgent == null)
			return null;
		
		LinkedList<T> liste = mapAgent.get(monde);
		if(liste == null)
			return null;
		
		return new LinkedList<T>(liste);
	}
	
	/**
	 * Compte le nombre de relations présente dans le modèle
	 * en parcourant les listes de successeurs pour chaque agent.
	 * @return le nombre de relations total.
	 */
	public int nbRelations() {
		int compteur = 0;
		for(HashMap<T, LinkedList<T>> mapAgent : this.relationsSucc.values()) {
			for(LinkedList<T> liste : mapAgent.values())
				compteur += liste.size();
		}
		return compteur;
	}
	
	/**
	 * Vérifie si la relation décrite par les arguments existe.
	 * @param agent l'agent concerné par la relation.
	 * @param mondeA le monde de départ.
	 * @param mondeB le monde d'arrivée.
	 * @return true si la relation (agent, mondeA, mondeB) existe dans le modèle.
	 */
	public boolean relationExiste(Agent agent, T mondeA, T mondeB) {
		HashMap<T, LinkedList<T>> mapAgent = this.relationsSucc.get(agent);
		//si pas de clé pour l'agent, pas besoin d'aller plus loin : false.
		if(mapAgent == null)
			return false;
		
		//si pas de clé pour le mondeA, pas besoin d'aller plus loin : false.
		LinkedList<T> liste = mapAgent.get(mondeA);
		if(liste == null)
			return false;
		
		//sinon on parcourt le chaînage et on retourne true si le mondeB figure dedans.
		return liste.contains(mondeB);
	}
	
	/**
	 * Mutateur.
	 * @param mondePointe le nouveau monde pointé. Attention, il doit faire parti des mondes du modèle !
	 * @throws IllegalArgumentException si le monde donné n'appartient pas au modèle.
	 */
	public void setMondePointe(T mondePointe) throws IllegalArgumentException {
		if(!this.mondes.contains(mondePointe))
			throw new IllegalArgumentException("Le monde pointé doit faire parti du modèle au préalable.");
		else
			this.mondePointe = mondePointe;
	}
	
	/**
	 * Supprime l'agent du modèle.
	 * @param agent l'agent à supprimer.
	 * @return true si l'agent a bien été supprimé (si il existait avant la suppression), false sinon.
	 */
	public boolean supprimerAgent(Agent agent) {
		this.relationsSucc.remove(agent);
		return this.agents.remove(agent);
	}
	
	/** 
	 * Supprime le monde en argument.
	 * @param monde le monde à supprimer du modèle.
	 * @return true si le monde a bien été supprimé (c-à-dire si il existait parmis les mondes).
	 */
	public boolean supprimerMonde(T monde) {
		if(this.mondePointe == monde)
			this.mondePointe = null;
		return this.mondes.remove(monde);
	}
	
	/**
	 * Supprime la relation décrite par les arguments.
	 * @param agent l'agent concerné par la relation.
	 * @param mondeA le monde de départ.
	 * @param mondeB le monde d'arrivée.
	 * @return true si la relation (agent, mondeA, mondeB) a bien été supprimée (si elle existait).
	 */
	public boolean supprimerRelation(Agent agent, T mondeA, T mondeB) {
		HashMap<T, LinkedList<T>> mapAgent = this.relationsSucc.get(agent);
		// si pas de clé pour l'agent, pas besoin d'aller plus loin : false.
		if(mapAgent == null)
			return false;
		
		// si pas de clé pour le mondeA, pas besoin d'aller plus loin : false.
		LinkedList<T> liste = mapAgent.get(mondeA);
		if(liste == null)
			return false;
		
		// on supprime l'objet si il existe
		boolean supprime = liste.remove(mondeB);
		if(supprime && liste.isEmpty())
			mapAgent.remove(mondeA);
		
		return supprime; // retourne true si la relation a bien été supprimée.
	}
	
	/**
	 * Supprime une double relation (symétrique) définie par l'agent étiqueté et les deux mondes en argument.
	 * Elle appelle {@link #supprimerRelation(Agent, Monde, Monde)} pour (monde1 vers monde2) et (monde2 vers monde1).
	 * @param agent l'agent étiqueté par la relation.
	 * @param monde1 le premier monde.
	 * @param monde2 le deuxième monde.
	 * @return true si au moins un relation dans un sens a bien été supprimée (si elle existait), false sinon.
	 */
	public boolean supprimerRelationSym(Agent agent, T monde1, T monde2) {
		boolean b1 = this.supprimerRelation(agent, monde1, monde2);
		boolean b2 = this.supprimerRelation(agent, monde2, monde1);
		return b1 || b2;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final String NL = System.lineSeparator();
		String 
		chaine = "------------ Modèle Kripke ------------"+NL;
		chaine += "  - Mondes ("+this.mondes.size()+") :"+NL;
		for(T monde : this.mondes) {
			if(monde instanceof MondeEpistemique && this.getMondePointe() == monde)
				chaine += "    * "+monde.toString() +" (pointé)"+NL;
			else
				chaine += "    * "+monde.toString() + NL;
		}
		chaine += "  - Relations ("+this.nbRelations()+") :"+NL;
		
		for(Entry<Agent, HashMap<T, LinkedList<T>>> mapAgent : this.relationsSucc.entrySet()) {
			Agent a = mapAgent.getKey();
			for(Entry<T, LinkedList<T>> liste : mapAgent.getValue().entrySet()) {
				Monde m = liste.getKey();
				for(T mo : liste.getValue()) {
					chaine += "    * [agent "+a+"] "+m+" --> "+mo + NL;
				}
			}
		}
		return chaine+"---------------------------------------";
	}
}
