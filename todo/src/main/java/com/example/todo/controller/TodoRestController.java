package com.example.todo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.todo.dto.ToDoDTO;
import com.example.todo.entity.ToDo;
import com.example.todo.service.TodoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/todos")
@RestController
public class TodoRestController {

    private final TodoService todoService;

    @PostMapping("/add")
    public ToDoDTO postCreate(@RequestBody ToDoDTO dto) {
        log.info("todo 입력 {}", dto);

        ToDoDTO newDto = todoService.create2(dto);
        return newDto;
    }

    @DeleteMapping("{id}")
    public String getRemove(@PathVariable Long id) {
        log.info("삭제 {}", id);

        todoService.remove(id);
        return "success";
    }

    @GetMapping("")
    public List<ToDoDTO> getList() {
        log.info("전체 todo 가져오기 {}, completed");
        List<ToDoDTO> todos = todoService.list2();

        return todos;
    }

    @PutMapping("/{id}")
    public Long postMethodName(@RequestBody ToDoDTO dto) {
        log.info("수정 {}", dto);

        Long id = todoService.changeCompleted(dto);
        return id;
    }

}
