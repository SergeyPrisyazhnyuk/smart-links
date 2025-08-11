package token;

import org.junit.jupiter.api.Test;
import ru.otus.authservice.token.JwtTokenProvider;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JwtTokenProviderTest {

    private final JwtTokenProvider provider = new JwtTokenProvider();

    @Test
    void validTokenGenerationAndValidationTest() {
        String token = provider.generateToken("test-user");
        assertTrue(provider.validateToken(token));
    }

    @Test
    void invalidTokenTest() {
        assertFalse(provider.validateToken("invalid-token"));
    }
}
