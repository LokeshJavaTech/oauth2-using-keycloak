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


# Keycloak Server Setup - Client Credential Grant Type Flow:
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

# Access Secured endpoints by providing OAuth2 access token - Client Credential Grant Type Flow:

Now if we access GET endpoint it will work, however any endpoint other than GET, will result in 401 Unauthorized.  
Hence, we need to pass Access Token, along with the request.  
Steps to invoke any endpoint except GET. EX: Post end point `localhost:8080/api/user`

1. Generate access token using endpoint `http://localhost:8081/realms/user_dev_realm/protocol/openid-connect/token`
2. Copy access_token value
3. Go to POST endpoint `localhost:8080/api/user` inside postman
4. Inside Authentication:
   1. Auth Type = `OAuth2`
   2. Add Authorization data to = `Request Headers`
   3. Token = `Copied access_token_value`
   4. Header prefix = `Bearer`
5. Now hit the POST endpoint, and it will become successful, and not result in 401 Unauthorized.
6. Because the token that we provided in Authorization tab, it will be added inside HEADER as:
   1. Header key = `Authorization`
   2. Header value = `Bearer <access_token_value>`

<br/>
Instead of manually creating the access token and copying into actual request, we can leverage POSTMAN to do the same task. <br/>

1. Go to POST endpoint `localhost:8080/api/user` inside postman
2. Inside Authorization:
   1. Auth Type = `OAuth2`
   2. Add Authorization data to = `Request Headers`
   3. Token = `Let it be empty`
   4. Header prefix = `Bearer`
   5. Inside "Configure New Token"
      1. Token Name = `any name`
      2. Grant Type = `Client Credentials`
      3. Access Token Url = `http://localhost:8081/realms/user_dev_realm/protocol/openid-connect/token`
      4. client id = `user_cc_clientid`
      5. client Secret = `Copy client secret from keycloak`
      6. Scope = `openid email profile`
      7. Client Authentication = `Send client credentials in Body`
3. Click "Get New Access Token"
4. Click "Use Token" --> Token will automatically be added in the Request Header as:
   1. Key = `Authorization`
   2. Value = `Bearer <Access_Token_Value>`
5. Click Send on the request, and it will become successful.


# Adding Authorization using ROLES - Client Credential Grant Type flow

1. Add ROLE inside Keycloak server:
   1. Inside `user_dev_realm` realm, go to 'Realm roles' and create role `User_CallCenter_Role`
2. Go to 'Clients', inside client id `user_cc_clientid`
3. Go to 'Service account roles' and Assign role `User_CallCenter_Role`
4. Update Micro Service SecurityConfig to check for role `User_CallCenter_Role` and add Converter to convert roles from Jwt token to Spring Security GrantedAuthority
5. Now we can check role `User_CallCenter_Role` will be coming inside the JWT token and all the microservice APIs are accessible


