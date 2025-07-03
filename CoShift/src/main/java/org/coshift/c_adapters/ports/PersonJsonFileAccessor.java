package org.coshift.c_adapters.ports;

import org.coshift.c_adapters.dto.PersonDto;

import java.util.List;

public interface PersonJsonFileAccessor {

    List<PersonDto> readAll();

    boolean writeAll(List<PersonDto> persons);
}