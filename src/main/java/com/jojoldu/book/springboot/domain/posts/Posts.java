// JPA를 사용하여 DB 데이터 작업을 실제쿼리를 날리는 대신에 이 Entity 클래스의 수정을 통해 작업함
package com.jojoldu.book.springboot.domain.posts;

import com.jojoldu.book.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

//lombok 어노테이션
@Getter            // 클래스 내 모든 필드의 Getter 메소드 자동생성
@NoArgsConstructor // 기본 생성자 자동 추가 (public Posts())
//JPA 어노테이션
@Entity  // 테이블과 링크될 클래스임을 나타냄
public class Posts extends BaseTimeEntity { // BaseTimeEntity 상속

    @Id // 해당 테이블의 PK 필드를 나타냄
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 생성규칙을 나타냄. IDENTITY옵션이 있어야 auto_increment 됨
    private Long id;

    // 테이블의 컬럼 - 선언하지 않더라도 해당 클래스의 필드는 모두 컬럼이 됨 (옵션이 있는 경우만 @Column 사용)
    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    // lombok
    @Builder  // 해당 클래스의 빌더 패턴 클래스를 생성 (생성자 상단에 선언 시 생성자에 포함된 필드만 빌더에 포함)
    public Posts(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    // update
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        // DB에 update쿼리를 날리는 부분이 없는 이유는 "JPA 영속성 컨텍스트(엔티티를 영구 저장하는 환경)" 때문임
        // JPA의 EntityManager가 활성화된 상태(Spring Data Jpa사용시 기본옵션)로 트랜잭션 안어세 DB에서 데이터를 가져오면
        // 이 데이터는 영속성 컨텍스트가 유지된 상태임
        // 이 상태에서 해당 데이터의 값을 변경하면 트랜잭션이 끝나는 시점에 해당 테이블에 변경분을 반영하므로, Entity 객체의 값만
        // 변경하면 별도로 update 쿼리를 날릴 필요가 없음 (이 개념을 Dirty Checking 이라고 함)
    }

}
