package com.example.demo.dto.search;

import com.example.demo.searchSpec.core.AbstractSearchParam;
import com.example.demo.searchSpec.core.annotation.ListObject;
import com.example.demo.searchSpec.core.annotation.VirtualField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Дто для поиска организаций по параметрам.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationSearchParam {

    @ListObject
    @Schema(description = "Название организации")
    private List<String> name;

    @ListObject
    @Schema(description = "Имя менеджера")
    private List<String> nameManager;

    @ListObject
    @VirtualField(field = "chain.nameChain")
    @Schema(description = "Название сети")
    private List<String> nameChain;
}
