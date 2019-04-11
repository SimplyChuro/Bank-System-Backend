package actors;

import javax.inject.Named;
import javax.inject.Inject;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;
import services.DataGenerator;

import java.util.concurrent.TimeUnit;

public class GeneratorActor {

	private final ActorSystem actorSystem;
    private final ExecutionContext executionContext;

    @Inject
    public GeneratorActor(ActorSystem actorSystem, ExecutionContext executionContext) {
        this.actorSystem = actorSystem;
        this.executionContext = executionContext;

        this.initialize();
    }

    private void initialize() {
    	this.actorSystem.scheduler().schedule(
            Duration.create(1, TimeUnit.MINUTES),
            Duration.create(7, TimeUnit.DAYS),
            () -> generate()
            ,
            this.executionContext
        );
    }
    
    private void generate() {
    	DataGenerator data = new DataGenerator();
        data.generate();
    }

}
