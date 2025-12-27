package kr.co.jparangdev.application.user;

import kr.co.jparangdev.application.common.exception.NotFoundException;
import kr.co.jparangdev.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Service implementation for user withdrawal.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserWithdrawService implements UserWithdrawUseCase {

    private final UserRepository userRepository;
    private final TransactionTemplate transactionTemplate;

    @Override
    public void withdraw(Long userId) {
        transactionTemplate.executeWithoutResult(status -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User", userId));
            
            user.withdraw();
            userRepository.save(user);
            
            log.info("User {} has been withdrawn", userId);
        });
    }
}
