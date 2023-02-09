package com.alex.multithreading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class MultithreadingController {

    @Autowired
    private MultithreadingService service;

    @GetMapping("/do-something")
    public CompletableFuture<String> doSomething() throws InterruptedException, ExecutionException {
        var result1 = service.doSomething1();
        var result2 = service.doSomething2();
        System.out.println("Hi! I am being displayed before result1 and result2 are received");

        service.doSomething3();
        service.doSomething3();
        service.doSomething3();
        service.doSomething3();
        System.out.println("Hi! I am being displayed before result3 is received");

        return result1.thenCombine(result2, (r1, r2) -> r1 + " and " + r2);
    }
}