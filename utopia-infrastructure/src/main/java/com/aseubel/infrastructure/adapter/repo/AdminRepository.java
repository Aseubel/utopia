package com.aseubel.infrastructure.adapter.repo;

import com.aseubel.domain.user.adapter.repo.IAdminRepository;
import com.aseubel.infrastructure.dao.AdminMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

/**
 * @author Aseubel
 * @description AdminRepository
 * @date 2025-03-28 18:17
 */
@Repository
public class AdminRepository implements IAdminRepository {

    @Resource
    private AdminMapper adminMapper;

    @Override
    public boolean isAdmin(String userId, String schoolCode) {
        int count = adminMapper.isSchoolAdmin(userId, schoolCode);
        return count > 0;
    }

    @Override
    public boolean isAdmin(String userId) {
        int count = adminMapper.isSuperAdmin(userId);
        return count > 0;
    }

}
