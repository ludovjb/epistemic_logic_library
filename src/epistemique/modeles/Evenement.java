package epistemique.modeles;

import java.util.HashMap;
import java.util.Map.Entry;

import epistemique.formule.Formule;
import epistemique.formule.Vrai;

/**
 * Classe métant à disposition des méthodes statiques pour effectuer des annonces sur un modèle épistémique.
 * @author Ludovic JEAN-BAPTISTE
 *
 */
public class Evenement {

	/**
	 * Annonce privée pour un agent particulier sur un modèle épistémique.
	 * @param modeleInitial le modèle épistémique initial.
	 * @param agent l'agent à qui l'annonce est faîte.
	 * @param formule la formule décrivant l'évènement à annoncer.
	 * @return le modèle épistémique post-annonce.
	 */
	public static Modele<MondeEpistemique> annoncePrivee(Modele<MondeEpistemique> modeleInitial, Agent agent, Formule formule) {
		Modele<MondeEvenement> modeleEv = new Modele<MondeEvenement>();
		MondeEvenement mondePrive = modeleEv.ajouterMonde(true, new MondeEvenement(formule));
		MondeEvenement mondePublic = modeleEv.ajouterMonde(false, new MondeEvenement(new Vrai()));
		
		modeleEv.ajouterRelation(agent, mondePrive, mondePrive);
		for(Agent agent2 : modeleInitial.getAgents()) {	
			if(agent2 != agent)
				modeleEv.ajouterRelation(agent2, mondePrive, mondePublic);
			modeleEv.ajouterRelation(agent2, mondePublic, mondePublic); //on ajoute la relation réflexive pour chaque agent.
		}
		return produitMAJ(modeleInitial, modeleEv);
		//return produitMAJNaif(modeleInitial, modeleEv);
	}

	/**
	 * Annonce publique sur un modèle épistémique.
	 * @param modeleInitial le modèle épistémique initial.
	 * @param formule la formule décrivant l'évènement à annoncer.
	 * @return le nouveau modèle épistémique post-annonce.
	 */
	public static Modele<MondeEpistemique> annoncePublique(Modele<MondeEpistemique> modeleInitial, Formule formule) {
		//on créé le modèle simple qui correspond à l'annonce publique
		//=> un seul monde avec les propositions
		//=> et une relation réflexive sur ce monde pour chaque agent du modèle initial
		
		Modele<MondeEvenement> modeleEv = new Modele<MondeEvenement>();
		MondeEvenement monde = modeleEv.ajouterMonde(true, new MondeEvenement(formule)); //le seul monde du modèle de l'annonce publique.

		for(Agent agent : modeleInitial.getAgents())	
			modeleEv.ajouterRelation(agent, monde, monde); //on ajoute la relation réflexive pour chaque agent.
		
		//on envoie le modèle d'évenement à la méthode produitMAJ(..) qui va se charger de faire le produit de mise à jour.
		return produitMAJ(modeleInitial, modeleEv);
	}
	
	/**
	 * Opère un évenement complexe sur un modèle épistémique initial.
	 * @param modeleInitial le modèle épistémique initial.
	 * @param modeleEvenement le modèle de l'évènement complexe.
	 * @return le nouveau modèle épistémique post-évènement, autrement dit le produit de mise à jour.
	 */
	public static Modele<MondeEpistemique> produitMAJ(Modele<MondeEpistemique> modeleInitial, Modele<MondeEvenement> modeleEvenement) {
		Modele<MondeEpistemique> postModele = new Modele<MondeEpistemique>(modeleInitial.getAgents(), modeleInitial.getPropositions());
		
		HashMap<Couple<MondeEpistemique, MondeEvenement>, MondeEpistemique> coupleMondes = new HashMap<>();
		
		//construction des mondes résultants (couples mI-mE)
		for(MondeEpistemique mI : modeleInitial.getMondes()) {
			for(MondeEvenement mE : modeleEvenement.getMondes()) {
				
				//si le monde résultant est cohérent (pas de propositions contraires) alors on l'ajoute au post-modèle
				//c'est-à-dire si la pre-condition est satisfaite pour ce monde épistemique.
				if(mE.getPreCondition().satisfaite(modeleInitial, mI)) {
					
					//on construit le monde résultant du couple mI-mE
					MondeEpistemique mRes = new MondeEpistemique(postModele, mI.getValuations());
					
					//on met à jour le monde résultant avec la post-condition (si elle existe)
					mRes.mettreAJour(mE.getPostCondition());
					
					//on ajoute le monde au post-modèle
					//si le monde résulte des monde pointé du modèle initial et évènement
					//alors il sera pointé aussi dans le post-modèle
					boolean estReel = modeleInitial.getMondePointe() == mI && modeleEvenement.getMondePointe() == mE;
					postModele.ajouterMonde(estReel, mRes);
					
					//on conserve le couple (mI-mE) : mRes dans un dictionnaire pour la suite (relations)
					Couple<MondeEpistemique, MondeEvenement> c = new Couple<>(mI, mE);
					coupleMondes.put(c, mRes);
				}
			}
		}
		
		// relations communes pour chaque agent
		for(Agent agent : modeleInitial.getAgents()) {
			
			// pour chaque couple
			for(Entry<Couple<MondeEpistemique, MondeEvenement>, MondeEpistemique> entree : coupleMondes.entrySet()) {
				MondeEpistemique mRes = entree.getValue();
				Couple<MondeEpistemique, MondeEvenement> cpl = entree.getKey();
				
				//on compare les successeurs du modèle initial
				for(MondeEpistemique mI : modeleInitial.getSuccesseurs(agent, cpl.getObj1())) {
					
					// et du modèle évènement
					for(MondeEvenement mE : modeleEvenement.getSuccesseurs(agent, cpl.getObj2())) {
						Couple<MondeEpistemique, MondeEvenement> cpl2 = new Couple<>(mI, mE);
						MondeEpistemique mRes2 = coupleMondes.get(cpl2);
						
						if(mRes2 != null) //si un monde résultant existe pour ce couple (si pre-condition OK)
							postModele.ajouterRelation(agent, mRes, mRes2); //ajout de la relation au post-modèle
					}
				}
			}
		}
		return postModele;
	}
	
}
