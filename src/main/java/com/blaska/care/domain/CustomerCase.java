package com.blaska.care.domain;


import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.List;

@Value
@Builder(toBuilder = true)
@With
public class CustomerCase {
    Long caseId;
    String customerReference;
    String customerName;

    List<Message> messages;

}
