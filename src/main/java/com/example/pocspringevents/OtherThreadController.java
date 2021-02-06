package com.example.pocspringevents;

import com.example.pocspringevents.model.OperationSimulator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OtherThreadController {

  private final OperationSimulator simulator;

  public OtherThreadController(OperationSimulator simulator) {
    this.simulator = simulator;
  }

  @GetMapping("/other-thread/transaction-commit")
  public void simulateTransactionCommitOnOtherThread() {
    simulator.simulateTransactionCommit(false);
  }

  @GetMapping("/other-thread/transaction-rollback")
  public void simulateTransactionRollbackOnOtherThread() {
    simulator.simulateTransactionRollback(false);
  }

}