package com.korgutlova.diplom.model.mapper;

import com.korgutlova.diplom.model.entity.Message;
import com.korgutlova.diplom.model.entity.view.MessageView;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageView toView(Message message);

    List<MessageView> toListViews(List<Message> messages);
}
