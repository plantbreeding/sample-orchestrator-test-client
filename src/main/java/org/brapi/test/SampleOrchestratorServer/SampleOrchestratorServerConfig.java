package org.brapi.test.SampleOrchestratorServer;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.TextConfigurationRealm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.subject.Subject;
import org.brapi.test.SampleOrchestratorServer.serializer.CustomDateSerializer;
import org.brapi.test.SampleOrchestratorServer.serializer.CustomInstantDeserializer;
import org.brapi.test.SampleOrchestratorServer.serializer.CustomTimeStampSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZonedDateTime;

import com.fasterxml.jackson.datatype.threetenbp.ThreeTenModule;

@Configuration
@EnableJpaRepositories("org.brapi.test.SampleOrchestratorServer.repository")
@PropertySource(value = "classpath:properties/application.properties")
@EnableScheduling
@EnableAsync
public class SampleOrchestratorServerConfig implements WebMvcConfigurer{
	@Bean
	@ConditionalOnMissingBean(ThreeTenModule.class)
	ThreeTenModule threeTenModule() {
		ThreeTenModule module = new ThreeTenModule();
		module.addDeserializer(Instant.class, CustomInstantDeserializer.INSTANT);
		module.addDeserializer(OffsetDateTime.class, CustomInstantDeserializer.OFFSET_DATE_TIME);
		module.addDeserializer(ZonedDateTime.class, CustomInstantDeserializer.ZONED_DATE_TIME);

		module.addSerializer(OffsetDateTime.class, new CustomTimeStampSerializer());
		module.addSerializer(LocalDate.class, new CustomDateSerializer());
		return module;
	}


    @Bean
    public Realm realm() {
        TextConfigurationRealm realm = new TextConfigurationRealm();
        realm.setUserDefinitions("joe.coder=password,user\n" +
                "jill.coder=password,admin");

        realm.setRoleDefinitions("admin=read,write\n" +
                "user=read");
        realm.setCachingEnabled(true);
        return realm;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/**", "anon");
//        chainDefinition.addPathDefinition("/api/**", "anon");
//        chainDefinition.addPathDefinition("/login.html", "authc"); 
//        chainDefinition.addPathDefinition("/logout", "logout");
        return chainDefinition;
    }

    @ModelAttribute(name = "subject")
    public Subject subject() {
        return SecurityUtils.getSubject();
    }
    
//    @Override
//    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//        registry
//        .addResourceHandler("/static/**")
//        .addResourceLocations("/");
//    }
    
//    @Bean
//    public InternalResourceViewResolver resolver() {
//       InternalResourceViewResolver resolver = new InternalResourceViewResolver();
//       resolver.setPrefix("/webapp/templates/");
//       resolver.setSuffix(".html");
//       return resolver;
//    }
}
