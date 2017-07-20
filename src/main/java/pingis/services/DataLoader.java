package pingis.services;

import pingis.entities.Challenge;
import pingis.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pingis.repositories.TaskRepository;
import pingis.repositories.ChallengeRepository;

@Component
public class DataLoader implements ApplicationRunner {

    private ChallengeRepository cr;
    private TaskRepository tr;

    @Autowired
    public DataLoader(ChallengeRepository cr, TaskRepository tr) {
        this.cr = cr;
        this.tr = tr;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Challenge c = new Challenge("Pysähtymisongelma", "Ratkaise pysahtymisongelma.");
        Task t = new Task("metodi 1", "Kirjoita metodi, joka ratkaisee pysähtymisongelman."
                + "' Aikavaativuus korkeintaan O(1).");
        c.addTask(t);
        t.setChallenge(c);
        cr.save(c);
        tr.save(t);
    }
}
