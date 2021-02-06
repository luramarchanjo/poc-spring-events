package com.example.pocspringevents;

import com.example.pocspringevents.model.OperationSimulator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

  private final OperationSimulator simulator;

  public MyController(OperationSimulator simulator) {
    this.simulator = simulator;
  }

  @GetMapping("/same-thread/exception-on-listener")
  public void getSameThreadExceptionOnListener() {
    simulator.simulateErrorAndSlowAtListener();
  }

}
