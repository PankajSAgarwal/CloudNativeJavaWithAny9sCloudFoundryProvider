package complaints.query;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentQueryObjectRepository
        extends JpaRepository<CommentQueryObject,String> {

    @Override
    Optional<CommentQueryObject> findById(String s);
}
