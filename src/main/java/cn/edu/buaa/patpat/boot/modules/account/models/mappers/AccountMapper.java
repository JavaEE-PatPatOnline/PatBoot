/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.account.models.mappers;

import cn.edu.buaa.patpat.boot.modules.account.models.entities.Account;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AccountMapper {
    @Insert("""
            INSERT INTO `account` (
                       `buaa_id`,
                       `name`,
                       `password`,
                       `gender`,
                       `school`,
                       `ta`,
                       `teacher`,
                       `avatar`,
                       `created_at`)
            VALUES (
                #{buaaId},
                #{name},
                #{password},
                #{gender},
                #{school},
                #{ta},
                #{teacher},
                #{avatar},
                #{createdAt})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Account account);

    @Update("""
            UPDATE `account`
            SET `buaa_id` = #{buaaId},
                `name` = #{name},
                `gender` = #{gender},
                `school` = #{school}
            WHERE `id` = #{id}
            """)
    void updateInfo(Account account);

    @Update("""
            UPDATE `account`
            SET `buaa_id` = #{buaaId},
                `name` = #{name},
                `gender` = #{gender},
                `school` = #{school},
                `teacher` = #{teacher},
                `ta` = #{ta}
            WHERE `id` = #{id}
            """)
    void update(Account account);

    @Select("SELECT `id`, `buaa_id`, `password` FROM `account` WHERE `id` = #{id} LIMIT 1")
    Account findUpdatePassword(int id);

    @Update("UPDATE `account` SET `password` = #{password} WHERE `id` = #{id}")
    void updatePassword(Account account);

    @Select("SELECT `id`, `avatar` FROM `account` WHERE `id` = #{id} LIMIT 1")
    Account findUpdateAvatar(int id);

    @Update("UPDATE `account` SET `avatar` = #{avatar} WHERE `id` = #{id}")
    void updateAvatar(Account account);

    @Delete("DELETE FROM `account` WHERE `id` = #{id}")
    void delete(int id);
}
