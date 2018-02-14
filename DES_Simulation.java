import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

/*
  Main simulation engine.
  Do not modify class/method names used in supplied code.
  Usage: java DES_Simulation plaintext-filename key-filename
*/
class DES_Simulation{
   public static void main(String[] args) throws FileNotFoundException {
      if (args.length != 2) {
           System.out.println("Usage: java DES_Simulation plaintext-filename key-filename");
           return;
       }   
            
      String plaintextFileName = args[0];
      String keyFileName = args[1];
      
      //Open file to read plaintext and key
      Scanner input0 = new Scanner(new File(plaintextFileName));  
      Scanner input1 = new Scanner(new File(keyFileName)); 
        
      /*A key of 64-bits*/
      String key = input1.nextLine();
      
      String recoveredText = "";
      
   
      /* Plaintext, a block of 64 bits*/
      String x = Conversion.strToBits(input0.nextLine());// plaintext, a block of 64 bits
      String y = DES.encrypt(x, key); //64-bits ciphertext
      System.out.println("ciphertext: " + y);          
      String decrypted = DES.decrypt(y, key);
      System.out.println(decrypted.equals(x) ? "Success" : "Fail");
      recoveredText = Conversion.bitsToStr(decrypted);
       
      System.out.println("recoverted Text: ");
      System.out.println(recoveredText);   
   }
}