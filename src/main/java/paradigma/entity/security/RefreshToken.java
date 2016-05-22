package paradigma.entity.security;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.io.Serializable;
import java.nio.ByteBuffer;

@Table("oauth_refresh_token")
public class RefreshToken implements Serializable {

    @PrimaryKey
    private String token_id;

    private ByteBuffer token_value;

    private ByteBuffer authentication;

    public RefreshToken(final String token_id,final ByteBuffer token_value,final ByteBuffer authentication) {
        this.token_id = token_id;
        this.token_value = token_value;
        this.authentication = authentication;
    }

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public ByteBuffer getToken_value() {
        return token_value;
    }

    public void setToken_value(ByteBuffer token_value) {
        this.token_value = token_value;
    }

    public ByteBuffer getAuthentication() {
        return authentication;
    }

    public void setAuthentication(ByteBuffer authentication) {
        this.authentication = authentication;
    }
}