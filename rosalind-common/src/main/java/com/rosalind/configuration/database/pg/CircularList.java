package com.rosalind.configuration.database.pg;

import java.util.List;

public class CircularList<T> {
  private final List<T> list;

  public CircularList(List<T> list) {
    this.list = list;
  }
  public T getOne() {
    int index = (int) Math.floor(Math.random() * list.size());
    return list.get(index);
  }

  public int size() {
    return list.size();
  }
}
