# oauth2-using-keycloak
Implementing OAuth - Authorization using OAuth2 &amp; Authentication using OIDC with Keycloak Server

# API Endpoints:
POST endpoint:
`localhost:8080/api/user`
<br/>
```
{  
    "id" : 1,
    "name" : "Lokesh Mittal",  
    "mobileNumber" : "12312312312",  
    "city" : "Noida"  
}
```

GET endpoint: `localhost:8080/api/user`
<br/>

DELETE endpoint: `localhost:8080/api/user/1`


# Keycloak Server Setup:
1. Setup keycloak server using command:
   `docker run -p 8081:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:26.1.2 start-dev`
2. login using username 'admin' and password 'admin'
3. Create Realm: user_dev_realm
4. Create client with client_id: user_cc_clientid
5. Copy "Client Secret" <br/>
6. Test keycloak server, with POST Endpoint: 
`http://localhost:8081/realms/user_dev_realm/protocol/openid-connect/token` <br/>
Body: x-www-form-urlencoded: <br/>
```
grant_type = client_credentials
client_id = user_cc_clientid
client_secret = <copied client secret>
scope = openid email profile
```
Output should be like below: <br/>
```
{
    "access_token": "aaaaa.bbbbb.ccccc",
    "expires_in": 299,
    "refresh_expires_in": 0,
    "token_type": "Bearer",
    "id_token": "xxxxx.yyyyy.zzzzz",
    "not-before-policy": 0,
    "scope": "openid email profile"
}
```



