package epistemique.exemples;

import epistemique.formule.Formule;
import epistemique.formule.Non;
import epistemique.formule.Proposition;
import epistemique.formule.SaitQue;
import epistemique.modeles.Agent;
import epistemique.modeles.Evenement;
import epistemique.modeles.Modele;
import epistemique.modeles.MondeEpistemique;

/**
 * Problème du pile ou face.
 * @author Ludovic JEAN-BAPTISTE
 *
 */
public class PileOuFace {

	/** 
	 * Programme principal.
	 * @param args les paramètres à passer au programme (inutile ici).
	 */
	public static void main(String[] args) {
		System.out.println("PILE OU FACE");
		
		// Définition des agents du problème.
		Agent a1 = new Agent("1");
		Agent a2 = new Agent("2");
		
		// Définition des propositions du problème. 
		Proposition p = new Proposition("p"); // la pièce est sur pile
		
		// Création du modèle de Kripke.
		Modele<MondeEpistemique> modele = new Modele<MondeEpistemique>();
		modele.ajouterProposition(p);
		
		//on ajoute les agents au modèle
		modele.ajouterAgent(a1);
		modele.ajouterAgent(a2);
		
		// Ajout des mondes possibles dans le modèle de Kripke.
		MondeEpistemique m1 = modele.ajouterMonde(true, new MondeEpistemique(modele, p));
		MondeEpistemique m2 = modele.ajouterMonde(false, new MondeEpistemique(modele));
		
		// Ajout des relations du modèle.
		modele.ajouterRelation(a1, m1, m1);
		modele.ajouterRelation(a1, m2, m2);
		modele.ajouterRelation(a1, m1, m2);
		modele.ajouterRelation(a1, m2, m1);

		modele.ajouterRelation(a2, m1, m1);
		modele.ajouterRelation(a2, m2, m2);
		modele.ajouterRelation(a2, m1, m2);
		modele.ajouterRelation(a2, m2, m1);
		System.out.println(modele);

		//K1{p}
		Formule f1 = new SaitQue(a1, p);
		System.out.println(f1+" --> "+f1.satisfaite(modele, m1));
		
		//K1{¬K2{p}}
		Formule f2 = new SaitQue(a1, new Non(new SaitQue(a2, p)));
		System.out.println(f2+" --> "+f2.satisfaite(modele, m1));
		
		
		
		Modele<MondeEpistemique> nouveauModele = Evenement.annoncePrivee(modele, a1, p);
		System.out.println(nouveauModele);
		System.out.println(f1+" --> "+f1.satisfaite(nouveauModele, nouveauModele.getMondePointe()));
		
		
		
	}

}
