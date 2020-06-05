package com.jojoldu.book.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jojoldu.book.springboot.domain.posts.Posts;
import com.jojoldu.book.springboot.domain.posts.PostsRepository;
import com.jojoldu.book.springboot.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //랜덤포트 실행
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    //매번 테스트가 시작되기 전에 MockMvc 인스턴스를 생성
    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    // 인증된 모의 사용자를 만들어서 사용. roles에 권한을 추가. ROLE_USER 권한을 가진 사용자가 API를 요청하는 효과
    @WithMockUser(roles = "USER")
    public void Posts_등록된다() throws Exception {
        //given
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        //ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
        //생성된 MockMvc를 통해 API를 테스트 - Body영역은 문자열로 표현하기 위해 ObjectMapper를 통해 문자열 JSON으로 변환
        //ObjectMapper 클래스는 서버 단에서 오브젝트 형식을 JSON으로 변환하기위해 사용
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    // 인증된 모의 사용자를 만들어서 사용. roles에 권한을 추가. ROLE_USER 권한을 가진 사용자가 API를 요청하는 효과
    @WithMockUser(roles = "USER")
    public void Posts_수정된다() throws Exception {
        //given - 초기화 데이터 save
        Posts savePosts = postsRepository.save(Posts.builder()
        .title("title")
        .content("content")
        .author("author")
        .build());

        Long updateId = savePosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        //HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
//        ResponseEntity<Long> responseEntity = restTemplate
//                .exchange(url, HttpMethod.PUT, requestEntity, Long.class);
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Posts_삭제된다() throws Exception {
        //given - 초기데이터
        Posts savePosts = postsRepository.save(Posts.builder()
        .title("title")
        .content("content")
        .author("author")
        .build());

        Long deleteId = savePosts.getId();
        System.out.println("############# deleteId :: " + deleteId);

        String url = "http://localhost:" + port + "/api/v1/posts/" + deleteId;

        Optional<Posts> optional = postsRepository.findById(deleteId);

        if (optional.isPresent()) {
            Posts queryPosts = optional.get();
            System.out.println("########## queryPosts ::" + queryPosts);
            HttpEntity<Posts> requestEntity = new HttpEntity<>(queryPosts);

            //when
//            ResponseEntity<Long> responseEntity = restTemplate
//                    .exchange(url, HttpMethod.DELETE, requestEntity, Long.class);

            mvc.perform(delete(url)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(new ObjectMapper().writeValueAsString(optional)))
                    .andExpect(status().isOk());

            //then
//            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//            assertThat(responseEntity.getBody()).isGreaterThan(0L);

        }
    }
}
