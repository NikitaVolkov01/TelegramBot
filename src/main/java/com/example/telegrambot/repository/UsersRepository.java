package com.example.telegrambot.repository;

import com.example.telegrambot.dto.Users;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<Users, Long> {

}
