package fr.utc.miage.wallet;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class UtilisateurTest {
  private static final String NAME = "Doe";
  private static final String FIRST_NAME = "John";
  private static final int CORRECT_QUANTITY = 3;
  private static final Date BIRTHDAY = Date.valueOf("2000-01-01");

  public Utilisateur getCorrectUtilisateur() {
    return new Utilisateur(FIRST_NAME, NAME, BIRTHDAY);
  }

  @Test
  void testGetBirthday() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Date result = utilisateur.getBirthday();
    assertEquals(BIRTHDAY, result);
  }

  @Test
  void testGetFirstName() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    String result = utilisateur.getFirstName();
    assertEquals(FIRST_NAME, result);
  }

  @Test
  void testGetName() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    String result = utilisateur.getName();
    assertEquals(NAME, result);
  }

  @Test
  void testGetWallet() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Wallet result = utilisateur.getWallet();
    assertEquals(new Wallet(), result);
  }

  @Test
  void testSetWallet() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Wallet newWallet = new Wallet();
    utilisateur.setWallet(newWallet);
    assertEquals(newWallet, utilisateur.getWallet());
  }

  @Test
  void testBuyActionNullShouldNotWork() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    assertThrows(IllegalArgumentException.class, () -> {
      utilisateur.buyAction(null, CORRECT_QUANTITY);
    });
  }

  @Test
  void testBuyActionQuantityNullShouldNotWork() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Action action = ActionTest.getCorrectAction();
    assertThrows(IllegalArgumentException.class, () -> {
      utilisateur.buyAction(action, null);
    });
  }

  @Test
  void testBuyActionShouldNWork() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Action action = ActionTest.getCorrectAction();
    assertDoesNotThrow(() -> {
      utilisateur.buyAction(action, CORRECT_QUANTITY);
    });
  }

}
