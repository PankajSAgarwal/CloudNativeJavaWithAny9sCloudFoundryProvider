package demo.actors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Actor {
    @Id
    @GeneratedValue
    public Long id;

    @ManyToOne
    public Movie movie;

    public String fullName;

    public Actor(String fullName,Movie movie) {
        this.movie = movie;
        this.fullName = fullName;
    }

    public Actor() {
    }


    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", movie=" + movie +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
