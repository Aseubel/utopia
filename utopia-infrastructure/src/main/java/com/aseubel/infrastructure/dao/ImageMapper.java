package com.aseubel.infrastructure.dao;

import com.aseubel.infrastructure.dao.po.Image;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Aseubel
 * @description 图片统一dao接口
 * @date 2025-01-21 20:35
 */
@Mapper
public interface ImageMapper {

    /**
     * 添加图片
     * @param image
     */
    @Insert("insert into `image`(user_id, image_id, image_url) values (#{userId}, #{imageId}, #{imageUrl})")
    void addImage(Image image);

    /**
     * 根据图片id列表查询图片列表，用于批量关联
     * @param imageIds
     * @return
     */
    List<Image> listImageByImageIds(List<String> imageIds);

    /**
     * 根据图片id查询图片url
     * @param imageId
     * @return
     */
    String getImageUrl(String imageId);
}
