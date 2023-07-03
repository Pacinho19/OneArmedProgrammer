package pl.pacinho.onearmedprogrammer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.pacinho.onearmedprogrammer.config.CryptoConfig;
import pl.pacinho.onearmedprogrammer.model.dto.AccountDetailsDto;
import pl.pacinho.onearmedprogrammer.model.dto.AccountDto;
import pl.pacinho.onearmedprogrammer.model.dto.mapper.AccountDtoMapper;
import pl.pacinho.onearmedprogrammer.model.entity.Account;
import pl.pacinho.onearmedprogrammer.repository.AccountRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final AccountRepository accountRepository;
    private final CryptoConfig cryptoConfig;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> user = accountRepository.findByName(username);
        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
        return user.map(AccountDetailsDto::new).get();
    }

    public long getCount() {
        return accountRepository.count();
    }

    public Account save(Account account) {
        account.setPassword(cryptoConfig.encoder().encode(account.getPassword()));
        return accountRepository.save(account);
    }

    public Account findByLogin(String name) {
        return accountRepository.findByName(name)
                .orElse(null);
    }

    public AccountDto getDtoByLogin(String name) {
        return AccountDtoMapper.parse(findByLogin(name));
    }
}
