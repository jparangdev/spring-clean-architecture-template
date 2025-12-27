package kr.co.jparangdev.application.user;

import kr.co.jparangdev.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service implementation for user batch operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserBatchService implements UserBatchUseCase {

    private final UserRepository userRepository;
    private final TransactionTemplate transactionTemplate;

    @Override
    public int processDormantUsers() {
        LocalDateTime threshold = LocalDateTime.now().minusYears(1);

        Integer result = transactionTemplate.execute(status -> {
            List<User> targets = userRepository.findByLastLoginAtBeforeAndStatus(threshold, User.Status.ACTIVE);

            for (User user : targets) {
                user.switchToDormant();
                userRepository.save(user);
            }

            log.info("Processed {} users to dormant status", targets.size());
            return targets.size();
        });

        return result != null ? result : 0;
    }
}
