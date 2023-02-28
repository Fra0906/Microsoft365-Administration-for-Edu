package utility;

import java.nio.charset.StandardCharsets;

public class RandomString {
  
    // function to generate a random string of length n

    public static String getAlphaNumericString(String model)
    {

        if(model==null) return "";
        int n = model.length();
        // chose a Character random from this String
        String maiuscole="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String minuscole="abcdefghijklmnopqrstuvxyz";
        String numeri = "0123456789";
  
        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);
  
        for (int i = 0; i < n; i++) {
            String AlphaNumericString ="";
            if(maiuscole.contains(Character.toString(model.charAt(i)))) {
                AlphaNumericString = maiuscole;
            }
            if(minuscole.contains(Character.toString(model.charAt(i)))) {
                AlphaNumericString = minuscole;
            }
            if(numeri.contains(Character.toString(model.charAt(i)))) {
                AlphaNumericString = numeri;
            }

  
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                = (int)(AlphaNumericString.length()
                        * Math.random());
  
            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                          .charAt(index));
        }
  
        return sb.toString();
    }

    public static void main(String[] args){

        String model = "Mmm00000";

        String r = getAlphaNumericString(model);
        System.out.println(r);

    }

}