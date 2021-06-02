package epistemique.exemples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import epistemique.formule.*;
import epistemique.modeles.*;

/**
 * Problème du théatre et des deux nombres.
 * Plus long : une proposition par couple de nombres.
 * @author Ludovic JEAN-BAPTISTE
 *
 */
public class SumAndProduct {
	
	/** 
	 * Programme principal.
	 * @param args les paramètres à passer au programme (inutile ici).
	 */
	public static void main(String[] args) {
		System.out.println("THEATRE");
		
		// Définition des agents du problème.
		Agent aP = new Agent("P");
		Agent aS = new Agent("S");
		HashSet<Agent> agents = new HashSet<>();
		agents.add(aP);
		agents.add(aS);
		
		ArrayList<Proposition> listeProp = new ArrayList<>();
		HashMap<Proposition, Integer> produits = new HashMap<Proposition, Integer>();
		HashMap<Proposition, Integer> sommes = new HashMap<Proposition, Integer>();
		HashMap<Proposition, MondeEpistemique> propMonde = new HashMap<Proposition, MondeEpistemique>();
		for(int x=2; x <= 100; x++) {
			for(int y=2; y <= 100; y++) {
				if(x >= y)
					continue;
				if(x+y <= 100) {
					Proposition prop = new Proposition(x+"_"+y);
					listeProp.add(prop);
					produits.put(prop, x*y);
					sommes.put(prop, x+y);
				}
			}
		}
		
		// Création du modèle de Kripke.
		Modele<MondeEpistemique> modele = new Modele<MondeEpistemique>(agents, listeProp);
		
		//on ajoute les agents au modèle
		modele.ajouterAgent(aP);
		modele.ajouterAgent(aS);
		
		// Ajout des mondes possibles dans le modèle de Kripke.
		for(Proposition p : listeProp)
			propMonde.put(p, modele.ajouterMonde(false, new MondeEpistemique(modele, p)));
		System.out.println("fin mondes : "+modele.getMondes().size());
		
		// Ajout des relations du modèle.
		System.out.println(produits.size());
		for(Entry<Proposition, Integer> e1 : produits.entrySet()) {
			Proposition p1 = e1.getKey();
		    Integer i1 = e1.getValue();
			for(Entry<Proposition, Integer> e2 : produits.entrySet()) {
				Proposition p2 = e2.getKey();
			    Integer i2 = e2.getValue();
			    
			    if(i1.intValue() == i2.intValue())
			    	modele.ajouterRelation(aP, propMonde.get(p1), propMonde.get(p2));
			}
		}
		int nbRelProduit = modele.nbRelations();
		System.out.println("fin relations produit : "+nbRelProduit);
		
		for(Entry<Proposition, Integer> e1 : sommes.entrySet()) {
			Proposition p1 = e1.getKey();
		    Integer i1 = e1.getValue();
			for(Entry<Proposition, Integer> e2 : sommes.entrySet()) {
				Proposition p2 = e2.getKey();
			    Integer i2 = e2.getValue();
			    
			    if(i1 == i2)
			    	modele.ajouterRelation(aS, propMonde.get(p1), propMonde.get(p2));
			}
		}
		System.out.println("fin relations somme : "+(modele.nbRelations()-nbRelProduit));
		
		System.out.println("FIN DE CREATION DU MODELE INITIAL");
		
		
		System.out.println("1) formule : S sait que P ne sait pas les deux nombres.");
		Formule f = new Vrai();
		for(Proposition p : modele.getPropositions())
			f = new Et(new Non(new SaitQue(aP, p)), f);
		f = new SaitQue(aS, f);
		modele = Evenement.annoncePublique(modele, f);
		System.out.println(modele);
		
		System.out.println("2) formule : P connait les deux nombres");
		Formule g = new Faux();
		for(Proposition p : modele.getPropositions())
			g = new Ou(g, new SaitQue(aP, p));
		modele = Evenement.annoncePublique(modele, g);
		System.out.println(modele);
		
		System.out.println("3) formule : S connait les deux nombres aussi");
		Formule h = new Faux();
		for(Proposition p : modele.getPropositions())
			h = new Ou(h, new SaitQue(aS, p));
		modele = Evenement.annoncePublique(modele, h);
		System.out.println(modele);
	}
}
