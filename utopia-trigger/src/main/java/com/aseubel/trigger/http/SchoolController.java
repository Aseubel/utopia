package com.aseubel.trigger.http;

import com.aseubel.api.SchoolInterface;
import com.aseubel.api.dto.user.school.SchoolResponse;
import com.aseubel.domain.user.model.vo.School;
import com.aseubel.domain.user.service.IUserService;
import com.aseubel.types.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 高校
 * @author Aseubel
 * @date 2025/11/23 下午1:25
 */
@Slf4j
@RestController()
@Validated
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/v1/school") //${app.config.api-version}
@RequiredArgsConstructor
public class SchoolController implements SchoolInterface {

    private final IUserService userService;

    /**
     * 注册
     */
    @Override
    @GetMapping("/all")
    public Response<List<SchoolResponse>> queryAllSchools() {
        List<School> schools = userService.querySchools();

        List<SchoolResponse> schoolResponses = schools.stream().map(this::from).toList();

        return Response.SYSTEM_SUCCESS(schoolResponses);
    }

    public SchoolResponse from(School school) {
        return SchoolResponse.builder()
                .schoolCode(school.getSchoolCode())
                .schoolName(school.getSchoolName())
                .studentCount(school.getStudentCount())
                .discussPostCount(school.getDiscussPostCount())
                .tradePostCount(school.getTradePostCount())
                .build();
    }

}
