package com.rosalind.configuration.database.pg;

import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties("spring.datasource.hikari")
public class HikariConfigCustom extends HikariConfig {
  private List<Slave> slave;

  @Getter
  @Setter
  public static class Slave {
    private String name;
    private String jdbcUrl;
  }
}
