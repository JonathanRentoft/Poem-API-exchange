package app.controller;

import app.dao.PoemDAO;
import app.entities.Poem;
import app.dto.PoemDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PoemController {

    private final PoemDAO poemDAO = PoemDAO.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void getAllPoems(Context ctx) {
        List<Poem> poems = poemDAO.getAll();
        List<PoemDTO> poemDTOs = poems.stream()
                .map(PoemDTO::new)
                .collect(Collectors.toList());
        ctx.json(poemDTOs);
    }

    public void getPoemById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Poem poem = poemDAO.getById(id);
        if (poem != null) {
            ctx.json(new PoemDTO(poem));
        } else {
            ctx.status(404).result("Poem not found");
        }
    }

    public void createPoem(Context ctx) {
        Poem poem = ctx.bodyAsClass(Poem.class);
        Poem createdPoem = poemDAO.create(poem);
        ctx.status(201).json(new PoemDTO(createdPoem));
    }

    public void updatePoem(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Poem poemToUpdate = ctx.bodyAsClass(Poem.class);
        poemToUpdate.setId(id); // Sørg for at ID'et er sat
        Poem updatedPoem = poemDAO.update(poemToUpdate);
        if (updatedPoem != null) {
            ctx.json(new PoemDTO(updatedPoem));
        } else {
            ctx.status(404).result("Poem not found");
        }
    }

    public void deletePoem(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        poemDAO.delete(id);
        ctx.status(200).result("Poem deleted");
    }

    /**
     * Henter digte fra både dit eget og din partners API.
     */
    public void getCombinedPoems(Context ctx) {
        // *** UDSKIFT MED DIN PARTNERS IP-ADRESSE ***
        String friendsApiUrl = "http://10.132.XX.XX:7071/poem";

        // 1. Hent dine egne digte
        List<Poem> myPoems = poemDAO.getAll();
        List<PoemDTO> combinedList = myPoems.stream()
                .map(PoemDTO::new)
                .collect(Collectors.toCollection(ArrayList::new)); // Gør listen muterbar

        // 2. Hent din partners digte
        try {
            List<PoemDTO> friendsPoems = fetchPoemsFromFriend(friendsApiUrl);
            combinedList.addAll(friendsPoems);
        } catch (IOException | InterruptedException e) {
            // Hvis partnerens API er nede, kan vi vælge at sende vores egne digte alligevel
            // eller sende en fejlmeddelelse. Vi vælger det første.
            System.err.println("Could not fetch poems from friend's API: " + e.getMessage());
        }

        // 3. Send den kombinerede liste
        ctx.json(combinedList);
    }

    /**
     * En hjælpe-metode der bruger Javas indbyggede HttpClient til at hente data.
     * @param apiUrl URL til partnerens API.
     * @return En liste af PoemDTO'er.
     */
    private List<PoemDTO> fetchPoemsFromFriend(String apiUrl) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Konverter JSON-strengen til en liste af PoemDTO objekter
        return objectMapper.readValue(response.body(), new TypeReference<>() {});
    }
}
