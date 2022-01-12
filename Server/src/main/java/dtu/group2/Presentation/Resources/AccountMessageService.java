package dtu.group2.Presentation.Resources;

import dtu.group2.Application.AccountServiceServer;
import dtu.ws.fastmoney.Account;
import messaging.MessageQueue;

import java.util.concurrent.CompletableFuture;

public class AccountMessageService {
    private MessageQueue messageQueue;
    private CompletableFuture<Account> accountPending;
    private AccountServiceServer accountServiceServer;

    
}
