---
title: SpringBoot+Shiro+Jwt填坑-3
date: 2019-09-07 18:27:28
categories: 读书笔记
tags:
- Java
---

整合SpringBoot

<!-- more -->

省去建表，数据库读取代码。

# 程序逻辑

在`SpringBoot`中使用`JWT`来做接口权限认证，安全框架依旧使用`Shiro`

1. 我们`POST`用户名与密码到`/login`进行登入，如果成功返回一个加密`token`，失败的话直接返回401错误。
2. 之后用户访问每一个需要权限的网址请求必须在`header`中添加`Authorization`字段，例如`Authorization: token`，`token`为密钥。
3. 后台会进行`token`的校验，如果不通过直接返回401。

## Token加密说明

- 携带了 `username` 信息在 token 中。
- 设定了过期时间。
- 使用用户登入密码对 `token` 进行加密。

## Token校验流程

1. 获得 `token` 中携带的 `username` 信息。
2. 进入数据库搜索这个用户，得到他的密码。
3. 使用用户的密码来检验 `token` 是否正确。

# Maven

添加依赖

```java
<dependency>
	<groupId>com.auth0</groupId>
	<artifactId>java-jwt</artifactId>
	<version>3.8.2</version>
</dependency>

<dependency>
	<groupId>org.apache.shiro</groupId>
	<artifactId>shiro-spring-boot-web-starter</artifactId>
	<version>1.4.1</version>
</dependency>
```

# JWT工具类

我们写一个简单的`JWT`加密，校验工具，并且使用用户自己的密码充当加密密钥， 这样保证了`token` 即使被他人截获也无法破解。并且我们在`token`中附带了`username`信息，并且设置密钥5分钟就会过期。

```java
@Component
public class JWTUtil {

    // 过期时间30天
    private static final long EXPIRE_TIME = 24 * 60 * 30 * 1000;

    /**
     * 校验token是否正确
     * @param token    密钥
     * @param username 登录名
     * @param password 密码
     * @return
     */
    public static boolean verify(String token, String username, String password) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(password);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名
     * @param username 用户名
     * @param secret 用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String secret) {
        Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 生成前端需要的用户信息，包括：
     * 1. token
     * 2. userInfo
     * @param userInfo
     * @return
     */
    public static Result generateUserInfo(UserInfo userInfo){
        Map<String, Object> responseBean = new HashMap<>(2);
        String token = sign(userInfo.getUsername(), userInfo.getPassword());
        responseBean.put("token", token);
        userInfo.setPassword("");
        responseBean.put("userInfo", userInfo);
        return ResultFactory.buildSuccessResult(responseBean);
    }
}
```



# 创建JWTToken替换Shiro原生Token

1. `Shiro` 原生的 `Token` 中存在用户名和密码以及其他信息 [验证码，记住我]
2. 在 `JWT` 的 `Token` 中因为**已将用户名和密码通过加密处理整合到一个加密串中**，所以只需要一个 `token` 字段即可

```java
public class JWTToken implements AuthenticationToken {

    // 密钥
    private String token;

    public JWTToken(String token) {
        if(token.contains("Bearer")){
            token = token.substring(7, token.length());
        }
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
```

# 创建JWTFilter实现前端请求统一拦截及处理

所有的请求都会先经过`Filter`，所以我们继承官方的`BasicHttpAuthenticationFilter`，并且重写鉴权的方法， 另外通过重写`preHandle`，实现跨越访问。

**代码的执行流程`preHandle->isAccessAllowed->isLoginAttempt->executeLogin`**

```java
public class JWTFilter extends BasicHttpAuthenticationFilter {

    /**
     * 前端放置在 headers 头文件中的登录标识
     * 如果用户发起的请求是需要登录才能正常返回的，那么头文件中就必须存在该标识并携带有效值
     */
    private static String LOGIN_SIGN = "Authorization";

    /**
     * 检测用户是否登录
     * 检测header里面是否包含Authorization字段即可
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;

        String authorization = req.getHeader(LOGIN_SIGN);

        return authorization != null;

    }

    //getSubject(request, response).login(token) 就是触发 Shiro Realm 自身的登录控制，具体内容需要手动实现
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        String header = req.getHeader(LOGIN_SIGN);

        JWTToken token = new JWTToken(header);

        getSubject(request, response).login(token);

        return true;
    }

    //始终返回 true 的原因是因为具体的是否登录成功的判断，需要在 Realm 中手动实现，此处不做统一判断
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
            } catch (Exception e) {
                throw new TSharkException("登录权限不足！", e);
            }
        }

        return true;
    }

    /**
     * 对跨域提供支持
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}
```

# 实现Realm

`realm`的用于处理用户是否合法的这一块，需要我们自己实现

```java
public class ShiroRealm extends AuthorizingRealm{

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * 重写Realm的supports()方法是通过JWT进行登录判断的关键
     * 因为前文中创建了JWTToken用于替换 Shiro 原生 token
     * 所以必须在此方法中显式的进行替换，否则在进行判断时会一直失败
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 执行授权逻辑
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        List<String> userPermissions = new ArrayList<>();
        userPermissions.add("user");
        info.addStringPermissions(userPermissions);
        return info;
    }

    /**
     * 执行认证逻辑
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     * 登录的合法验证通常包括 token 是否有效 、用户名是否存在 、密码是否正确
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        String username = JWTUtil.getUsername(token);

        if (StringUtils.isBlank(username)){
            throw  new CustomException(401, "token检验不通过");
        }

        UserInfo userInfo = userRepository.findByUsername(username);
        if(userInfo == null){
            throw new CustomException(404, "用户不存在");
        }

        if(!JWTUtil.verify(token, username, userInfo.getPassword())){
            throw new CustomException(401, "账户或者密码错误");
        }

        //认证成功，将用户信息封装成SimpleAuthenticationInfo
        return new SimpleAuthenticationInfo(token, token, "realm");
    }
}
```

# 配置Shiro

```java
@Configuration
public class ShiroConfig {

    @Primary
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 设置 securityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 在 Shiro过滤器链上加入 JWTFilter
        LinkedHashMap<String, Filter> filters = new LinkedHashMap<>();
        filters.put("jwt", new JWTFilter());
        shiroFilterFactoryBean.setFilters(filters);

        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 所有请求都要经过 jwt过滤器
        filterChainDefinitionMap.put("/**", "jwt");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Primary
    @Bean()
    public DefaultWebSecurityManager securityManager(ShiroRealm shiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 配置 SecurityManager，并注入 shiroRealm
        securityManager.setRealm(shiroRealm());
        return securityManager;
    }

    @Primary
    @Bean
    public ShiroRealm shiroRealm() {
        // 配置 Realm
        return new ShiroRealm();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
```

# controller

```java
@PostMapping("/login")
public Result login(@RequestBody Map<String, String> payload){
    String username = payload.get("username");
    String password = payload.get("password");
    final String errorMessage = "用户名或密码错误";

    UserInfo userInfo = userRepository.findByUsername(username);
    if(userInfo == null){
        return ResultFactory.buildFailResult(errorMessage);
    }
    if(!Argon2Util.verify(userInfo.getPassword(), password)){
        return ResultFactory.buildFailResult(errorMessage);
    }
    return JWTUtil.generateUserInfo(userInfo);
}
```





---

参考：

[SpringBoot系列 - 集成JWT实现接口权限认证 | 飞污熊博客](https://www.xncoding.com/2017/07/09/spring/sb-jwt.html)

[Shiro + JWT + Spring Boot Restful 简易教程 - 沧海月明](https://www.inlighting.org/archives/spring-boot-shiro-jwt.html)

[SpringBoot+Shiro+Vue前后端分离项目通过JWT实现自动登录 | asing1elife's blog](http://asing1elife.com/java/springboot/shiro/vue/2018/08/09/SpringBoot+Shiro%E5%89%8D%E5%90%8E%E7%AB%AF%E5%88%86%E7%A6%BB%E9%A1%B9%E7%9B%AE%E9%80%9A%E8%BF%87JWT%E5%AE%9E%E7%8E%B0%E8%87%AA%E5%8A%A8%E7%99%BB%E5%BD%95/)

----

