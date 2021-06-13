package complaints.query;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComplaintQueryObjectRepository
        extends JpaRepository<ComplaintQueryObject,String> {

    @Override
    Optional<ComplaintQueryObject> findById(String s);
}
