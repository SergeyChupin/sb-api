package sovkom.api.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transport.http.asyncclient.AsyncHTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sovkom.api.configuration.properties.CxfClientProperties;

@Configuration
@EnableConfigurationProperties(CxfClientProperties.class)
public class CxfClientConfiguration {

    @Bean
    public SpringBus springBus() {
        var bus = new SpringBus();
        bus.setProperty(AsyncHTTPConduit.USE_ASYNC, Boolean.TRUE);
        return bus;
    }

    @Bean
    public CxfClientFactory bus(SpringBus bus, CxfClientProperties properties) {
        return new CxfClientFactory(bus, properties);
    }

    @RequiredArgsConstructor
    public static class CxfClientFactory {

        private final SpringBus bus;
        private final CxfClientProperties properties;

        @SuppressWarnings("unchecked")
        public <T> T create(Class<T> wsServiceClass, String uri) {
            var jaxWsProxyFactory = new JaxWsProxyFactoryBean();
            jaxWsProxyFactory.setServiceClass(wsServiceClass);
            jaxWsProxyFactory.setAddress(uri);
            jaxWsProxyFactory.setBus(bus);
            jaxWsProxyFactory.getInInterceptors().add(new LoggingInInterceptor());
            jaxWsProxyFactory.getOutInterceptors().add(new LoggingOutInterceptor());
            Object wsClient = jaxWsProxyFactory.create();
            setTimeouts(wsClient, properties);
            return (T) wsClient;
        }

        private void setTimeouts(Object wsClient, CxfClientProperties properties) {
            var client = ClientProxy.getClient(wsClient);
            var http = (HTTPConduit) client.getConduit();
            var httpClientPolicy = new HTTPClientPolicy();

            if (properties.getConnectionTimeoutMs() > 0) {
                httpClientPolicy.setConnectionTimeout(properties.getConnectionTimeoutMs());
            }
            if (properties.getReceiveTimeoutMs() > 0) {
                httpClientPolicy.setReceiveTimeout(properties.getReceiveTimeoutMs());
            }

            http.setClient(httpClientPolicy);
        }
    }
}
