# nginx 详解

--
## nginx 常用命令
```shell
# 查看版本号
./nginx -v
# 启动
./nginx
# 指定配置文件启动
./nginx -c /usr/local/nginx/sbin/nginx.conf
# 重载配置文件
./nginx -s relaod
# 安全退出
./nginx -s quit
# 强制退出
./nginx -s stop
# 查看进程
ps -ef|grep nginx
# 查看占用端口
netstat -tlnp|grep ${pid} # pid 进程号
```

## 配置文件
nginx.conf  
1、全局块：配置影响nginx全局的指令。一般有运行nginx服务器的用户组，nginx进程pid存放路径，日志存放路径，配置文件引入，允许生成worker process数等。  
2、events块：配置影响nginx服务器或与用户的网络连接。有每个进程的最大连接数，选取哪种事件驱动模型处理连接请求，是否允许同时接受多个网路连接，开启多个网络连接序列化等。  
3、http块：可以嵌套多个server，配置代理，缓存，日志定义等绝大多数功能和第三方模块的配置。如文件引入，mime-type定义，日志自定义，是否使用sendfile传输文件，连接超时时间，单连接请求数等。  
4、server块：配置虚拟主机的相关参数，一个http中可以有多个server。  
5、location块：配置请求的路由，以及各种页面的处理情况。  
6.upstream: 定义一组 HTTP服务器，用于负载均衡匹配到upstream指定服务器。  
```conf
# 全局块
worker_processes  1;
# envents块
events {
    worker_connections  1024;
}
# http块
http {
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
    keepalive_timeout  65;
    
    # server块
    server {
        listen       80;
        server_name  localhost;
        
        # upstream块
        upstream test {
            server 192.168.1.102;
        }
        
        # location块
        location / {
            root   html;
            index  index.html index.htm;
        }

        #location ~ \.php$ {
        #    proxy_pass   http://127.0.0.1;
        #}
    }
}
```

location匹配规则
```
location [ = | ~ | ~* | ^~ | @ | / ] uri {

}
```
`=` 精确匹配;  
`~` 正则匹配，区分大小写;  
`~*` 正则匹配，忽略大小写;  
`^~` 带参前缀匹配，匹配符合以后，停止往下搜索正则;  
`@` nginx内部跳转 跳转到指定规则;  
`/uri` 普通前缀匹配，优先级低于带参数前缀匹配;  
`/` 任何没有匹配成功的，都会匹配这里处理;  

正则匹配会根据匹配顺序，找到第一个匹配的正则表达式后将停止搜索。普通字符串匹配则无视顺序，只会选择最精确的匹配。  

alias——别名配置，用于访问文件系统，在匹配到location配置的URL路径后，指向alias配置的路径;  
root——根路径配置，用于访问文件系统，在匹配到location配置的URL路径后，指向root配置的路径，并把请求路径附加到其后;  
proxy_pass——反向代理配置，用于代理请求;  

upstream:  
轮询、权重、IP散列这三种分配方式。

默认轮询
```
# 默认轮询
upstream name {
    server 192.168.1.11:8080;
    server 192.168.1.12:8080;
    server 192.168.1.13:8080;
}
# 权重
upstream name {
    server 192.168.1.11:8080 weight=1;
    server 192.168.1.12:8080 weight=1;
    server 192.168.1.13:8080 weight=3;
}
# ip_hash
# 根据ip生成hash结果分配，可以解决 session 一致问题
upstream name {
    ip_hash;
    server 192.168.1.11:8080;
    server 192.168.1.12:8080;
    server 192.168.1.13:8080;
}
```

参考：  
https://www.jianshu.com/p/38810b49bc29