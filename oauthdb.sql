DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
	login text,
	password text,
	email text,
	active boolean,
	roles set<text>,
	PRIMARY KEY (login)
);
INSERT INTO users (login, password, email, active, roles) VALUES ('john', '123', 'john@mail.com', true, {'user', 'admin'});

DROP TABLE IF EXISTS clients;

CREATE TABLE IF NOT EXISTS clients (
	client_id text,
	client_secret text,
	redirect_url text,
	access_token_validity int,
	refresh_token_validity int,
	scope set<text>,
	resources_ids set<text>,
	grant_types set<text>,
	authorities set<text>,
	additional_info text,
	PRIMARY KEY (client_id)
);

INSERT INTO clients (client_id, client_secret, scope, redirect_url, access_token_validity, refresh_token_validity,
					resources_ids, grant_types, authorities, additional_info)
			VALUES ('acme', 'acmesecret', {'openid'}, 'http://localhost:8080', 30, 30,
					{}, {'authorization_code', 'refresh_token', 'password', 'client_credentials'}, {'user', 'admin'}, '');

DROP TABLE IF EXISTS oauth_access_token;

CREATE TABLE IF NOT EXISTS oauth_access_token (
	token_id text,
	token_object blob,
  	authentication_id text,
  	user_name text,
  	client_id text,
  	authentication blob,
  	refresh_token text,
  	PRIMARY KEY (token_id)
);
CREATE INDEX refreshTokenIdx ON oauth_access_token (refresh_token);
CREATE INDEX authenticationIdIdx ON oauth_access_token (authentication_id);
CREATE INDEX clientIdIdx ON oauth_access_token (client_id);
CREATE INDEX userNameIdx ON oauth_access_token (user_name);

DROP TABLE IF EXISTS oauth_refresh_token;

CREATE TABLE IF NOT EXISTS oauth_refresh_token (
  token_id text,
  token_value blob,
  authentication blob,
  PRIMARY KEY (token_id)
);
