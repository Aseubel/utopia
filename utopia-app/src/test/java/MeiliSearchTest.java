import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.aseubel.Application;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.IndexesQuery;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.SearchResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = Application.class)
public class MeiliSearchTest {

    @Resource
    private Client meilisearchClient;

//    @Test
    public void testMeiliSearch() {
        JSONArray array = new JSONArray();
        List<JSONObject> items = new ArrayList<>() {{
            add(new JSONObject().set("id", "1").set("title", "Carol").set("genres", new JSONArray("[\"Romance\",\"Drama\"]")));
            add(new JSONObject().set("id", "2").set("title", "Wonder Woman").set("genres", new JSONArray("[\"Action\",\"Adventure\"]")));
            add(new JSONObject().set("id", "3").set("title", "Life of Pi").set("genres", new JSONArray("[\"Adventure\",\"Drama\"]")));
            add(new JSONObject().set("id", "4").set("title", "Mad Max: Fury Road").set("genres", new JSONArray("[\"Adventure\",\"Science Fiction\"]")));
            add(new JSONObject().set("id", "5").set("title", "Moana").set("genres", new JSONArray("[\"Fantasy\",\"Action\"]")));
            add(new JSONObject().set("id", "6").set("title", "Philadelphia").set("genres", new JSONArray("[\"Drama\"]")));
        }};

        array.put(items);
        String documents = array.getJSONArray(0).toString();

        // An index is where the documents are stored.
        Index index = meilisearchClient.index("movies");

        // If the index 'movies' does not exist, Meilisearch creates it when you first add the documents.
        index.addDocuments(documents); // => { "taskUid": 0 }

        SearchResult results = index.search("carlo");
        System.out.println(results);
//        meilisearchClient.index("movies").deleteAllDocuments();
    }

//    @Test
    public void testIndexQuery() {
        IndexesQuery query = new IndexesQuery().setLimit(10);
        Results<Index> indexes = meilisearchClient.getIndexes(query);
        System.out.println(indexes.toString());
    }

//    @Test
    public void testSearch() {
        SearchRequest searchRequest
                = SearchRequest.builder()
                       .build();
        meilisearchClient.index("movies").search(searchRequest);
    }

}
