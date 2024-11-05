import java.util.HashMap;

public class Service {
   public String name;
   private HashMap<String, String> properties = new HashMap<String, String>();

   public Service(String name, HashMap<String, String> properties) {
      this.name = name;
      this.properties = properties;
   }

   public String getProperty(String key) {
      return properties.get(key);
   }
}
