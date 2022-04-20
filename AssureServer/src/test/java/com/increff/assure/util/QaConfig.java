package com.increff.assure.util;

import com.increff.assure.spring.SpringConfig;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(//
        basePackages = { "com.increff.assure" }, //
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = { SpringConfig.class })//
)
@PropertySources({ //
        @PropertySource(value = "classpath:./com/increff/assure/test.properties", ignoreResourceNotFound = true) //
})
public class QaConfig {

}
