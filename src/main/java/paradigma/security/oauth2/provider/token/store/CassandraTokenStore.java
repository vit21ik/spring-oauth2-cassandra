package paradigma.security.oauth2.provider.token.store;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import paradigma.entity.security.AccessToken;
import paradigma.entity.security.AccessTokenBuilder;
import paradigma.entity.security.RefreshToken;
import paradigma.entity.security.RefreshTokenBuilder;
import paradigma.repository.security.AccessTokenRepository;
import paradigma.repository.security.RefreshTokenRepository;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created on 5/22/16.
 */
@Component
public class CassandraTokenStore implements TokenStore {

    private static final Log LOG = LogFactory.getLog(CassandraTokenStore.class);

    private AccessTokenRepository tokenRepository;

    private RefreshTokenRepository refreshTokenRepository;

    private CassandraOperations cassandraOperations;

    @Autowired
    public void setTokenRepository(AccessTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Autowired
    public void setRefreshTokenRepository(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Autowired
    public void setCassandraOperations(CassandraOperations cassandraOperations) {
        this.cassandraOperations = cassandraOperations;
    }

    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

    protected OAuth2AccessToken deserializeAccessToken(byte[] token) {
        return SerializationUtils.deserialize(token);
    }

    protected OAuth2RefreshToken deserializeRefreshToken(byte[] token) {
        return SerializationUtils.deserialize(token);
    }

    protected OAuth2Authentication deserializeAuthentication(byte[] authentication) {
        return SerializationUtils.deserialize(authentication);
    }

    protected byte[] serializeAccessToken(OAuth2AccessToken token) {
        return SerializationUtils.serialize(token);
    }

    protected byte[] serializeRefreshToken(OAuth2RefreshToken token) {
        return SerializationUtils.serialize(token);
    }

    protected byte[] serializeAuthentication(OAuth2Authentication authentication) {
        return SerializationUtils.serialize(authentication);
    }

    protected String extractTokenKey(String value) {
        if (value == null) {
            return null;
        }
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
        }

        try {
            byte[] bytes = digest.digest(value.getBytes("UTF-8"));
            return String.format("%032x", new BigInteger(1, bytes));
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
        }
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return readAuthentication(token.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        OAuth2Authentication authentication = null;
        final AccessToken auth2AccessToken = tokenRepository.findOne( extractTokenKey(token) );
        try {
            authentication = deserializeAuthentication(auth2AccessToken.getAuthentication().array());
        } catch (IllegalArgumentException e) {
            LOG.warn("Failed to deserialize authentication for " + token, e);
            removeAccessToken(token);
        }
        return authentication;
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        String refreshToken = null;
        if (token.getRefreshToken() != null) {
            refreshToken = token.getRefreshToken().getValue();
        }
        if (readAccessToken(token.getValue())!=null) {
            removeAccessToken(token.getValue());
        }
        final AccessTokenBuilder accessTokenBuilder = new AccessTokenBuilder();
        tokenRepository.save(accessTokenBuilder
                .tokenId( extractTokenKey(token.getValue()) )
                .tokenObject(ByteBuffer.wrap(serializeAccessToken(token)))
                .authenticationId(authenticationKeyGenerator.extractKey(authentication))
                .userName(authentication.isClientOnly() ? null : authentication.getName())
                .clientId(authentication.getOAuth2Request().getClientId())
                .authentication(ByteBuffer.wrap(serializeAuthentication(authentication)))
                .refreshToken( extractTokenKey(refreshToken) )
                .build());
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        final AccessToken auth2AccessToken = tokenRepository.findOne( extractTokenKey(tokenValue) );
        return auth2AccessToken != null ? deserializeAccessToken(auth2AccessToken.getToken_object().array()) : null;
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        removeAccessToken(token.getValue());
    }

    public void removeAccessToken(String tokenValue) {
        tokenRepository.delete( extractTokenKey(tokenValue) );
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        refreshTokenRepository.save(new RefreshTokenBuilder()
                .tokenId( extractTokenKey(refreshToken.getValue()) )
                .tokenValue(ByteBuffer.wrap(serializeRefreshToken(refreshToken)))
                .authentication(ByteBuffer.wrap(serializeAuthentication(authentication)))
                .build());
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        final RefreshToken refreshToken = refreshTokenRepository.findOne( extractTokenKey(tokenValue) );
        return refreshToken != null ? deserializeRefreshToken( refreshToken.getToken_value().array() ) : null;
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return readAuthenticationForRefreshToken(token.getValue());
    }

    public OAuth2Authentication readAuthenticationForRefreshToken(String tokenValue) {
        final RefreshToken refreshToken = refreshTokenRepository.findOne( extractTokenKey(tokenValue) );
        return refreshToken != null ? deserializeAuthentication( refreshToken.getAuthentication().array() ) : null;
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        removeRefreshToken(token.getValue());
    }

    public void removeRefreshToken(final String tokenValue) {
        refreshTokenRepository.delete( extractTokenKey(tokenValue) );
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        removeAccessTokenUsingRefreshToken(refreshToken.getValue());
    }

    public void removeAccessTokenUsingRefreshToken(final String refreshTokenValue) {
        final Select select = QueryBuilder.select("token_object").from("oauth_access_token");
        select.where(QueryBuilder.eq("refresh_token", extractTokenKey(refreshTokenValue) ));
        final List<ByteBuffer> tokens = cassandraOperations.queryForList(select, ByteBuffer.class);
        tokens.forEach(t -> {
            final OAuth2AccessToken accessToken = deserializeAccessToken(t.array());
            tokenRepository.delete( accessToken.getValue() );
        });
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        OAuth2AccessToken accessToken = null;

        String key = authenticationKeyGenerator.extractKey(authentication);

        final Select select = QueryBuilder.select("token_object").from("oauth_access_token");
        select.where(QueryBuilder.eq("authentication_id", key));
        final ByteBuffer token = cassandraOperations.queryForObject(select, ByteBuffer.class);
        if (token != null) {
            accessToken = deserializeAccessToken(token.array());
            if (accessToken != null && !key.equals(authenticationKeyGenerator.extractKey(readAuthentication(accessToken.getValue())))) {
                removeAccessToken(accessToken.getValue());
                storeAccessToken(accessToken, authentication);
            }
        }
        return accessToken;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        final Select select = QueryBuilder.select("token_object").from("oauth_access_token");
        select.where(QueryBuilder.eq("client_id", clientId)).and(QueryBuilder.eq("user_name", userName));
        return cassandraOperations.queryForList(select, ByteBuffer.class).stream().map( it -> deserializeAccessToken(it.array()) ).collect(Collectors.toList());
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        final Select select = QueryBuilder.select("token_object").from("oauth_access_token");
        select.where(QueryBuilder.eq("client_id", clientId));
        return cassandraOperations.queryForList(select, ByteBuffer.class).stream().map( it -> deserializeAccessToken(it.array()) ).collect(Collectors.toList());
    }

}
