package com.example.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.board.entity.Member;

public interface MemberRepoistory extends JpaRepository<Member, String> {

}
