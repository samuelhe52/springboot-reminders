package com.java26groupwork.reminder.mapper;

import com.java26groupwork.reminder.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("""
            select id, username, password, create_time
            from `user`
            where username = #{username}
            """)
    User findByUsername(@Param("username") String username);

    @Select("""
            select id, username, password, create_time
            from `user`
            where username = #{username}
              and password = #{password}
            limit 1
            """)
    User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Insert("""
            insert into `user`(username, password, create_time)
            values (#{username}, #{password}, #{createTime})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);
}
