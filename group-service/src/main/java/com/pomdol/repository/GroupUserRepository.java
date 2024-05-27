package com.pomdol.repository;

import com.pomdol.domain.GroupUser;
import com.pomdol.dto.GroupUserDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GroupUserRepository extends CrudRepository<GroupUser, Integer> {}
