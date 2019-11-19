package com.cheer.ad.dao.unit_condition;

import com.cheer.ad.entity.unit_condition.CreativeUnit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 兴趣单元关联dao
 *
 * @Created by ljp on 2019/11/14.
 */
public interface CreativeUnitRepository extends JpaRepository<CreativeUnit, Long> {
}
