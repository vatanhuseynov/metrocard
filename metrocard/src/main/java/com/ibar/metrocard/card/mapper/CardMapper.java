package com.ibar.metrocard.card.mapper;

import com.ibar.metrocard.card.dto.request.CardRequest;
import com.ibar.metrocard.card.dto.response.CardResponse;
import com.ibar.metrocard.card.model.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;

@Mapper(componentModel = "spring", nullValueCheckStrategy = ALWAYS)
public interface CardMapper {
    Card toEntity(CardRequest request);
    CardResponse toResponse(Card card);
    @Mapping(target = "id", ignore = true)
    void updateCard(CardRequest request, @MappingTarget Card card);
}
