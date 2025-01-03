/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.message.services;

import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.message.dto.MessageWrapper;
import cn.edu.buaa.patpat.boot.modules.message.models.entities.Message;
import cn.edu.buaa.patpat.boot.modules.message.models.mappers.MessageMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService extends BaseService {
    private final MessageMapper messageMapper;

    public void sendMessage(MessageWrapper wrapper) {
        try {
            Message message = wrapper.toMessage(mappers);
            messageMapper.save(message);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize message: {}", e.getMessage());
        }
    }

    public void sendMessages(List<MessageWrapper> wrappers) {
        if (wrappers.isEmpty()) {
            return;
        }

        List<Message> messages = new ArrayList<>();
        try {
            for (var wrapper : wrappers) {
                messages.add(wrapper.toMessage(mappers));
            }
            messageMapper.batchSave(messages);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize message, bulk send terminated: {}", e.getMessage());
        }
    }

    public void readMessage(int id, int accountId, boolean read) {
        int updated = messageMapper.read(id, accountId, read);
        if (updated == 0) {
            throw new NotFoundException(M("message.exists.not"));
        }
    }

    public void updateMessage(int id, int accountId, Object argument) {
        int updated;
        try {
            updated = messageMapper.update(id, accountId, mappers.toJson(argument));
        } catch (JsonProcessingException e) {
            throw new BadRequestException(M("message.argument.invalid"));
        }
        if (updated == 0) {
            throw new NotFoundException(M("message.exists.not"));
        }
    }

    public void deleteMessage(int id, int accountId) {
        int updated = messageMapper.delete(id, accountId);
        if (updated == 0) {
            throw new NotFoundException(M("message.exists.not"));
        }
    }

    public List<Message> query(int courseId, int accountId) {
        return messageMapper.query(courseId, accountId);
    }
}
