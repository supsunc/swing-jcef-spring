package my.client.impl;

import my.client.interfaces.AsyncService;
import my.client.interfaces.MyService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class MyServiceImpl implements MyService {
    private AsyncService asyncService;

    public MyServiceImpl(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    private Future<String> futureAsyncMethod = null;

    @Override
    public String doSomething() {
        if (futureAsyncMethod == null)
            futureAsyncMethod = asyncService.asyncMethod();

        if (futureAsyncMethod.isDone()) {
            String result = "";
            try {
                result = futureAsyncMethod.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            futureAsyncMethod = null;
            return result;
        } else {
            return "doing";
        }
    }
}
