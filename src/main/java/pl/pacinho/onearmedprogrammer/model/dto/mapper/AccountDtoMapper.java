package pl.pacinho.onearmedprogrammer.model.dto.mapper;

import pl.pacinho.onearmedprogrammer.model.dto.AccountDto;
import pl.pacinho.onearmedprogrammer.model.entity.Account;

public class AccountDtoMapper {

    public static AccountDto parse(Account account) {
        return new AccountDto(
                account.getName(),
                account.getBalance()
        );
    }
}
