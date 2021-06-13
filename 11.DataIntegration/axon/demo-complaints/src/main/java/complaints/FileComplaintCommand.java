package complaints;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileComplaintCommand {
    @TargetAggregateIdentifier
    private String id;//<1>
    private String company,description;//<2>
}
