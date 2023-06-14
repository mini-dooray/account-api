package com.minidooray.accountapi.request;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestAccountDtoTest {

    @Test
    void testOnAccess() {

        RequestAccountDto requestAccountDto = new RequestAccountDto();

        requestAccountDto.onAccess();

        LocalDate currentDate = LocalDate.now();
        assertEquals(currentDate, requestAccountDto.getLastAccessDate());
    }

}
