package com.cheer.ad.dao;

import com.cheer.ad.entity.Creative;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 创意dao
 *
 * @Created by ljp on 2019/11/14.
 */
public interface CreativeRepository extends JpaRepository<Creative, Long> {
}
