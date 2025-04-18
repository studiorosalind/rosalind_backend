package com.rosalind.domain.jpa;

public interface SystemMetadata extends java.io.Serializable {
  void setCreatedAt(final Long date);
  void setUpdatedAt(final Long date);
}