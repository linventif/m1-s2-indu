package fr.utc.miage.wallet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UtilisateurTest {

  @Test
  void deleteActionRemovesExistingAction() {
    Utilisateur utilisateur = new Utilisateur();
    Action ovh = new Action("OVH", 10);
    Action google = new Action("Google", 15);

    utilisateur.addAction(ovh);
    utilisateur.addAction(google);

    boolean deleted = utilisateur.deleteAction(ovh);

    assertTrue(deleted);
    assertEquals(1, utilisateur.getActions().size());
    assertFalse(utilisateur.getActions().contains(ovh));
    assertTrue(utilisateur.getActions().contains(google));
  }

  @Test
  void deleteActionReturnsFalseWhenActionDoesNotExist() {
    Utilisateur utilisateur = new Utilisateur();
    Action ovh = new Action("OVH", 10);

    boolean deleted = utilisateur.deleteAction(ovh);

    assertFalse(deleted);
    assertTrue(utilisateur.getActions().isEmpty());
  }
}
