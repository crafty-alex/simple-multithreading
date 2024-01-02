package com.alex.multithreading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class MultithreadingController {

    @Autowired
    private MultithreadingService service;

    @GetMapping("/do-something")
    public CompletableFuture<String> doSomething() throws InterruptedException {
        var result1 = service.doSomething1();
        var result2 = service.doSomething2();
        System.out.println("Hi! I am being displayed before Result 1 and Result 2 are received");

        service.doSomething3();
        service.doSomething3();
        service.doSomething3();
        service.doSomething3();
        System.out.println("Hi! I am being displayed before Result 3 is received");

        return result1.thenCombine(result2, (r1, r2) -> r1 + " and " + r2);
    }

    @GetMapping("/do-request-1")
    public CompletableFuture<Void> sendAsyncRequest1() {
        CompletableFuture<String> response = service.sendAsyncRequest("https://jsonplaceholder.typicode.com/todos/1");
        return response.thenAccept(result -> System.out.println("Result: " + result)); // prints the string created in the console
    }

    @GetMapping("/do-request-2")
    public CompletableFuture<String> sendAsyncRequest2() {
        CompletableFuture<String> response = service.sendAsyncRequest("https://jsonplaceholder.typicode.com/todos/1");
        return response.thenApply(result -> "Result: " + result); // returns the string created
    }

    @GetMapping("/do-request-3")
    public CompletableFuture<String> sendAsyncRequest3() {
        return service.sendAsyncRequest("https://jsonplaceholder.typicode.com/todos/1"); // returns directly the response from API
    }

    @GetMapping("/do-request-4")
    public Mono<String> sendAsyncRequest4() {
        return service.sendAsyncRequestWebClient("https://jsonplaceholder.typicode.com/todos/1");
    }

    @GetMapping("/do-request-5")
    public void sendAsyncRequest5() throws ExecutionException, InterruptedException {
        CompletableFuture<Long> asyncFactorialFuture = service.calculateFactorialAsync(5);
        asyncFactorialFuture.thenAccept(factorial -> System.out.println("@Async Factorial: " + factorial));

        Future<Long> executorServiceFactorialFuture = service.calculateFactorialWithExecutorService(5);
        System.out.println("ExecutorService Factorial: " + executorServiceFactorialFuture.get());
    }

    @GetMapping("/do-request-6")
    public void sendAsyncRequest6() throws ExecutionException, InterruptedException {
       service.processTasksWithExecutorService();
    }

     @GetMapping("/eat-calories")
    public void calories() throws ExecutionException, InterruptedException {
        CalorieCounter counter = new CalorieCounter();

        Thread eatingThread = new Thread(() -> counter.consumeCalories("donuts", 1500));
        Thread exerciseThread = new Thread(() -> counter.burnCalories("jogging", 600));

        eatingThread.start();
        exerciseThread.start();
    }
}
