package complaints;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCommentCommand {
    @TargetAggregateIdentifier
    private String complaintId; //<1>

    private String commentId,comment,user;

    private Date when;
}
