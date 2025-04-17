package com.rosalind.api.tshirtiti;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TshirtitiHelloController {

  @GetMapping("/hello-tshirtiti")
  public String hello() {
    return "Hello World! - TSHIRTITI";
  }
}
