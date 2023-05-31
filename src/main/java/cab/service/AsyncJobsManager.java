package cab.service;

import cab.response.SimpleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
@EnableScheduling
public class AsyncJobsManager {

    private final ConcurrentMap<String, SimpleResponse> mapOfJobs;

    public AsyncJobsManager() {
        mapOfJobs = new ConcurrentHashMap<String, SimpleResponse>();
    }

    public void putJob(String jobId, SimpleResponse response) {
        mapOfJobs.put(jobId, response);
    }

    public SimpleResponse getJob(String jobId) {
        return mapOfJobs.get(jobId);
    }

    public void removeJob(String jobId) {
        mapOfJobs.remove(jobId);
    }

    //@Scheduled(cron="*/60 * * * * *")
    public void printJobs() {
        log.info("Printing all keys and values of ConcurrentHashMap");
        for (Map.Entry<String, SimpleResponse> entry : mapOfJobs.entrySet()) {
            String key = entry.getKey().toString();
            log.info("key={} value={}", entry.getKey().toString(), entry.getValue());
        }
    }
}
