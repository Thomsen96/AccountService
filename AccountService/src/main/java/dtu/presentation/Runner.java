package dtu.presentation;

import dtu.application.AccountService;
import dtu.infrastructure.MessageQueueFactory;
import dtu.infrastructure.AccountRepository;
import messaging.MessageQueue;

public class Runner {
    public static void main(String[] args) {
        MessageQueue mq = new MessageQueueFactory().getMessageQueue();
        AccountService as = new AccountService(new AccountRepository(), new AccountRepository());
        AccountEventHandler aeh = new AccountEventHandler(mq, as);
    }
}
