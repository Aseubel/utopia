package com.aseubel.infrastructure.dao;

import com.aseubel.infrastructure.dao.po.Avatar;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AvatarMapper {

    /**
     * 添加新头像
     * @param avatar
     */
    @Insert("insert into `avatar` (avatar_id,user_id,avatar_url) values(#{avatarId},#{userId},#{avatarUrl})")
    void addAvatar(Avatar avatar);

}
