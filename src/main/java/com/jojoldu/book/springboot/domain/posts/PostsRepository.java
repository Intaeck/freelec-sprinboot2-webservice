package com.jojoldu.book.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// ibatis, MyBatis등에서 Dao라고 불리는 DB Layer접근자를 JPA에서는 Repository라고 부르며 인터페이스로 생성함
//*****************************
// DAO = Repository
//*****************************

// JpaRepository<Entity 클래스, PK 타입> 으로 선언하면 기본적인 CRUD 메소드가 자동으로 생성됨
// @Repository 추가 필요 없음
// Entity클래스와 기본 Entity Repository는 함께 위치해야함
public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();
}
