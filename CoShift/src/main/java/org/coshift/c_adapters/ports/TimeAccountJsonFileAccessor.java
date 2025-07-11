package org.coshift.c_adapters.ports;

import org.coshift.c_adapters.dto.TimeAccountDto;
import java.util.List;

public interface TimeAccountJsonFileAccessor {
    List<TimeAccountDto> readAll();
    boolean writeAll(List<TimeAccountDto> accounts);
}