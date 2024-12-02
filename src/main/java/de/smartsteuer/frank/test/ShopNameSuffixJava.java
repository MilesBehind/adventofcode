package de.smartsteuer.frank.test;

public class ShopNameSuffixJava {
  public static String getNameSuffix(String nameMann, String nameFrau) {
    String res = "(MANNPLUSFRAU)";
    res = res.replace("MANN",nameMann);
    res = res.replace("FRAU",nameFrau);
    if (empty(nameMann) || empty(nameFrau)) {
      // there is only one name
      res = res.replace("PLUS","");
    } else {
      // we have two names
      res = res.replace("PLUS"," + ");
    }
    return res;
  }

  private static boolean empty(String s) {
    return (s == null) || (s.trim().isEmpty());
  }
}
