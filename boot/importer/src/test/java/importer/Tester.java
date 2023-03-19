package importer;

import jakarta.annotation.Resource;
import jmc.upstream.ServiceAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Slf4j
@SpringBootTest
public class Tester {
    @Autowired
    ServiceAdapter serviceAdapter;

    @Test
    public void noop() throws IllegalAccessException {
        Assertions.assertNotNull(serviceAdapter);
        Assertions.assertNotNull(
                FieldUtils.readField(
                        serviceAdapter,
                        "resourceAdapter", true));

    }

    @Autowired
    ConfigurableApplicationContext ctx;

    @Test
    public void test0() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ConfigurableListableBeanFactory beanFactory = ctx.getBeanFactory();
        Assertions.assertFalse(ctx.containsBean("serviceMock"));
        Assertions.assertFalse(ctx.containsBean("resourceMock"));

        new Reflections("importer", new SubTypesScanner(false)).getSubTypesOf(Object.class).stream().filter(t -> t.isAnnotationPresent(Service.class))
                .forEach(
                        t -> {
                            log.info("type {}", t);
                            log.info("\tname {}", t.getAnnotation(Service.class).value());


                            try {
                                Constructor<?> ctor = t.getDeclaredConstructor();

                                Object existingBean = ctor.newInstance();
                                beanFactory.autowireBean(
                                        existingBean);
                                beanFactory.registerSingleton(
                                        t.getAnnotation(Service.class).value(), existingBean);

                            } catch (InstantiationException e) {
                                throw new RuntimeException(e);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            } catch (InvocationTargetException e) {
                                throw new RuntimeException(e);
                            } catch (NoSuchMethodException e) {
                                throw new RuntimeException(e);
                            }
                        });


        Assertions.assertTrue(ctx.containsBean("resourceMock"));
        Assertions.assertTrue(ctx.containsBean("serviceMock"));
        Object serviceBean = ctx.getBean("serviceMock");
        Assertions.assertEquals(ctx.getBean("resourceMock"), FieldUtils.readField(serviceBean, "resourceMock", true));

    }

    @Service("serviceMock")
    public static class ServiceMock {

        @Resource(name = "resourceMock")
        ResourceMock resourceMock;

    }

    @Service("resourceMock")

    public static class ResourceMock {

    }
}
