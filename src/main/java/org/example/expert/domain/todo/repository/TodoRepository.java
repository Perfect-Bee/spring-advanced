package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // Todo 전체 조회 : 수정일 기준 내림차순 + 연관된 user를 즉시 로딩 (N+1 문제 방지)
    @EntityGraph(attributePaths = "user")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    // Todo 조회(todoId) : 연관된 User를 즉시 로딩
    @EntityGraph(attributePaths = "user")
    Optional<Todo> findById(Long todoId); // @Param 삭제, 매서드명에 WithUser : 이걸 쿼리로 읽음

    int countById(Long todoId);
}
