package epistemique.formule;

import epistemique.modeles.Modele;
import epistemique.modeles.ModeleNonPointeException;
import epistemique.modeles.MondeEpistemique;

/**
 * Classe abstraite représentant une formule quelconque de la logique propositionnelle
 * mais également pour la logique épistémique.
 * @author ludovicjeanbaptiste
 * 
 */
public abstract class Formule {
	
	/**
	 * Méthode qui retourne true si la formule utilise des modaux de la logique épistémique.
	 * @return true si épistémique, false sinon.
	 */
	public abstract boolean estEpistemique();
	
	/**
	 * Méthode qui retourne true si la formule est un littéral (proposition ou négation de proposition).
	 * @return true si c'est un littéral, false sinon.
	 */
	public abstract boolean estLitteral();
	
	/**
	 * Méthode qui détermine si la formule est satisfaite par le modèle pointé donné.
	 * @param modelePointe le modèle de Kripke pointé.
	 * @return true si le modèle pointé donné satisfait la formule, false sinon.
	 * @throws ModeleNonPointeException si le modèle donné n'est pas pointé.
	 */
	public boolean satisfaite(Modele<MondeEpistemique> modelePointe) throws ModeleNonPointeException {
		if(modelePointe.getMondePointe() == null)
			throw new ModeleNonPointeException();
		
		return this.satisfaite(modelePointe, modelePointe.getMondePointe());
	}
	
	/**
	 * Méthode qui détermine si la formule est satisfaite par le modèle donné avec un monde de référence donné.
	 * @param modele le modèle de Kripke.
	 * @param mondeRef le monde de référence.
	 * @return true si le modèle et le monde donné satisfait la formule, false sinon.
	 */
	public abstract boolean satisfaite(Modele<MondeEpistemique> modele, MondeEpistemique mondeRef);
	
	/**
	 * Méthode qui détermine si la formule est valide pour un modèle donné.
	 * Autrement dit, détermine si elle est satisfaite pour l'ensemble des mondes de ce modèle.
	 * @param modele le modèle de Kripke.
	 * @return true si la formule est valide dans ce modèle, false sinon.
	 */
	public boolean valide(Modele<MondeEpistemique> modele) {
		for(MondeEpistemique m : modele.getMondes()) {
			if(!this.satisfaite(modele, m))
				return false;
		}
		return true;
	}
}
