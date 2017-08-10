package pingis.repositories;

import org.springframework.data.repository.CrudRepository;
import pingis.entities.tmc.TmcSubmission;

import java.util.UUID;

/**
 * Created by dwarfcrank on 7/28/17.
 */
public interface TmcSubmissionRepository extends CrudRepository<TmcSubmission, UUID> {
}
