package com.pomdol.repository;

import com.pomdol.domain.GroupUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GroupUserRepository extends CrudRepository<GroupUser, Integer> {
    @Query("select u from GroupUser u join fetch u.group g where u.userId = :userId and not u.isDeleted")
    List<GroupUser>  findByUserIdFetchGroup(Integer userId);
}
