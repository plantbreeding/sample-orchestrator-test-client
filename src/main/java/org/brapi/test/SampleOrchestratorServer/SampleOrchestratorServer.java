package org.brapi.test.SampleOrchestratorServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
public class SampleOrchestratorServer 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(SampleOrchestratorServer.class, args);
    }
}
