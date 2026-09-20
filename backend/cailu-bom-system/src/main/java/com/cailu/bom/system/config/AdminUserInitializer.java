package com.cailu.bom.system.config;

import com.cailu.bom.system.application.CreateUserCommand;
import com.cailu.bom.system.application.UserApplicationService;
import com.cailu.bom.system.domain.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements ApplicationRunner {

    private final SysUserRepository sysUserRepository;
    private final UserApplicationService userApplicationService;

    @Override
    public void run(ApplicationArguments args) {
        if (sysUserRepository.findByUsername("admin").isPresent()) {
            return;
        }
        userApplicationService.create(new CreateUserCommand(
                "admin", "admin123", "系统管理员", null, null));
        log.info("Seeded default admin user (admin / admin123)");
    }
}
