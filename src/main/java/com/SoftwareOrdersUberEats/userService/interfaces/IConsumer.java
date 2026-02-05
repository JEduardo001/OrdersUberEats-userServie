package com.SoftwareOrdersUberEats.userService.interfaces;

public interface IConsumer {
    void handleCreateUser(String rawEvent,String correlationId);
}
