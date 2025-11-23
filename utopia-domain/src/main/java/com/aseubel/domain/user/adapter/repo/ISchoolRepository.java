package com.aseubel.domain.user.adapter.repo;

import com.aseubel.domain.user.model.vo.School;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025/11/23 下午1:30
 */
public interface ISchoolRepository {
    List<School> findAll();
}
