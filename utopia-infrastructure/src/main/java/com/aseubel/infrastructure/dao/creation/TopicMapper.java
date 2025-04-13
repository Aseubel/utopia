package com.aseubel.infrastructure.dao.creation;

import com.aseubel.infrastructure.dao.po.Topic;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025/4/8 上午11:20
 */
@Mapper
public interface TopicMapper {

    @Insert("INSERT INTO `topic` (topic_id, topic_name, topic_desc, phrase, type, example) VALUES (#{topicId}, #{topicName}, #{topicDesc}, #{phrase}, #{type}, #{example})")
    void insert(Topic topic);


    void addTopics(List<Topic> topics);

    @Update("UPDATE topic " +
            "SET status = 1 " +
            "WHERE topic_id = ( " +
            "    SELECT topic_id FROM ( " +
            "        SELECT topic_id FROM topic " +
            "        ORDER BY phrase DESC, vote_count DESC " +
            "        LIMIT 1 " +
            "    ) AS tmp " +
            ")")
    void changeToSelected();

    @Select("SELECT MAX(phrase) FROM `topic`")
    Integer getPhrase();

    @Select("SELECT * FROM `topic` WHERE phrase = (SELECT MAX(phrase) FROM `topic`)")
    List<Topic> getTopics();

    @Select("SELECT * FROM `topic` WHERE phrase = (SELECT MAX(phrase) FROM `topic`) AND status = 1")
    Topic getSelectedTopic();
}
