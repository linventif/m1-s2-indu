package fr.utc.miage.wallet;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class DemoTest {

  @Test
  void demoScenarioShouldIllustrateBuyAndSellWorkflow() {
    Action actionOvh = new Action("OVH Demo", 10.0, ActionCategory.INDUSTRIAL);
    Action actionGoogle = new Action("Google Demo", 20.0, ActionCategory.OTHER);

    Map<String, Float> composition = new HashMap<>();
    composition.put(actionOvh.getLabel(), 0.4f);
    composition.put(actionGoogle.getLabel(), 0.6f);
    Action actionTechFund = new Action("Tech Fund Demo", 17.0, composition);

    Utilisateur utilisateur = new Utilisateur("John", "Doe", Date.valueOf("2000-01-01"));

    utilisateur.buyAction(actionOvh, 5);
    utilisateur.buyAction(actionGoogle, 2);
    utilisateur.buyAction(actionTechFund, 1);

    assertEquals(93.0, utilisateur.getCashAmount(), 0.001);
    assertEquals(5, utilisateur.getWallet().getActions().get(actionOvh));
    assertEquals(2, utilisateur.getWallet().getActions().get(actionGoogle));
    assertEquals(1, utilisateur.getWallet().getActions().get(actionTechFund));

    utilisateur.sellAction(actionGoogle, 1);

    assertEquals(113.0, utilisateur.getCashAmount(), 0.001);
    assertEquals(1, utilisateur.getWallet().getActions().get(actionGoogle));
    assertEquals(87.0, utilisateur.getWallet().getTotalValue(), 0.001);
    assertTrue(Action.getActionsByCategory(ActionCategory.INDUSTRIAL).containsKey(actionOvh.getLabel()));
  }
}
