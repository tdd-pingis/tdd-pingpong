package pingis.repositories;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import pingis.entities.tmc.TmcSubmission;

/**
 * Created by dwarfcrank on 7/28/17.
 */
public interface TmcSubmissionRepository extends CrudRepository<TmcSubmission, UUID> {

}
