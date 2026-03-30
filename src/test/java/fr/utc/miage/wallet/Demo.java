package fr.utc.miage.wallet;

public class Demo {
  public static void main(String[] args) {

    final String NAME = "OVH";
    final String OTHER_NAME = "OVH";
    final Double PRICE = 100.0;
    final Double OTHER_PRICE = 100.0;

    // Action
    Action actOVH = new Action(NAME, PRICE);
    Action actGoogle = new Action(OTHER_NAME, OTHER_PRICE);
    System.out.println(actOVH);
    System.out.println(actGoogle);
  }
}
