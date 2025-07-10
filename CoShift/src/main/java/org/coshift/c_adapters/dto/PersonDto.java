package org.coshift.c_adapters.dto;

public record PersonDto(
    long   id,
    String nickname,
    String password,
    long   timeAccountId,
    String role
) 
{}