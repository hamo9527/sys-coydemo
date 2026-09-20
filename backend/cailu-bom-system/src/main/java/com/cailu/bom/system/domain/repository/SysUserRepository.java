package com.cailu.bom.system.domain.repository;

import com.cailu.bom.system.domain.model.SysUser;

import java.util.Optional;

public interface SysUserRepository {

    Optional<SysUser> findById(Long id);

    Optional<SysUser> findByUsername(String username);

    Optional<String> findPasswordHashByUsername(String username);

    SysUser save(SysUser user, String passwordHash);
}
