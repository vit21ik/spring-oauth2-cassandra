package paradigma.entity.security;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.io.Serializable;
import java.nio.ByteBuffer;

@Table("oauth_access_token")
public class AccessToken implements Serializable {

    @PrimaryKey
    private String token_id;

    private ByteBuffer token_object;

    private String authentication_id;

    private String user_name;

    private String client_id;

    private ByteBuffer authentication;

    private String refresh_token;

    public AccessToken(final String token_id, final ByteBuffer token_object, final String authentication_id, final String user_name,
                       final String client_id, final ByteBuffer authentication, final String refresh_token) {
        this.token_id = token_id;
        this.token_object = token_object;
        this.authentication_id = authentication_id;
        this.user_name = user_name;
        this.client_id = client_id;
        this.authentication = authentication;
        this.refresh_token = refresh_token;
    }

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public ByteBuffer getToken_object() {
        return token_object;
    }

    public void setToken_object(ByteBuffer token_object) {
        this.token_object = token_object;
    }

    public String getAuthentication_id() {
        return authentication_id;
    }

    public void setAuthentication_id(String authentication_id) {
        this.authentication_id = authentication_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public ByteBuffer getAuthentication() {
        return authentication;
    }

    public void setAuthentication(ByteBuffer authentication) {
        this.authentication = authentication;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
