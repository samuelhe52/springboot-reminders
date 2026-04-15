package com.java26groupwork.reminder.mapper;

import com.java26groupwork.reminder.entity.TodoCategory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TodoCategoryMapper {
    @Insert("""
            insert into todo_category(user_id, name, create_time)
            values (#{userId}, #{name}, #{createTime})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(TodoCategory category);

    @Select("""
            select id, user_id, name, create_time
            from todo_category
            where user_id = #{userId}
            order by id desc
            """)
    List<TodoCategory> listByUserId(@Param("userId") Long userId);

    @Update("""
            update todo_category
            set name = #{name}
            where id = #{id}
              and user_id = #{userId}
            """)
    int updateName(@Param("id") Long id, @Param("userId") Long userId, @Param("name") String name);

    @Delete("""
            delete from todo_category
            where id = #{id}
              and user_id = #{userId}
            """)
    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Select("""
            select count(*)
            from todo_category
            where id = #{id}
              and user_id = #{userId}
            """)
    int countByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Select("""
            select id
            from todo_category
            where user_id = #{userId}
              and name = #{name}
            order by id asc
            limit 1
            """)
    Long findIdByUserIdAndName(@Param("userId") Long userId, @Param("name") String name);
}
