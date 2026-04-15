package com.java26groupwork.reminder.mapper;

import com.java26groupwork.reminder.entity.Todo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TodoMapper {
    @Insert("""
            insert into todo(user_id, category_id, title, content, status, deadline, create_time, update_time)
            values (#{userId}, #{categoryId}, #{title}, #{content}, #{status}, #{deadline}, #{createTime}, #{updateTime})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Todo todo);

    @Select({
            "<script>",
            "select id, user_id, category_id, title, content, status, deadline, create_time, update_time",
            "from todo",
            "where user_id = #{userId}",
            "<if test='status != null'>and status = #{status}</if>",
            "<if test='categoryId != null'>and category_id = #{categoryId}</if>",
            "order by id desc",
            "</script>"
    })
    List<Todo> listByFilter(@Param("userId") Long userId, @Param("status") Integer status, @Param("categoryId") Long categoryId);

    @Update({
            "<script>",
            "update todo",
            "set category_id = #{categoryId},",
            "title = #{title},",
            "content = #{content},",
            "status = #{status},",
            "deadline = #{deadline},",
            "update_time = #{updateTime}",
            "where id = #{id}",
            "and user_id = #{userId}",
            "</script>"
    })
    int updateByOwner(Todo todo);

    @Delete("""
            delete from todo
            where id = #{id}
              and user_id = #{userId}
            """)
    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Update("""
            update todo
            set category_id = null,
                update_time = now()
            where category_id = #{categoryId}
              and user_id = #{userId}
            """)
    int clearCategoryByCategoryId(@Param("categoryId") Long categoryId, @Param("userId") Long userId);
}
