package bgu.spl.mics;
import java.util.Scanner;

public class StrToInt {

    public static int stoi(String str){

         if(str == null || str.equals(""))
             return 0 ;
         int i = 0;
         while (str.charAt(i) == ' ')
             i++ ;

         boolean isPositive = true ;

         if(!check(str.charAt(i)))
         {
             //Check whether the first character at the beginning of the integer is-or +, if the result is false, the input is illegal
            if(str.charAt(i) != '-' && str.charAt(i) != '+')
                 return 0 ;
             if(str.charAt(i) == '-')
                 isPositive = false ;
             i++ ;
         }

         int theNumber = 0 ;

         for( ; i<str.length() ; i++)
         {
             char ch = str.charAt(i) ;
             int x = ch-48 ;
             if(!check(ch))  //Check legitimacy
                 return 0 ;
             boolean overflow = false ;
             //Compare the current number with max/10, if it is greater than it means overflow, if it is equal, then compare the remainder of x and max%10, if it is greater, it means overflow
             if(isPositive && (theNumber > Integer.MAX_VALUE/10 || (theNumber == Integer.MAX_VALUE/10 && x >= Integer.MAX_VALUE%10)))
                 overflow = true ;
             //Since Java does not have an unsigned type, it can only convert the current number to a negative number for equivalent comparison
             else if(-theNumber < (Integer.MIN_VALUE)/10 || (-theNumber == Integer.MIN_VALUE/10 && -x <= Integer.MIN_VALUE%10))
                 overflow = true ;
             if(overflow)
                 return isPositive ? Integer.MAX_VALUE : Integer.MIN_VALUE ;
             else
                 theNumber = theNumber*10 + x ;
         }

         return isPositive ? theNumber : -theNumber ;
     }

     /**
      * Check whether it is a legal character, legal characters only include 0-9
      */
    private static boolean check(char ch)
     {
         if(ch>=48 && ch <=57)
             return true ;
         return false ;
     }

     public static void main(String[] args) {
         Scanner sc = new Scanner(System.in) ;

         while (true)
         {
             String line = sc.nextLine() ;
             int x = stoi(line) ;

             System.out.println(x);
         }

     }

}
