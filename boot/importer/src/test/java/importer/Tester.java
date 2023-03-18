package importer;

import jmc.upstream.ServiceAdapter;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;

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
}
