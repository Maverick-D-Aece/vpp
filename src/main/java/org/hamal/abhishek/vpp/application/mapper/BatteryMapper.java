package org.hamal.abhishek.vpp.application.mapper;

import org.hamal.abhishek.vpp.application.dto.BatteryDto;
import org.hamal.abhishek.vpp.domain.model.Battery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BatteryMapper {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "postCode", expression = "java(Integer.parseInt(dto.postCode()))")
    Battery toDomain(BatteryDto dto);

    List<Battery> toDomainList(List<BatteryDto> dtoList);

}
