package gr.aueb.cf.insuranceapp.core.filters;


import gr.aueb.cf.insuranceapp.core.enums.InsuranceType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePolicyFilters extends GenericFilters{

    private LocalDate fromDate;
    private LocalDate toDate;
    private InsuranceType insuranceType;
    private Long vehicleId;
    private String uuid;
}
