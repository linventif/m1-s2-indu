package fr.utc.miage.wallet;

/**
 * Représente les catégories principales auxquelles une action peut appartenir.
 * Ces catégories permettent notamment de filtrer ou d'organiser les actions
 * enregistrées dans l'application.
 */
public enum ActionCategory {
  /** Action liée au secteur alimentaire. */
  FOOD,
  /** Action liée au transport. */
  TRANSPORT,
  /** Action liée au divertissement. */
  ENTERTAINMENT,
  /** Action liée à l'industrie. */
  INDUSTRIAL,
  /** Catégorie par défaut lorsqu'aucun classement précis n'est fourni. */
  OTHER
}
