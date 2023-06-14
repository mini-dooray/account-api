package com.minidooray.accountapi.request;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestAccountDtoTest {

    @Test
    void testOnAccess() {

        RequestAccountDto requestAccountDto = new RequestAccountDto();

        requestAccountDto.onAccess();

        LocalDate currentDate = LocalDate.now();
        assertEquals(currentDate, requestAccountDto.getLastAccessDate());
    }
    @Test
    void testToString() {
        RequestAccountDto accountDto = new RequestAccountDto();
        accountDto.setAccountId("test123");
        accountDto.setPassword("password");
        accountDto.setName("John Doe");

        String toStringResult = accountDto.toString();

        assertEquals("RequestAccountDto(accountId=test123, password=password, name=John Doe, email=null, phoneNumber=null, lastAccessDate=null, status=1)", toStringResult);
    }

    @Test
    void testEqualsAndHashCode() {

        RequestAccountDto accountDto1 = new RequestAccountDto();
        accountDto1.setAccountId("test123");
        accountDto1.setPassword("password");
        accountDto1.setName("John Doe");

        RequestAccountDto accountDto2 = new RequestAccountDto();
        accountDto2.setAccountId("test123");
        accountDto2.setPassword("password");
        accountDto2.setName("John Doe");

        boolean equalsResult = accountDto1.equals(accountDto2);
        int hashCode1 = accountDto1.hashCode();
        int hashCode2 = accountDto2.hashCode();

        assertTrue(equalsResult);
        assertEquals(hashCode1, hashCode2);
    }
}
