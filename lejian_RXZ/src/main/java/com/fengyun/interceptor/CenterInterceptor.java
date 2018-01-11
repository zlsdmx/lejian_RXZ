package com.fengyun.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fengyun.exception.EServerException;
import com.fengyun.exception.ServiceException;
import com.fengyun.utils.json.HtmlUtil;

/**
 * 中心拦截器
 * 日志记录
 */
public class CenterInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)    
	        throws Exception {   
		//访问日志
//		String userId = "";
//		Object session = request.getSession().getAttribute(SessionUtils.SESSION_USERID);
//		if(session != null)
//			userId = String.valueOf(session);
//		LogManager.visit(request.getRequestURL().toString(), request.getRemoteAddr(),userId );
		return true;    
	}   
	
	@Override
    public void postHandle(    
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)    
            throws Exception {    
    }    
    
    @Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
    	//拦截业务异常信息
    	if(ex != null){
    		int status = EServerException.OK.status;
			String msg = "";
			if (ex instanceof ServiceException) {
				msg = ((ServiceException)ex).getMsg();
				status = ((ServiceException)ex).getStatus();
			}else {
				//服务器内部错误
				status = EServerException.ServerError.status;
				msg = ex.toString();
			}
			//logger(request, handler, ex);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("status", status);
			result.put("msg", msg);
			result.put("data", null);
			HtmlUtil.writerJson(response, result);
		}else{
			super.afterCompletion(request, response, handler, ex);
		}
    }
}
