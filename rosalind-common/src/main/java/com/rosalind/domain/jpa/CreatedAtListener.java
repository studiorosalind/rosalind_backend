package com.rosalind.domain.jpa;

import com.rosalind.util.TimeUtil;

import javax.persistence.PrePersist;
import java.util.Date;

public class CreatedAtListener {
  @PrePersist
  public void setCreatedAt(final SystemMetadata entity) {
    entity.setCreatedAt(TimeUtil.getCurrentTimeMillisUtc());
  }
}