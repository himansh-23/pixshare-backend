package com.pixshare;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.resource.HttpResource;

import com.pixshare.utils.Extra;

import javax.servlet.Filter;


@WebFilter("/*")
public class APIFilter implements Filter{
	/**
	 * 
	 */
	
	
	private static final long serialVersionUID = 6426829730982787766L;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletResponse res=(HttpServletResponse)response;
		HttpServletRequest req=(HttpServletRequest)request;
			System.out.println("Response is coming");
				res.addHeader("Access-Control-Allow-Origin", "*");
				res.addHeader("Access-Control-Allow-Headers", "*");
//			}
			filterChain.doFilter(request, response);
	}

}
