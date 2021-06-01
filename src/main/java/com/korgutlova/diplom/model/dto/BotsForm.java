package com.korgutlova.diplom.model.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BotsForm {
    private Long projectId;

    private List<BotDto> bots = new ArrayList<>();

}
