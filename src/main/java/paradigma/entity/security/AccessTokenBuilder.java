package paradigma.entity.security;

import java.nio.ByteBuffer;

public class AccessTokenBuilder {
    private String token_id;
    private ByteBuffer token_object;
    private String authentication_id;
    private String user_name;
    private String client_id;
    private ByteBuffer authentication;
    private String refresh_token;

    public AccessTokenBuilder tokenId(String token_id) {
        this.token_id = token_id;
        return this;
    }

    public AccessTokenBuilder tokenObject(ByteBuffer token_object) {
        this.token_object = token_object;
        return this;
    }

    public AccessTokenBuilder authenticationId(String authentication_id) {
        this.authentication_id = authentication_id;
        return this;
    }

    public AccessTokenBuilder userName(String user_name) {
        this.user_name = user_name;
        return this;
    }

    public AccessTokenBuilder clientId(String client_id) {
        this.client_id = client_id;
        return this;
    }

    public AccessTokenBuilder authentication(ByteBuffer authentication) {
        this.authentication = authentication;
        return this;
    }

    public AccessTokenBuilder refreshToken(String refresh_token) {
        this.refresh_token = refresh_token;
        return this;
    }

    public AccessToken build() {
        return new AccessToken(token_id, token_object, authentication_id, user_name, client_id, authentication, refresh_token);
    }
}