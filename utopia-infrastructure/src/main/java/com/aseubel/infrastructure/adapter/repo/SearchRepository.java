package com.aseubel.infrastructure.adapter.repo;

import com.aseubel.domain.search.adapter.repo.ISearchRepository;
import com.aseubel.infrastructure.dao.ImageMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

/**
 * @author Aseubel
 * @date 2025-03-13 09:43
 */
@Repository
public class SearchRepository implements ISearchRepository {

    @Resource
    private ImageMapper imageMapper;

    @Override
    public String getImageUrlByImageId(String imageId) {
        return imageMapper.getImageUrl(imageId);
    }
}
