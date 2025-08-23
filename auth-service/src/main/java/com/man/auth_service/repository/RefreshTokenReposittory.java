package com.man.auth_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.man.auth_service.entity.RefreshToken;

@Repository
public interface RefreshTokenReposittory extends JpaRepository<RefreshToken, Long>{
	Optional<RefreshToken> findByToken(String token);
	
	long deleteByUser_Id(Long id);

}
