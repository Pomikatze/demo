package com.example.demo.service;

import com.example.demo.dto.ChainDto;
import com.example.demo.entity.ChainEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.DemoException;
import com.example.demo.mapper.ChainMapper;
import com.example.demo.repositories.ChainRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ChainService {

    private final ChainRepository chainRepository;

    private final ChainMapper chainMapper;

    private final UserService userService;

    public ChainDto findById(Long id, Principal principal) {
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не зарегистрирован"));
        return chainMapper.toDto(chainRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new DemoException("Сетка не найден")));
    }

    public ChainDto findByName(String name, Principal principal) {
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не зарегистрирован"));
        return chainMapper.toDto(chainRepository.findByNameChainAndUserId(name, user.getId()));
    }

    public ChainDto findByName(String name) {
        return chainMapper.toDto(chainRepository.findByNameChain(name));
    }

    public List<ChainDto> findAll(Principal principal) {
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не зарегистрирован"));
        List<ChainEntity> chainEntityList = chainRepository.findAllByUserId(user.getId())
                .orElseThrow(() -> new DemoException("Пользователь не зарегистрирован"));
        List<ChainDto> chainDtos = new ArrayList<>();

        for (ChainEntity c : chainEntityList) {
            chainDtos.add(chainMapper.toDto(c));
        }

        return chainDtos;
    }

    public String save(ChainDto chainDto, Principal principal) {
        ChainEntity chain = chainMapper.toEntity(chainDto);
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не найден"));
        chain.setUser(user);
        chainRepository.save(chain);
        return "complete";
    }

    public String delete(ChainDto chainDto) throws PersistenceException {
        if (chainRepository.existsById(chainDto.getId())) {
            ChainEntity chain = chainMapper.toEntity(chainDto);
            chainRepository.delete(chain);
            return "complete";
        } else {
            return "false";
        }
    }

    public String deleteById(Long id) {
        if (chainRepository.existsById(id)) {
            chainRepository.deleteById(id);
            return "true";
        }
        return "false";
    }
}
