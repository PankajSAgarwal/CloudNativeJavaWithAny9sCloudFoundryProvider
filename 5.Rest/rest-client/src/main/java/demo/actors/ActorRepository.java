package demo.actors;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

@RepositoryRestResource
public interface ActorRepository extends JpaRepository<Actor, Long> {
    @RestResource(path="by-movie", rel = "by-movie")
    Page<Actor> findByMovieTitleIgnoringCase(@Param("movie") String title, Pageable pageable);

    @Override
    Optional<Actor> findById(Long id);
}