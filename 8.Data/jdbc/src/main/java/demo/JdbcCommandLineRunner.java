package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JdbcCommandLineRunner implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcCommandLineRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    @Override
    public void run(String... args) throws Exception {

        //1
        jdbcTemplate.execute("DROP TABLE user IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE user(id serial,first_name "
                +"VARCHAR(255), last_name VARCHAR(255), email VARCHAR(255))" );

        //2
        List<Object[]> userRecords = Stream.of("Pankaj Agarwal pagarwal@hotmail.com",
                "John Doe jdoe@yahoo.com",
                "Max Musterman mmusterman@outlook.com",
                "Jane Doe jDoe@test.com").map(name -> name.split(" "))
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate("INSERT INTO USER (first_name,last_name,email) VALUES(?,?,?)",
                userRecords);

        //3
        RowMapper<User> userRowMapper = (rs,rownum)-> new User(rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),rs.getString("email"));

        List<User> users = jdbcTemplate.query("SELECT id,first_name,last_name,email FROM user WHERE first_name= ?",
                userRowMapper,"Pankaj");

        users.forEach(user -> log.info(user.toString()));


    }
}
