package epistemique.exemples;
import java.util.HashMap;

import epistemique.formule.Formule;
import epistemique.formule.Non;
import epistemique.formule.Proposition;
import epistemique.formule.SaitQue;
import epistemique.modeles.Agent;
import epistemique.modeles.Evenement;
import epistemique.modeles.Modele;
import epistemique.modeles.MondeEpistemique;
import epistemique.modeles.MondeEvenement;

public class PNotP {

	public static void main(String[] args) {
		// Définition des entités du problème
		Agent a = new Agent("a");
		Agent b = new Agent("b");
		Proposition p = new Proposition("p");
		
		// création du modèle épistémique
		Modele<MondeEpistemique> modeleEp = new Modele<>();
		modeleEp.ajouterAgent(a);
		modeleEp.ajouterAgent(b);
		modeleEp.ajouterProposition(p);
		
		MondeEpistemique w = new MondeEpistemique(modeleEp, p);
		modeleEp.ajouterMonde(false, w);
		
		MondeEpistemique u = new MondeEpistemique(modeleEp);
		modeleEp.ajouterMonde(false, u);
		
		modeleEp.ajouterRelations(a, w, u);
		modeleEp.ajouterRelations(b, w, u);
		
		// Création du modèle d'évènement
		Modele<MondeEvenement> modeleEv = new Modele<>();
		modeleEv.ajouterProposition(p);
		
		HashMap<Proposition, Boolean> postCondition = new HashMap<>();
		postCondition.put(p, false);
		MondeEvenement e = new MondeEvenement(p, postCondition);
		modeleEv.ajouterMonde(false, e);
		
		MondeEvenement f = new MondeEvenement();
		modeleEv.ajouterMonde(false, f);
		
		modeleEv.ajouterRelation(a, e, e);
		modeleEv.ajouterRelation(a, f, f);
		modeleEv.ajouterRelation(b, e, f);
		modeleEv.ajouterRelation(b, f, f);
		
		//Affichage des modèles
		System.out.println(modeleEp);
		System.out.println(modeleEv);
		
		//Affichage du produit de mise à jour entre les deux modèles
		Modele<MondeEpistemique> modeleProduit = Evenement.produitMAJ(modeleEp, modeleEv);
		System.out.println(modeleProduit);
		
		Formule phi = new SaitQue(b, new Non(new SaitQue(a, p)));
		Modele<MondeEpistemique> nouveauModele = Evenement.annoncePublique(modeleProduit, phi);
		System.out.println(nouveauModele);
	}
}
