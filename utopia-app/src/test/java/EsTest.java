import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import com.aseubel.Application;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class)
public class EsTest {

    @Resource
    private ElasticsearchClient elasticsearchClient;

    @Test
    public void testElasticsearchClient() throws IOException {
        // 1. 创建一个索引
        String indexName = "test-index";
        CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(c -> c.index(indexName));
        assertTrue(createIndexResponse.acknowledged());

        // 2. 索引一个文档
        String id = "1";
        String jsonData = "{\"name\":\"John Doe\",\"age\":30}";
        InputStream jsonInputStream = new ByteArrayInputStream(jsonData.getBytes(StandardCharsets.UTF_8));
        IndexResponse indexResponse = elasticsearchClient.index(i -> i
                .index(indexName)
                .id(id)
                .withJson(jsonInputStream)
        );
        assertEquals(indexName, indexResponse.index());
        assertEquals(id, indexResponse.id());

        // 3. 获取索引的文档
        GetResponse<Object> getResponse = elasticsearchClient.get(g -> g
                .index(indexName)
                .id(id), Object.class);
        assertTrue(getResponse.found());
        assertNotNull(getResponse.source());

        // 4. 搜索文档
        SearchResponse<Object> searchResponse = elasticsearchClient.search(s -> s
                .index(indexName)
                .query(q -> q
                        .match(t -> t
                                .field("name")
                                .query("John Doe")
                        )
                ), Object.class);
        TotalHits total = searchResponse.hits().total();
        assertNotNull(total);
        assertEquals(TotalHitsRelation.Eq, total.relation());
        assertEquals(1, total.value());

        for (Hit<Object> hit : searchResponse.hits().hits()) {
            assertNotNull(hit.source());
        }

        // 5. 删除索引
        elasticsearchClient.indices().delete(d -> d.index(indexName));
    }

    @Test
    public void simpleTest() {
        System.out.println(elasticsearchClient);
    }
}
