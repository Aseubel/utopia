package com.aseubel.infrastructure.adapter.repo;

import cn.hutool.json.JSONUtil;
import com.aseubel.domain.bazaar.adapter.repo.IAIBazaarRepository;
import com.aseubel.domain.community.adapter.repo.IAICommunityRepository;
import com.aseubel.types.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Aseubel
 * @date 2025/4/15 上午1:37
 */
@Repository
@Slf4j
public class AIUtilRepository implements IAICommunityRepository, IAIBazaarRepository {

    @Value("${POST_AUDIT_TOKEN}")
    private String POST_AUDIT_TOKEN;

    /**
     * 图片审核
     * @param imgUrl 图片url
     * @param userId 用户id
     * @return 审核结果 true合规 false不合规
     */
    public boolean ImgCensor(String imgUrl, String userId) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/solution/v1/img_censor/v2/user_defined";
        try {
            String param = "imgUrl=" + imgUrl;
            if (StringUtils.isNotEmpty(userId)) {
                param += "&userId=" + userId;
            }

            String accessToken = POST_AUDIT_TOKEN;

            String result = HttpUtil.post(url, accessToken, param);
            // conclusionType 审核结果类型，可取值1、2、3、4，分别代表1：合规，2：不合规，3：疑似，4：审核失败
            int conclusionType = (int) JSONUtil.parseObj(result).get("conclusionType");
            return switch (conclusionType) {
                case 1 -> true;
                case 2 -> false;
                default -> false;
            };
        } catch (Exception e) {
            log.error("图像审核失败", e);
        }
        return false;
    }

    /**
     * 图片审核
     * @param imgUrl 图片url
     * @return 审核结果
     */
    public boolean ImgCensor(String imgUrl) {
        return ImgCensor(imgUrl, null);
    }

    /**
     * 文本审核
     * @param text 文本
     * @param userId 用户id
     * @return 审核结果 true合规 false不合规
     */
    public boolean TextCensor(String text, String userId) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined";
        try {
            String param = "text=" + URLEncoder.encode(text, StandardCharsets.UTF_8);
            if (StringUtils.isNotEmpty(userId)) {
                param += "&userId=" + userId;
            }

            String accessToken = POST_AUDIT_TOKEN;

            String result = HttpUtil.post(url, accessToken, param);
            // conclusionType 审核结果类型，可取值1、2、3、4，分别代表1：合规，2：不合规，3：疑似，4：审核失败
            int conclusionType = (int) JSONUtil.parseObj(result).get("conclusionType");
            return switch (conclusionType) {
                case 1 -> true;
                case 2 -> false;
                default -> false;
            };
        } catch (Exception e) {
            log.error("文本审核失败", e);
        }
        return false;
    }

    /**
     * 文本审核
     * @param text 文本
     * @return 审核结果
     */
    public boolean TextCensor(String text) {
        return TextCensor(text, null);
    }
}
