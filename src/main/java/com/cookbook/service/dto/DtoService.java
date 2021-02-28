package com.cookbook.service.dto;

import java.util.List;

public interface DtoService<DM, DTO> {

    DTO convertToDto(DM domainModel);

    List<DTO> convertToDto(List<DM> domainModels);

    DM convertToDomain(DTO dtoModel);
}
