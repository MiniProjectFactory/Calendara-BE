package com.dev.calendara.apply.repository;

import com.dev.calendara.apply.domain.Apply;
import com.dev.calendara.apply.domain.enumeration.ApplyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplyRepository extends JpaRepository<Apply, Long> {

    List<Apply> findAllByMemberIdAndApplyStatusNot(Long memberId, ApplyStatus applyStatus);
}
