/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingis.entities.tmc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author authority
 */
@Entity
public class Logs {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    private TestOutput testOutput;

    private byte[] stdout;
    private byte[] stderr;

    public byte[] getStdout() {
        return stdout;
    }

    public void setStdout(byte[] stdout) {
        this.stdout = stdout;
    }

    public byte[] getStderr() {
        return stderr;
    }

    public void setStderr(byte[] stderr) {
        this.stderr = stderr;
    }
}
