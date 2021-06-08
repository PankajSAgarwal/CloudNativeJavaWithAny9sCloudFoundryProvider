package edabatch;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.batch.integration.launch.JobLaunchingGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.file.dsl.Files;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.io.File;

@Configuration
@EnableAutoConfiguration
@EnableBatchIntegration
@EnableBatchProcessing
public class EtlFlowConfiguration {
    //1
    @Bean
    IntegrationFlow etlFlow(@Value("${input-directory:${HOME}/Desktop/in}") File directory,
                            BatchChannels c, JobLauncher launcher, Job job){
        return IntegrationFlows
                .from(Files.inboundAdapter(directory).autoCreateDirectory(true),
                        cs -> cs.poller(p -> p.fixedDelay(1000)))
                .handle(File.class, (file, headers) ->{

                            String absolutePath = file.getAbsolutePath();
                            //2
                            JobParameters params = new JobParametersBuilder()
                                    .addString("file",absolutePath).toJobParameters();

                            return MessageBuilder.withPayload(new JobLaunchRequest(job,params))
                                    .setHeader(FileHeaders.ORIGINAL_FILE,absolutePath)
                                    .copyHeadersIfAbsent(headers).build();
                        })
                //3
                .handle(new JobLaunchingGateway(launcher))
                //4
                .routeToRecipients(
                        spec->spec.recipient(c.invalid(), this::notFinished)
                                .recipient(c.completed(),this::finished))
                .get();
    }

    private boolean finished(Message<?> msg){
        Object payload = msg.getPayload();
        return ((JobExecution) payload)
                .getExitStatus()
                .equals(ExitStatus.COMPLETED);
    }

    private boolean notFinished(Message<?> msg){
        return !this.finished(msg);
    }

}
