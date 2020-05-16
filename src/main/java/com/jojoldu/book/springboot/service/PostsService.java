package com.jojoldu.book.springboot.service;

import com.jojoldu.book.springboot.domain.posts.Posts;
import com.jojoldu.book.springboot.domain.posts.PostsRepository;
import com.jojoldu.book.springboot.web.dto.PostsListResponseDto;
import com.jojoldu.book.springboot.web.dto.PostsResponseDto;
import com.jojoldu.book.springboot.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor // final이 선언된 모든 필드를 인자값으로 하는 생성자를 생성 (lombok)
@Service
public class PostsService {

    // @RequiredArgsConstructor가 PostsRepository 클래스 생성자를 생성함
    private final PostsRepository postsRepository;

    // 인서트
    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        // springframework의 CrudRepository에 있는 save 메소드 호출
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    // 업데이트
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        // springframework의 CrudRepository에 있는 findById 메소드 호출
        Posts posts = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 없습니다. id=" +id));

        // Posts에 있는 update 메소드
        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    // 단건 조회
    public PostsResponseDto findById(Long id) {
        // springframework의 CrudRepository에 있는 save 메소드 호출
        Posts entity = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    // readonly=true는 트랜잭션 범위는 유지하되, 조회 기능만 남겨두어 조회속도가 개선되므로 등록,수정,삭제 기능이 없는 메소드에 사용
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        // postsRepository 결과(@Query)로 넘어온 Posts의 Stream을 map을 통해 PostsListResponseDto 변환 -> List로 반환하는 메소드
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)  // == .map(posts -> new PostsListResponseDto(posts))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete (Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        // springframework의 CrudRepository에 있는 delete 메소드 호출 (deleteById 메소드로 id로 삭제 가능)
        postsRepository.delete(posts);
    }
}
