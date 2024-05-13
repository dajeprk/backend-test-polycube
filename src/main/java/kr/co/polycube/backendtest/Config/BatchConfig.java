package kr.co.polycube.backendtest.Config;

import kr.co.polycube.backendtest.Domain.Lotto;
import kr.co.polycube.backendtest.Domain.Winner;
import kr.co.polycube.backendtest.Service.LottoService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private LottoService lottoService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public Step processWinnersStep() {
        return new StepBuilder("processwinnersstep", jobRepository)
                .<Lotto, Winner>chunk(10, transactionManager)
                .reader(lottoService.lottoItemReader())
                .processor(lottoService.lottoItemProcessor())
                .writer(lottoService.winnerItemWriter())
                .build();
    }

    @Bean
    public Job processWinnersJob() {
        return new JobBuilder("processWinnersJob", jobRepository)
                .start(processWinnersStep())
                .build();
    }
}
