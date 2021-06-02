/**
 * 
 */
package epistemique.modeles;

/**
 * Exception qui caractérise l'absence d'un monde pointé dans un modèle donné.
 * @author Ludovic JEAN-BAPTISTE
 *
 */
public class ModeleNonPointeException extends Exception {

	/**
	 * Version de la classe (requis car Exception serializable)
	 */
	private static final long serialVersionUID = 3547777003560409883L;

	/**
	 * Constructeur par défaut.
	 */
	public ModeleNonPointeException() {
	}

	/**
	 * {@inheritDoc}
	 */
	public ModeleNonPointeException(String message) {
		super(message);
	}

	/**
	 * {@inheritDoc}
	 */
	public ModeleNonPointeException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
