package pingis.repositories.sandbox;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import pingis.entities.sandbox.Submission;

/**
 * Created by dwarfcrank on 7/28/17.
 */
public interface SubmissionRepository extends CrudRepository<Submission, UUID> {

}
