package com.cfgglobal.common.util;


import com.cfgglobal.test.domain.BaseEntity;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@UtilityClass
@Slf4j
public class Utils {
    private static final int RETRY_TIMES = 3;


    public Try retryWhenOptimisticLockingFailure(CheckedFunction0<BaseEntity> checkedFunction0) throws Throwable {
        return retryWhenOptimisticLockingFailure(RETRY_TIMES, checkedFunction0);
    }

    public Try retryWhenOptimisticLockingFailure(int retryTimes, CheckedFunction0 checkedFunction0) throws Throwable {

        Try customerOrderTry = Try.of(checkedFunction0);
        log.debug("=======1=====" + customerOrderTry);
        if (customerOrderTry.isFailure()) {
            if (!(customerOrderTry.getCause() instanceof ObjectOptimisticLockingFailureException)) {
                throw customerOrderTry.getCause();
            }
            log.debug("=====2=========" + customerOrderTry.getCause());
            while (retryTimes > 0) {
                log.debug("====3==========retryTimes " + retryTimes);
                customerOrderTry = Try.of(checkedFunction0);
                if (customerOrderTry.isFailure() && !(customerOrderTry.getCause() instanceof ObjectOptimisticLockingFailureException)) {
                    throw new RuntimeException("you are unlucky (:,someone else is faster than you. Go back to shop page and refresh.");
                }
                if (customerOrderTry.isSuccess() || retryTimes-- < 0) break;
            }
        }

        return customerOrderTry;
    }


}
