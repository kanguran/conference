package com.conference.service.mapper;

import static com.conference.domain.ApplicationUserAsserts.*;
import static com.conference.domain.ApplicationUserTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApplicationUserMapperTest {

    private ApplicationUserMapper applicationUserMapper;

    @BeforeEach
    void setUp() {
        applicationUserMapper = new ApplicationUserMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getApplicationUserSample1();
        var actual = applicationUserMapper.toEntity(applicationUserMapper.toDto(expected));
        assertApplicationUserAllPropertiesEquals(expected, actual);
    }
}
