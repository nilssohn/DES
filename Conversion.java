/**
The Conversion class provides two utility functions for string conversion:
1. strToBits - converts a text of 8 characters to a binary string pf 64-bits
2. bitsToStr - inverse operation of strToBits
*/
class Conversion{
   /**
   Converts a text of 8 characters to 64-bits 
   @param a string of 8 characters e.g. "ABCD-xyz"
   @return a string of 64 bits e.g. "0100...."
   */
   public static String strToBits(String str){
      byte[] bytes = str.getBytes(); //an array of 8 bytes 
                                     //e.g. {65, 66, 67, 68, 45, 120, 121, 122}
      String result = ""; 
      //Extract each bit from bytes and concatenate to result
      for (byte b : bytes)
      {
         int temp = b;
         for (int i = 0; i < 8; i++)
         {
            result = result + ((temp & 128) == 0 ? 0 : 1);
            temp = temp << 1;
         }
      
      }
      return result;
   }
   /**
   Converts a binary string of 64-bits to a text of 8 characters 
   @param bits A string of 64-bits e.g. "01000001..."
   @return A string of 8-characters  e.g. "ABCD-xyz"
   */
   public static String bitsToStr(String bits){
      byte[] bytes = new byte[8];
      for(int j=0; j<8; j++){
         bytes[j] = 0;
      }
      for(int i =0; i<bits.length(); i+=8){
         for(int j=0; j<8; j++){
            char ch = bits.charAt(i+j);
            if(ch == '1'){
               bytes[i/8] += (byte) Math.pow(2, 7-j);
            }
            else if(ch != '0'){
               System.out.println("Wrong input bits");
       
            }
         }
      }
      return new String(bytes);
   }
}