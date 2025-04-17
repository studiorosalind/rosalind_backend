package com.rosalind.configuration.database.pg;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;
import java.util.stream.Collectors;

public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {
  private static final String MASTER = "master";
  private static final String SLAVE = "slave";
  private CircularList<String> dataSourceNameList;

  @Override
  public void setTargetDataSources(@NotNull Map<Object, Object> targetDataSources) {
    super.setTargetDataSources(targetDataSources);

    dataSourceNameList = new CircularList<>(
      targetDataSources.keySet()
        .stream()
        .filter(key -> key.toString().contains(SLAVE))
        .map(Object::toString)
        .collect(Collectors.toList())
    );
  }
  @Override
  protected Object determineCurrentLookupKey() {
    boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

    // dataSourceNameList 1개인 경우에는 master만 존재하는 경우이므로 master를 리턴한다.
    if (dataSourceNameList.size() > 0 && isReadOnly) {
      return dataSourceNameList.getOne();
    } else {
      return MASTER;
    }
  }
}
