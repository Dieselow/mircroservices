package fr.esgi.trading.infrastructure.web;

import fr.esgi.logger.ESLogger;
import fr.esgi.trading.domain.Job;
import fr.esgi.trading.domain.JobRepository;
import fr.esgi.trading.domain.JobStatus;
import fr.esgi.trading.infrastructure.messaging.RabbitMQSender;
import fr.esgi.trading.infrastructure.messaging.keys.UserQueueKey;
import fr.esgi.trading.infrastructure.web.dto.JobActionDTO;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController()
public class JobController {

    private final JobRepository jobRepo;
    private final ESLogger logger;

    @Autowired
    private RabbitMQSender rabbitMQSender;

    JobController(JobRepository jobRepo, ESLogger logger){
        this.jobRepo = jobRepo;
        this.logger = logger;
    }

    @GetMapping("{id}")
    public ResponseEntity<Job> get(@PathVariable int id){
        Optional<Job> jobDB = jobRepo.findById(id);

        if(!jobDB.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "couldn't find job");
        }

        return new ResponseEntity<>(jobDB.get(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Job> createJob(@RequestBody Job job) {

        job.setStatus(JobStatus.OPEN);
        job.setWorker(-1);

        Job postDB = jobRepo.save(job);

        JSONObject body = new JSONObject();
        body.appendField("id", job.getCreator());
        rabbitMQSender.sendToUser(UserQueueKey.INCR_POSTED, body);
        logger.log("created job: " + job.getTitle() + " " + job.getDescription());


        return new ResponseEntity<>(postDB, HttpStatus.CREATED);
    }

    @PutMapping("{id}/accept")
    public ResponseEntity<Job> acceptJob(@PathVariable int id, @RequestBody JobActionDTO dto){

        Optional<Job> jobDB = jobRepo.findById(id);

        if(!jobDB.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "couldn't find job");
        }
        Job job = jobDB.get();

        job.setStatus(JobStatus.STARTED);
        job.setWorker(dto.getUserID());

        Job updatedJobDB = jobRepo.save(job);

        JSONObject body = new JSONObject();
        body.appendField("id", dto.getUserID());
        rabbitMQSender.sendToUser(UserQueueKey.INCR_ACCEPTED, body);

        return new ResponseEntity<>(updatedJobDB, HttpStatus.OK);
    }

    @PutMapping("{id}/finish")
    public ResponseEntity<Job> finishJob(@PathVariable int id, @RequestBody JobActionDTO dto){

        Optional<Job> jobDB = jobRepo.findById(id);

        if(!jobDB.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "couldn't find job");
        }

        Job job = jobDB.get();

        if(dto.getUserID() != job.getWorker()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you can't finish a job you havn't started");
        }

        job.setStatus(JobStatus.FINISHED);

        Job updatedJobDB = jobRepo.save(job);

        JSONObject body = new JSONObject();
        body.appendField("id", dto.getUserID());
        rabbitMQSender.sendToUser(UserQueueKey.INCR_FINISHED, body);

        return new ResponseEntity<>(updatedJobDB, HttpStatus.OK);
    }
}
