package com.cheer.ad.dao;

import com.cheer.ad.entity.AdUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户dao
 *
 * @Created by ljp on 2019/11/14.
 */
public interface AdUserRespository extends JpaRepository<AdUser, Long> {
    AdUser findByUsername(String username);
}


