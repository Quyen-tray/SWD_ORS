package org.ors.subsystem.moderation.job_moderation.strategy;

import org.ors.cross.share_kernel.entity.Company;
import org.ors.cross.share_kernel.entity.JobReport;
import org.ors.cross.share_kernel.repository.CompanyRepository;
import org.ors.subsystem.moderation.job_moderation.enums.EnforcementType;
import org.springframework.stereotype.Component;

@Component
public class SuspendCompanyStrategy implements EnforcementStrategy {

    private final CompanyRepository companyRepository;

    public SuspendCompanyStrategy(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public EnforcementType type() {
        return EnforcementType.SUSPEND_COMPANY;
    }

    @Override
    public void execute(JobReport report, String summary) {
        Company company = report.getJobPost().getCompany();
        company.setVerificationStatus("SUSPENDED");
        companyRepository.save(company);
    }
}
