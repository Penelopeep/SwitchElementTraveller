package test.gc.switchele;

import com.google.gson.JsonParser;
import emu.grasscutter.Grasscutter;

import java.io.*;

public class LanguageHelper{
    public static String reader(String text){
        String file = String.format("%s.json", Grasscutter.getLanguage().getLanguageCode());
        try (InputStream stream = Switchele.getInstance().getResource(file)) {
            Grasscutter.getLogger().info(file);
            System.out.println(stream);
            //Reader reader = new InputStreamReader(stream);
            //return new JsonParser().parse(reader).getAsJsonObject().get(text).getAsString();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}