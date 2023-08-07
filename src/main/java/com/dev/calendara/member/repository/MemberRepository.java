package com.dev.calendara.member.repository;

import com.dev.calendara.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
