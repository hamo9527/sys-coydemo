package com.cailu.bom.system.application;

import com.cailu.bom.common.exception.BusinessException;
import com.cailu.bom.system.domain.model.SysUser;
import com.cailu.bom.system.domain.repository.SysUserRepository;
import com.cailu.bom.system.infrastructure.security.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthApplicationServiceImpl implements AuthApplicationService {

    private final SysUserRepository sysUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    @Override
    public LoginView login(LoginCommand command) {
        SysUser user = sysUserRepository.findByUsername(command.username())
                .orElseThrow(() -> new BusinessException("invalid username or password", 401));
        if (!user.isEnabled()) {
            throw new BusinessException("user disabled", 403);
        }
        String hash = sysUserRepository.findPasswordHashByUsername(command.username())
                .orElseThrow(() -> new BusinessException("invalid username or password", 401));
        if (!passwordEncoder.matches(command.password(), hash)) {
            throw new BusinessException("invalid username or password", 401);
        }
        String token = jwtTokenService.createToken(user.getId(), user.getUsername());
        return new LoginView(token, new UserView(
                user.getId(), user.getUsername(), user.getRealName(),
                user.getMobile(), user.getOrgId(), user.getStatus()));
    }
}
