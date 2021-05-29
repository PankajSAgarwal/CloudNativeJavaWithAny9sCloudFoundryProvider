package demo.user;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
public class UserTest {

    private User user;

    @Autowired
    private JacksonTester<User> json;

    @BeforeEach
    public void setUp() throws Exception {
        User user = new User("user", "Jack", "Frost", "jfrost@example.com");
        user.setId(0L);
        user.setCreatedAt(12345L);
        user.setLastModified(12346L);

        this.user = user;
    }

    @Test
    public void serializeJson() throws Exception {
        assertThat(this.json.write(user)).isEqualTo("user.json");
        assertThat(this.json.write(user)).isEqualToJson("user.json");
        assertThat(this.json.write(user)).hasJsonPathStringValue("@.username");

        assertJsonPropertyEquals("@.username", "user");
        assertJsonPropertyEquals("@.firstName", "Jack");
        assertJsonPropertyEquals("@.lastName", "Frost");
        assertJsonPropertyEquals("@.email", "jfrost@example.com");
    }

    @Test
    public void deserializeJson() throws Exception {
        String content = "{\"username\": \"user\", \"firstName\": \"Jack\", "
                + "\"lastName\": \"Frost\", \"email\": \"jfrost@example.com\"}";

        assertThat(this.json.parse(content)).isEqualTo(
                new User("user", "Jack", "Frost", "jfrost@example.com"));
        assertThat(this.json.parseObject(content).getUsername()).isEqualTo("user");
    }

    private void assertJsonPropertyEquals(String key, String value)
            throws java.io.IOException {
        assertThat(this.json.write(user)).extractingJsonPathStringValue(key)
                .isEqualTo(value);
    }

}