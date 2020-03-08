package server.dataCenter.models.db;

import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.Unirest;
import server.clientPortal.models.JsonConverter;
import server.dataCenter.DataBase;
import server.dataCenter.models.account.Collection;
import server.dataCenter.models.card.ServerCard;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static server.dataCenter.DataCenter.loadFile;

public class Rest implements DataBase {

    private static final String ACCOUNTS_PATH = "Server/resources/accounts";
    private static final String[] CARDS_PATHS = {
            "resources/heroCards",
            "resources/minionCards",
            "resources/spellCards"};

    private enum maps {
        ORIGINAL_CARDS("originalCards");

        maps(String s) {
            path = s;
        }

        String path;
    }

    final String baseAddress = "http://127.0.0.1:8080/";

    public static void main(String[] args) {
        Rest rest = new Rest();

        readCards(rest);
    }

    private static void readCards(Rest dataBase) {
        for (String path : CARDS_PATHS) {
            File[] files = new File(path).listFiles();
            if (files != null) {
                for (File file : files) {
                    ServerCard card = loadFile(file, ServerCard.class);
                    if (card != null) {
                        dataBase.addOriginalCard(card);
                    }
                }
            }
        }
    }

    public Rest() {
        if (isEmpty()) {
            for (maps s : maps.values()) {
                createMap(s.path);
            }
        }
    }

    private int put(String name, String key, String value) {
        final String path = "put";
        HttpResponse<String> response;
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("key", key);
        parameters.put("value", value);
        try {
            response = Unirest.post(baseAddress + path)
                    .fields(parameters)
                    .asString();
            return response.getStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int createMap(String name) {
        final String path = "init_DB";
        HttpResponse<String> response;
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        try {
            response = Unirest.post(baseAddress + path)
                    .fields(parameters)
                    .asString();
            return response.getStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private List getAllKeys(String name) {
        final String path = "get_all_keys";
        return getList(name, path);
    }

    private ArrayList getList(String name, String path) {
        HttpResponse<String> response;
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        try {
            response = Unirest.post(baseAddress + path)
                    .fields(parameters)
                    .asString();
            if (response.getStatus() == 200)
                return JsonConverter.fromJson(response.body(), ArrayList.class);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    private List getAllValues(String name) {
        final String path = "get_all_values";
        return getList(name, path);
    }

    private String getFromDataBase(String name, String key) {
        final String path = "get";
        HttpResponse<String> response;
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("key", key);
        try {
            response = Unirest.post(baseAddress + path)
                    .fields(parameters)
                    .asString();
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private int delete(String name, String key) {
        final String path = "del_from_DB";
        HttpResponse<String> response;
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("key", key);
        try {
            response = Unirest.post(baseAddress + path)
                    .fields(parameters)
                    .asString();
            return response.getStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public ServerCard getCard(String cardName) {
        String json = getFromDataBase(maps.ORIGINAL_CARDS.path, cardName);
        return JsonConverter.fromJson(json, ServerCard.class);
    }

    @Override
    public Collection getOriginalCards() {
        List jsons = getAllValues(maps.ORIGINAL_CARDS.path);
        Collection collection = new Collection();
        for (Object o : jsons) {
            collection.addCard(JsonConverter.fromJson((String) o, ServerCard.class));
        }
        return collection;
    }

    private <T> List<T> getList(List list, Class<T> classOfT) {
        List<T> arrayList = new ArrayList<>();
        for (Object o
                : list) {
            arrayList.add(JsonConverter.fromJson((String) o, classOfT));
        }
        return arrayList;
    }

    @Override
    public void addOriginalCard(ServerCard card) {
        put(maps.ORIGINAL_CARDS.path, card.getName(), JsonConverter.toJson(card));
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}
