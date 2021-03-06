package py.com.kalpa.springsecurityldap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan("py.com.kalpa.springsecurityldap")
public class AppConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {

		registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT");
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver createMultipartResolver() {

		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding("utf-8");
		return resolver;
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/assets/**", "/vistas/**").addResourceLocations("/app/assets/", "/app/vistas/");
		// .setCachePeriod(315569126);
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/app/vistas/");
		viewResolver.setSuffix(".html");

		viewResolver.setViewClass(InternalResourceView.class);
		return viewResolver;
	}
}