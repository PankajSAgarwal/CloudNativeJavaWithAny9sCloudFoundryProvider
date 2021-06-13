package complaints;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value="/complaints",
consumes = APPLICATION_JSON_VALUE,
produces = APPLICATION_JSON_VALUE)
public class ComplaintsRestController {
    private final CommandGateway cg;

    @Autowired
    public ComplaintsRestController(CommandGateway cg) {
        this.cg = cg;
    }
    //<1>
    @PostMapping
    CompletableFuture<ResponseEntity<?>> createComplaint(@RequestBody Map<String,String> body){
        String id = UUID.randomUUID().toString();
        FileComplaintCommand complaint = new FileComplaintCommand(id, body.get("company"), body.get("description"));

        return this.cg.send(complaint).thenApply(complaintId->{
            URI uri = uri("/complaints/{id}", Collections.singletonMap("id",complaint.getId()));
            return ResponseEntity.created(uri).build();
        });

    }

    //<2>
    @PostMapping("/{complaintId}/comments")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    CompletableFuture<ResponseEntity<?>> addComment(@PathVariable String complaintId,
                                                    @RequestBody Map<String,Object> body){

        Long when = Long.class.cast(body.getOrDefault("when",
                System.currentTimeMillis()));

        AddCommentCommand command = new AddCommentCommand(complaintId,UUID.randomUUID().toString(),
                String.class.cast(body.get("comment")),
                String.class.cast(body.get("user")), new Date(when));

        return this.cg.send(command).thenApply(commentId->{
            Map<String,String> parms = new HashMap<>();
            parms.put("complaintId", complaintId);
            parms.put("commentId",command.getCommentId());

            URI uri = uri("/complaints/{complaintId}/comments/{commentId}",parms);

            return ResponseEntity.created(uri).build();
        });
    }

    @DeleteMapping("/{complaintId}")
    CompletableFuture<ResponseEntity<?>> closeComplaint(@PathVariable String complaintId){
        CloseComplaintCommand csc = new CloseComplaintCommand(complaintId);
        return this.cg.send(csc).thenApply(none->ResponseEntity.notFound().build());


    }

    private static URI uri(String uri, Map<String,String> template){
        UriComponents uriComponents = UriComponentsBuilder.newInstance().path(uri).build().expand(template);
        return uriComponents.toUri();
    }
}
