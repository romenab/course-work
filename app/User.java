// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package app;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class User {
   private String username;
   private List<Map<String, String>> allUsers;

   public User() {
      try {
         FileReader var1 = new FileReader("users.json");

         try {
            JsonObject var2 = JsonParser.parseReader(var1).getAsJsonObject();
            JsonArray var3 = var2.getAsJsonArray("users");
            this.allUsers = new ArrayList();
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               JsonElement var5 = (JsonElement)var4.next();
               JsonObject var6 = var5.getAsJsonObject();
               HashMap var7 = new HashMap();
               var7.put("username", var6.get("username").getAsString());
               var7.put("password", var6.get("password").getAsString());
               this.allUsers.add(var7);
            }
         } catch (Throwable var9) {
            try {
               var1.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }

            throw var9;
         }

         var1.close();
      } catch (Exception var10) {
         throw new RuntimeException("Failed to load users.json", var10);
      }
   }

   public void logIn(Scanner var1) {
      for(int var2 = 3; var2 > 0; --var2) {
         System.out.print("Username: ");
         String var3 = var1.nextLine();
         System.out.print("Password: ");
         String var4 = var1.nextLine();
         Iterator var5 = this.allUsers.iterator();

         while(var5.hasNext()) {
            Map var6 = (Map)var5.next();
            if (((String)var6.get("username")).equals(var3) && ((String)var6.get("password")).equals(var4)) {
               System.out.println("Login was successful!");
               this.username = var3;
               return;
            }
         }

         System.out.println("Login was not successful! Try again!");
      }

      System.out.println("Maximum login attempts reached! Bye!");
      System.exit(0);
   }

   public String getUsername() {
      return this.username;
   }
}
