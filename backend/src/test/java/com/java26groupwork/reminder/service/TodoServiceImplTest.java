package com.java26groupwork.reminder.service;

import com.java26groupwork.reminder.entity.Todo;
import com.java26groupwork.reminder.mapper.TodoCategoryMapper;
import com.java26groupwork.reminder.mapper.TodoMapper;
import com.java26groupwork.reminder.service.impl.TodoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoMapper todoMapper;

    @Mock
    private TodoCategoryMapper categoryMapper;

    @InjectMocks
    private TodoServiceImpl todoService;

    @Test
    void addShouldSetDefaultFields() {
        Todo todo = new Todo();
        todo.setTitle("Finish homework");
        when(categoryMapper.findIdByUserIdAndName(1L, "Default")).thenReturn(10L);
        when(categoryMapper.countByIdAndUserId(10L, 1L)).thenReturn(1);

        todoService.add(1L, todo);

        assertEquals(1L, todo.getUserId());
        assertEquals(10L, todo.getCategoryId());
        assertEquals(0, todo.getStatus());
        assertNotNull(todo.getCreateTime());
        assertNotNull(todo.getUpdateTime());
        verify(todoMapper).insert(todo);
    }

    @Test
    void addShouldThrowWhenTitleIsBlank() {
        Todo todo = new Todo();
        todo.setTitle("   ");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> todoService.add(1L, todo));

        assertEquals("Todo title is required", exception.getMessage());
    }

    @Test
    void updateShouldThrowWhenCategoryDoesNotBelongToUser() {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Finish homework");
        todo.setStatus(0);
        todo.setCategoryId(99L);

        when(categoryMapper.countByIdAndUserId(99L, 1L)).thenReturn(0);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> todoService.update(1L, todo));

        assertEquals("Category not found", exception.getMessage());
    }

    @Test
    void updateShouldPersistWhenDataIsValid() {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Finish homework");
        todo.setStatus(1);

        when(todoMapper.updateByOwner(todo)).thenReturn(1);

        todoService.update(1L, todo);

        assertEquals(1L, todo.getUserId());
        assertNotNull(todo.getUpdateTime());
        verify(todoMapper).updateByOwner(todo);
    }
}
