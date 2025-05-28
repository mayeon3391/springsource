package com.example.movie.repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.movie.entity.Member;
import com.example.movie.entity.MemberRole;
import com.example.movie.entity.Movie;
import com.example.movie.entity.MovieImage;
import com.example.movie.entity.Review;

import jakarta.transaction.Transactional;

@SpringBootTest
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieImageRepository movieImageRepository;

    @Autowired
    private MemberReposiroty memberReposiroty;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 리뷰 조회
    @Test
    public void testFindByMovie() {
        System.out.println(reviewRepository.findByMovie(Movie.builder().mno(2L).build()));
    }

    // @Transactional
    @Test
    public void testFindByMovie2() {

        List<Review> list = reviewRepository.findByMovie(Movie.builder().mno(2L).build());

        for (Review review : list) {
            System.out.println(review);
            // 리뷰 작성자 조회
            System.out.println(review.getMember().getEmail());
        }

    }

    // 영화 삽입
    @Test
    public void insertMovieTest() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Movie movie = Movie.builder()
                    .title("Movie" + i)
                    .build();
            movieRepository.save(movie);

            // 임의의 이미지
            int count = (int) (Math.random() + 5) + 1;
            for (int j = 0; j < count; j++) {
                MovieImage movieImage = MovieImage.builder()
                        .uuid(UUID.randomUUID().toString())
                        .ord(j)
                        .imgName("test" + j + ".jpg")
                        .movie(movie)
                        .build();

                // movie.addImage(movieImage);
                movieImageRepository.save(movieImage);
            }
        });
    }

    // 멤버 삽입
    @Test
    public void memberInsertTest() {
        IntStream.rangeClosed(1, 20).forEach(i -> {
            Member member = Member.builder()
                    .email("user" + i + "@gmail.com")
                    .password(passwordEncoder.encode("1111"))
                    .memberRole(MemberRole.MEMBER)
                    .nickname("viewer" + i)
                    .build();

            memberReposiroty.save(member);
        });
    }

    // 리뷰 삽입
    @Test
    public void reviewInsertTest() {
        // 리뷰 200개 / 영화 100 무작위로 추출 / 멤버 무작위 추출
        IntStream.rangeClosed(1, 200).forEach(i -> {

            // 멤버아이디 무작위
            Long mid = (long) (Math.random() * 20) + 1;

            // 영화아이디 무작위
            Long mnd = (long) (Math.random() * 100) + 1;

            Review review = Review.builder()
                    .grade((int) (Math.random() * 5) + 1)
                    .text("이 영화에 대한 느낌은" + i)
                    .member(Member.builder().mid(mid).build())
                    .movie(Movie.builder().mno(mnd).build())
                    .build();

            reviewRepository.save(review);
        });

    }

    // list
    @Test
    public void listTest() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<Object[]> result = movieImageRepository.getTotalList(null, null, pageable);

        // [Movie(mno=99, title=Movie99), MovieImage(inum=589,
        // uuid=40dfc6ef-5734-4f61-8556-94dbcda745b6, imgName=test0.jpg, path=null,
        // ord=0), 3, 2.0]
        for (Object[] objects : result) {
            System.out.println(Arrays.toString(objects));
        }
    }

    @Test
    public void getMovieTest() {
        List<Object[]> result = movieImageRepository.getMovieRow(2L);
        for (Object[] objects : result) {
            System.out.println(Arrays.toString(objects));
        }

        // [Movie(mno=2, title=Movie2), MovieImage(inum=12,
        // uuid=0e17844e-643e-497e-9976-f8a08b397094, imgName=test5.jpg, path=null,
        // ord=5), 2, 5.0]

        // Movie movie = (Movie) result.get(0)[0];
        // MovieImage movieImage = (MovieImage) result.get(0)[1];
        // Long cnt = (Long) result.get(0)[2];
        // Double avg = (Double) result.get(0)[3];

    }

}
