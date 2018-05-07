>在整合Spring Security 及 Thymeleaf 时遇到点问题， 下面都有记录


# pom配置文件
```
<dependency

> 引用块内容

    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<!-- https://mvnrepository.com/artifact/org.thymeleaf.extras/thymeleaf-extras-springsecurity4 -->
<!-- 没有这个依赖html页面无法使用sec标签 -->
<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-springsecurity4</artifactId>
    <version>3.0.2.RELEASE</version>
</dependency>

```

# 从一个简单的demo开始

# 后台代码Config类部分，Controller类略
``` java
/**
 * @author mengqa
 * @create 2018-05-07 14:15
 **/
@EnableWebSecurity // 开启Security
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/fonts/**", "/index").permitAll() // 都可以访问
                .antMatchers("/users/**").hasRole("ADMIN") // 需要相关的角色才能访问
                .and()
                .formLogin()
                .loginPage("/login").failureUrl("/login-error"); // 自定义登录页面
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
       auth.inMemoryAuthentication() // 内存中
                .withUser("mqa").password("{noop}123456").roles("ADMIN");
    }

}

```

# 前台代码

index.html : 
``` html 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity4">
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

<div th:replace="~{common/header :: header}"></div>

<div>

    <div sec:authorize="isAuthenticated()">
        <p>已有用户登录</p>
        <p>登录者:<span sec:authentication="name"></span></p>
        <p>角色:<span sec:authentication="principal.authorities"></span></p>
    </div>
    <div sec:authorize="isAnonymous()">
        <p>未有用户登录</p>
    </div>
</div>

<div th:replace="~{common/footer :: footer}"></div>

</body>
</html>

```

header.html : 

``` html 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity4">
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

<div th:fragment="header">
    <h1>权限测试</h1>
    <a href="/" th:href="@{~/index}">首页</a>

    <div sec:authorize="isAuthenticated()">
        登录者:<span sec:authentication="name"></span>
        <form action="/logout" th:action="@{/logout}" method="post">
            <input type="submit" value="退出"/>
        </form>
    </div>
    <div sec:authorize="isAnonymous()">
        <a href="/login" th:href="@{~/login}">登录</a>
    </div>
</div>

</body>
</html>

```

login.html : 

```
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity4">
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

<div th:replace="~{common/header :: header}"></div>

<h3>登录</h3>

<form th:action="@{~/login}" method="POST">
    用户名 ： <br>
    <input type="text" id="username" name="username" />
    <br>
    密码： <br>
    <input type="text" id="password" name="password" />
    <br>
    <button type="submit">登录</button>
    <div>
        <div th:if="${loginError}">
            <p th:text="${errorMsg}"></p>
        </div>
    </div>
</form>

<div th:replace="~{common/footer :: footer}"></div>
</body>
</html>

```

# 遇到问题

> 1.使用正确的用户名登录会报错 ：spring security 5 There is no PasswordEncoder mapped for the id "null" 错误

```
是因为spring security 升级到了5.0版本问题, 
要求设置密码时需要这样设置


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
       auth.inMemoryAuthentication() // 内存中
                .withUser("mqa").password("{noop}123456").roles("ADMIN");
    }

没有 {noop} 会报错, 大概意思就是为了更加安全，所以就需要添加这个类型，
原文地址: https://www.cnblogs.com/majianming/p/7923604.html
```

>2 sec:标签  html里要用的话必须注意的是,  注意结尾是springsecurity4 , 不是3
```
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity4">
```

同时pom里是这段

``` xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<!-- https://mvnrepository.com/artifact/org.thymeleaf.extras/thymeleaf-extras-springsecurity4 -->
<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-springsecurity4</artifactId>
    <version>3.0.2.RELEASE</version>
</dependency>
```

这样下面这段代码就可用了
``` html

<div sec:authorize="isAuthenticated()">
    <p>已有用户登录</p>
    <p>登录者:<span sec:authentication="name"></span></p>
    <p>角色:<span sec:authentication="principal.authorities"></span></p>
</div>
<div sec:authorize="isAnonymous()">
    <p>未有用户登录</p>
</div>
```