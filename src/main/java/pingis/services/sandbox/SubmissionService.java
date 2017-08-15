package pingis.services.sandbox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pingis.repositories.sandbox.SubmissionRepository;

@Service
public class SubmissionService {

  @Autowired
  private SubmissionSenderService senderService;

  @Autowired
  private SubmissionPackagingService packagingService;

  @Autowired
  private SubmissionRepository submissionRepository;


}
