import com.alibaba.otter.canal.client.CanalConnector;
import com.aseubel.Application;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class CanalTest {

    @Resource
    private CanalConnector canalConnector;

    @Test
    public void testConnection() {
        try {
            canalConnector.connect();
            System.out.println("✅ 成功连接到Canal服务端");
            canalConnector.disconnect();
        } catch (Exception e) {
            System.out.println("❌ 连接Canal服务端失败: " + e.getMessage());
        }
    }

}
