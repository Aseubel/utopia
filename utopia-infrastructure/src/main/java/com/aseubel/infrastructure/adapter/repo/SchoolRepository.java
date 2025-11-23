package com.aseubel.infrastructure.adapter.repo;

import com.aseubel.domain.user.adapter.repo.ISchoolRepository;
import com.aseubel.domain.user.model.vo.School;
import com.aseubel.infrastructure.dao.SchoolMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025/11/23 下午1:29
 */
@Repository
@RequiredArgsConstructor
public class SchoolRepository implements ISchoolRepository {

    private final SchoolMapper schoolMapper;

    @Override
    public List<School> findAll() {
        return schoolMapper.listAllSchool();
    }
}
