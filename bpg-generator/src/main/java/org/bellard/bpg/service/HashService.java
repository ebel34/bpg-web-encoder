package org.bellard.bpg.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HashService {

	@Value("${hash.seed}")
	private String hashSeed;

	public String createHashCode() {

		return UUID.randomUUID().toString();
	}
}
