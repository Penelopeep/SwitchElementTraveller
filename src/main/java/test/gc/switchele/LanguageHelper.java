package test.gc.switchele;

import com.google.gson.JsonParser;
import emu.grasscutter.Grasscutter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class LanguageHelper{
    public static String reader(String text, String username) {
        String fileName = String.format("%s.json", Grasscutter.getGameServer().getAccountByName(username).getLocale());
        ClassLoader classLoader = Switchele.getInstance().getClass().getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(fileName);
             InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
             text = new JsonParser().parse(reader).getAsJsonObject().get(text).getAsString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }
}