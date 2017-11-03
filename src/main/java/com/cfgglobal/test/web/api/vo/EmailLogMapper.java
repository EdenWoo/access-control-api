package com.cfgglobal.test.web.api.vo;

import com.cfgglobal.test.domain.EmailLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmailLogMapper {

    EmailLogMapper INSTANCE = Mappers.getMapper(EmailLogMapper.class);

    EmailLogVo toDto(EmailLog emailLog);

}