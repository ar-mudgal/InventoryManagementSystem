package com.inventory.management.service;

import java.util.List;
import java.util.concurrent.Future;

public interface EmailService {
     void sendEmail(String subject, String message, String to);

//    Future<Boolean> sendMailFromGmail(String emailRecipients, String emailSubject, String emailBody,
//                                      List<String> fileName, List<byte[]> ba, String cc);


}
