package com.example.pocspringevents.tests;

import com.example.pocspringevents.model.MyEntity;
import com.example.pocspringevents.model.MyEntityRepository;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class ExceptionListener {

  private final MyEntityRepository repository;

  public ExceptionListener(MyEntityRepository repository) {
    this.repository = repository;
  }

  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void listenBeforeCommit(ExceptionEvent event) throws InterruptedException {
    // Throwing exception on the same case to force rollback até the main process
    TimeUnit.SECONDS.sleep(3);
    log(event, TransactionPhase.BEFORE_COMMIT);
    throw new RuntimeException("I am forcing a transaction rollback. Please ignore this Exception");
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
  public void listenAfterRollback(ExceptionEvent event) {
    log(event, TransactionPhase.AFTER_ROLLBACK);
  }

  private void log(ExceptionEvent event, TransactionPhase phase) {
    final MyEntity entity = event.getEntity();
    if (!entity.isSameThread()) {
      throw new RuntimeException(String.format("The event was originated in a other thread. "
              + "Event Thread=[%s] Current Thread=[%s]", entity.getSourceThread(),
          Thread.currentThread().getName()));
    }

    log.info("[{}] Listen {} - {}", entity.id, phase, entity);
  }

}