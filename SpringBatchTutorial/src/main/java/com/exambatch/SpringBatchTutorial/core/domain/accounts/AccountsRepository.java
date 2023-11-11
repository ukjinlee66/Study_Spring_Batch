package com.exambatch.SpringBatchTutorial.core.domain.accounts;

import com.exambatch.SpringBatchTutorial.core.domain.orders.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AccountsRepository extends JpaRepository<Accounts, Long> {

}
