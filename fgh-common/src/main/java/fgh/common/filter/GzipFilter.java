package fgh.common.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class GzipFilter implements Filter{

	/**
	 * 头信息
	 */
	public static final String PARAM_KEY_HEADERS="headers";
	
	/**头信息**/
	private Map<String,String> headers;
	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String param = filterConfig.getInitParameter(PARAM_KEY_HEADERS);
		if(null == param || param.trim().length()<1){
			return;
		}
		this.headers = new HashMap<String,String>();
		String[] params  = param.split(",");
		String[] kvs = null;
		for(int i=0;i<params.length;i++){
			param = params[i];
			if(null != param && param.trim().length()>0){
				kvs = param.split("=");
				if(null!=kvs && kvs.length==2){
					headers.put(kvs[0], kvs[1]);	
				}
			}
		}
		
		if(this.headers.isEmpty()){
			this.headers = null;
		}
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if(this.headers != null){
			HttpServletResponse resp = (HttpServletResponse) response;
			for(Entry<String,String> header:this.headers.entrySet()){
				resp.addHeader(header.getKey(), header.getValue());
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

	
}
