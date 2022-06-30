package com.example.zboard.advice;

import org.springframework.web.bind.annotation.*;

@RestControllerAdvice(basePackages={"com.example.zboard.web.controller.rest", "com.example.zboard.service.rest"})
public class ZBoardRestControllerAdvice {

}
