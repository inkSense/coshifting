package org.coshift.c_adapters.ports;

import org.coshift.c_adapters.dto.PersonDetailsDto;

import java.util.List;

public interface PersonJsonFileAccessor {

    List<PersonDetailsDto> readAll();

    boolean writeAll(List<PersonDetailsDto> persons);
}