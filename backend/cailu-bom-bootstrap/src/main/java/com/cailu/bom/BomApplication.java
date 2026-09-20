package com.cailu.bom;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.cailu.bom")
@MapperScan({
        "com.cailu.bom.system.infrastructure.persistence",
        "com.cailu.bom.erp.infrastructure.persistence",
        "com.cailu.bom.oa.infrastructure.persistence",
        "com.cailu.bom.finance.infrastructure.persistence"
})
public class BomApplication {

    public static void main(String[] args) {
        SpringApplication.run(BomApplication.class, args);
    }
}
