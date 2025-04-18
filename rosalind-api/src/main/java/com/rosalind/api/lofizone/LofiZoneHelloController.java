package com.rosalind.api.lofizone;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LofiZoneHelloController {

  @GetMapping("/hello/lofi")
  public String hello() {
    return "Hello World! - LOFI ZONE";
  }
}
