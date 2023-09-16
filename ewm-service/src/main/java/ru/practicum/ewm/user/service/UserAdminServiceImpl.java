package ru.practicum.ewm.user.service;

import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.NotSaveException;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAllUsers(List<Long> ids, Integer from, Integer size) {
        Pageable page = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        if (ids == null) {
            return UserMapper.INSTANCE.convertUserListToUserDTOList(
                    userRepository.findAll(page).getContent());
        } else {
            return UserMapper.INSTANCE.convertUserListToUserDTOList(
                    userRepository.findAllByIdIn(ids, page));
        }
    }

    @Override
    public UserDto saveUser(NewUserRequest newUserRequest) {
        try {
            User user = userRepository.save(
                    UserMapper.INSTANCE.toUserFromNewDto(newUserRequest));
            return UserMapper.INSTANCE.toUserDto(user);
        } catch (DataIntegrityViolationException e) {
            throw new NotSaveException("Пользователь не был создан: " + newUserRequest);
        }
    }

    @Override
    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с ид = " + userId + " не был найден.");
        }
        userRepository.deleteById(userId);
    }

}