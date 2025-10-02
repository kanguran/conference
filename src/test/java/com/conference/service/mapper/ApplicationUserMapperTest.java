package com.conference.service.mapper;


import org.junit.jupiter.api.BeforeEach;

class ApplicationUserMapperTest {

    private ApplicationUserMapper applicationUserMapper;

    @BeforeEach
    public void setUp() {
        applicationUserMapper = new ApplicationUserMapperImpl();
    }
}
