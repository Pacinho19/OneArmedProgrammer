package pl.pacinho.onearmedprogrammer.initializer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.pacinho.onearmedprogrammer.model.entity.Account;
import pl.pacinho.onearmedprogrammer.service.AccountService;

import java.math.BigDecimal;


@RequiredArgsConstructor
@Component
public class StartApp {

    private final AccountService accountService;

    @EventListener(ApplicationReadyEvent.class)
    public void appReady() {
        initUsers();
    }

    private void initUsers() {
        if (accountService.getCount() > 0)
            return;

        accountService.save(new Account("1", "1", new BigDecimal(1_000)));
        accountService.save(new Account("2", "2", new BigDecimal(1_000)));
    }

}