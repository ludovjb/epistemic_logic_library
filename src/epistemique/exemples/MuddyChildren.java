package epistemique.exemples;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import epistemique.formule.*;
import epistemique.modeles.*;

/**
 * Problème des Muddy Children.
 * @author Ludovic JEAN-BAPTISTE
 *
 */
public class MuddyChildren {
	
	/** 
	 * Programme principal.
	 * @param args les paramètres à passer au programme (inutile ici).
	 */
	public static void main(String[] args) {
		
		// vérification du nombre d'arguments
		if(args.length != 2) {
			System.out.println("USAGE: MuddyChildren <nb_enfants> <nb_enfants_sales>");
			return;
		}
		
		// initialisation des variables k et n selon les arguments
		int n, k;
		try {
			n = Integer.parseInt(args[0]);
			k = Integer.parseInt(args[1]);
		}
		catch(NumberFormatException e) {
			System.out.println("Un des arguments n'est pas un nombre.");
			return;
		}
		
		// k doit être forcément <= à n
		if(k <= 0 || n <= 0) {
			System.out.println("Les arguments doivent être supérieurs à 0.");
			return;
		}
		
		// k doit être forcément <= à n
		if(k > n) {
			System.out.println("<nb_enfants_sales> doit être inférieur ou égal à <nb_enfants>.");
			return;
		}
		
		System.out.println("MUDDY CHILDREN (n="+n+", k="+k+")");
		
		// Définition des agents du problème et de leur proposition respective.
		HashMap<Agent, Proposition> agentProp = new HashMap<>();

		for(int i=0; i < n; i++) {
			Proposition prop = new Proposition("sale"+(i+1));
			Agent agt = new Agent(i+1+"");
			agentProp.put(agt, prop);
		}

		// Création du modèle de Kripke.
		Modele<MondeEpistemique> modele = new Modele<MondeEpistemique>(agentProp.keySet(), agentProp.values());
		modele.ajouterMonde(true, new MondeEpistemique(modele)); //ajout du monde "vide"
		
		// Création des mondes (2^k mondes)
		for(Proposition p : agentProp.values()) {
			for(MondeEpistemique m : modele.getMondes()) {
				MondeEpistemique nouveau = new MondeEpistemique(modele, m.getValuations());
				nouveau.ajouterProposition(p, true);
				
				int nbSales = Collections.frequency(nouveau.getValuations().values(), true);
				modele.ajouterMonde(nbSales == k, nouveau);
			}
		}

		// Ajout des relations.
		HashSet<MondeEpistemique> mondes = modele.getMondes();
		// pour chaque agent
		for(Entry<Agent, Proposition> agtProp : agentProp.entrySet()) {
			
			//pour chaque monde du modèle
			for(MondeEpistemique m1 : mondes) {
				
				// création d'une formule f pour sélectionner les mondes que confond l'agent
				Formule f = new Vrai();
				for(Entry<Proposition, Boolean> propBool : m1.getValuations().entrySet()) {
					Proposition prop = propBool.getKey();
					if(agtProp.getValue() == prop)
						continue;
					
					f = new Et(f, propBool.getValue() ? prop : new Non(prop));
				}
				
				// pour chaque monde que confond l'agent (<=> f est satisfaite par ce monde)
				for(MondeEpistemique m2 : mondes) {
					if(f.satisfaite(modele, m2))
						
						// on ajout la relation entre les deux mondes
						modele.ajouterRelation(agtProp.getKey(), m1, m2);
				}
			}
		}
		
		System.out.println("FIN DE CREATION DU MODELE - DEBUT DES ANNONCES");
		// AFFICHAGE : modèle initial
		System.out.println(modele);
		
		// ANNONCE : au moins un des agents est sale
		Formule g = new Faux();
		for(Proposition p : agentProp.values())
			g = new Ou(g, p);
		modele = Evenement.annoncePublique(modele, g);
		System.out.println(modele);
		
		// compteur d'annonces
		// initialisé à 1 car on a déjà envoyé "au moins l'un des enfants est sale"
		int nbAnnonces = 1;
		long time = 0;
		try {
			// pour chaque étape
			while(modele.getMondes().size() > 1) {
				nbAnnonces++;
				
				// on créé la formule à annoncer selon les connaissances des agents
				// conjonction de (non) disjonction
				Formule i = new Vrai();
				for(Agent a : modele.getAgents()) {
					Formule h = new Ou(new SaitQue(a, agentProp.get(a)), new SaitQue(a, new Non(agentProp.get(a))));
					if(!h.satisfaite(modele))
						h = new Non(h);
	
					i = new Et(i, h);
				}
				
				// calcul du temps de réponse pour la première annonce
				if(nbAnnonces == 2) {
					long debut = System.currentTimeMillis();
					modele = Evenement.annoncePublique(modele, i);
					time = System.currentTimeMillis()-debut;
				}
				else
					modele = Evenement.annoncePublique(modele, i);
				
				System.out.println("Annonce de : "+i);
				System.out.println(modele);
			}
		}
		catch(ModeleNonPointeException e) {
			System.err.println("Le modèle évalué doit être pointé !");
		}
		
		System.out.println("Nombres d'annonces au total : "+nbAnnonces);
		System.out.println("Temps d'exécution de la première annonce : "+time+" ms");
	}
}
