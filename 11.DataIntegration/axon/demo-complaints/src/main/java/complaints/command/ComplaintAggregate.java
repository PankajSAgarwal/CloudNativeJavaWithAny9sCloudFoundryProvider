package complaints.command;

import complaints.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.util.Assert;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

//<1>
@Aggregate
public class ComplaintAggregate {
    //<2>
    @AggregateIdentifier
    private String complaintId;

    private boolean closed;

    public ComplaintAggregate() {
    }

    //<3>
    @CommandHandler
    public ComplaintAggregate(FileComplaintCommand c) {
        Assert.hasLength(c.getCompany(), "complaint should specify company name");
        Assert.hasLength(c.getDescription(),"compliant should have a description");
        apply(new ComplaintFiledEvent(c.getId(),c.getCompany(),c.getDescription()));
    }

    //<4>
    @CommandHandler
    public void resolveComplaint(CloseComplaintCommand ccc){
        if(!this.closed){
            apply(new ComplaintClosedEvent(this.complaintId));
        }
    }

    //<5>
    @CommandHandler
    public void addComment(AddCommentCommand c){
        Assert.hasLength(c.getComment(),"Comment should have a description");
        Assert.hasLength(c.getCommentId(),"Comment should have a commentid");
        Assert.hasLength(c.getComplaintId(),"comment should specify complaint id");
        Assert.hasLength(c.getUser()," comment should be from a user");
        Assert.notNull(c.getWhen(),"comment should have a timestamp");
        Assert.isTrue(!this.closed,"complaint should not be closed");
        apply(new CommentAddedEvent(c.getComplaintId(),c.getCommentId(),
                c.getComment(),c.getUser(),
                c.getWhen()));
    }

    //<6>
    @EventSourcingHandler
    protected void on(ComplaintFiledEvent cfe){
        this.complaintId=cfe.getId();
        this.closed=false;
    }

    //<7>
    @EventSourcingHandler
    protected void on(ComplaintClosedEvent cce){
        this.closed=true;
    }

}
