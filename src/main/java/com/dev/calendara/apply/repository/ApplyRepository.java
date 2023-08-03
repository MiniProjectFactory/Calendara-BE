package com.dev.calendara.apply.repository;

import com.dev.calendara.apply.domain.Apply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplyRepository extends JpaRepository<Apply, Long> {
}
