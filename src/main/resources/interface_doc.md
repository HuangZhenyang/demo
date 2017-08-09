## 注册
`提交注册的用户信息`
1.url: `/user/register`
2.type: `post`
3.dataType: `json`
4.发送的数据（Client->Server）：
```json
{
    "userName": "何干事",
    "password": "123456",
    "email": "667785888@qq.com",
    "address": "四里屯那个什么小胡同",
    "gender": "男"    
}
```
5.返回的数据（Server->Client)
`成功：`
```json
{
    "ok": "true"
}
```

`失败：`
```json
{
    "ok":"false",
    "reason":"邮箱已被注册"
}
```

---
---
## 登录
`提交用户登录信息`
1.url: `/user/login`
2.type: `post`
3.dataType: `json`
4.发送的数据（Client->Server）：
```json
{
    "userName": "何干事",
    "password": "123456" 
}
```
5.返回的数据（Server->Client)
`成功：`
```json
{
    "ok": "true"
}
```

`失败：`
```json
{
    "ok":"false",
    "reason":"密码错误"
}
```

---
## 主页
`服务端返回主页的相关信息`
1.url: `/user/main`
2.type: `get`
3.dataType: `json`
4.发送的数据（Client->Server）：`无`
5.返回的数据（Server->Client：
```json
{

}
```









