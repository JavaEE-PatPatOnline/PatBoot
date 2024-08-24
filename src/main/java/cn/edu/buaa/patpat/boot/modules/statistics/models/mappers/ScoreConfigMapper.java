package cn.edu.buaa.patpat.boot.modules.statistics.models.mappers;

import cn.edu.buaa.patpat.boot.modules.statistics.models.entities.ScoreConfig;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ScoreConfigMapper {
    @Insert("""
            INSERT INTO `score_config` (
                `course_id`, `lab_score`, `iter_score`, `proj_score`
            ) VALUES (
                #{courseId}, #{labScore}, #{iterScore}, #{projScore}
            ) ON DUPLICATE KEY UPDATE
                `lab_score` = #{labScore},
                `iter_score` = #{iterScore},
                `proj_score` = #{projScore}
            """)
    void saveOrUpdate(ScoreConfig config);

    @Select("SELECT * FROM `score_config` WHERE `course_id` = #{courseId}")
    @Options(useCache = true)
    ScoreConfig find(int courseId);
}
