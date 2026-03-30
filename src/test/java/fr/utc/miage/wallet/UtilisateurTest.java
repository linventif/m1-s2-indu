package fr.utc.miage.wallet;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class UtilisateurTest {
  private final String NAME = "Doe";
  private final String FIRST_NAME = "John";
  private final Date BIRTHDAY = Date.valueOf("2000-01-01");
  private final Wallet WALLET = new Wallet();
  private final List<Action> ACTION_LIST = new ArrayList<>();

  public Utilisateur getCorrectUtilisateur(){
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
    assertEquals(WALLET, result);
  }

  @Test
  void testSetWallet() {
    Utilisateur utilisateur = getCorrectUtilisateur();
    Wallet newWallet = new Wallet();
    utilisateur.setWallet(newWallet);
    assertEquals(newWallet, utilisateur.getWallet());
  }

}
