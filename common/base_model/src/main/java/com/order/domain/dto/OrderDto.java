package com.order.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.movie.utils.argumentResolverConfig.PropertyNamingStrategyConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategyConfig.SnakeCaseStrategy.class) // 将下划线映射为驼峰
public class OrderDto {
    Long viewId;
    String seats;
}
