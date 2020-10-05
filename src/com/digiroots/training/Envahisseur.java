
package com.digiroots.training;


public class Envahisseur {

/*    public static String traductionAlien (String message){
         String[][] map={{"0000","A"},
                        {"0001","B"},
                        {"0010","I"},
                        {"0011","J"},
                        {"0100","O"},
                        {"0110","R"},
                        {"0101","M"},
                        {"0111","S"},
                        {"1000","N"},
                        {"1001","V"},
                        {"1111"," "}
                 //EXTENDED
                        ,   {"1011","L"}
                        ,{"1100","F"},
                        {"1101","G"}
                    };
        String res = "";
        for (int i=0; i<message.length(); i++){
            String lettre = ""+message.charAt(i);
            String lettreDecode = "?";
            for (int j=0; j<map.length; j++){
                if(map[j][1].equals(lettre)){
                    lettreDecode = map[j][0];
                }
            }
            res += lettreDecode;
        }
        return res;
    }
*/
    public static String traduction (String message){
         String[][] map={{"0000","A"},
                        {"0001","B"},
                        {"0010","I"},
                        {"0011","J"},
                        {"0100","O"},
                        {"0110","R"},
                        {"0101","M"},
                        {"0111","S"},
                        {"1000","N"},
                        {"1001","V"},
                        {"1111"," "}
                 //EXTENDED
                        ,{"1011","L"},
                        {"1100","F"},
                        {"1101","G"}
                    };
        String res = "";
        for (int i=0; i<message.length(); i+=4){
            String lettre = ""+message.charAt(i)+message.charAt(i+1)+message.charAt(i+2)+message.charAt(i+3);
            System.out.println(lettre);
            String lettreDecode = "?";
            for (int j=0; j<map.length; j++){//Better to move to a separate method
                if(map[j][0].equals(lettre)){
                    lettreDecode = map[j][1];
                }
            }
            res += lettreDecode;
            System.out.println(res);

        }
        
        return res;        
    }
    
    public static double variance(double[] obs){
        //calcul de la moyenne:
        if (obs.length==0)
            return -1;
        
        int moy = 0;
        for (int i=0; i<obs.length; i++){
            moy += obs[i];
        }
        moy = moy /obs.length;
        int var = 0;
        for (int i=0; i<obs.length; i++){
            var += Math.pow(obs[i]-moy,2);
        }
        return var/obs.length;
    }
            
    public static void main(String[] args){
       String msg = "0001001000110100011011110101001001110010010011111001000010000111000010001111";
       System.out.println(traduction(msg));
       msg = "01010010011001110010111101010100100000000101001011110000101100001111010011110000000100000110​";
       System.out.println(traduction(msg));
       //tests
       /*System.out.println(traduction(traductionAlien("BIJOR MISIO VANSAN")));
       System.out.println(traduction(traductionAlien("BIJOR MISIO VANSAN")));                
       */
       
       String str = "Hello, world";
       //System.out.println(str[0]); //Exception
       
       char[] strAsChar = str.toCharArray();
       System.out.println(strAsChar[0]); //OK
    }
}
