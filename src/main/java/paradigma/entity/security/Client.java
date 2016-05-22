package paradigma.entity.security;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.io.Serializable;
import java.util.Set;

@Table("clients")
public class Client implements Serializable {

    @PrimaryKey
    private String client_id;

    private String client_secret;

    private String redirect_url;

    private int access_token_validity;

    private int refresh_token_validity;

    private Set<String> scope;

    private Set<String> resources_ids;

    private Set<String> grant_types;

    private Set<String> authorities;

    private String additional_info;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }

    public int getAccess_token_validity() {
        return access_token_validity;
    }

    public void setAccess_token_validity(int access_token_validity) {
        this.access_token_validity = access_token_validity;
    }

    public int getRefresh_token_validity() {
        return refresh_token_validity;
    }

    public void setRefresh_token_validity(int refresh_token_validity) {
        this.refresh_token_validity = refresh_token_validity;
    }

    public Set<String> getResources_ids() {
        return resources_ids;
    }

    public void setResources_ids(Set<String> resources_ids) {
        this.resources_ids = resources_ids;
    }

    public Set<String> getGrant_types() {
        return grant_types;
    }

    public void setGrant_types(Set<String> grant_types) {
        this.grant_types = grant_types;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    public Set<String> getScope() {
        return scope;
    }

    public void setScope(Set<String> scope) {
        this.scope = scope;
    }

}
