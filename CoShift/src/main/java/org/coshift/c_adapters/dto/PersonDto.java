package org.coshift.c_adapters.dto;

public record PersonDto(Long   id,
                            String nickname,
                            String password,
                            long   timeAccountId) {
}