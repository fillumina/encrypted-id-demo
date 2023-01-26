package com.fillumina.demo.encryptedid.accounting.repository;

import com.fillumina.demo.encryptedid.accounting.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}
