package com.cailu.bom.system.application;

public interface UserApplicationService {

    UserView getById(Long id);

    UserView create(CreateUserCommand command);
}
