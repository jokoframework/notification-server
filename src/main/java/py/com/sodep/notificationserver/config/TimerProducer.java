package py.com.sodep.notificationserver.config;

import java.util.Timer;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class TimerProducer {

    /**
     * @param injectionPoint
     * @return logger
     */
    @Produces
    public Timer produceLogger(InjectionPoint injectionPoint) {
        return new Timer("ProducedTimer");
    }
}
