package org.ors.cross.Iam.auth.repositories;

import org.ors.cross.share_kernel.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
}
