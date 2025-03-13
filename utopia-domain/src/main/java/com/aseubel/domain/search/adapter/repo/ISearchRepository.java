package com.aseubel.domain.search.adapter.repo;

public interface ISearchRepository {

    /**
     * Get the image URL by image ID.
     * @param imageId
     * @return
     */
    String getImageUrlByImageId(String imageId);

}
