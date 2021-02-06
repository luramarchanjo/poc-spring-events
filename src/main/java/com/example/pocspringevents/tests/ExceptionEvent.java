package com.example.pocspringevents.tests;

import com.example.pocspringevents.model.MyEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ExceptionEvent {

  private final MyEntity entity;

}