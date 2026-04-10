package xyz.datt.domain.place.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.JobInstance;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.datt.global.common.ApiResponse;

@Slf4j
@RestController
@RequestMapping("/api/v1/place/admin")
@RequiredArgsConstructor
public class PlaceAdminController {
    private final JobRepository jobRepository;
    private final Job storeImportJob;
    private final TaskExecutor taskExecutor;

    @PostMapping("/sync")
    public ResponseEntity<ApiResponse<?>> syncStoreData() {
        final String jobName = storeImportJob.getName();

        try {
            log.info("상권 데이터 동기화 배치 요청 수신 [Job: {}]", jobName);

            JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

            JobInstance jobInstance = jobRepository.createJobInstance(jobName, jobParameters);
            JobExecution execution = jobRepository.createJobExecution(
                jobInstance,
                jobParameters,
                new ExecutionContext()
            );

            taskExecutor.execute(() -> {
                log.info("배치 실행 시작 [ID: {}, Job: {}]", execution.getId(), jobName);
                try {
                    storeImportJob.execute(execution);
                    log.info("배치 실행 완료 [ID: {}, Status: {}]",
                        execution.getId(), execution.getStatus());
                } catch (Exception e) {
                    log.error("배치 실행 오류 발생 [ID: {}]", execution.getId(), e);
                    execution.setStatus(BatchStatus.FAILED);
                    execution.setExitStatus(ExitStatus.FAILED);
                    jobRepository.update(execution);
                }
            });

            return ResponseEntity.accepted()
                .body(ApiResponse.success("상권 데이터 동기화 접수 완료 [ID: " + execution.getId() + "]"));
        } catch (Exception e) {
            log.error("상권 데이터 동기화 접수 실패 [Job: {}]", jobName, e);
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error("E007", "상권 데이터 동기화 접수 실패"));
        }
    }
}