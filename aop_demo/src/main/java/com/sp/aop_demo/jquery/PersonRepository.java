package com.sp.aop_demo.jquery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
