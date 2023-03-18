package jmc.upstream;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("serviceAdapter")
public class ServiceAdapter {
    @Resource(name="resourceAdapter")
    ResourceAdapter resourceAdapter;

}
