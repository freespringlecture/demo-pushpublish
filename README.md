# 스프링5 PushBuilder

### 유튜브 영상 링크
https://www.youtube.com/watch?v=1b4RI15A91k

웹페이지에 대한 요청을 응답으로 주면 응답에 들어있는 또다른 리소스들을 브라우저가 요청함  
그래서 `'/'`만 요청했는데 요청이 여러개가 감

Servlet4 에 포함되어 있으며 Spring5에서부터 HTTP2에서만 사용이 가능함
HTTP2를 사용하려면 HTTPS를 설정해야함
HTTP2가 아니면 PushBuilder는 null을 반환함

#### keystore.jks 파일 생성
```bash
keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.jks -validity 4000
```

#### https로만 접근 가능하도록 설정
```properties
server.ssl.key-store=keystore.jks
server.ssl.key-store-password=111111
server.ssl.enabled=true
```

#### http2 활성화
```properties
server.http2.enabled=true
```

#### SimpleController 
필요한 리소스를 서버에서 미리 Push로 보내서 로딩 지연시간을 감소시킴  
Push를 하면 브라우저에서 Push를 받음 Network - Initiator 텝에서 확인 가능

```java
@Controller
public class SimpleController {

    @GetMapping("/")
    public String index(Model model, PushBuilder pushBuilder) {
        model.addAttribute("name", "freelife");
        model.addAttribute("message", "여러분 반갑습니다!!");
        /**
         * <script src="/webjars/jquery/3.3.1/dist/jquery.js"></script>
         *     <script src="/webjars/bootstrap/4.2.1/js/bootstrap.js"></script>
         *     <link rel="stylesheet" href="/webjars/bootstrap/4.2.1/css/bootstrap.min.css">
         */
        pushBuilder.path("/webjars/jquery/3.3.1/dist/jquery.js").push();
        pushBuilder.path("/webjars/bootstrap/4.2.1/js/bootstrap.js").push();
        pushBuilder.path("/webjars/bootstrap/4.2.1/css/bootstrap.min.css").push(); 

        return "index";
    }
}
```

#### index.html
Thymeleaf로 inline으로 메세지 작성
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Title</title>
    <script src="/webjars/jquery/3.3.1/dist/jquery.js"></script>
    <script src="/webjars/bootstrap/4.2.1/js/bootstrap.js"></script>
    <link rel="stylesheet" href="/webjars/bootstrap/4.2.1/css/bootstrap.min.css">
</head>
<body>
<h1>Without PushBuilder</h1>
<h2 th:text="|${name}: ${message}|">Message</h2>
</body>
</html>
```

#### PushBuilder Filter 생성
Trans Parent 하게 처리하기 위해서 팀에게 Servlet Filter 또는 Spring handler Interceptor 를 만들어 달라고 요청  
아래의 코드는 리소스를 알고 있어야하고 Handler가 알아야 되서 매우 침투적임
비 침투적인 코드를 위해 PushBuilder를 Filter로 적용하여야 함
```java
public class PushBuilderFilter extends Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        PushBuilder pushBuilder = servletRequest.newPushBuilder();
        //wrapping
        chain.doFilter(request, response);
        // TODO: HTML 응답 본문 가져오기
        // TODO: 추가로 가져올 리소스 분석
        // TODO: PushBuilder로 보내기
    }
}
```