- 获取授权码：
http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=oidc-client&scope=profile openid&redirect_uri=http://127.0.0.1:9000/receiver/code

- 授权码模式
```shell
curl --location 'http://127.0.0.1:9000/oauth2/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'Authorization: Basic b2lkYy1jbGllbnQ6c2VjcmV0' \
--header 'Cookie: JSESSIONID=2E05E4AE73692B9398BB5566FCB5AC92' \
--data-urlencode 'grant_type=authorization_code' \
--data-urlencode 'code=OfswJuqi6PKTdifl0Ve44rVA35mhST8x34KRJse_6KuQ-dHXna0iYqHz5elcdycVaLYnEIC7Dm-ESx2r_gS7Mmrc9KvPgVbmQXTel9aVPbiwmSGt0FhlwicEY9QLd9AP' \
--data-urlencode 'redirect_uri=http://127.0.0.1:9000/receiver/code'
```

- 客户端模式
```shell
curl --location 'http://127.0.0.1:9000/oauth2/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'Authorization: Basic b2lkYy1jbGllbnQ6c2VjcmV0' \
--data-urlencode 'grant_type=client_credentials'
```

- 刷新令牌
```shell
curl --location 'http://127.0.0.1:9000/oauth2/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'Authorization: Basic b2lkYy1jbGllbnQ6c2VjcmV0' \
--data-urlencode 'grant_type=refresh_token' \
--data-urlencode 'refresh_token=O9KqgmIKZSjoXCyxm2R_F1h_0kX3CNvc7AN_gofpuMY6iW7PL9EkGTA0DkvrAdWRgx0YT4aOIQbKWrMcGtDLX58RE9SmbSuAiIFagVGuO5hY55kAMfFYH3QnAcn8W2CB'
```
