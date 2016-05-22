package paradigma.entity.security;

import java.nio.ByteBuffer;

public class RefreshTokenBuilder {
    private String token_id;
    private ByteBuffer token_value;
    private ByteBuffer authentication;

    public RefreshTokenBuilder tokenId(String token_id) {
        this.token_id = token_id;
        return this;
    }

    public RefreshTokenBuilder tokenValue(ByteBuffer token_value) {
        this.token_value = token_value;
        return this;
    }

    public RefreshTokenBuilder authentication(ByteBuffer authentication) {
        this.authentication = authentication;
        return this;
    }

    public RefreshToken build() {
        return new RefreshToken(token_id, token_value, authentication);
    }
}