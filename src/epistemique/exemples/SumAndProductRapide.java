package epistemique.exemples;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import epistemique.formule.*;
import epistemique.modeles.*;

/**
 * Problème "The Sum and product Riddle".
 * plus rapide : une proposition par nombre.
 * @author Ludovic JEAN-BAPTISTE
 *
 */
public class SumAndProductRapide {
	
	/** 
	 * Programme principal.
	 * @param args les paramètres à passer au programme (inutile ici).
	 */
	public static void main(String[] args) {
		System.out.println("THEATRE BIS");
		
		// Définition des agents du problème.
		Agent aP = new Agent("P");
		Agent aS = new Agent("S");
		HashSet<Agent> agents = new HashSet<>();
		agents.add(aP);
		agents.add(aS);
		
		// création des propositions
		HashMap<Proposition, Integer> propInt = new HashMap<Proposition, Integer>();
		for(int x=2; x <= 100; x++) {
			Proposition prop = new Proposition(x+"");
			propInt.put(prop, x);
		}
		
		// Création du modèle de Kripke.
		Modele<MondeEpistemique> modele = new Modele<MondeEpistemique>(agents, propInt.keySet());
		HashMap<Couple<Integer, Integer>, MondeEpistemique> coupleMondes = new HashMap<Couple<Integer, Integer>, MondeEpistemique>();
		
		// Ajout des mondes possibles dans le modèle de Kripke.
		for(Proposition p1 : propInt.keySet()) {
			for(Proposition p2 : propInt.keySet()) {
				if(propInt.get(p1).intValue() >= propInt.get(p2).intValue())
					continue;
				
				if(propInt.get(p1).intValue() + propInt.get(p2).intValue() > 100)
					continue;
				
				MondeEpistemique m = new MondeEpistemique(modele, p1, p2);
				coupleMondes.put(new Couple<>(propInt.get(p1), propInt.get(p2)), m);
				modele.ajouterMonde(false, m);
			}
		}
		System.out.println("nombre de mondes : "+modele.getMondes().size());
		
		
		
		// on créé des lots pour les relations sommes et produit
		// un lot somme (resp. produit) = un ensemble de monde que confond l'agent somme (resp. produit).
		HashMap<Integer, ArrayList<MondeEpistemique>> lotSommes = new HashMap<>();
		HashMap<Integer, ArrayList<MondeEpistemique>> lotProduits = new HashMap<>();

		for(Couple<Integer, Integer> couple : coupleMondes.keySet()) {
			//pour les produits
			int produit = couple.getObj1().intValue() * couple.getObj2().intValue();
			ArrayList<MondeEpistemique> listeProduit = lotProduits.get(produit);
			if(listeProduit == null) {
				listeProduit = new ArrayList<>();
				lotProduits.put(produit, listeProduit);
			}
			listeProduit.add(coupleMondes.get(couple));
			
			// pour les sommes
			int somme = couple.getObj1().intValue() + couple.getObj2().intValue();
			ArrayList<MondeEpistemique> listeSomme = lotSommes.get(somme);
			if(listeSomme == null) {
				listeSomme = new ArrayList<>();
				lotSommes.put(somme, listeSomme);
			}
			listeSomme.add(coupleMondes.get(couple));
		}
		
		// on ajoute vraiment les relations à partir des lots pour l'agent produit
		for(ArrayList<MondeEpistemique> mondes : lotProduits.values())
			modele.ajouterRelations(aP, mondes);

		int nbRelProduit = modele.nbRelations();
		
		// idem pour les lots produits
		for(ArrayList<MondeEpistemique> mondes : lotSommes.values())
			modele.ajouterRelations(aS, mondes);
		
		System.out.println("relations produit : "+nbRelProduit);
		System.out.println("relations somme : "+(modele.nbRelations()-nbRelProduit));

		System.out.println("FIN DE CREATION DU MODELE - DEBUT DES ANNONCES");
		// On envoie les annonces !
		System.out.println("1) formule : S sait que P ne sait pas les deux nombres.");
		Formule f = new Vrai();
		for(Proposition p : modele.getPropositions())
			f = new Et(new Non(new SaitQue(aP, p)), f);
		f = new SaitQue(aS, f);
		modele = Evenement.annoncePublique(modele, f);
		System.out.println(modele); // affichage du modèle mis à jour
		
		System.out.println("2) formule : P connait les deux nombres");
		Formule g = new Faux();
		for(Proposition p : modele.getPropositions())
			g = new Ou(g, new SaitQue(aP, p));
		modele = Evenement.annoncePublique(modele, g);
		System.out.println(modele); // affichage du modèle mis à jour
		
		System.out.println("3) formule : S connait les deux nombres aussi");
		Formule h = new Faux();
		for(Proposition p : modele.getPropositions())
			h = new Ou(h, new SaitQue(aS, p));
		modele = Evenement.annoncePublique(modele, h);
		System.out.println(modele); // affichage du modèle mis à jour
	}
}
