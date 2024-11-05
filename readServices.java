import java.io.*;
import java.util.*;

public class readServices {
   public static void main(String[] args) {
      // read services.yel
      try {
         BufferedReader in = new BufferedReader(new FileReader("services.yel"));

         List<String> lines = new ArrayList<String>();
         String inLine;
         while ((inLine = in.readLine()) != null) {
            // check if line is empty
            if (inLine.trim().isEmpty()) {
               continue;
            }
            // check if line is a comment
            if (inLine.trim().charAt(0) == '#') {
               continue;
            }

            lines.add(inLine);
         }
         in.close();

         // create services
         String name = "none";
         HashMap<String, String> properties = new HashMap<String, String>();
         boolean inService = false;
         // list of services
         List<Service> services = new ArrayList<Service>();

         for (String line : lines) {
            // check for start of service and get name
            if (line.contains("{")) {
               name = line.substring(0, line.indexOf("{") - 1);
               inService = true;
               continue;
            }

            if (line.contains(":") && inService) {
               String[] parts = line.split(":");
               String key = parts[0].trim();
               String value = parts[1].trim();
               properties.put(key, value);
            }

            // check for end of service
            if (line.contains("}")) {
               // make the service and add to list
               services.add(new Service(name, properties));
               name = "none";
               properties = new HashMap<String, String>();
               inService = false;
               continue;

            }
         }

         // print services
         for (Service service : services) {
            System.out.println(service.name);
            System.out.println(service.getProperty("path"));
            System.out.println(service.getProperty("git"));
            System.out.println();

            // run system command
            try {
               String command = "git -C " + service.getProperty("path") + " status";
               Process p = Runtime.getRuntime().exec(command);
               p.waitFor();
               BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
               String line = "";
               while ((line = reader.readLine()) != null) {
                  System.out.println(line);
               }
               reader.close();
            } catch (IOException e) {
               System.out.println("Command Error");
            } catch (InterruptedException e) {
               System.out.println("Command Error");
            }

         }
      } catch (IOException e) {
         System.out.println("File Read Error");
      }
   }
}