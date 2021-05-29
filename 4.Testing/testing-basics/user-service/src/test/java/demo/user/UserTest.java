package demo.user;

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
    public void setup() throws Exception{
        User user = new User("user", "Jack", "Frost", "jfrost@example.com");
        user.setId(0L);
        user.setCreatedAt(12345L);
        user.setLastModified(12346L);

        this.user = user;
    }

    @Test
    public void deserializeJson() throws Exception {
        String content = "{\"username\": \"user\", \"firstName\": \"Jack\", "
                + "\"lastName\": \"Frost\", \"email\": \"jfrost@example.com\"}";

        assertThat(this.json.parse(content)).isEqualTo(
                new User("user", "Jack", "Frost", "jfrost@example.com"));
        assertThat(this.json.parseObject(content).getUsername()).isEqualTo("user");
    }

    @Test
    public void serializeJson() throws Exception {
        // <2>
        assertThat(this.json.write(user)).isEqualTo("user.json");
        assertThat(this.json.write(user)).isEqualToJson("user.json");
        assertThat(this.json.write(user)).hasJsonPathStringValue("@.username");

        // <3>
        assertJsonPropertyEquals("@.username", "user");
        assertJsonPropertyEquals("@.firstName", "Jack");
        assertJsonPropertyEquals("@.lastName", "Frost");
        assertJsonPropertyEquals("@.email", "jfrost@example.com");
    }

    private void assertJsonPropertyEquals(String key, String value)
            throws java.io.IOException {
        assertThat(this.json.write(user)).extractingJsonPathStringValue(key)
                .isEqualTo(value);
    }


}