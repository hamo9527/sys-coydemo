package com.cailu.bom.system.application;

import com.cailu.bom.common.exception.BusinessException;
import com.cailu.bom.system.domain.model.SysUser;
import com.cailu.bom.system.domain.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserApplicationServiceImpl implements UserApplicationService {

    private final SysUserRepository sysUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserView getById(Long id) {
        SysUser user = sysUserRepository.findById(id)
                .orElseThrow(() -> new BusinessException("user not found", 404));
        return toView(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserView create(CreateUserCommand command) {
        sysUserRepository.findByUsername(command.username()).ifPresent(u -> {
            throw new BusinessException("username already exists");
        });
        SysUser user = new SysUser(null, command.username(), command.realName(),
                command.mobile(), command.orgId(), 1);
        SysUser saved = sysUserRepository.save(user, passwordEncoder.encode(command.password()));
        return toView(saved);
    }

    private UserView toView(SysUser user) {
        return new UserView(user.getId(), user.getUsername(), user.getRealName(),
                user.getMobile(), user.getOrgId(), user.getStatus());
    }
}
