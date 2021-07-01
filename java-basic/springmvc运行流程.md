# springmvc运行流程

---
(DispatcherServlet.java)
doService()
```java
	@Override
protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //...
        //执行调度
        doDispatch(request, response);
        //...
}
```
doDispatch()
```java
	protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
                //...
        
				//得到处理当前请求的HandlerExecutionChain(处理程序执行链)，该对象封装了Handler对象和HandlerInterceptor对象
                //Handler是Controller的Bean本身和请求Method的包装
				mappedHandler = getHandler(processedRequest);
				//如果Handler为空，返回404
				if (mappedHandler == null) {
					noHandlerFound(processedRequest, response);
					return;
				}

				//得到处理当前请求的处理适配器HandlerAdapter
				HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

				// Process last-modified header, if supported by the handler.
                //...
                //执行拦截器的preHandle(预处理)
				if (!mappedHandler.applyPreHandle(processedRequest, response)) {
					return;
				}

				//执行真正的处理流程，处理适配器调用对应的Handler处理请求，执行controller层对应目标方法(反射调用目标方法)
				mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
				
				//...
                //设置默认视图名称
				applyDefaultViewName(processedRequest, mv);
				//执行拦截器的postHandle(后处理)
				mappedHandler.applyPostHandle(processedRequest, response, mv);
			
			    //...
            //处理调用结果
			processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);

            //...
        
	}
```

```java
	@Nullable
	protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
		if (this.handlerMappings != null) {
			for (HandlerMapping hm : this.handlerMappings) {
			    //HandlerMapping处理器映射器 找到具体的Handler处理器
				HandlerExecutionChain handler = hm.getHandler(request);
				if (handler != null) {
					return handler;
				}
			}
		}
		return null;
	}
```

(AbstractHandlerMapping.java)

```java
	@Override
	@Nullable
	public final HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        //得到Handler
		Object handler = getHandlerInternal(request);
		if (handler == null) {
			handler = getDefaultHandler();
		}
		if (handler == null) {
			return null;
		}
		// Bean name or resolved handler?
		if (handler instanceof String) {
			String handlerName = (String) handler;
			handler = obtainApplicationContext().getBean(handlerName);
		}

		//构造获取HandlerExecutionChain对象
		HandlerExecutionChain executionChain = getHandlerExecutionChain(handler, request);
		
		//...
		return executionChain;
	}
```


processDispatchResult()
```java
	private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,
			@Nullable HandlerExecutionChain mappedHandler, @Nullable ModelAndView mv,
			@Nullable Exception exception) throws Exception {
        //...
		if (exception != null) {
			if (exception instanceof ModelAndViewDefiningException) {
				mv = ((ModelAndViewDefiningException) exception).getModelAndView();
			}
			else {
				Object handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
				//如果有异常，调用processHandlerException对异常进行处理，返回异常视图
				mv = processHandlerException(request, response, handler, exception);
				errorView = (mv != null);
			}
		}

		// Did the handler return a view to render?
		if (mv != null && !mv.wasCleared()) {
		    //渲染ModelAndView
			render(mv, request, response);
			//...
		}
		//...

		if (mappedHandler != null) {
		    //调用拦截器的afterCompletion(调用完成方法)
			mappedHandler.triggerAfterCompletion(request, response, null);
		}
	}
```

render()
```java
	protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //...
		View view;
		String viewName = mv.getViewName();
		if (viewName != null) {
			//ViewReslover解析返回具体View
			view = resolveViewName(viewName, mv.getModelInternal(), locale, request);
			if (view == null) {
				throw new ServletException("Could not resolve view with name '" + mv.getViewName() +
						"' in servlet with name '" + getServletName() + "'");
			}
		}
		else {
			// No need to lookup: the ModelAndView object contains the actual View object.
            //获取视图
			view = mv.getView();
			if (view == null) {
				throw new ServletException("ModelAndView [" + mv + "] neither contains a view name nor a " +
						"View object in servlet with name '" + getServletName() + "'");
			}
		}
		//...
		try {
			if (mv.getStatus() != null) {
				response.setStatus(mv.getStatus().value());
			}
			//对视图进行渲染
			view.render(mv.getModelInternal(), request, response);
		}
		//...
	}
```

view.render()
(AbstractView.java)
render()
```java
	public void render(@Nullable Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, Object> mergedModel = createMergedOutputModel(model, request, response);
		prepareResponse(request, response);
		//渲染视图并绑定数据
		renderMergedOutputModel(mergedModel, getRequestToExpose(request), response);
	}
```

## springmvc处理流程
1.用户发送请求到DispatcherServlet。DispatcherServlet执行doService方法里边的核心方法doDispatch进行调度处理。  
2.根据HandlerMapping处理器映射器 找到具体的Handler处理器，和HandlerInterceptor对象一起封装，得到处理当前请求的HandlerExecutionChain(处理程序执行链)(Handler是Controller的Bean本身和请求Method的包装)。  
3.得到HandlerAdapter处理器适配器。
4.通过HandlerAdapter处理器适配器调用具体的Handler处理器，处理对应的请求，执行controller层对应目标方法，得到ModelAndView对象。  
5.与此同时，在调用目标方法前后，分别调用HandlerInterceptor拦截器对请求进行调用前后的处理。  
6.ViewReslover解析后返回具体View。  
7.根据模型数据渲染视图。  
8.将视图响应给用户。  

---
参考：  
https://www.cnblogs.com/xiaoxi/p/6164383.html