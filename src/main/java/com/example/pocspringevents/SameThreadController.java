package com.example.pocspringevents;

import com.example.pocspringevents.model.OperationSimulator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SameThreadController {

  private final OperationSimulator simulator;

  public SameThreadController(OperationSimulator simulator) {
    this.simulator = simulator;
  }

  @GetMapping("/same-thread/transaction-commit")
  public void simulateTransactionCommit() {
    simulator.simulateTransactionCommit(true);
  }

  @GetMapping("/same-thread/transaction-rollback")
  public void simulateTransactionRollback() {
    simulator.simulateTransactionRollback(true);
  }

  @GetMapping("/same-thread/listener-error")
  public void simulateErrorAndSlowAtListener() {
    simulator.simulateErrorAndSlowAtListener();
  }

}