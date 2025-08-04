package org.coshift.c_adapters.dto;

public record PersonDetailsDto(
    long   id,
    String nickname,
    String password,
    long   timeAccountId,
    String role
) 
{}