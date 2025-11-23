package com.aseubel.api;

import com.aseubel.api.dto.user.school.SchoolResponse;
import com.aseubel.types.Response;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025/11/23 下午1:33
 */
public interface SchoolInterface {

    Response<List<SchoolResponse>> queryAllSchools();
}
