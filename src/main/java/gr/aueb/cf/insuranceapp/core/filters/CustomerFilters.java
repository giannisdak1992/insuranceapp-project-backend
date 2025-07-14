package gr.aueb.cf.insuranceapp.core.filters;

import lombok.*;
import org.springframework.lang.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerFilters extends GenericFilters{
    @Nullable
    private String uuid;

    @Nullable
    private String userAfm;


    @Nullable
    private Boolean isActive;
}
