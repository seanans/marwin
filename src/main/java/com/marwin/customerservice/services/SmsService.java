package com.marwin.customerservice.services;

import com.marwin.customerservice.models.SmsVerifyDTO;
import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@NoArgsConstructor
@Component
public class SmsService {
    @Autowired
    private VonageClient vonageClient;

    @Autowired
    private RandomCodeGenerator randomCodeGenerator;

    @Value("${vonage.api.key}")
    private String apiKey;

    @Value("${vonage.api.secret}")
    private String apiSecret;

    @Value("Marwin")
    private String brandName;

    public SmsVerifyDTO sendSms(String phoneNumber) {

        VonageClient client = VonageClient.builder().apiKey(apiKey).apiSecret(apiSecret).build();
        String code = randomCodeGenerator.generateRandomCode();
        TextMessage message = new TextMessage(
                brandName,
                phoneNumber,
                "Welcome from Marwin, your verification code: " + code + " Enter this code to the site"
        );
        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);
        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            System.out.println("Message sent successfully.");
            return new SmsVerifyDTO(code, true);
        } else {
            System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
            return new SmsVerifyDTO(code, false);
        }

    }
}
