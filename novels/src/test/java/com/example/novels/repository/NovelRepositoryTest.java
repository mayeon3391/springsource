package com.example.novels.repository;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.novels.entity.Genre;
import com.example.novels.entity.Grade;
import com.example.novels.entity.Member;
import com.example.novels.entity.Novel;

@SpringBootTest
public class NovelRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private NovelRepository novelRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void getNovelTest() {
        Object[] result = novelRepository.getNovelById(150L);
        // System.out.println(Arrays.toString(result));
        Novel novel = (Novel) result[0];
        Genre genre = (Genre) result[1];
        Double avgGrade = (Double) result[2];
        System.out.println(novel);
        System.out.println(genre);
        System.out.println(avgGrade);

    }

    @Test
    public void getNovelListTest() {

        Pageable pageable = PageRequest.of(1, 10, Sort.by("id").descending());
        Page<Object[]> result = novelRepository.list(pageable);
        for (Object[] objects : result) {
            System.out.println(Arrays.toString(objects));
        }

    }

    @Test
    public void userInsert() {
        // user 50 삽입
        IntStream.rangeClosed(1, 50).forEach(i -> {
            Member member = Member.builder()
                    .email("user" + i + "@emial.com")
                    .pw("1111")
                    .nickname("user" + i)
                    .build();

            memberRepository.save(member);
        });

    }

    // grade 200 삽입
    @Test
    public void gradeInsert() {
        // grade 200 삽입
        IntStream.rangeClosed(1, 200).forEach(i -> {

            // novel id 생성
            long nid = (long) (Math.random() * 160) + 1;

            int rating = (int) (Math.random() * 5) + 1;

            int uid = (int) (Math.random() * 50) + 1;

            Grade grade = Grade.builder()
                    .member(Member.builder().email("user" + uid + "@emial.com").build())
                    .novel(Novel.builder().id(nid).build())
                    .rating(rating)
                    .build();

            gradeRepository.save(grade);
        });

    }

}
