package demo.actors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Override
    Optional<Movie> findById(Long id);
}