/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.group.models.mappers;

import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupConfig;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
@CacheNamespace
public interface GroupConfigMapper {
    @Insert("""
            INSERT INTO `group_config` (`course_id`, `max_size`, `min_weight`, `max_weight`, `enabled`)
            VALUES (#{courseId}, #{maxSize}, #{minWeight}, #{maxWeight}, #{enabled})
            ON DUPLICATE KEY UPDATE
                `max_size` = #{maxSize},
                `min_weight` = #{minWeight},
                `max_weight` = #{maxWeight},
                `enabled` = #{enabled}
            """)
    void saveOrUpdate(GroupConfig config);

    @Select("SELECT * FROM `group_config` WHERE `course_id` = #{courseId}")
    GroupConfig find(int courseId);
}
