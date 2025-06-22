package org.coshift.c_adapters;

public record PersonDto(Long   id,
                            String nickname,
                            String password,
                            long   timeAccountId) {
}