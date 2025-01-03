/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.group.models.mappers;

import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupAssignment;
import org.apache.ibatis.annotations.*;

@Mapper
@CacheNamespace
public interface GroupAssignmentMapper {
    @Insert("""
            INSERT INTO `group_assignment` (`course_id`, `comment`, `visible`, `start_time`, `end_time`)
            VALUES (#{courseId}, #{comment}, #{visible}, #{startTime}, #{endTime})
            ON DUPLICATE KEY UPDATE `comment` = #{comment}, `visible` = #{visible}, `start_time` = #{startTime}, `end_time` = #{endTime}
            """)
    void saveOrUpdate(GroupAssignment assignment);

    @Delete("DELETE FROM `group_assignment` WHERE `course_id` = #{courseId}")
    int delete(int courseId);

    @Select("SELECT * FROM `group_assignment` WHERE `course_id` = #{courseId}")
    GroupAssignment find(int courseId);
}
