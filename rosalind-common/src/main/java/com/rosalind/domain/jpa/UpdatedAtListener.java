package com.rosalind.domain.jpa;

import com.rosalind.util.TimeUtil;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class UpdatedAtListener {
  @PreUpdate
  @PrePersist
  public void setUpdatedAt(final SystemMetadata entity) {
    entity.setUpdatedAt(TimeUtil.getCurrentTimeMillisUtc());
  }
}