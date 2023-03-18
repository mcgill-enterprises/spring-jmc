package importer;

import jakarta.annotation.Resource;
import jmc.upstream.ResourceAdapter;
import jmc.upstream.ServiceAdapter;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceLoader {
    @Autowired
    ApplicationContext ctx;
    @Bean("resourceAdapter")
    public ResourceAdapter resourceAdapter(){
        return new ResourceAdapter();
    }
    @Bean
    public ServiceAdapter serviceAdapter() {
        ServiceAdapter serviceAdapter = new ServiceAdapter();
        FieldUtils.getFieldsListWithAnnotation(ServiceAdapter.class,Resource.class).forEach(f->{
            try {
                FieldUtils.writeField(f, serviceAdapter, ctx.getBean(f.getName()), true);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        return serviceAdapter;
    }
}
