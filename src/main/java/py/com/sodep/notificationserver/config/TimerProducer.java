package py.com.sodep.notificationserver.config;

import java.util.Timer;
import javax.enterprise.inject.Produces;

public class TimerProducer {

    @Produces
    public Timer produceLogger() {
        return new Timer("ProducedTimer");
    }
}
