package com.blaska.care.domain;


import com.blaska.care.Message;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.List;

@Value
@Builder
@With
public class CustomerCase {
    long caseId;
    String customerId;
    String customerName;

    List<Message> messages;

}
