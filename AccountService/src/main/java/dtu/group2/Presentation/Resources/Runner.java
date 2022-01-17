package dtu.group2.Presentation.Resources;

import dtu.group2.Application.AccountService;
import dtu.group2.Infrastructure.MessageQueueFactory;
import dtu.group2.Repositories.CustomerRepository;
import dtu.group2.Repositories.MerchantRepository;
import io.cucumber.java.bs.A;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;

public class Runner {
    public static MessageQueue mq = new MessageQueueFactory().getMessageQueue();
    AccountService as = new AccountService(new CustomerRepository(), new MerchantRepository());
    AccountEventHandler aeh = new AccountEventHandler(mq, as);

}
