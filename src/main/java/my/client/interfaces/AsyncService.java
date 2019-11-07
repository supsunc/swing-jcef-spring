package my.client.interfaces;

import java.util.concurrent.Future;

public interface AsyncService {
    Future<String> asyncMethod();
}
