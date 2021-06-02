package epistemique.modeles;

/**
 * Classe qui permet de créer des simples couples (tuples).
 * @author Ludovic JEAN-BAPTISTE
 *
 */
public class Couple<T1, T2> {
	
	/**
	 * L'objet 1
	 */
	private T1 obj1;
	
	/**
	 * L'objet 2
	 */
	private T2 obj2;
	
	/**
	 * Constructeur.
	 * @param obj1 l'objet 1.
	 * @param obj2 l'objet 2.
	 */
	public Couple(T1 obj1, T2 obj2) {
		this.obj1 = obj1;
		this.obj2 = obj2;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		try {
			Couple<?, ?> cpl = (Couple<?, ?>) o;
			return obj1.equals(cpl.obj1) && obj2.equals(cpl.obj2);
		}
		catch(ClassCastException e) {
			return false;
		}
	}
	
	/**
	 * Accesseur.
	 * @return l'objet 1.
	 */
	public T1 getObj1() {
		return this.obj1;
	}
	
	/**
	 * Accesseur.
	 * @return l'objet 2.
	 */
	public T2 getObj2() {
		return this.obj2;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return (obj1.hashCode() * obj2.hashCode()) % Integer.MAX_VALUE; 
	}
}
