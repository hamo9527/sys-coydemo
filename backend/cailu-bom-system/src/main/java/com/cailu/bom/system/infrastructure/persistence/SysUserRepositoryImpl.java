package com.cailu.bom.system.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cailu.bom.system.domain.model.SysUser;
import com.cailu.bom.system.domain.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SysUserRepositoryImpl implements SysUserRepository {

    private final SysUserMapper sysUserMapper;

    @Override
    public Optional<SysUser> findById(Long id) {
        return Optional.ofNullable(sysUserMapper.selectById(id)).map(this::toDomain);
    }

    @Override
    public Optional<SysUser> findByUsername(String username) {
        SysUserDO row = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUserDO>()
                .eq(SysUserDO::getUsername, username)
                .last("LIMIT 1"));
        return Optional.ofNullable(row).map(this::toDomain);
    }

    @Override
    public Optional<String> findPasswordHashByUsername(String username) {
        SysUserDO row = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUserDO>()
                .eq(SysUserDO::getUsername, username)
                .last("LIMIT 1"));
        return Optional.ofNullable(row).map(SysUserDO::getPasswordHash);
    }

    @Override
    public SysUser save(SysUser user, String passwordHash) {
        SysUserDO row = new SysUserDO();
        row.setId(user.getId());
        row.setUsername(user.getUsername());
        row.setPasswordHash(passwordHash);
        row.setRealName(user.getRealName());
        row.setMobile(user.getMobile());
        row.setOrgId(user.getOrgId());
        row.setStatus(user.getStatus());
        if (row.getId() == null) {
            sysUserMapper.insert(row);
        } else {
            sysUserMapper.updateById(row);
        }
        return toDomain(row);
    }

    private SysUser toDomain(SysUserDO row) {
        return new SysUser(row.getId(), row.getUsername(), row.getRealName(),
                row.getMobile(), row.getOrgId(), row.getStatus());
    }
}
