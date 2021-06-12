package partition;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.batch.integration.partition.MessageChannelPartitionHandler;

@Configuration
public class LeaderStepConfiguration {

    //<1> : truncate the table NEW_PEOPLE
    @Bean
    Step stagingStep(StepBuilderFactory sbf, JdbcTemplate jdbcTemplate){
        return sbf.get("staging")
                .tasklet((contribution, chunkContext) -> {
            jdbcTemplate.execute("truncate NEW_PEOPLE");
            return RepeatStatus.FINISHED;
        }).build();

    }

    //<2>
    @Bean
    Step partitionStep(StepBuilderFactory sbf, Partitioner p,
                       PartitionHandler ph,WorkerStepConfiguration wsc){
        Step workerStep = wsc.workerStep(null);
        return sbf.get("partitionStep").partitioner(workerStep.getName(),p)
                .partitionHandler(ph).build();
    }

    //<3>
    @Bean
    MessageChannelPartitionHandler partitionHandler(
            @Value("${partition.grid-size:4}") int gridSize,
            MessagingTemplate messagingTemplate,
            JobExplorer jobExplorer){
        MessageChannelPartitionHandler ph = new MessageChannelPartitionHandler();
        ph.setMessagingOperations(messagingTemplate);
        ph.setJobExplorer(jobExplorer);
        ph.setStepName("workerStep");
        ph.setGridSize(gridSize);
        return ph;
    }

    //<4>
    @Bean
    MessagingTemplate messagingTemplate(LeaderChannels channels){
        return new MessagingTemplate(channels.leaderRequestsChannel());
    }

    //<5>
    @Bean
    Partitioner partitioner(JdbcOperations jdbcTemplate,
                            @Value("${partition.table:PEOPLE}") String table,
                            @Value("${partition.column:ID}") String column){
        return new IdRangePartitioner(jdbcTemplate,table,column);
    }



}
