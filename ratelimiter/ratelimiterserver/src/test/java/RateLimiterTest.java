import com.raviv.RateLimiter;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


public class RateLimiterTest {

    @Test
    public void shouldLimitAfter3Attempts() throws ExecutionException {
        RateLimiter rateLimiter =new RateLimiter(30,2);
        Assert.assertFalse(rateLimiter.consume("1",3));
    }

    @Test
    public void shouldNotLimitAfter1Attempts() throws ExecutionException {
        RateLimiter rateLimiter =new RateLimiter(30,2);
        Assert.assertTrue(rateLimiter.consume("1",1));
    }

    @Test
    public void shouldNotLimitAfter3AttemptsAndTimePass() throws ExecutionException, InterruptedException {
        RateLimiter rateLimiter =new RateLimiter(1,2);
        Assert.assertFalse(rateLimiter.consume("1",3));
        Thread.sleep(1500);
        Assert.assertTrue(rateLimiter.consume("1",1));
    }

    @Test
    public void shouldLimitWithMultiAccess() throws InterruptedException {
        RateLimiter rateLimiter =new RateLimiter(30,1);
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        final int[] failedRequests = {0};
        final int[] successRequests = {0};
        List<Future> futureList = new ArrayList<>();
        for (int i=0;i<100;++i) {
            futureList.add(executorService.submit( () -> rateLimiter.consume("1",1)));
        }

        futureList.forEach(x -> {
            try {
                if ((Boolean)x.get())
                    ++successRequests[0];
                else
                    ++failedRequests[0];
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });


        Assert.assertTrue(successRequests.length == 1 && successRequests[0] == 1);
        Assert.assertTrue(failedRequests.length == 1 && failedRequests[0] == 99);


    }


}
