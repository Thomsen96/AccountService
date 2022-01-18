package dtu.group2.Presentation.Resources;

import dtu.group2.Application.AccountService;
import dtu.group2.Infrastructure.MessageQueueFactory;
import dtu.group2.Repositories.AccountRepository;
import messaging.MessageQueue;

public class Runner {
    public static void main(String[] args) {
        MessageQueue mq = new MessageQueueFactory().getMessageQueue();
        AccountService as = new AccountService(new AccountRepository(), new AccountRepository());
        AccountEventHandler aeh = new AccountEventHandler(mq, as);
    }
}
