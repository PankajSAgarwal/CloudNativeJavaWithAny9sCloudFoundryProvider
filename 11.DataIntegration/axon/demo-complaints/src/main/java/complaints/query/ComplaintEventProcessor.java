package complaints.query;

import complaints.CommentAddedEvent;
import complaints.ComplaintClosedEvent;
import complaints.ComplaintFiledEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;

@Component
public class ComplaintEventProcessor {
    private final ComplaintQueryObjectRepository complaints;
    private final CommentQueryObjectRepository comments;

    //<1>
    public ComplaintEventProcessor(ComplaintQueryObjectRepository complaints,
                                   CommentQueryObjectRepository comments) {
        this.complaints = complaints;
        this.comments = comments;
    }

    private static void accept(ComplaintQueryObject complaintQueryObject1) {
        complaintQueryObject1.setClosed(true);
    }

    @EventHandler
    public void on(ComplaintFiledEvent cfe){
        ComplaintQueryObject complaint = new ComplaintQueryObject(cfe.getId(),
                cfe.getComplaint(),cfe.getCompany(), Collections.emptySet(),false);

        this.complaints.save(complaint);
    }

    @EventHandler
    public void on(CommentAddedEvent cae){
        Optional<ComplaintQueryObject> complaint = this.complaints.findById(cae.getComplaintId());
        CommentQueryObject comment = new CommentQueryObject(complaint.orElseThrow(), cae.getCommentId(), cae.getComment(), cae.getUser(), cae.getWhen());
        this.comments.save(comment);
    }

    @EventHandler
    public void on(ComplaintClosedEvent cce){
        Optional<ComplaintQueryObject> complaintQueryObject = this.complaints.findById(cce.getComplaintId());
        complaintQueryObject.ifPresent(new Consumer<ComplaintQueryObject>() {
            @Override
            public void accept(ComplaintQueryObject complaintQueryObject1) {
                complaintQueryObject1.setClosed(true);
            }
        });
        this.complaints.save(complaintQueryObject.orElseThrow());

    }
}
